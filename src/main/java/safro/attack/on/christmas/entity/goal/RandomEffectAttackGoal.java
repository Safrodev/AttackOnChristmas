package safro.attack.on.christmas.entity.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;

import java.util.Random;

public class RandomEffectAttackGoal extends MeleeAttackGoal {

    public RandomEffectAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }

    protected void attack(LivingEntity target, double squaredDistance) {
        super.attack(target, squaredDistance);
        if (this.mob.getRandom().nextFloat() <= 0.1F) {
            target.addStatusEffect(new StatusEffectInstance(randomEffect(this.mob.getRandom()), 100, 0));
        }
    }

    private StatusEffect randomEffect(Random random) {
        if (random.nextFloat() < 0.2F) {
            return StatusEffects.NAUSEA;
        } else if (random.nextFloat() < 0.2F) {
            return StatusEffects.UNLUCK;
        } else if (random.nextFloat() < 0.2F) {
            return StatusEffects.SLOWNESS;
        }
        return StatusEffects.BLINDNESS;
    }
}
