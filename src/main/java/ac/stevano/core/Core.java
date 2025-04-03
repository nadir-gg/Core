package ac.stevano.core;

import ac.stevano.core.command.cmdGrant;
import ac.stevano.core.command.cmdList;
import ac.stevano.core.listener.BasicListener;
import ac.stevano.core.playerdata.PlayerDataListener;
import ac.stevano.core.playerdata.PlayerDataManager;
import ac.stevano.core.rank.RankManager;
import ac.stevano.core.rank.cmdRank;
import ac.stevano.core.staff.mode.ModeManager;
import ac.stevano.core.staff.mode.cmdStaffmode;
import ac.stevano.core.utils.CC;
import fr.mrmicky.fastboard.FastBoard;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Core extends JavaPlugin {
    private static Core core;
    private RankManager rankManager;
    private PlayerDataManager playerDataManager;
    private ModeManager modeManager;
    public Map<UUID, FastBoard> boards = new HashMap<>();

    /*
    TODO: API, GITHUB, ALLOW SOUP USE WITH CORE (SCOREBOARD), FORK ASSEMBLE OR CHANGE SCOREBOARD API

    ** Check BasicListener
    ** Check Grant
    ** Add creation date to ranks
    ** Hide list name on vanish
    ** staff mode
    ** update everyone's permission when their ranks permission(s) changes
    ** FORK ASSEMBLE OR CHANGE SCOREBOARD API

    ** /rank inherit
    ** /rank uninherit
    ** remove duplicate permissions in yml
     */

    @Override
    public void onEnable() {
        core = this;
        saveDefaultConfig();

        rankManager = new RankManager();
        rankManager.startup();
        playerDataManager = new PlayerDataManager();
        playerDataManager.startup();

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (FastBoard board : this.boards.values()) {
                updateLines(board, board.getPlayer());
            }
        }, 0L, 3L);

        modeManager = new ModeManager();

        this.getCommand("rank").setExecutor(new cmdRank());
        this.getCommand("grant").setExecutor(new cmdGrant());
        this.getCommand("list").setExecutor(new cmdList());
        this.getCommand("staffmode").setExecutor(new cmdStaffmode());

        this.getServer().getPluginManager().registerEvents(new BasicListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDataListener(), this);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&b[CORE]&a Startup successful."));
    }

    public void createBoard(Player player) {
        FastBoard board = new FastBoard(player);
        if (boards.containsKey(player.getUniqueId())) return;

        board = new FastBoard(player);
        board.updateTitle(CC.translate("&d&lLain &7(Staff)"));
        updateLines(board, player);

        this.boards.put(player.getUniqueId(), board);
    }

    public void hideBoard(Player player) {
        if (!boards.containsKey(player.getUniqueId())) return;
        FastBoard board = boards.get(player.getUniqueId());

        if (board != null) board.delete();
        this.boards.remove(player.getUniqueId());
    }

    public boolean hasBoard(Player player) {
        return boards.containsKey(player.getUniqueId());
    }

    public void updateLines(FastBoard board, Player player) {
        board.updateLines(
                CC.SB_BAR,
                CC.translate("Online&7: &6" + player.getServer().getOnlinePlayers().size() + "/" + player.getServer().getMaxPlayers()),
                CC.translate("TPS&7: &6" + new DecimalFormat("#.##").format(MinecraftServer.getServer().recentTps[0])),
                "",
                CC.translate("Ping&7: &6" + ((CraftPlayer) player).getHandle().ping + "ms"),
                CC.translate("Chat&7: &eGlobal"),
                CC.translate("Gamemode&7: &6" + WordUtils.capitalizeFully(player.getGameMode().name())),
                CC.translate("X&7: &6" + player.getLocation().getBlockX()),
                "",
                CC.translate("&d&olain.gg"),
                CC.SB_BAR
        );
    }

    @Override
    public void onDisable() {
        rankManager.slowdown();
        playerDataManager.slowdown();

        Bukkit.getConsoleSender().sendMessage(CC.translate("&b[CORE]&c Shutdown successful."));
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public ModeManager getModeManager() {
        return modeManager;
    }

    public static Core getCore() {
        return core;
    }
}
