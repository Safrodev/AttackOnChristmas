package safro.attack.on.christmas.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import safro.attack.on.christmas.AttackOnChristmas;

import java.util.Random;

public class SnowMachineBlock extends Block {
    public static final IntProperty SNOW_LAYER;
    private int amount;

    public SnowMachineBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SNOW_LAYER, 0));
        this.amount = 0;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (amount > 600) {
            amount = amount - 600;
            if (!world.isClient) {
                dropStack(world, pos, new ItemStack(Items.SNOWBALL, 1));
                updateState(state);
            }
            AttackOnChristmas.LOGGER.info("reach");
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (amount < 1200) {
            ++amount;
            if (!world.isClient) {
                updateState(state);
            }
        }
    }

    public boolean isFull() {
        return amount >= 1200;
    }

    public void updateState(BlockState state) {
        if (amount < 100) {
            state.with(SNOW_LAYER, 0);
        } else if (amount >= 100 && amount <= 500) {
            state.with(SNOW_LAYER, 1);
        } else {
            state.with(SNOW_LAYER, 2);
        }
    }

    public int getAmount() {
        return amount;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SNOW_LAYER);
        super.appendProperties(builder);
    }

    static {
        SNOW_LAYER = IntProperty.of("snow_layer", 0, 2);
    }
}
