package ac.stevano.core;

import ac.stevano.core.playerdata.PlayerData;
import ac.stevano.core.rank.Rank;
import org.bukkit.entity.Player;

public class CoreAPI {
    public static Rank getPlayerRank(Player player) {
        return Core.getCore().getPlayerDataManager().getPlayerData(player).getRank();
    }

    public static Rank getRank(String name) {
        return Core.getCore().getRankManager().getByName(name);
    }

    public static PlayerData getPlayerData(Player player) {
        return Core.getCore().getPlayerDataManager().getPlayerData(player);
    }

    public static boolean isStaffMode(Player player) {
        return Core.getCore().getModeManager().isInStaffMode(player);
    }

    public void setCustomScoreboard(boolean bool) {

    }
}
