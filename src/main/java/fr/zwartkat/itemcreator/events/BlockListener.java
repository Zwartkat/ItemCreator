package fr.zwartkat.itemcreator.events;

import fr.zwartkat.itemcreator.items.ItemCreatorItem;
import fr.zwartkat.itemcreator.items.ItemDataUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.http.WebSocket;

public class BlockListener implements Listener {

    public BlockListener(JavaPlugin plugin){

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(ItemDataUtil.isPluginItem(item)){
            ItemCreatorItem itemCreatorItem = ItemDataUtil.getItem(item);
            if(itemCreatorItem.isPreventAction()){
                event.setCancelled(true);
            }
        }
    }
}
