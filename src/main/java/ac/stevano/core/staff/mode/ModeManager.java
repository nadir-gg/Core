package ac.stevano.core.staff.mode;

import ac.stevano.core.utils.CC;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ModeManager {
    Set<UUID> modeMap;

    public ModeManager() {
        modeMap = new HashSet<>();
    }

    public void putInStaffMode(Player player) {
        if (isInStaffMode(player)) {
            player.sendMessage(CC.RED + "You are already in staff mode.");
            return;
        }

        player.sendMessage(CC.WHITE + "You are" + CC.GREEN + " now " + CC.WHITE + "in staff mode.");
        modeMap.add(player.getUniqueId());
    }

    public void removeFromStaffMode(Player player) {
        if (!isInStaffMode(player)) {
            player.sendMessage(CC.RED + "You are not in staff mode.");
            return;
        }

        player.sendMessage(CC.WHITE + "You are" + CC.RED + " no longer " + CC.WHITE + "in staff mode.");
        modeMap.remove(player.getUniqueId());
    }

    public boolean isInStaffMode(Player player) {
        return modeMap.contains(player.getUniqueId());
    }

    public int getAmountInStaffMode() {
        return modeMap.size();
    }

    public Set<UUID> getModeMap() {
        return modeMap;
    }
}
