package ac.stevano.core.command;

import ac.stevano.core.Core;
import ac.stevano.core.playerdata.PlayerDataManager;
import ac.stevano.core.rank.Rank;
import ac.stevano.core.rank.RankManager;
import ac.stevano.core.utils.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class cmdList implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("core.command.list")) {
            sender.sendMessage(CC.RED + "No permission.");
            return false;
        }

        RankManager rankManager = Core.getCore().getRankManager();
        PlayerDataManager playerDataManager = Core.getCore().getPlayerDataManager();
        Set<Rank> ranks = rankManager.getRanks();
        List<Rank> sortedRanks = new ArrayList<>(ranks);
        sortedRanks.sort((r1, r2) -> Integer.compare(r2.getWeight(), r1.getWeight()));

        // Format rank names
        String rankList = sortedRanks.stream()
                .map(Rank::getDisplayName)
                .collect(Collectors.joining(CC.RESET + ", "));

        // Get and sort online players by rank weight
        List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        onlinePlayers.sort((p1, p2) -> {
            Rank r1 = playerDataManager.getPlayerData(p1).getRank();
            Rank r2 = playerDataManager.getPlayerData(p2).getRank();
            return Integer.compare(r2 != null ? r2.getWeight() : 0, r1 != null ? r1.getWeight() : 0);
        });

        // Format player list with rank name colors
        String playerList = onlinePlayers.stream()
                .map(player -> {
                    Rank rank = playerDataManager.getPlayerData(player).getRank();
                    String color = (rank != null) ? String.valueOf(CC.getColorFromName(rank.getNameColor())) : CC.WHITE;
                    return color + player.getName() + CC.RESET;
                })
                .collect(Collectors.joining(", "));

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage( rankList);
        sender.sendMessage(CC.GRAY + "(" + onlinePlayers.size() + "/" + Bukkit.getMaxPlayers() + "): " + CC.WHITE + playerList);
        sender.sendMessage(CC.CHAT_BAR);

        return true;
    }
}