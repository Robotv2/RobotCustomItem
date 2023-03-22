package fr.robotv2.robotcustomitems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.UUID;

public abstract class CustomItemStack extends ItemStack {

    public final static NamespacedKey CUSTOM_ITEM_KEY = new NamespacedKey("robot-custom-item", "custom-item");

    public CustomItemStack(ItemStack stack) {
        super(stack);
        register();
    }

    public CustomItemStack(Material material) {
        this(new ItemStack(material, 1));
    }

    public CustomItemStack(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    private void register() {
        Objects.requireNonNull(this.getItemMeta());
        this.getItemMeta().getPersistentDataContainer().set(CUSTOM_ITEM_KEY, PersistentDataType.STRING, UUID.randomUUID().toString());
    }

    /**
     * Called when a player is interacting with the custom item.
     */
    public void onInteract(PlayerInteractEvent event) { }

    /**
     * Called when a block is broken using the custom item.
     */
    public void onBlockBreak(BlockBreakEvent event) { }

    /**
     * Called when the custom item is dropped from an entity.
     */
    public void onDrop(EntityDropItemEvent event) { }

    /**
     * Called when an entity is damaged by another entity using the custom item.
     */
    public void onDamage(EntityDamageByEntityEvent event) { }
}
