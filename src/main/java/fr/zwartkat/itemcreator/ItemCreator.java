package fr.zwartkat.itemcreator;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import fr.zwartkat.itemcreator.api.SSkyblockAPI;
import fr.zwartkat.itemcreator.api.WorldGuardAPI;
import fr.zwartkat.itemcreator.commands.ItemCreatorCommand;
import fr.zwartkat.itemcreator.events.BlockListener;
import fr.zwartkat.itemcreator.events.CraftListener;
import fr.zwartkat.itemcreator.events.EffectAppliedListener;
import fr.zwartkat.itemcreator.events.PlayerListener;
import fr.zwartkat.itemcreator.items.ItemEffectLoader;
import fr.zwartkat.itemcreator.items.ItemLoader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import fr.zwartkat.itemcreator.config.*;

public final class ItemCreator extends JavaPlugin {

    private static ItemCreator instance;
    private static Configuration config,itemConfig;
    private static ItemLoader itemLoader;
    private static ItemEffectLoader effectLoader;
    private static WorldGuardAPI wg;
    private static SSkyblockAPI superiorSkyblock;
    @Override
    public void onEnable() {

        instance = this;
        wg = new WorldGuardAPI();
        superiorSkyblock = new SSkyblockAPI();

        config = new Configuration("config.yml");
        config.loadConfiguration();
        itemConfig = new Configuration("items.yml");
        itemConfig.loadConfiguration();
        effectLoader = new ItemEffectLoader();
        itemLoader = new ItemLoader();


        getCommand("itemcreator").setExecutor(new ItemCreatorCommand());
        getServer().getPluginManager().registerEvents(new PlayerListener(this),this);
        getServer().getPluginManager().registerEvents(new EffectAppliedListener(this),this);
        getServer().getPluginManager().registerEvents(new CraftListener(this),this);
        getServer().getPluginManager().registerEvents(new BlockListener(this),this);
        getLogger().info("Starting");
    }

    @Override
    public void onDisable() {
        getLogger().info("Shutdown");
    }

    public static ItemCreator getInstance(){
        return instance;
    }

    public static Configuration getPluginConfig(){
        return config;
    }
    public static ConfigurationSection getItemConfig(){ return itemConfig.getConfigurationSection("items"); }

    public static ItemLoader getItemLoader(){
        return  itemLoader;
    }
    public static ItemEffectLoader getEffectLoader(){ return effectLoader;}
    public static WorldGuardAPI getWg(){ return wg;}
    public static SSkyblockAPI getSuperiorSkyblock(){ return superiorSkyblock;}

    public static void reload(){
        config.loadConfiguration();
        itemConfig.loadConfiguration();
        itemLoader = new ItemLoader();
        effectLoader = new ItemEffectLoader();
        getInstance().getCommand("itemcreator").setExecutor(new ItemCreatorCommand());
    }
}
