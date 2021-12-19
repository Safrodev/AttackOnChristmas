package safro.attack.on.christmas.util;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import safro.attack.on.christmas.entity.RaiderElfEntity;
import safro.attack.on.christmas.registry.EntityRegistry;

import java.util.List;
import java.util.Random;

public class RaiderElfSpawner implements Spawner {
    private static final int SPAWN_COOLDOWN = 1200;
    private int ticksUntilNextSpawn;

    public RaiderElfSpawner() {
    }

    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (spawnAnimals && world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            --this.ticksUntilNextSpawn;
            if (this.ticksUntilNextSpawn > 0) {
                return 0;
            } else {
                this.ticksUntilNextSpawn = 1200;
                PlayerEntity playerEntity = world.getRandomAlivePlayer();
                if (playerEntity == null) {
                    return 0;
                } else {
                    Random random = world.random;
                    int i = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    int j = (8 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                    BlockPos blockPos = playerEntity.getBlockPos().add(i, 0, j);
                    if (!world.isRegionLoaded(blockPos.getX() - 10, blockPos.getZ() - 10, blockPos.getX() + 10, blockPos.getZ() + 10)) {
                        return 0;
                    } else {
                        if (SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, blockPos, EntityRegistry.RAIDER_ELF)) {
                            if (world.isNearOccupiedPointOfInterest(blockPos, 2)) {
                                return this.spawnInHouse(world, blockPos);
                            }
                        }

                        return 0;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    private int spawnInHouse(ServerWorld world, BlockPos pos) {
        if (world.getPointOfInterestStorage().count(PointOfInterestType.HOME.getCompletionCondition(), pos, 48, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED) > 4L) {
            List<RaiderElfEntity> list = world.getNonSpectatingEntities(RaiderElfEntity.class, (new Box(pos)).expand(48.0D, 8.0D, 48.0D));
            if (list.size() < 1) {
                return this.spawn(pos, world);
            }
        }

        return 0;
    }

    private int spawn(BlockPos pos, ServerWorld world) {
        RaiderElfEntity catEntity = (RaiderElfEntity)EntityRegistry.RAIDER_ELF.create(world);
        if (catEntity == null) {
            return 0;
        } else {
            catEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, (EntityData)null, (NbtCompound)null);
            catEntity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
            world.spawnEntityAndPassengers(catEntity);
            return 1;
        }
    }
}
