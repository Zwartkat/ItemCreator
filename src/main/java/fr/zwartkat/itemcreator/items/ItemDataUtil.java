package fr.zwartkat.itemcreator.items;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import fr.zwartkat.itemcreator.utils.Keys;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ItemDataUtil {

    /*public static void setPersitantData(ItemStack item, String itemId){
        if(itemId != null){

            ConfigurationSection itemConfig = ItemCreator.getItemConfig().getConfigurationSection(itemId);
            ConfigurationSection effectsConfig = itemConfig.getConfigurationSection("effects");
            List<String> effectsId = new ArrayList<>();
            if(effectsConfig != null){
                Set<String> keys = effectsConfig.getKeys(false);
                effectsId = new ArrayList<>(keys);
            }

            Integer maxDurability = itemConfig.getInt("durability");
            Bukkit.broadcastMessage(maxDurability.toString());
            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(Keys.IDKEY, PersistentDataType.STRING,itemId);

            if(maxDurability != 0){
                meta.setUnbreakable(true);
                meta.getPersistentDataContainer().set(Keys.MAXDURAKEY,PersistentDataType.INTEGER,maxDurability);
                if(!meta.getPersistentDataContainer().has(Keys.DURAKEY,PersistentDataType.INTEGER)){
                    meta.getPersistentDataContainer().set(Keys.DURAKEY,PersistentDataType.INTEGER,maxDurability);
                }
            }
            else {
                meta.setUnbreakable(false);
            }

            item.setItemMeta(meta);
            updateLore(item);
        }
    }
*/


    public static Boolean isPluginItem(ItemStack item){
        if(item != null && !item.getType().isAir()){
            if(item.getItemMeta().getPersistentDataContainer().has(Keys.IDKEY)){
                if(ItemCreator.getItemConfig().getKeys(false).contains(item.getItemMeta().getPersistentDataContainer().get(Keys.IDKEY,PersistentDataType.STRING))){
                    return true;
                }
            }
        }
        return false;
    }

    public static ItemCreatorItem getItem(ItemStack item){
        if(isPluginItem(item)){
            ItemCreatorItem itemCreatorItem = ItemCreator.getItemLoader().getLoadedItems().get(getItemId(item));
            return itemCreatorItem;
        }
        else {
            return null;
        }
    }

    public static String getItemId(ItemStack item){
        String itemId = null;
        if(isPluginItem(item)){
            itemId = item.getItemMeta().getPersistentDataContainer().get(Keys.IDKEY,PersistentDataType.STRING);
        }
        return itemId;
    }


    public static Boolean isUsable(ItemStack item){
        ItemCreatorItem itemCreatorItem = getItem(item);
        return itemCreatorItem.isUsable(item);
    }

    public static void removeDurability(ItemStack item, Integer nb){
        ItemCreatorItem itemCreatorItem = getItem(item);
        itemCreatorItem.removeDurability(item,nb);
        if(!itemCreatorItem.isUnbreakable()){
            if(itemCreatorItem.getDurability(item) != null && itemCreatorItem.getDurability(item) <= 0){

            }
        }
    }

/*
    public static List<String> getEffects(ItemStack item){
        List<String> effects = new ArrayList<>();
        if(hasEffect(item)){
            effects = List.of(item.getItemMeta().getPersistentDataContainer().get(Keys.EFFECTKEY,PersistentDataType.STRING).split(","));
        }
        return effects;
    }

    public static Boolean hasEffect(ItemStack item){
        if(item != null){
            if(item.getItemMeta().getPersistentDataContainer().has(Keys.EFFECTKEY)){
                return true;
            }
        }
        return false;
    }

    public static ItemCreatorItem getItemConfig(ItemStack item){
        String itemId = getItemId(item);
        return ItemCreator.getItemLoader().getLoadedItems().get(itemId);
    }

    public static void useEffect(ItemStack item,Event event){
        ItemCreatorItem itemConfig = getItemConfig(item);
        for(ItemEffect effect : (List<ItemEffect>) itemConfig.getEffects()){
            effect.applyEffect(item,event);
        }
    }

    public static void updateLore(ItemStack item){
        if(isPluginItem(item)){

            String id = getItemId(item);
            ItemMeta meta = item.getItemMeta();
            List<String> itemLore = ItemCreator.getItemConfig().getConfigurationSection(id).getStringList("lore");

            if(!itemLore.isEmpty()){

                List<String> coloredLore = new ArrayList<>();

                for(String loreLine : itemLore){
                    if(loreLine.contains("%durability%") || loreLine.contains("%max_durability%")){
                        if(meta.getPersistentDataContainer().has(Keys.DURAKEY,PersistentDataType.INTEGER)){
                            Integer durability = meta.getPersistentDataContainer().get(Keys.DURAKEY,PersistentDataType.INTEGER);
                            Integer maxDurability;
                            if(ItemCreator.getItemLoader() != null){
                                ItemMeta configItemMeta = ((ItemStack) ItemCreator.getItemLoader().getLoadedItems().get(meta.getPersistentDataContainer().get(Keys.IDKEY,PersistentDataType.STRING)).give()).getItemMeta();
                                maxDurability = configItemMeta.getPersistentDataContainer().get(Keys.MAXDURAKEY,PersistentDataType.INTEGER);
                            }
                            else {
                                maxDurability = ItemCreator.getItemConfig().getConfigurationSection(id).getInt("durability");
                            }
                            loreLine = loreLine.replace("%durability%",durability.toString());
                            loreLine = loreLine.replace("%max_durability%",maxDurability.toString());
                        }
                    }
                    String colored = ChatColorUtil.convert(loreLine);
                    coloredLore.add(colored);
                }

                meta.setLore(coloredLore);

                item.setItemMeta(meta);
            }
        }
    }


    public static int getDurability(ItemStack item){
        if(item.getItemMeta().getPersistentDataContainer().has(Keys.DURAKEY)){
            return item.getItemMeta().getPersistentDataContainer().get(Keys.DURAKEY,PersistentDataType.INTEGER);
        }
        return -1;
    }

    public static void removeDurability(ItemStack item, Integer nb){
        if(isPluginItem(item) && getDurability(item) > 0){
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            if(!data.has(Keys.DURAKEY)){
                data.set(Keys.DURAKEY,PersistentDataType.INTEGER,data.get(Keys.MAXDURAKEY,PersistentDataType.INTEGER));
            }
            Integer new_dura = data.get(Keys.DURAKEY,PersistentDataType.INTEGER)-nb;
            if(new_dura < 0){
                new_dura = 0;
            }
            data.set(Keys.DURAKEY,PersistentDataType.INTEGER, new_dura);
            item.setItemMeta(meta);
            updateLore(item);
        }
    }

    /*public void addDurability(Integer nb){
        this.durability = this.durability + nb;
        if(this.durability > this.maxDurability){
            this.durability = this.maxDurability;
        }
        this.updateLore();
    }*/
}
