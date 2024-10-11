package fr.zwartkat.itemcreator.api;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.zwartkat.itemcreator.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.annotation.Nullable;

public class WorldGuardAPI {

    private WorldGuardPlugin wgPlugin;
    private WorldEditPlugin wePlugin;

    public WorldGuardAPI() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Plugin pluginWg = pluginManager.getPlugin("WorldGuard");

        if (pluginWg instanceof WorldGuardPlugin) {
            this.wgPlugin = (WorldGuardPlugin) pluginWg;
        } else {
            ItemCreator.getInstance().getLogger().info("WorldGuard not installed, WorldGuard features disabled.");
        }

        Plugin pluginWe = pluginManager.getPlugin("WorldEdit");

        if (pluginWe instanceof WorldEditPlugin) {
            this.wePlugin = (WorldEditPlugin) pluginWe;
        } else {
            ItemCreator.getInstance().getLogger().info("WorldEdit not installed, WorldEdit features disabled");
        }
    }

    public boolean canPlayerPerformAction(Player player, StateFlag flagToCheck) {
        if (wgPlugin == null || wePlugin == null) {
            ItemCreator.getInstance().getLogger().warning("WorldGuard or WorldEdit is not installed!");
            return true;
        }

        Location loc = player.getLocation();
        com.sk89q.worldedit.util.Location position = BukkitAdapter.adapt(loc);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        if (container == null) {
            return false;
        }

        RegionQuery query = container.createQuery();

        StateFlag.State stateFlag = query.queryState(position, localPlayer, flagToCheck);

        if (stateFlag != null && stateFlag.equals(StateFlag.State.DENY)) {
            return false;
        }

        return true;
    }

    public boolean hasWorldGuard(){
        if(wgPlugin != null){
            return true;
        }
        else {
            return false;
        }
    }
}
