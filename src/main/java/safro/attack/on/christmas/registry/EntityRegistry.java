package safro.attack.on.christmas.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import safro.attack.on.christmas.AttackOnChristmas;
import safro.attack.on.christmas.entity.*;

public class EntityRegistry {

    public static final EntityType<SnowCannonEntity> SNOW_CANNON = FabricEntityTypeBuilder.create(SpawnGroup.MISC, SnowCannonEntity::new)
            .dimensions(EntityDimensions.fixed(1.0F, 1.3F)).trackRangeBlocks(20).build();

    public static final EntityType<EvilElfEntity> EVIL_ELF = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EvilElfEntity::new)
            .dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeBlocks(15).build();

    public static final EntityType<ElvishBatEntity> ELVISH_BAT = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ElvishBatEntity::new)
            .dimensions(EntityDimensions.fixed(0.5F, 0.9F)).trackRangeBlocks(8).build();

    public static final EntityType<EvilElfBruteEntity> EVIL_ELF_BRUTE = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EvilElfBruteEntity::new)
            .dimensions(EntityDimensions.fixed(1.4F, 2.7F)).trackRangeBlocks(10).build();

    public static final EntityType<RaiderElfEntity> RAIDER_ELF = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RaiderElfEntity::new)
            .dimensions(EntityDimensions.fixed(0.6F, 1.99F)).trackRangeBlocks(15).build();

    public static void init() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(AttackOnChristmas.MODID, "snow_cannon"), SNOW_CANNON);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(AttackOnChristmas.MODID, "evil_elf"), EVIL_ELF);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(AttackOnChristmas.MODID, "elvish_bat"), ELVISH_BAT);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(AttackOnChristmas.MODID, "evil_elf_brute"), EVIL_ELF_BRUTE);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(AttackOnChristmas.MODID, "raider_elf"), RAIDER_ELF);

        FabricDefaultAttributeRegistry.register(SNOW_CANNON, SnowCannonEntity.createCannonAttributes());
        FabricDefaultAttributeRegistry.register(EVIL_ELF, EvilElfEntity.createElfAttributes());
        FabricDefaultAttributeRegistry.register(ELVISH_BAT, ElvishBatEntity.createElfAttributes());
        FabricDefaultAttributeRegistry.register(EVIL_ELF_BRUTE, EvilElfBruteEntity.createIronGolemAttributes());
        FabricDefaultAttributeRegistry.register(RAIDER_ELF, RaiderElfEntity.createElfAttributes());

        SpawnRestrictionAccessor.callRegister(EntityRegistry.ELVISH_BAT, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ElvishBatEntity::canSpawn);
    }
}
