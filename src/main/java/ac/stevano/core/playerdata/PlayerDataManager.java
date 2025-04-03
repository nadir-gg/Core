package ac.stevano.core.playerdata;

import ac.stevano.core.Core;
import ac.stevano.core.rank.Rank;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private Map<UUID, PlayerData> playerDataMap;
    private final File directory = new File(Core.getCore().getDataFolder() + File.separator + "playerdata");

    public PlayerDataManager() {
        playerDataMap = new HashMap<>();
    }

    public void startup() {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    public void slowdown() {
        for (PlayerData playerData : playerDataMap.values()) {
            playerData.save();
        }
    }

    public void createPlayerData(Player player) {
        PlayerData data = new PlayerData(player.getUniqueId());
        Rank rank = Core.getCore().getRankManager().getDefaultRank();

        if (data.firstTime()) data.setRank(rank);
        if (data.firstTime()) if (rank.hasPermissions()) data.setPermissions(rank.getPermissions());
        if (data.firstTime()) System.out.println("Created player data for " + player.getName() + " (" + player.getUniqueId() + ")");

        if (data.firstTime()) data.save();

        data.load();
        playerDataMap.put(player.getUniqueId(), data);
    }

    public PlayerData getPlayerData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public File getDirectory() {
        return directory;
    }

    public void refreshData(Player player) {
        Core.getCore().getPlayerDataManager().getPlayerData(player).save();

        new BukkitRunnable() {
            @Override
            public void run() {
                Core.getCore().getPlayerDataManager().getPlayerData(player).load();
            }
        }.runTaskLater(Core.getCore(), 1L);
    }
}
