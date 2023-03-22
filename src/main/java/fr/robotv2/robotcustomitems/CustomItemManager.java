package fr.robotv2.robotcustomitems;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.robotv2.robotcustomitems.CustomItemStack.CUSTOM_ITEM_KEY;

public class CustomItemManager {

    private static final AtomicBoolean REGISTERED = new AtomicBoolean(false);

    private CustomItemManager() {
        throw new UnsupportedOperationException();
    }

    /**
     * Register listeners for RobotCustomItems.
     *
     * @param plugin plugin to register
     * @throws NullPointerException if plugin is null
     * @throws IllegalStateException if RobotCustomItems is already registered
     */
    public static void register(Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin");

        if (REGISTERED.getAndSet(true)) {
            throw new IllegalStateException("FastInv is already registered");
        }

        Bukkit.getPluginManager().registerEvents(new CustomItemListeners(), plugin);
    }

    public static boolean isCustomItem(@Nullable ItemStack stack) {

        if(stack == null || stack.getItemMeta() == null) {
            return false;
        }

        return stack.getItemMeta().getPersistentDataContainer().has(CUSTOM_ITEM_KEY, PersistentDataType.STRING);
    }

    private static class CustomItemListeners implements Listener {

        @EventHandler
        public void onInteract(PlayerInteractEvent event) {
            if(event.getItem() instanceof CustomItemStack) {
                ((CustomItemStack) event.getItem()).onInteract(event);
            }
        }

        @EventHandler
        public void onBreak(BlockBreakEvent event) {
            final ItemStack heldItem = event.getPlayer().getInventory().getItem(event.getPlayer().getInventory().getHeldItemSlot());
            if(heldItem instanceof CustomItemStack) {
                ((CustomItemStack) heldItem).onBlockBreak(event);
            }
        }

        @EventHandler
        public void onDrop(EntityDropItemEvent event) {
            if(event.getItemDrop() instanceof CustomItemStack) {
                ((CustomItemStack) event.getItemDrop()).onDrop(event);
            }
        }

        @EventHandler
        public void onDamage(EntityDamageByEntityEvent event) {

            if(!(event.getDamager() instanceof LivingEntity)) {
                return;
            }

            final LivingEntity livingEntity = (LivingEntity) event.getDamager();

            if(livingEntity.getEquipment() == null) {
                return;
            }

            final ItemStack heldItem = livingEntity.getEquipment().getItemInMainHand();

            if(heldItem instanceof CustomItemStack) {
                ((CustomItemStack) heldItem).onDamage(event);
            }
        }
    }
}
