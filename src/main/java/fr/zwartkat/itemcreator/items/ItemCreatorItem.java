package fr.zwartkat.itemcreator.items;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.exceptions.ItemCreationException;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import fr.zwartkat.itemcreator.utils.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class ItemCreatorItem {

    private String itemId;
    private ItemStack item;
    private Integer maxDurability;
    private Boolean unbreakable,stackable,preventAction;
    private Collection<ItemEffect> effects;
    private List<String> itemLore;

    public ItemCreatorItem(String itemId, ConfigurationSection itemSection) throws ItemCreationException{

        this.itemId = itemId;

        String materialName = itemSection.getString("material");
        if(materialName != null){

            Material material = Material.getMaterial(materialName);

            item = new ItemStack(material);
            ItemMeta itemMeta = item.getItemMeta();

            itemMeta.getPersistentDataContainer().set(Keys.IDKEY, PersistentDataType.STRING,itemId);

            String maxDurabilityStr = itemSection.getString("durability");
            if(maxDurabilityStr != null){
                this.maxDurability = Integer.parseInt(itemSection.getString("durability"));
            }
            else {
                this.maxDurability = null;
            }


            if(this.maxDurability != null && this.maxDurability != 0){
                itemMeta.setUnbreakable(true);
                itemMeta.getPersistentDataContainer().set(Keys.DURAKEY,PersistentDataType.INTEGER,this.maxDurability);
            } else {
                itemMeta.setUnbreakable(false);
            }

            String itemName = itemSection.getString("name");

            if(itemName != null){
                itemMeta.setDisplayName(ChatColorUtil.convert(itemName));
            }

            itemLore = (List<String>) itemSection.getList("lore");

            if(itemLore != null && !itemLore.isEmpty()){
                itemMeta.setLore(itemLore);
            }

            this.stackable = itemSection.getBoolean("stackable");
            this.unbreakable = itemSection.getBoolean("unbreakable");
            this.preventAction = itemSection.getBoolean("prevent_action");


            item.setItemMeta(itemMeta);

            ConfigurationSection effectsConfig = itemSection.getConfigurationSection("effects");
            if(effectsConfig != null && !effectsConfig.getKeys(false).isEmpty()){
                this.effects = new ArrayList<>();
                for(String effectKey : effectsConfig.getKeys(false)){
                    ConfigurationSection effectConfig = effectsConfig.getConfigurationSection(effectKey);
                    String effectName = effectConfig.getString("type");
                    if(effectName != null && ItemCreator.getEffectLoader().isEffect(effectName)){
                        Class effectClass = ItemCreator.getEffectLoader().getLoadedEffects().get(effectName);
                        try{
                            Constructor<?> constructor = effectClass.getDeclaredConstructor(ConfigurationSection.class);
                            ItemEffect effect = (ItemEffect) constructor.newInstance(effectConfig);
                            this.effects.add(effect);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            }



        } else {
          throw new ItemCreationException(itemId,"material");
        }

    }

    public String getItemId(){
        return this.itemId;
    }

    public boolean isStackable(){
        return this.stackable;
    }
    public boolean isPreventAction(){ return this.preventAction; }
    public void setUniqueId(ItemStack item){
        if(!this.stackable){
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer data = meta.getPersistentDataContainer();
            data.set(Keys.UNIQUEID,PersistentDataType.LONG, Instant.now().toEpochMilli() + new Random().nextInt(1000));
            item.setItemMeta(meta);
        }
    }

    public void updateLore(ItemStack item){
        if(itemLore != null && !itemLore.isEmpty()){

            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer itemDataContainer = itemMeta.getPersistentDataContainer();
            List<String> coloredLore = new ArrayList<>();

            for(String loreLine : itemLore){
                if(loreLine.contains("%durability%") || loreLine.contains("%max_durability%")){

                    if(itemDataContainer.has(Keys.DURAKEY,PersistentDataType.INTEGER)){
                        Integer durability = itemDataContainer.get(Keys.DURAKEY,PersistentDataType.INTEGER);
                        loreLine = loreLine.replace("%durability%",durability.toString());
                        loreLine = loreLine.replace("%max_durability%",maxDurability.toString());
                    }
                }
                String colored = ChatColorUtil.convert(loreLine);
                coloredLore.add(colored);
            }

            itemMeta.setLore(coloredLore);
            item.setItemMeta(itemMeta);
        }
    }

    public ItemStack give(){
        ItemStack itemGive = new ItemStack(this.item);
        this.updateLore(itemGive);
        return itemGive;
    }

    public Integer getDurability(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemDataContainer = itemMeta.getPersistentDataContainer();
        return itemDataContainer.get(Keys.DURAKEY,PersistentDataType.INTEGER);
    }

    public Integer addDurability(ItemStack item, int nb){
        if(nb > 0){

            Integer durability = this.getDurability(item) + nb;

            if(durability > this.maxDurability){
                durability = this.maxDurability;
            }

            this.setDurability(item,durability);
            return durability;
        }
        return null;
    }

    public Integer removeDurability(ItemStack item, int nb){
        if (nb > 0) {

            Integer durability = this.getDurability(item) - nb;

            if(durability < 0){
                durability = 0;
            } else if (durability > this.maxDurability){
                durability = this.maxDurability;
            }

            this.setDurability(item,durability);
            return durability;
        }
        return null;

    }

    public void setDurability(ItemStack item, int nb){
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer itemDataContainer = itemMeta.getPersistentDataContainer();
        itemDataContainer.set(Keys.DURAKEY,PersistentDataType.INTEGER,nb);
        item.setItemMeta(itemMeta);
        this.updateLore(item);
    }

    public void setMaxDurability(int newMaxDurability){
        this.maxDurability = newMaxDurability;
    }

    public Boolean isUnbreakable(){
        return this.unbreakable;
    }

    public boolean isUsable(ItemStack item){
        if(this.getDurability(item) > 0){
            return true;
        } else {
            return  false;
        }

    }

    public Collection<ItemEffect> getEffects(){
        return this.effects;
    }

    public void useEffects(ItemStack item, Event event){
        for(ItemEffect effect : this.effects){
            effect.applyEffect(item,event);
        }
    }


}
