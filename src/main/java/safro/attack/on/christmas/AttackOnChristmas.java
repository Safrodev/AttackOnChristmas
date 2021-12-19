package safro.attack.on.christmas;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import safro.attack.on.christmas.registry.EntityRegistry;
import safro.attack.on.christmas.registry.ObjectsRegistry;
import safro.attack.on.christmas.registry.TagRegistry;

public class AttackOnChristmas implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("aoc");
	public static final String MODID = "aoc";
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(MODID, MODID), () -> new ItemStack(Items.SNOWBALL));

	@Override
	public void onInitialize() {
		ObjectsRegistry.init();
		EntityRegistry.init();
		TagRegistry.init();
	}
}
