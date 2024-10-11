package fr.zwartkat.itemcreator.events;

import fr.zwartkat.itemcreator.items.ItemDataUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CraftListener implements Listener {

    private Plugin plugin;

    public CraftListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPrepareCraft(PrepareItemCraftEvent event){
        if(event.getRecipe().getResult().getType() != Material.AIR){
            for(ItemStack item : event.getInventory().getMatrix()){
                if(ItemDataUtil.isPluginItem(item)){
                    event.getInventory().setResult(new ItemStack(Material.AIR));
                }
            }
        }

    }
}
