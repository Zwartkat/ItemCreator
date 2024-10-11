package fr.zwartkat.itemcreator.events;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.items.ItemCreatorItem;
import fr.zwartkat.itemcreator.items.ItemDataUtil;
import fr.zwartkat.itemcreator.items.ItemEffect;
import fr.zwartkat.itemcreator.items.ItemEffectLoader;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import fr.zwartkat.itemcreator.utils.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {

    public PlayerListener(JavaPlugin plugin) {

    }

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            if (ItemDataUtil.isPluginItem(heldItem)) {
                ItemDataUtil.getItem(heldItem).useEffects(heldItem,event);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Entity entity = event.getRightClicked();
        if (ItemDataUtil.isPluginItem(heldItem)) {
            if(ItemDataUtil.isUsable(heldItem)){
                ItemDataUtil.getItem(heldItem).useEffects(heldItem,event);
            }
            else {
                player.sendMessage(ChatColorUtil.convert(ItemCreator.getPluginConfig().getString("no_durability")));
            }
            event.setCancelled(true);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(ItemDataUtil.isPluginItem(item)){
            ItemCreatorItem itemCreatorItem = ItemDataUtil.getItem(item);
            if(itemCreatorItem.isPreventAction()){
                event.setCancelled(true);
                event.getPlayer().setCooldown(item.getType(), 0);
            }
        }
    }
}
