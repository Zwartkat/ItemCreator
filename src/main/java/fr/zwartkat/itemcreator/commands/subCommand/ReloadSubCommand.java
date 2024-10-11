package fr.zwartkat.itemcreator.commands.subCommand;

import fr.zwartkat.itemcreator.ItemCreator;
import fr.zwartkat.itemcreator.commands.SubCommand;
import fr.zwartkat.itemcreator.commands.SubCommandExecutionInterface;
import fr.zwartkat.itemcreator.utils.ChatColorUtil;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommand {
    public ReloadSubCommand() {
        super("reload", "itemcreator.reload", "To reload the configuration", "/itc reload");
        SubCommandExecutionInterface<CommandSender, String, String[], Object> reloadAction =
                (CommandSender sender, String label, String[] args) -> {
                    ItemCreator.reload();
                    sender.sendMessage(ChatColorUtil.convert(ItemCreator.getPluginConfig().getString("reload")));
                };

        this.setSubCommandAction(reloadAction);
    }
}
