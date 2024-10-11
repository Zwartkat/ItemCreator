package fr.zwartkat.itemcreator.events;

import fr.zwartkat.itemcreator.items.ItemEffect;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EffectAppliedEvent extends Event{

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemEffect effect;
    private final ItemStack item;
    private final Cancellable event;

    public EffectAppliedEvent(Player player, ItemStack item, ItemEffect effect, Cancellable event){
        this.player = player;
        this.item = item;
        this.effect = effect;
        this.event = event;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer(){
        return player;
    }

    public ItemEffect getEffect(){
        return effect;
    }

    public ItemStack getItem(){
        return item;
    }
    public Cancellable getEvent() { return event;}
}
