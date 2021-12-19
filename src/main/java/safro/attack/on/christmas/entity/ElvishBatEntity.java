package safro.attack.on.christmas.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import safro.attack.on.christmas.entity.goal.RandomEffectAttackGoal;

import java.util.Random;

public class ElvishBatEntity extends HostileEntity {
    public static final float field_30268 = 74.48451F;
    public static final int field_28637 = MathHelper.ceil(2.4166098F);
    private static final TrackedData<Byte> BAT_FLAGS;
    private static final int ROOSTING_FLAG = 1;
    private static final TargetPredicate CLOSE_PLAYER_PREDICATE;
    @Nullable
    private BlockPos hangingPosition;

    public ElvishBatEntity(EntityType<? extends ElvishBatEntity> entityType, World world) {
        super(entityType, world);
        this.setRoosting(true);
    }

    protected void initGoals() {
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[]{RaiderEntity.class}).setGroupRevenge(new Class[0]));
        this.goalSelector.add(2, new RandomEffectAttackGoal(this, 1.0D, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createElfAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2799999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D);
    }

    public boolean canBeLeashedBy(PlayerEntity player) {
        return false;
    }

    public boolean hasWings() {
        return !this.isRoosting() && this.age % field_28637 == 0;
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BAT_FLAGS, (byte)0);
    }

    protected float getSoundVolume() {
        return 0.1F;
    }

    public float getSoundPitch() {
        return super.getSoundPitch() * 0.95F;
    }

    @Nullable
    public SoundEvent getAmbientSound() {
        return this.isRoosting() && this.random.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    public boolean isPushable() {
        return false;
    }

    protected void pushAway(Entity entity) {
    }

    protected void tickCramming() {
    }

    public boolean isRoosting() {
        return ((Byte)this.dataTracker.get(BAT_FLAGS) & 1) != 0;
    }

    public void setRoosting(boolean roosting) {
        byte b = (Byte)this.dataTracker.get(BAT_FLAGS);
        if (roosting) {
            this.dataTracker.set(BAT_FLAGS, (byte)(b | 1));
        } else {
            this.dataTracker.set(BAT_FLAGS, (byte)(b & -2));
        }

    }

    public void tick() {
        super.tick();
        if (this.isRoosting()) {
            this.setVelocity(Vec3d.ZERO);
            this.setPos(this.getX(), (double)MathHelper.floor(this.getY()) + 1.0D - (double)this.getHeight(), this.getZ());
        } else {
            this.setVelocity(this.getVelocity().multiply(1.0D, 0.6D, 1.0D));
        }

    }

    protected void mobTick() {
        super.mobTick();
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.up();
        if (this.isRoosting()) {
            boolean bl = this.isSilent();
            if (this.world.getBlockState(blockPos2).isSolidBlock(this.world, blockPos)) {
                if (this.random.nextInt(200) == 0) {
                    this.headYaw = (float)this.random.nextInt(360);
                }

                if (this.world.getClosestPlayer(CLOSE_PLAYER_PREDICATE, this) != null) {
                    this.setRoosting(false);
                    if (!bl) {
                        this.world.syncWorldEvent((PlayerEntity)null, 1025, blockPos, 0);
                    }
                }
            } else {
                this.setRoosting(false);
                if (!bl) {
                    this.world.syncWorldEvent((PlayerEntity)null, 1025, blockPos, 0);
                }
            }
        } else {
            if (this.hangingPosition != null && (!this.world.isAir(this.hangingPosition) || this.hangingPosition.getY() <= this.world.getBottomY())) {
                this.hangingPosition = null;
            }

            if (this.hangingPosition == null || this.random.nextInt(30) == 0 || this.hangingPosition.isWithinDistance(this.getPos(), 2.0D)) {
                this.hangingPosition = new BlockPos(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0D, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
            }

            double bl = (double)this.hangingPosition.getX() + 0.5D - this.getX();
            double d = (double)this.hangingPosition.getY() + 0.1D - this.getY();
            double e = (double)this.hangingPosition.getZ() + 0.5D - this.getZ();
            Vec3d vec3d = this.getVelocity();
            Vec3d vec3d2 = vec3d.add((Math.signum(bl) * 0.5D - vec3d.x) * 0.10000000149011612D, (Math.signum(d) * 0.699999988079071D - vec3d.y) * 0.10000000149011612D, (Math.signum(e) * 0.5D - vec3d.z) * 0.10000000149011612D);
            this.setVelocity(vec3d2);
            float f = (float)(MathHelper.atan2(vec3d2.z, vec3d2.x) * 57.2957763671875D) - 90.0F;
            float g = MathHelper.wrapDegrees(f - this.getYaw());
            this.forwardSpeed = 0.5F;
            this.setYaw(this.getYaw() + g);
            if (this.random.nextInt(100) == 0 && this.world.getBlockState(blockPos2).isSolidBlock(this.world, blockPos2)) {
                this.setRoosting(true);
            }
        }

    }

    public static boolean canSpawn(EntityType<ElvishBatEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (pos.getY() >= world.getSeaLevel()) {
            return false;
        } else {
            int i = world.getLightLevel(pos);
            int j = 4;
            if (random.nextBoolean()) {
                return false;
            }
            return i > random.nextInt(j) ? false : canMobSpawn(type, world, spawnReason, pos, random);
        }
    }

    protected MoveEffect getMoveEffect() {
        return MoveEffect.EVENTS;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
    }

    public boolean canAvoidTraps() {
        return true;
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.world.isClient && this.isRoosting()) {
                this.setRoosting(false);
            }

            return super.damage(source, amount);
        }
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(BAT_FLAGS, nbt.getByte("BatFlags"));
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("BatFlags", (Byte)this.dataTracker.get(BAT_FLAGS));
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height / 2.0F;
    }

    static {
        BAT_FLAGS = DataTracker.registerData(BatEntity.class, TrackedDataHandlerRegistry.BYTE);
        CLOSE_PLAYER_PREDICATE = TargetPredicate.createNonAttackable().setBaseMaxDistance(4.0D);
    }
}
