package safro.attack.on.christmas.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import safro.attack.on.christmas.AttackOnChristmas;

import java.util.Iterator;
import java.util.List;

public class SnowCannonEntity extends LivingEntity {
    private static final TrackedData<Integer> SHOT_COOLDOWN;
    private static final TrackedData<Boolean> FIRING;
    private float ticksUnderwater;
    private float yawVelocity;

    public SnowCannonEntity(EntityType<? extends SnowCannonEntity> type, World world) {
        super(type, world);
    }

    public static DefaultAttributeContainer.Builder createCannonAttributes() {
        return DefaultAttributeContainer.builder().add(EntityAttributes.GENERIC_MAX_HEALTH).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED).add(EntityAttributes.GENERIC_ARMOR).add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHOT_COOLDOWN, 0);
        this.dataTracker.startTracking(FIRING, false);
    }

    public boolean isPushedByFluids() {
        return false;
    }

    protected MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean isCollidable() {
        return true;
    }

    @Override
    public Arm getMainArm() {
        return null;
    }

    public void pushAwayFrom(Entity entity) { }

    public Direction getMovementDirection() {
        return this.getHorizontalFacing().rotateYClockwise();
    }

    public void updatePassengerPosition(Entity passenger) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
            float g = (float)((this.isRemoved() ? 0.009999999776482582D : this.getMountedHeightOffset()) + passenger.getHeightOffset());
            if (this.getPassengerList().size() > 1) {
                int i = this.getPassengerList().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (passenger instanceof AnimalEntity) {
                    f = (float)((double)f + 0.2D);
                }
            }

            Vec3d i = (new Vec3d((double)f, 0.0D, 0.0D)).rotateY(-this.getYaw() * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.getX() + i.x, this.getY() + (double)g, this.getZ() + i.z);
            passenger.setYaw(passenger.getYaw() + this.yawVelocity);
            passenger.setHeadYaw(passenger.getHeadYaw() + this.yawVelocity);
            this.copyEntityData(passenger);
            if (passenger instanceof AnimalEntity && this.getPassengerList().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setBodyYaw(((AnimalEntity)passenger).bodyYaw + (float)j);
                passenger.setHeadYaw(passenger.getHeadYaw() + (float)j);
            }

        }
    }

    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3d = getPassengerDismountOffset((double)(this.getWidth() * MathHelper.SQUARE_ROOT_OF_TWO), (double)passenger.getWidth(), passenger.getYaw());
        double d = this.getX() + vec3d.x;
        double e = this.getZ() + vec3d.z;
        BlockPos blockPos = new BlockPos(d, this.getBoundingBox().maxY, e);
        BlockPos blockPos2 = blockPos.down();
        if (!this.world.isWater(blockPos2)) {
            List<Vec3d> list = Lists.newArrayList();
            double f = this.world.getDismountHeight(blockPos);
            if (Dismounting.canDismountInBlock(f)) {
                list.add(new Vec3d(d, (double)blockPos.getY() + f, e));
            }

            double g = this.world.getDismountHeight(blockPos2);
            if (Dismounting.canDismountInBlock(g)) {
                list.add(new Vec3d(d, (double)blockPos2.getY() + g, e));
            }

            UnmodifiableIterator var14 = passenger.getPoses().iterator();

            while(var14.hasNext()) {
                EntityPose entityPose = (EntityPose)var14.next();
                Iterator var16 = list.iterator();

                while(var16.hasNext()) {
                    Vec3d vec3d2 = (Vec3d)var16.next();
                    if (Dismounting.canPlaceEntityAt(this.world, vec3d2, passenger, entityPose)) {
                        passenger.setPose(entityPose);
                        return vec3d2;
                    }
                }
            }
        }

        return super.updatePassengerForDismount(passenger);
    }

    protected void copyEntityData(Entity entity) {
        entity.setBodyYaw(this.getYaw());
        float f = MathHelper.wrapDegrees(entity.getYaw() - this.getYaw());
        float g = MathHelper.clamp(f, -105.0F, 105.0F);
        entity.prevYaw += g - f;
        entity.setYaw(entity.getYaw() + g - f);
        entity.setHeadYaw(entity.getYaw());
    }

    public void onPassengerLookAround(Entity passenger) {
        this.copyEntityData(passenger);
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            this.discard();
            return ActionResult.CONSUME;
        } else if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.ticksUnderwater < 60.0F) {
            if (!this.world.isClient) {
                return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    public void tick() {
        int tickCooldown = 0;
        ++tickCooldown;

        if (this.isSubmergedInWater()) {
            ++ticksUnderwater;
        } else {
            this.ticksUnderwater = 0.0F;
        }
        if (!this.world.isClient && this.ticksUnderwater >= 60.0F) {
            this.removeAllPassengers();
        }
        super.tick();
        if (tickCooldown >= 60) {
            if (this.getPrimaryPassenger() instanceof PlayerEntity player) {
                AttackOnChristmas.LOGGER.info("cannon past cooldown");
                if (this.dataTracker.get(FIRING)) {
                    world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
                    AttackOnChristmas.LOGGER.info("cannon past shooting");
                    if (!world.isClient) {
                        SnowballEntity snowballEntity = new SnowballEntity(world, player);
                        snowballEntity.setItem(new ItemStack(Items.SNOWBALL));
                        snowballEntity.setVelocity(this, this.getPitch(), this.getYaw(), 0.0F, 1.5F, 1.0F);
                        snowballEntity.setOwner(player);
                        world.spawnEntity(snowballEntity);
                    }
                    tickCooldown = 0;
                }
            }
        }
    }

    public double getMountedHeightOffset() {
        return -0.3D;
    }

    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < 1;
    }

    @Nullable
    public Entity getPrimaryPassenger() {
        return this.getFirstPassenger();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return DefaultedList.of();
    }

    @Override
    public ItemStack getEquippedStack(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {
    }

    public void setInput(boolean jumping) {
        this.dataTracker.set(FIRING, jumping);
    }

    public boolean damage(DamageSource source, float amount) {
        if (source.getAttacker() instanceof LivingEntity) {
            return false;
        } else return !source.isExplosive() && !source.isProjectile();
    }

    static {
        SHOT_COOLDOWN = DataTracker.registerData(SnowCannonEntity.class, TrackedDataHandlerRegistry.INTEGER);
        FIRING = DataTracker.registerData(SnowCannonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
