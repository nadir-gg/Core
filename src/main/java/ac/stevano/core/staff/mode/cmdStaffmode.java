package ac.stevano.core.staff.mode;

import ac.stevano.core.Core;
import ac.stevano.core.utils.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdStaffmode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Player only.");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("core.staffmode")) {
            player.sendMessage(CC.RED + "No permission.");
            return false;
        }

        if (Core.getCore().getModeManager().isInStaffMode(player)) Core.getCore().getModeManager().removeFromStaffMode(player);
        else Core.getCore().getModeManager().putInStaffMode(player);
        return false;
    }
}
