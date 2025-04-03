package ac.stevano.core.rank;

import ac.stevano.core.Core;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RankManager {
    private final Set<Rank> ranks;
    private final File directory = new File(Core.getCore().getDataFolder() + File.separator + "ranks");

    public RankManager() {
        ranks = new HashSet<>();
    }

    public void startup() {
        if (!directory.exists()) directory.mkdir();

        if (directory.listFiles().length == 0) {createDefaultRank();}

        for (File file : directory.listFiles()) {
            ranks.add(new Rank(file.getName().replace(".yml", "")));
        }
        for (Rank rank : ranks) {
            rank.load();
            System.out.println("Loaded rank: " + rank.getName() + ".");
        }
    }

    public void slowdown() {
        if (ranks.isEmpty()) return;
        for (Rank rank : ranks) {
            rank.save();
        }
    }

    public void createRank(String name) {
        int highestWeight = 0;

        for (Rank rank : ranks) {
            if (rank.getWeight() > highestWeight) {
                highestWeight = rank.getWeight();
            }
        }

        int newWeight = highestWeight + 10;

        Rank rank = new Rank(name);
        rank.setWeight(newWeight);
        rank.setNameColor(ChatColor.WHITE.name());
        rank.setChatColor(ChatColor.WHITE.name());
        rank.setPrefix("");
        rank.setSuffix("");
        ranks.add(rank);

        System.out.println("Created new rank " + name + " with weight " + newWeight + ".");
    }

    public Rank getByName(String name) {
        for (Rank rank : ranks) if (rank.getName().equalsIgnoreCase(name)) return rank;
        return null;
    }

    public Rank editRank(Rank rank) {
        return rank;
    }

    private void createDefaultRank() {
        Rank defaultRank = new Rank("Default");
        defaultRank.setChatColor(ChatColor.WHITE.name());
        defaultRank.setNameColor(ChatColor.GRAY.name());
        defaultRank.setWeight(0);
        defaultRank.setPrefix("");
        defaultRank.setSuffix("");
        defaultRank.save();
        System.out.println("Created the default rank.");
    }

    public Rank getDefaultRank() {
        for (Rank rank : ranks) {
            if (rank.getWeight() == 0) return rank;
        }

        return null;
    }

    public File getDirectory() {
        return directory;
    }

    public Set<Rank> getRanks() {
        return ranks;
    }

    public Rank editRank(String name) {
        return editRank(getByName(name));
    }
}
