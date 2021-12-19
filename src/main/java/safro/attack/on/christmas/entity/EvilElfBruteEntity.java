package safro.attack.on.christmas.entity;

import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import safro.attack.on.christmas.block.PresentPileBlock;
import safro.attack.on.christmas.registry.ObjectsRegistry;
import safro.attack.on.christmas.util.AOCEntityGroup;

public class EvilElfBruteEntity extends IronGolemEntity {
    private boolean group = false;

    public EvilElfBruteEntity(EntityType<? extends EvilElfBruteEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        return ActionResult.PASS;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean hasGroup() {
        return group;
    }

    public void onDeath(DamageSource source) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            this.world.setBlockState(this.getBlockPos(), ObjectsRegistry.PRESENT_PILE.getDefaultState());
            PresentPileBlock block = (PresentPileBlock) world.getBlockState(this.getBlockPos()).getBlock();
            block.setReceiver(player);
        }
        super.onDeath(source);
    }

    public EntityGroup getGroup() {
        return AOCEntityGroup.EVIL_ELVES;
    }
}
