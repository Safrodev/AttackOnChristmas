package safro.attack.on.christmas.mixin;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.attack.on.christmas.registry.EntityRegistry;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {

    @Inject(method = "addCaveMobs", at = @At("TAIL"))
    private static void addElvishBats(SpawnSettings.Builder builder, CallbackInfo ci) {
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityRegistry.ELVISH_BAT, 10, 8, 8));
    }
}
