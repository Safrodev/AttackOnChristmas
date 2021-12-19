package safro.attack.on.christmas.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.attack.on.christmas.util.ElfGroupSpawner;
import safro.attack.on.christmas.util.RaiderElfSpawner;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Inject(method = "tickSpawners", at = @At("TAIL"))
    private void tickElfGroup(boolean spawnMonsters, boolean spawnAnimals, CallbackInfo ci) {
        ElfGroupSpawner spawner = new ElfGroupSpawner();
        spawner.spawn((ServerWorld) (Object) this, spawnMonsters, spawnAnimals);

        RaiderElfSpawner spawner1 = new RaiderElfSpawner();
        spawner1.spawn((ServerWorld) (Object) this, spawnMonsters, spawnAnimals);
    }
}
