package fr.zwartkat.itemcreator.events;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.items.ItemCreatorItem;
import fr.zwartkat.itemcreator.items.ItemDataUtil;
import fr.zwartkat.itemcreator.items.ItemEffect;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EffectAppliedListener implements Listener {

    private Plugin plugin;

    public EffectAppliedListener(Plugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEffectApplied(EffectAppliedEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        ItemCreatorItem itemCreatorItem = ItemDataUtil.getItem(item);
        ItemEffect effect = event.getEffect();
        if(itemCreatorItem.isUsable(item)){
            Integer newDurability = itemCreatorItem.removeDurability(item,1);
            if(newDurability != null && newDurability <= 0 && !itemCreatorItem.isUnbreakable()){
                player.getInventory().remove(item);
            }
        }
        else {
            String noDura = ItemCreator.getPluginConfig().getString("no_durability");
            player.sendMessage(ChatColorUtil.convert(noDura));
            event.getEvent().setCancelled(true);
        }
    }
}
