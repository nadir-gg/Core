package ac.stevano.core.playerdata;

import ac.stevano.core.Core;
import ac.stevano.core.rank.Rank;
import ac.stevano.core.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Set;

public class PlayerDataListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void createPlayerData(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Core.getCore().getPlayerDataManager().createPlayerData(player);

        RankManager rankManager = Core.getCore().getRankManager();
        PlayerDataManager playerDataManager = Core.getCore().getPlayerDataManager();

        PlayerData data = playerDataManager.getPlayerData(player);
        Rank rank = data.getRank();

        player.getEffectivePermissions().clear();

        if (rank != null) {
            applyPermissions(player, rank.getPermissions());
        }

        applyPermissions(player, data.getPermissions());
    }

    private void applyPermissions(Player player, Set<Permission> permissions) {
        PlayerDataManager playerDataManager = Core.getCore().getPlayerDataManager();
        PlayerData data = playerDataManager.getPlayerData(player);
        Set<Permission> currentPermissions = data.getPermissions();

        PermissionAttachment attachment = player.addAttachment(Core.getCore());
        for (Permission permission : permissions) {
            attachment.setPermission(permission, true);
            if (currentPermissions.contains(permission)) return;
            data.addPermission(permission);
            data.save();
        }
    }
}
