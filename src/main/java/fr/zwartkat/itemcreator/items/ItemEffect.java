package fr.zwartkat.itemcreator.items;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public interface ItemEffect {

    void setParameterEffect(ConfigurationSection effectConfig);
    void applyEffect(ItemStack item, Event event);
    String getEffectId();
}
