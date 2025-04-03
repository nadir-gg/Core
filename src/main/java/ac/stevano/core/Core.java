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
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {
    private static Core core;
    private RankManager rankManager;
    private PlayerDataManager playerDataManager;
    private ModeManager modeManager;

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
        modeManager = new ModeManager();

        this.getCommand("rank").setExecutor(new cmdRank());
        this.getCommand("grant").setExecutor(new cmdGrant());
        this.getCommand("list").setExecutor(new cmdList());
        this.getCommand("staffmode").setExecutor(new cmdStaffmode());

        this.getServer().getPluginManager().registerEvents(new BasicListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDataListener(), this);

        Bukkit.getConsoleSender().sendMessage(CC.translate("&b[CORE]&a Startup successful."));
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
