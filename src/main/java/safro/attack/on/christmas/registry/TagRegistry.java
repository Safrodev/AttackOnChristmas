package safro.attack.on.christmas.registry;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import safro.attack.on.christmas.AttackOnChristmas;

public class TagRegistry {
    public static final Tag<Item> COMMON_GIFT = net.fabricmc.fabric.api.tag.TagRegistry.item(new Identifier(AttackOnChristmas.MODID, "common_gift"));
    public static final Tag<Item> UNCOMMON_GIFT = net.fabricmc.fabric.api.tag.TagRegistry.item(new Identifier(AttackOnChristmas.MODID, "uncommon_gift"));
    public static final Tag<Item> RARE_GIFT = net.fabricmc.fabric.api.tag.TagRegistry.item(new Identifier(AttackOnChristmas.MODID, "rare_gift"));

    public static void init() {
        // e
    }
}
