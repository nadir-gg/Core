package ac.stevano.core.playerdata;

import ac.stevano.core.Core;
import ac.stevano.core.rank.Rank;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerData {
    private final UUID uuid;
    private Set<Permission> permissions;
    private String tag;
    private Rank rank;

    public void save() {
        File file = new File(Core.getCore().getPlayerDataManager().getDirectory(), uuid + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        configuration.set("rank", rank.getName());
        if (tag != null) configuration.set("tag", tag);
        if (permissions != null) configuration.set("permissions", permissionsToSTringlist(permissions));

        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void load() {
        File file = new File(Core.getCore().getPlayerDataManager().getDirectory(), uuid + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (configuration.contains("tag")) this.tag = configuration.getString("tag");
        this.rank = Core.getCore().getRankManager().getByName(configuration.getString("rank"));
        if (configuration.contains("permissions"))this.permissions = stringListToPermissions(configuration.getStringList("permissions"));
    }

    public Set<Permission> stringListToPermissions(List<String> strings) {
        Set<Permission> permissionSet = new HashSet<>();

        for (String string : strings) {
            permissionSet.add(new Permission(string));
        }

        return permissionSet;
    }

    public List<String> permissionsToSTringlist(Set<Permission> permissionList) {
        List<String> strings = new ArrayList<>();

        for (Permission permission : permissionList) {
            strings.add(permission.getName());
        }

        return strings;
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.permissions = new HashSet<>();
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setPermissions(Set<Permission> permissions) {

    }

    public String getTag() {
        return tag;
    }

    public Rank getRank() {
        return rank;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean firstTime() {
        File file = new File(Core.getCore().getPlayerDataManager().getDirectory(), uuid + ".yml");

        return !file.exists();
    }
}
