package ac.stevano.core.command;

import ac.stevano.core.Core;
import ac.stevano.core.rank.Rank;
import ac.stevano.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cmdGrant implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.command.grant")) {
            sender.sendMessage(CC.RED + "No permission.");
            return false;
        }

        if (args.length != 2) {
            sender.sendMessage(CC.RED + "Usage: /grant <player> <rank>");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(CC.YELLOW + args[0] + CC.RED + " is not online.");
            return false;
        }

        Rank rank = Core.getCore().getRankManager().getByName(args[1]);
        if (rank == null) {
            sender.sendMessage(CC.RED + "The rank '" + CC.YELLOW + args[1] + CC.RED + "' does not exist.");
            return false;
        }

        setRank(sender, target, rank);
        return false;
    }

    public void setRank(CommandSender granter, Player receiver, Rank rank) {
        Core.getCore().getPlayerDataManager().getPlayerData(receiver).setRank(rank);
        Core.getCore().getPlayerDataManager().refreshData(receiver);

        granter.sendMessage(CC.translate("&aYou have updated &e" + receiver.getName() + "&a's rank to&r " + rank.getDisplayName() + "&a."));
        receiver.sendMessage(CC.translate("&eYour rank has been updated to&r " + rank.getDisplayName() + "&e."));
    }
}
