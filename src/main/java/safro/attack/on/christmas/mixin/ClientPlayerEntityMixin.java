package safro.attack.on.christmas.mixin;

import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import safro.attack.on.christmas.entity.SnowCannonEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow public Input input;

    @Shadow private boolean riding;

    @Inject(method = "tickRiding", at = @At("TAIL"))
    private void checkCannonInput(CallbackInfo ci) {
        ClientPlayerEntity cpe = (ClientPlayerEntity) (Object) this;
        if (cpe.getVehicle() instanceof SnowCannonEntity entity) {
            entity.setInput(this.input.jumping);
            this.riding |= this.input.jumping;
        }
    }
}
