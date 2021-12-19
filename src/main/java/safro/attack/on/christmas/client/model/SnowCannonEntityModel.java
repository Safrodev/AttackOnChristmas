package safro.attack.on.christmas.client.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import safro.attack.on.christmas.entity.SnowCannonEntity;

public class SnowCannonEntityModel extends EntityModel<SnowCannonEntity> {
	private final ModelPart cannon;
	private final ModelPart seat;
	private final ModelPart bottom;

	public SnowCannonEntityModel(ModelPart root) {
		this.cannon = root.getChild("cannon");
		this.seat = this.cannon.getChild("seat");
		this.bottom = this.cannon.getChild("bottom");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData1 = modelPartData.addChild("cannon", ModelPartBuilder.create().uv(0,27).cuboid(-4.0F, -1.0F, -6.0F, 8.0F, 1.0F, 8.0F).uv(27,0).cuboid(-2.0F, -5.0F, -4.0F, 4.0F, 4.0F, 4.0F).uv(24,19).cuboid(-4.0F, -6.0F, -6.0F, 8.0F, 1.0F, 8.0F).uv(0,0).cuboid(-4.0F, -13.0F, -17.0F, 8.0F, 7.0F, 11.0F).uv(38,0).cuboid(-5.0F, -12.0F, -17.0F, 1.0F, 5.0F, 10.0F).uv(0,36).cuboid(4.0F, -12.0F, -17.0F, 1.0F, 5.0F, 10.0F).uv(46,28).cuboid(-5.0F, -14.0F, -18.0F, 10.0F, 9.0F, 1.0F).uv(0,0).cuboid(-1.0F, -5.0F, -9.0F, 2.0F, 3.0F, 1.0F).uv(34,28).cuboid(-1.0F, -3.0F, -8.0F, 2.0F, 1.0F, 4.0F), ModelTransform.pivot(0.0F,24.0F,0.0F));
		modelPartData1.addChild("bottom", ModelPartBuilder.create().uv(22,28).cuboid(5.0F, -3.0F, -16.0F, 1.0F, 6.0F, 10.0F).uv(34,34).cuboid(13.0F, -3.0F, -16.0F, 1.0F, 6.0F, 10.0F), ModelTransform.pivot(0.0F,0.0F,-1.0F));
		modelPartData1.addChild("seat", ModelPartBuilder.create().uv(0,18).cuboid(-4.0F, 2.0F, 6.0F, 8.0F, 1.0F, 8.0F), ModelTransform.pivot(0.0F,0.0F,-2.0F));
		return TexturedModelData.of(modelData,128,128);
	}

	@Override
	public void setAngles(SnowCannonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		setRotationAngle(bottom, 0.0F, 0.0F, -1.5708F);
		setRotationAngle(seat, 1.309F, 0.0F, 0.0F);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		cannon.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}
}