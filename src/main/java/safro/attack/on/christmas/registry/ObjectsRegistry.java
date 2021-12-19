package safro.attack.on.christmas.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.block.PresentPileBlock;
import safro.attack.on.christmas.block.SnowMachineBlock;
import safro.attack.on.christmas.item.GiftItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ObjectsRegistry {
    public static final Map<Block, Identifier> BLOCKS = new LinkedHashMap<>();
    public static final Map<Item, Identifier> ITEMS = new LinkedHashMap<>();

    // Blocks
//    public static final Block SNOW_MACHINE = register("snow_machine", new SnowMachineBlock(FabricBlockSettings.of(Material.METAL).ticksRandomly().sounds(BlockSoundGroup.METAL).strength(2.0F).requiresTool()), true);
    public static final Block PRESENT_PILE = register("present_pile", new PresentPileBlock(FabricBlockSettings.of(Material.WOOL).strength(50.0F, 50.0F).sounds(BlockSoundGroup.WOOL)), true);

    // Items
    public static final Item GREEN_GIFT = register("green_gift", new GiftItem(Rarity.COMMON, simple()));
    public static final Item BLUE_GIFT = register("blue_gift", new GiftItem(Rarity.UNCOMMON, simple().rarity(Rarity.UNCOMMON)));
    public static final Item RED_GIFT = register("red_gift", new GiftItem(Rarity.RARE, simple().rarity(Rarity.RARE)));

    private static <T extends Block> T register(String name, T block, boolean createItem) {
        BLOCKS.put(block, new Identifier(AttackOnChristmas.MODID, name));
        if (createItem) {
            ITEMS.put(new BlockItem(block, simple()), BLOCKS.get(block));
        }
        return block;
    }

    private static <T extends Item> T register(String name, T item) {
        ITEMS.put(item, new Identifier(AttackOnChristmas.MODID, name));
        return item;
    }

    private static Item.Settings simple() {
        return new Item.Settings().group(AttackOnChristmas.ITEM_GROUP);
    }

    public static void init() {
        BLOCKS.keySet().forEach(block -> Registry.register(Registry.BLOCK, BLOCKS.get(block), block));
        ITEMS.keySet().forEach(item -> Registry.register(Registry.ITEM, ITEMS.get(item), item));
    }
}
