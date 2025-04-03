package ac.stevano.core.rank;

import ac.stevano.core.Core;
import ac.stevano.core.utils.CC;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rank {
    private final String name;
    private String prefix;
    private String suffix;
    private String nameColor;
    private String chatColor;
    private Set<Permission> permissions;
    private int weight;

    public Rank(String name) {
        this.name = name;
        permissions = new HashSet<>();
    }

    public void load() {
        File file = new File(Core.getCore().getRankManager().getDirectory(), name + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (configuration.contains("prefix")) this.prefix = configuration.getString("prefix");
        if (configuration.contains("name"))this.suffix = configuration.getString("suffix");
        if (configuration.contains("color.name"))this.nameColor = configuration.getString("color.name");
        if (configuration.contains("color.chat"))this.chatColor = configuration.getString("color.chat");
        if (configuration.contains("weight"))this.weight = configuration.getInt("weight");
        if (configuration.contains("permissions"))this.permissions = stringListToPermissions(configuration.getStringList("permissions"));
    }

    public void save() {
        File file = new File(Core.getCore().getRankManager().getDirectory(), name + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (prefix != null) configuration.set("prefix", prefix);
        if (suffix != null) configuration.set("suffix", suffix);
        if (nameColor != null) configuration.set("color.name", nameColor);
        if (chatColor != null) configuration.set("color.chat", chatColor);
        configuration.set("weight", weight);
        if (permissions != null) configuration.set("permissions", permissionsToSTringlist(permissions));

        try {
            configuration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void setChatColor(String chatColor) {
        this.chatColor = chatColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public String getChatColor() {
        return chatColor;
    }

    public String getNameColor() {
        return nameColor;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getWeight() {
        return weight;
    }

    public boolean hasPermissions() {
        return permissions != null && !permissions.isEmpty();
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public String getDisplayName() {
        return CC.getColorFromName(nameColor) + name;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

