package fr.zwartkat.itemcreator.api;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblock;
import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.island.PermissionNode;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class SSkyblockAPI {

    private SuperiorSkyblock superiorSkyblock;

    public SSkyblockAPI(){
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        Plugin pluginSSkyblock = pluginManager.getPlugin("SuperiorSkyblock2");

        if(pluginSSkyblock instanceof SuperiorSkyblock){
            this.superiorSkyblock = (SuperiorSkyblock) pluginSSkyblock;
            IslandPrivilege.register("ITC_ENTITY_CLICK");
        } else {
            ItemCreator.getInstance().getLogger().info("SuperiorSkyblock not installed, SuperiorSkyblock features disabled");
        }

    }

    public boolean hasSuperiorSkyblock(){
        if(this.superiorSkyblock != null) return true;
        return false;
    }

    public void addPrivilege(String name){
        IslandPrivilege.register(ChatColorUtil.convert(name));
    }

    public boolean canPerformAction(Player player, String privilegeName){
        Island island = SuperiorSkyblockAPI.getIslandAt(player.getLocation());
        if(island == null) return true;
        SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(player.getUniqueId());
        if(superiorPlayer.hasBypassModeEnabled()) return true;
        PermissionNode permissionNode = island.getPermissionNode(superiorPlayer);
        player.sendMessage(permissionNode.hasPermission(IslandPrivilege.getByName(privilegeName))+"");
        if(permissionNode.hasPermission(IslandPrivilege.getByName(privilegeName))) return true;
        return false;
    }
}
