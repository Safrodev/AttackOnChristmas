package safro.attack.on.christmas.registry;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.client.model.SnowCannonEntityModel;
import safro.attack.on.christmas.client.render.*;

public class ClientRegistry {
    public static final EntityModelLayer SNOW_CANNON_LAYER = new EntityModelLayer(new Identifier(AttackOnChristmas.MODID, "snow_cannon_layer"), "snow_cannon_layer");

    public static void init() {
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.SNOW_CANNON, SnowCannonEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.EVIL_ELF, EvilElfEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.ELVISH_BAT, ElvishBatEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.EVIL_ELF_BRUTE, EvilElfBruteEntityRenderer::new);
        EntityRendererRegistry.INSTANCE.register(EntityRegistry.RAIDER_ELF, RaiderElfEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(SNOW_CANNON_LAYER, SnowCannonEntityModel::getTexturedModelData);

    //    BlockRenderLayerMap.INSTANCE.putBlock(ObjectsRegistry.SNOW_MACHINE, RenderLayer.getCutout());
    }
}
