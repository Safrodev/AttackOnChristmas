package safro.attack.on.christmas.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.entity.EvilElfBruteEntity;

public class EvilElfBruteEntityRenderer extends MobEntityRenderer<EvilElfBruteEntity, IronGolemEntityModel<EvilElfBruteEntity>> {
    private static final Identifier TEXTURE = new Identifier(AttackOnChristmas.MODID, "textures/entity/evil_elf_brute.png");

    public EvilElfBruteEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IronGolemEntityModel(context.getPart(EntityModelLayers.IRON_GOLEM)), 0.7F);
    }

    public Identifier getTexture(EvilElfBruteEntity ironGolemEntity) {
        return TEXTURE;
    }

    protected void setupTransforms(EvilElfBruteEntity ironGolemEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(ironGolemEntity, matrixStack, f, g, h);
        if (!((double)ironGolemEntity.limbDistance < 0.01D)) {
            float i = 13.0F;
            float j = ironGolemEntity.limbAngle - ironGolemEntity.limbDistance * (1.0F - h) + 6.0F;
            float k = (Math.abs(j % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(6.5F * k));
        }
    }
}
