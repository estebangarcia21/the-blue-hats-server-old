package me.stevemmmmm.thehypixelpit.managers.other;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class PitScoreboardManager {
    private static PitScoreboardManager instance;

    private ScoreboardManager manager = Bukkit.getScoreboardManager();

    private HashMap<Integer, Team> tablistSortTeams = new HashMap<>();

    private PitScoreboardManager() {
        init();
    }

    public static PitScoreboardManager getInstance() {
        if (instance == null) instance = new PitScoreboardManager();

        return instance;
    }

    public void sort(Player player) {
        updateScoreboard();
        tablistSortTeams.get(GrindingSystem.getInstance().getPlayerLevel(player)).addEntry(player.getName());
    }

    private void init() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, this::updateScoreboard, 0L, 20L);
    }

    private void updateScoreboard() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Scoreboard board = manager.getNewScoreboard();
            createSortedTeams(board);

            Objective objective = board.registerNewObjective("test", "dummy");
            objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE HYPIXEL PIT");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            int index = 12;

            Score dataAndInstance = objective.getScore(ChatColor.GRAY + "01/26/20 " + ChatColor.DARK_GRAY + "mega13E");
            dataAndInstance.setScore(11);

            Score space1 = objective.getScore(" ");
            space1.setScore(10);

            if (CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)).equalsIgnoreCase("None")) index -= 2;

            Score prestige = objective.getScore(ChatColor.WHITE + "Prestige: " + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)));
            prestige.setScore(9);
            index--;

            Score level = objective.getScore(ChatColor.WHITE + "Level: " + GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player));
            level.setScore(8);

            Score xp = objective.getScore(ChatColor.WHITE + "XP: " + ChatColor.AQUA + "MAXED!");
            xp.setScore(7);

            Score space2 = objective.getScore("  ");
            space2.setScore(6);

            Score gold = objective.getScore(ChatColor.WHITE + "Gold: " + ChatColor.GOLD + GrindingSystem.getInstance().getPlayerGold(player));
            gold.setScore(5);

            Score space3 = objective.getScore("   ");
            space3.setScore(4);

            //TODO Get status w/ combat timer and change color accordingly
            Score status = objective.getScore(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Idling");
            status.setScore(3);

            Score space4 = objective.getScore("    ");
            space4.setScore(2);

            Score serverinfo = objective.getScore(ChatColor.YELLOW + "bluehats.ddns.net");
            serverinfo.setScore(1);

            player.setScoreboard(board);
        }
    }

    private void createSortedTeams(Scoreboard board) {
        int index = 0;

        String alphabet = "zyxwvutsrqponmlkjihgfedcba";

        for (int i = 120; i >= 0; i--) {
            String sortingCharachter = "";

            if (index < 10) {
                sortingCharachter = "6" + alphabet.split("")[index].toUpperCase();
            } else if (index < 36) {
                sortingCharachter = "5" + alphabet.split("")[index - 10].toUpperCase();
            } else if (index < 62) {
                sortingCharachter = "4" + alphabet.split("")[index - 26 - 10].toUpperCase();
            } else if (index < 88) {
                sortingCharachter = "3" + alphabet.split("")[index - (26 * 2) - 10].toUpperCase();
            } else if (index < 114) {
                sortingCharachter = "2" + alphabet.split("")[index - (26 * 3) - 10].toUpperCase();
            } else if (index < 130) {
                sortingCharachter = "1" + alphabet.split("")[index - (26 * 4) - 10].toUpperCase();
            }

            Team team = board.registerNewTeam(sortingCharachter + "TabList");

            tablistSortTeams.put(index, team);
            index++;
        }
    }
}
