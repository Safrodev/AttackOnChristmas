package safro.attack.on.christmas.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.attack.on.christmas.util.AOCEntityGroup;

@Mixin(SnowballEntity.class)
public class SnowballEntityMixin {
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    private void elfExtraDamage(EntityHitResult entityHitResult, CallbackInfo ci) {
        SnowballEntity sn = (SnowballEntity) (Object) this;
        if (entityHitResult.getEntity() instanceof LivingEntity entity) {
            int i = entity.getGroup().equals(AOCEntityGroup.EVIL_ELVES) ? 3 : 0;
            entity.damage(DamageSource.thrownProjectile(sn, sn.getOwner()), (float)i);
        }
    }
}
