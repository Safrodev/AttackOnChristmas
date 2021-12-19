package safro.attack.on.christmas.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.entity.RaiderElfEntity;

public class RaiderElfEntityRenderer extends LivingEntityRenderer<RaiderElfEntity, PlayerEntityModel<RaiderElfEntity>> {

    public RaiderElfEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new PlayerEntityModel(context.getPart(EntityModelLayers.PLAYER), false), 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER_SLIM_INNER_ARMOR)), new BipedEntityModel(context.getPart(EntityModelLayers.PLAYER_SLIM_OUTER_ARMOR))));
        this.addFeature(new PlayerHeldItemFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(RaiderElfEntity vampire) {
        return new Identifier(AttackOnChristmas.MODID, "textures/entity/raider_elf.png");
    }

    @Override
    protected boolean hasLabel(RaiderElfEntity entity) {
        return false;
    }
}
