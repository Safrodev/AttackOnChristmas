package safro.attack.on.christmas.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.attack.on.christmas.registry.ObjectsRegistry;

public class PresentPileBlock extends Block {
    private PlayerEntity receivingPlayer;

    public PresentPileBlock(Settings settings) {
        super(settings);
    }

    public void setReceiver(PlayerEntity receiver) {
        receivingPlayer = receiver;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (receivingPlayer != null && receivingPlayer == player) {
            Block.dropStack(world, pos, new ItemStack(ObjectsRegistry.GREEN_GIFT));
            Block.dropStack(world, pos, new ItemStack(ObjectsRegistry.RED_GIFT));
            Block.dropStack(world, pos, new ItemStack(ObjectsRegistry.BLUE_GIFT));
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return ActionResult.SUCCESS;
        } else {
            player.sendMessage(new LiteralText("You have not earned this gift!").formatted(Formatting.RED), true);
            return ActionResult.PASS;
        }
    }
}
