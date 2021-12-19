package safro.attack.on.christmas.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.util.Identifier;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.client.model.SnowCannonEntityModel;
import safro.attack.on.christmas.entity.SnowCannonEntity;
import safro.attack.on.christmas.registry.ClientRegistry;

public class SnowCannonEntityRenderer extends LivingEntityRenderer<SnowCannonEntity, SnowCannonEntityModel> {
    private static final Identifier TEXTURE = new Identifier(AttackOnChristmas.MODID, "textures/entity/snow_cannon.png");

    public SnowCannonEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SnowCannonEntityModel(ctx.getPart(ClientRegistry.SNOW_CANNON_LAYER)), 0.3F);
    }

    public Identifier getTexture(SnowCannonEntity entity) {
        return TEXTURE;
    }

    protected boolean hasLabel(SnowCannonEntity entity) {
        return false;
    }
}
