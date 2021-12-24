package safro.attack.on.christmas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import safro.attack.on.christmas.block.PresentPileBlock;
import safro.attack.on.christmas.registry.ObjectsRegistry;
import safro.attack.on.christmas.util.AOCEntityGroup;

public class EvilElfBruteEntity extends IronGolemEntity {
    private boolean group = false;

    public EvilElfBruteEntity(EntityType<? extends EvilElfBruteEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.6D, false));
        this.goalSelector.add(4, new IronGolemWanderAroundGoal(this, 0.6D));
        this.goalSelector.add(5, new IronGolemLookGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackIronGolemTargetGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean hasGroup() {
        return group;
    }

    public void onDeath(DamageSource source) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            this.world.setBlockState(this.getBlockPos(), ObjectsRegistry.PRESENT_PILE.getDefaultState());
            PresentPileBlock block = (PresentPileBlock) world.getBlockState(this.getBlockPos()).getBlock();
            block.setReceiver(player);
        }
        super.onDeath(source);
    }

    public EntityGroup getGroup() {
        return AOCEntityGroup.EVIL_ELVES;
    }

    @Override
    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
            Entity entity = this.world.getClosestPlayer(this, -1.0D);
            if (entity != null) {
                double d = entity.squaredDistanceTo(this);
                int i = this.getType().getSpawnGroup().getImmediateDespawnRange();
                int j = i * i;
                if (d > (double)j && this.canImmediatelyDespawn(d)) {
                    this.discard();
                }

                int k = this.getType().getSpawnGroup().getDespawnStartRange();
                int l = k * k;
                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.canImmediatelyDespawn(d)) {
                    this.discard();
                } else if (d < (double)l) {
                    this.despawnCounter = 0;
                }
            }

        } else {
            this.despawnCounter = 0;
        }
    }
}
