package fr.zwartkat.itemcreator.items.effects;

import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.api.WorldGuardAPI;
import fr.zwartkat.itemcreator.events.EffectAppliedEvent;
import fr.zwartkat.itemcreator.items.ItemCreatorItem;
import fr.zwartkat.itemcreator.items.ItemDataUtil;
import fr.zwartkat.itemcreator.items.ItemEffect;
import fr.zwartkat.itemcreator.utils.Keys;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Collections;

public class GivePotionEffect implements ItemEffect {

    private static final String EFFECT_ID = "give_effect";
    private static final String DEFAULT_ACTION = "DAMAGE";
    private static final String DEFAULT_EFFECT = "SLOWNESS";
    private static final String SB_PRIVILEGE_CLICK = "ITC_ENTITY_CLICK";
    private static final int DEFAULT_AMPLIFIER = 0;
    private static final int DEFAULT_TIME = -1;
    private String action,effect;
    private Integer amplifier,time;

    public GivePotionEffect(ConfigurationSection effectConfig){
        this.setParameterEffect(effectConfig);
    }

    @Override
    public void setParameterEffect(ConfigurationSection effectConfig) {
        this.action = effectConfig.getString("action",DEFAULT_ACTION);
        this.effect = effectConfig.getString("effect",DEFAULT_EFFECT);
        this.amplifier = effectConfig.getInt("amplifier",DEFAULT_AMPLIFIER);
        this.time = effectConfig.getInt("time",DEFAULT_TIME);
    }

    @Override
    public synchronized void applyEffect(ItemStack item, Event sendEvent) {
        if(!(sendEvent instanceof Cancellable)) return;

        Cancellable event = (Cancellable) sendEvent;

        switch (this.action.toUpperCase()){
            case "DAMAGE":
                if(event instanceof EntityDamageByEntityEvent){
                    this.handleDamageEvent((EntityDamageByEntityEvent) event);
                }
                break;
            case "CLICK":
                if(event instanceof PlayerInteractEntityEvent){
                    this.handleClickEvent((PlayerInteractEntityEvent) event);
                }
                break;
            default:
                ((Cancellable) event).setCancelled(true);
                break;
            }
    }

    private void handleDamageEvent(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;

        Player player = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();
        StateFlag flagToCheck = getFlagForEntity(entity);

        if(flagToCheck != null && !ItemCreator.getWg().canPlayerPerformAction(player,flagToCheck)) {
            event.setCancelled(true);
            return;
        }

        callEffectAppliedEvent(player,event);

        if(!event.isCancelled()){
            this.applyPotionEffect(entity);
        }
    }

    private StateFlag getFlagForEntity(LivingEntity entity) {
        if(!ItemCreator.getWg().hasWorldGuard()) return null;
        if(entity instanceof Player) return Flags.PVP;
        if(entity instanceof Animals) return Flags.DAMAGE_ANIMALS;
        return Flags.MOB_DAMAGE;
    }

    private void callEffectAppliedEvent(Player player, Cancellable event) {
        ItemCreator.getInstance().getServer().getPluginManager().callEvent(
                new EffectAppliedEvent(player,player.getInventory().getItemInMainHand(),this,event)
        );
    }

    private void handleClickEvent(PlayerInteractEntityEvent event){

        if(!(event.getRightClicked() instanceof  LivingEntity)) return;

        Player player = event.getPlayer();
        LivingEntity clickedEntity = (LivingEntity) event.getRightClicked();

        if(ItemCreator.getSuperiorSkyblock().hasSuperiorSkyblock()){
            if(!ItemCreator.getSuperiorSkyblock().canPerformAction(player,SB_PRIVILEGE_CLICK)) return;
        }
        if(clickedEntity.hasPotionEffect(PotionEffectType.getByName(effect))) return;

        this.callEffectAppliedEvent(player, event);

        if(!event.isCancelled()) {
            this.applyPotionEffect(clickedEntity);
        }
    }

    private void applyPotionEffect(LivingEntity entity){
        PotionEffectType potionEffectType = PotionEffectType.getByName(effect);

        if(potionEffectType == null || entity.hasPotionEffect(potionEffectType)) return;

        PotionEffect potionEffect = new PotionEffect(
                potionEffectType,
                time == DEFAULT_TIME ? PotionEffect.INFINITE_DURATION : time * 20,
                amplifier
        );

        entity.addPotionEffect(potionEffect);
    }
    
    @Override
    public String getEffectId() {
        return EFFECT_ID;
    }
}
