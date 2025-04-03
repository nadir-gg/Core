package ac.stevano.core.rank;

import ac.stevano.core.Core;
import ac.stevano.core.utils.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class cmdRank implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CC.RED + "Player only.");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("core.command.rank")) {
            player.sendMessage(CC.RED + "No permission.");
            return false;
        }

        if (args.length < 1) {
            displayRankHelp(player);
            return false;
        }

        RankManager rankManager = Core.getCore().getRankManager();
        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage(CC.RED + "Usage: /rank create <name>");
                    return false;
                }
                String createName = args[1];
                if (rankManager.getByName(createName) != null) {
                    player.sendMessage(CC.RED + "A rank with this name already exists.");
                    return false;
                }
                rankManager.createRank(createName);
                player.sendMessage(CC.GREEN + "Created rank: " + CC.WHITE + createName);
                rankManager.editRank(createName).load();
                break;

            case "delete":
                if (args.length < 2) {
                    player.sendMessage(CC.RED + "Usage: /rank delete <name>");
                    return false;
                }
                String deleteName = args[1];
                Rank deleteRank = rankManager.getByName(deleteName);
                if (deleteRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                File file = new File(rankManager.getDirectory(), deleteName + ".yml");
                if (file.exists()) {
                    file.delete();
                }
                rankManager.getRanks().remove(deleteRank);
                player.sendMessage(CC.GREEN + "Deleted rank: " + CC.AQUA + deleteName);
                break;
            case "addperm":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank addperm <name> <permission>");
                    return false;
                }
                Rank addPermRank = rankManager.getByName(args[1]);
                if (addPermRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                addPermRank.addPermission(new Permission(args[2]));
                addPermRank.save();
                player.sendMessage(CC.GREEN + "Added permission " + CC.AQUA + args[2] + CC.GREEN + " to rank " + addPermRank.getDisplayName());
                break;

            case "removeperm":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank removeperm <name> <permission>");
                    return false;
                }
                Rank removePermRank = rankManager.getByName(args[1]);
                if (removePermRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                removePermRank.removePermission(new Permission(args[2]));
                removePermRank.save();
                player.sendMessage(CC.GREEN + "Removed permission " + CC.AQUA + args[2] + CC.GREEN + " from rank " + removePermRank.getDisplayName());
                break;
            case "info":
                if (args.length < 2) {
                    player.sendMessage(CC.RED + "Usage: /rank info <name>");
                    return false;
                }
                Rank infoRank = rankManager.getByName(args[1]);
                if (infoRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.AQUA + "Rank Information: " + infoRank.getDisplayName());
                player.sendMessage(CC.GRAY + "Name Color: " + ChatColor.valueOf(infoRank.getNameColor()) + infoRank.getNameColor());
                player.sendMessage(CC.GRAY + "Chat Color: " + ChatColor.valueOf(infoRank.getChatColor()) + infoRank.getChatColor());
                player.sendMessage(CC.GRAY + "Prefix: " + CC.WHITE + CC.translate(infoRank.getPrefix() == null ? "&cNo prefix" : infoRank.getPrefix()));
                player.sendMessage(CC.GRAY + "Suffix: " + CC.WHITE + CC.translate(infoRank.getSuffix() == null ? "&cNo suffix" : infoRank.getSuffix()));
                player.sendMessage(CC.GRAY + "Weight: " + CC.YELLOW + infoRank.getWeight());
                player.sendMessage(CC.GRAY + "Permissions: " + CC.WHITE + String.join(", ", (infoRank.hasPermissions() ? infoRank.permissionsToSTringlist(infoRank.getPermissions()).toString() : "&cNo permissions")));
                player.sendMessage(CC.CHAT_BAR);
                break;
            case "prefix":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank prefix <name> <prefix>");
                    return false;
                }
                Rank prefixRank = rankManager.getByName(args[1]);
                if (prefixRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                String prefix = args[2].replace("_", " ");
                prefixRank.setPrefix(prefix);
                prefixRank.save();
                player.sendMessage(CC.GREEN + "Set prefix of " + prefixRank.getDisplayName() + CC.GREEN + " to " + CC.AQUA + CC.translate(prefix));
                break;

            case "suffix":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank suffix <name> <suffix>");
                    return false;
                }
                Rank suffixRank = rankManager.getByName(args[1]);
                if (suffixRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                String suffix = args[2].replace("_", " ");
                suffixRank.setSuffix(suffix);
                suffixRank.save();
                player.sendMessage(CC.GREEN + "Set suffix of " + suffixRank.getDisplayName() + CC.GREEN + " to " + CC.AQUA + CC.translate(suffix));
                break;

            case "cc":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank cc <name> <chatcolor>");
                    return false;
                }
                Rank ccRank = rankManager.getByName(args[1]);
                if (ccRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                String ccInput = args[2].toUpperCase();

                try {
                    ChatColor color = ChatColor.valueOf(ccInput);
                    ccRank.setChatColor(color.name());
                    ccRank.save();
                    player.sendMessage(CC.GREEN + "Set chat color of " + ccRank.getDisplayName() + CC.GREEN + " to " + color + color.name() + CC.GREEN + ".");
                } catch (IllegalArgumentException e) {
                    player.sendMessage(CC.RED + "Invalid chat color. Use one of: " + validChatColors());
                }
                break;
            case "nc":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank nc <name> <namecolor>");
                    return false;
                }
                Rank ncRank = rankManager.getByName(args[1]);
                if (ncRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                String ncInput = args[2].toUpperCase();

                try {
                    ChatColor color = ChatColor.valueOf(ncInput);
                    ncRank.setNameColor(color.name());
                    ncRank.save();
                    player.sendMessage(CC.GREEN + "Set the name color of " + ncRank.getDisplayName() + CC.GREEN + " to " + color + color.name() + CC.GREEN + ".");
                } catch (IllegalArgumentException e) {
                    player.sendMessage(CC.RED + "Invalid chat color. Use one of: " + validChatColors());
                }
                break;
            case "weight":
                if (args.length < 3) {
                    player.sendMessage(CC.RED + "Usage: /rank weight <name> <weight>");
                    return false;
                }
                Rank weightRank = rankManager.getByName(args[1]);
                if (weightRank == null) {
                    player.sendMessage(CC.RED + "Rank not found.");
                    return false;
                }
                try {
                    int weight = Integer.parseInt(args[2]);
                    weightRank.setWeight(weight);
                    weightRank.save();
                    player.sendMessage(CC.GREEN + "Set weight of " + weightRank.getDisplayName() + CC.GREEN + " to " + CC.YELLOW + weight + CC.GREEN + ".");
                } catch (NumberFormatException e) {
                    player.sendMessage(CC.RED + "Weight must be a number.");
                }
                break;
            case "reload":
                for (Rank rank : rankManager.getRanks()) {
                    rank.load();
                }
                player.sendMessage(CC.GRAY + CC.ITALIC + "Reloaded " + rankManager.getRanks().size() + " ranks.");
                break;

            case "list":
                Set<Rank> ranks = Core.getCore().getRankManager().getRanks();

                if (ranks.isEmpty()) {
                    player.sendMessage(CC.RED + "No ranks available.");
                    return false;
                }

                List<Rank> sortedRanks = new ArrayList<>(ranks);
                sortedRanks.sort((r1, r2) -> Integer.compare(r2.getWeight(), r1.getWeight()));

                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.BLUE + CC.BOLD + "Rank List (Highest to Lowest)");

                for (Rank rank : sortedRanks) {
                    player.sendMessage(CC.GRAY + "- " + rank.getDisplayName() + CC.WHITE +
                            " (Weight: " + CC.YELLOW + rank.getWeight() + CC.WHITE + ")");
                }

                player.sendMessage(CC.CHAT_BAR);
                break;
            default:
                displayRankHelp(player);
                break;
        }
        return true;
    }

    private String validChatColors() {
        StringBuilder colors = new StringBuilder();
        for (ChatColor color : ChatColor.values()) {
            if (color.isColor()) {
                String name = color + color.name() + CC.RESET;
                colors.append(name).append(", ");
            }
        }
        return colors.substring(0, colors.length() - 2);
    }


    public void displayRankHelp(Player p) {
        p.sendMessage(CC.CHAT_BAR);
        p.sendMessage(CC.BLUE + CC.BOLD.toString() + "Rank Commands");
        p.sendMessage("");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank create <name>" + CC.GRAY + " - " + CC.WHITE + "Creates a new rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank delete <name>" + CC.GRAY + " - " + CC.WHITE + "Deletes an existing rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank prefix <name> <prefix>" + CC.GRAY + " - " + CC.WHITE + "Sets the prefix for a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank suffix <name> <suffix>" + CC.GRAY + " - " + CC.WHITE + "Sets the suffix for a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank cc <name> <chatcolor>" + CC.GRAY + " - " + CC.WHITE + "Sets the chat color for a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank nc <name> <namecolor>" + CC.GRAY + " - " + CC.WHITE + "Sets the name color for a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank weight <name> <weight>" + CC.GRAY + " - " + CC.WHITE + "Sets the weight (priority) for a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank list" + CC.GRAY + " - " + CC.WHITE + "Shows all available ranks.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank addperm <name> <permission>" + CC.GRAY + " - " + CC.WHITE + "Adds a permission to a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank removeperm <name> <permission>" + CC.GRAY + " - " + CC.WHITE + "Removes a permission from a rank.");
        p.sendMessage(CC.GRAY + CC.BOLD + "* " + CC.AQUA + "/rank info <name>" + CC.GRAY + " - " + CC.WHITE + "Displays detailed information about a rank.");
        p.sendMessage(CC.CHAT_BAR);
    }
}
