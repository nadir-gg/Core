package ac.stevano.core.listener;

import ac.stevano.core.Core;
import ac.stevano.core.rank.Rank;
import ac.stevano.core.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class BasicListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void chatFormat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Rank rank = Core.getCore().getPlayerDataManager().getPlayerData(player).getRank();

        String prefix = (rank.getPrefix() == null || rank.getPrefix().isEmpty() ? "" : rank.getPrefix()) + (rank.getPrefix().isEmpty() ? "" : " ") + CC.getColorFromName(rank.getNameColor());
        String suffix = (rank.getSuffix() == null || rank.getSuffix().isEmpty() ? "" : rank.getSuffix()) + CC.GRAY + ": " + CC.RESET;

        String message = prefix + player.getName() + suffix + CC.getColorFromName(rank.getChatColor()) + event.getMessage();

        event.setFormat(CC.translate(message));
    }
}
