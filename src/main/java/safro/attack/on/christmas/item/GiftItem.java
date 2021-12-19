package safro.attack.on.christmas.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import safro.attack.on.christmas.registry.TagRegistry;

import java.util.Random;

public class GiftItem extends Item {
    private final Rarity rarity;

    public GiftItem(Rarity rarity, Settings settings) {
        super(settings);
        this.rarity = rarity;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Random random = player.getRandom();
        if (rarity != null) {
            if (rarity.equals(Rarity.COMMON)) {
                ItemStack gift = new ItemStack(TagRegistry.COMMON_GIFT.getRandom(random));
                if (!player.getInventory().insertStack(gift)) {
                    player.dropItem(gift, false);
                } else {
                    player.getInventory().insertStack(gift);
                }
                stack.decrement(1);
                player.world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

            } else if (rarity.equals(Rarity.UNCOMMON)) {
                ItemStack gift = new ItemStack(TagRegistry.UNCOMMON_GIFT.getRandom(random));
                if (!player.getInventory().insertStack(gift)) {
                    player.dropItem(gift, false);
                } else {
                    player.getInventory().insertStack(gift);
                }
                stack.decrement(1);
                player.world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);

            } else if (rarity.equals(Rarity.RARE)) {
                ItemStack gift = new ItemStack(TagRegistry.RARE_GIFT.getRandom(random));
                if (!player.getInventory().insertStack(gift)) {
                    player.dropItem(gift, false);
                } else {
                    player.getInventory().insertStack(gift);
                }
                stack.decrement(1);
                player.world.playSound((PlayerEntity)null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            return TypedActionResult.success(stack);
        } else
            return TypedActionResult.pass(stack);
    }
}
