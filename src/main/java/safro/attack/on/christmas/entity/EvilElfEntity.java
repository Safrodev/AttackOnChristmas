package safro.attack.on.christmas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.world.World;
import safro.attack.on.christmas.util.AOCEntityGroup;

public class EvilElfEntity extends HostileEntity {

    public EvilElfEntity(EntityType<? extends EvilElfEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this, new Class[]{EvilElfEntity.class}).setGroupRevenge(new Class[0]));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createElfAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 15.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3499999940395355D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 7.0D).add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D);
    }

    public boolean isTeammate(Entity other) {
        if (super.isTeammate(other)) {
            return true;
        }  else if (other instanceof LivingEntity && ((LivingEntity)other).getGroup() == AOCEntityGroup.EVIL_ELVES) {
            return this.getScoreboardTeam() == null && other.getScoreboardTeam() == null;
        } else {
            return false;
        }
    }

    public EntityGroup getGroup() {
        return AOCEntityGroup.EVIL_ELVES;
    }
}
