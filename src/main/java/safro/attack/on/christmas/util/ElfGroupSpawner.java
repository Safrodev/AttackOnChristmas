package safro.attack.on.christmas.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Spawner;
import safro.attack.on.christmas.entity.EvilElfBruteEntity;
import safro.attack.on.christmas.entity.EvilElfEntity;
import safro.attack.on.christmas.entity.RaiderElfEntity;
import safro.attack.on.christmas.registry.EntityRegistry;

import java.util.List;
import java.util.Random;

public class ElfGroupSpawner implements Spawner {
    private int ticksUntilNextSpawn;

    public ElfGroupSpawner() {}

    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (!spawnMonsters) {
            return 0;
        } else {
            Random random = world.random;
            --this.ticksUntilNextSpawn;
            if (this.ticksUntilNextSpawn > 0) {
                return 0;
            } else {
                this.ticksUntilNextSpawn += 12000 + random.nextInt(1200);
                long l = world.getTimeOfDay() / 24000L;
                if (l >= 5L && world.isDay()) {
                    if (random.nextInt(5) != 0) {
                        return 0;
                    } else {
                        int i = world.getPlayers().size();
                        if (i < 1) {
                            return 0;
                        } else {
                            PlayerEntity playerEntity = (PlayerEntity)world.getPlayers().get(random.nextInt(i));
                            if (playerEntity.isSpectator()) {
                                return 0;
                            } else if (world.isNearOccupiedPointOfInterest(playerEntity.getBlockPos(), 2)) {
                                return 0;
                            } else {
                                int j = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                                BlockPos.Mutable mutable = playerEntity.getBlockPos().mutableCopy().move(j, 0, k);
                                if (!world.isRegionLoaded(mutable.getX() - 10, mutable.getZ() - 10, mutable.getX() + 10, mutable.getZ() + 10)) {
                                    return 0;
                                } else {
                                    Biome biome = world.getBiome(mutable);
                                    Biome.Category category = biome.getCategory();
                                    if (category == Biome.Category.MUSHROOM) {
                                        return 0;
                                    } else {
                                        int n = 0;
                                        int o = (int)Math.ceil((double)world.getLocalDifficulty(mutable).getLocalDifficulty()) + 1;

                                        for(int p = 0; p < o; ++p) {
                                            ++n;
                                            mutable.setY(world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, mutable).getY());
                                            if (p == 0) {
                                                if (!this.spawnEvilElf(world, mutable, random, true)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnEvilElf(world, mutable, random, false);
                                            }

                                            mutable.setX(mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                            mutable.setZ(mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                        }

                                        return n;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean spawnEvilElf(ServerWorld world, BlockPos pos, Random random, boolean brute) {
        BlockState blockState = world.getBlockState(pos);
        if (brute) {
            if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), EntityRegistry.EVIL_ELF_BRUTE)) {
                return false;
            } else if (!EvilElfBruteEntity.canMobSpawn(EntityRegistry.EVIL_ELF_BRUTE, world, SpawnReason.PATROL, pos, random)) {
                return false;
            } else if (world.getNonSpectatingEntities(EvilElfBruteEntity.class, (new Box(pos)).expand(48.0D, 8.0D, 48.0D)).size() > 1) {
                return false;
            } else {
                EvilElfBruteEntity patrolEntity = EntityRegistry.EVIL_ELF_BRUTE.create(world);
                if (patrolEntity != null) {
                    patrolEntity.setPosition((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                    patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, (EntityData) null, (NbtCompound) null);
                    patrolEntity.setGroup(true);
                    world.spawnEntityAndPassengers(patrolEntity);
                    return true;
                } else {
                    return false;
                }
            }
        } else if (!SpawnHelper.isClearForSpawn(world, pos, blockState, blockState.getFluidState(), EntityRegistry.EVIL_ELF)) {
            return false;
        } else if (world.getNonSpectatingEntities(EvilElfEntity.class, (new Box(pos)).expand(448.0D, 8.0D, 448.0D)).size() > 4) {
            return false;
        } else {
            EvilElfEntity patrolEntity = EntityRegistry.EVIL_ELF.create(world);
            if (patrolEntity != null) {
                patrolEntity.setPosition((double) pos.getX(), (double) pos.getY(), (double) pos.getZ());
                patrolEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.PATROL, (EntityData) null, (NbtCompound) null);
                world.spawnEntityAndPassengers(patrolEntity);
                return true;
            } else {
                return false;
            }
        }
    }
}
