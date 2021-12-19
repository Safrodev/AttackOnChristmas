package safro.attack.on.christmas.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.client.model.ElvishBatEntityModel;
import safro.attack.on.christmas.entity.ElvishBatEntity;

public class ElvishBatEntityRenderer extends MobEntityRenderer<ElvishBatEntity, ElvishBatEntityModel> {
    private static final Identifier TEXTURE = new Identifier(AttackOnChristmas.MODID, "textures/entity/elvish_bat.png");

    public ElvishBatEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new ElvishBatEntityModel(context.getPart(EntityModelLayers.BAT)), 0.25F);
    }

    public Identifier getTexture(ElvishBatEntity batEntity) {
        return TEXTURE;
    }

    protected void scale(ElvishBatEntity batEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.35F, 0.35F, 0.35F);
    }

    protected void setupTransforms(ElvishBatEntity batEntity, MatrixStack matrixStack, float f, float g, float h) {
        if (batEntity.isRoosting()) {
            matrixStack.translate(0.0D, -0.10000000149011612D, 0.0D);
        } else {
            matrixStack.translate(0.0D, (double)(MathHelper.cos(f * 0.3F) * 0.1F), 0.0D);
        }

        super.setupTransforms(batEntity, matrixStack, f, g, h);
    }
}