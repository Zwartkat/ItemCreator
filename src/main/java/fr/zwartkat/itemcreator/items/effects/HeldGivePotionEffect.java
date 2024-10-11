package fr.zwartkat.itemcreator.items.effects;

import fr.zwartkat.itemcreator.items.ItemEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public class HeldGivePotionEffect implements ItemEffect {

    private final String EFFECT = "held_give_effect";
    @Override
    public void setParameterEffect(ConfigurationSection effectConfig) {

    }

    @Override
    public void applyEffect(ItemStack item, Event event) {

    }

    @Override
    public String getEffectId() {
        return null;
    }
}
