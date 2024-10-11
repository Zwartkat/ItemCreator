package fr.zwartkat.itemcreator.items;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.util.*;

public class ItemLoader {

    private HashMap<String, ItemCreatorItem> loadedItems;

    public ItemLoader(){
        loadedItems = new HashMap<>();
        ConfigurationSection itemsSection = ItemCreator.getItemConfig();
        Set<String> itemsId = itemsSection.getKeys(false);

        if(!itemsId.isEmpty()){
            for(String itemId : itemsId){
                ConfigurationSection itemConfig = itemsSection.getConfigurationSection(itemId);

                    ItemCreatorItem item = new ItemCreatorItem(itemId,itemConfig);
                    loadedItems.put(item.getItemId(),item);

            }
        }
        else{
            ItemCreator.getInstance().getLogger().warning("There is any items to load");
        }

    }


    public HashMap<String,ItemCreatorItem> getLoadedItems(){
        return loadedItems;
    }

    public List<String> getLoadedItemsId(){

        ItemCreator.getInstance().getLogger().info(loadedItems.keySet().toString());
        return new ArrayList<>(loadedItems.keySet());
    }
}
