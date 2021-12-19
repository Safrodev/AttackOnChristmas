package safro.attack.on.christmas.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class RaiderElfEntity extends EvilElfEntity {
    static final Predicate<ItemEntity> PICKABLE_DROP_FILTER;

    public RaiderElfEntity(EntityType<? extends RaiderElfEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void initGoals() {
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(1, new RaiderElfEntity.PickUpItemsGoal());
        this.goalSelector.add(2, new LongDoorInteractGoal(this, true));
    }

    public boolean canPickupItem(ItemStack stack) {
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        return itemStack.isEmpty();
    }

    public boolean canEquip(ItemStack stack) {
        EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
        if (!this.getEquippedStack(equipmentSlot).isEmpty()) {
            return false;
        } else {
            return equipmentSlot == EquipmentSlot.MAINHAND && super.canEquip(stack);
        }
    }

    int stashTicks = 0;

    public void tick() {
        super.tick();
        List<ItemEntity> list = this.world.getEntitiesByClass(ItemEntity.class, this.getBoundingBox().expand(0.5D, 0.5D, 0.5D), RaiderElfEntity.PICKABLE_DROP_FILTER);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (itemStack.isEmpty() && !list.isEmpty()) {
            this.equipStack(EquipmentSlot.MAINHAND, list.get(0).getStack());
            list.get(0).discard();
        }
        if (world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) && !this.getMainHandStack().isEmpty()) {
            if (stashTicks >= 1200) {
                this.getMainHandStack().decrement(this.getMainHandStack().getCount());
                stashTicks = 0;
            } else
                ++stashTicks;
        }
    }

    protected void dropLoot(DamageSource source, boolean causedByPlayer) {
        super.dropLoot(source, causedByPlayer);
        if (!this.getMainHandStack().isEmpty()) {
            this.dropStack(this.getMainHandStack());
        }
    }

    static {
        PICKABLE_DROP_FILTER = (item) -> {
            return !item.cannotPickup() && item.isAlive();
        };
    }

    class PickUpItemsGoal extends Goal {
        public PickUpItemsGoal() {
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            if (!RaiderElfEntity.this.getMainHandStack().isEmpty()) {
                return false;
            } else {
                List<ItemEntity> list = RaiderElfEntity.this.world.getEntitiesByClass(ItemEntity.class, RaiderElfEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), RaiderElfEntity.PICKABLE_DROP_FILTER);
                return !list.isEmpty() && RaiderElfEntity.this.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
            }
        }

        public void tick() {
            List<ItemEntity> list = RaiderElfEntity.this.world.getEntitiesByClass(ItemEntity.class, RaiderElfEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), RaiderElfEntity.PICKABLE_DROP_FILTER);
            ItemStack itemStack = RaiderElfEntity.this.getEquippedStack(EquipmentSlot.MAINHAND);
            if (itemStack.isEmpty() && !list.isEmpty()) {
                RaiderElfEntity.this.getNavigation().startMovingTo((Entity)list.get(0), 1.2000000476837158D);
            }
        }

        public void start() {
            List<ItemEntity> list = RaiderElfEntity.this.world.getEntitiesByClass(ItemEntity.class, RaiderElfEntity.this.getBoundingBox().expand(8.0D, 8.0D, 8.0D), RaiderElfEntity.PICKABLE_DROP_FILTER);
            if (!list.isEmpty()) {
                RaiderElfEntity.this.getNavigation().startMovingTo((Entity)list.get(0), 1.2000000476837158D);
            }
        }

        public void stop() {

        }
    }
}
