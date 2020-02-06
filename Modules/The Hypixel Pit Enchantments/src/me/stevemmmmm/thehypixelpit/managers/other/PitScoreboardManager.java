package me.stevemmmmm.thehypixelpit.managers.other;

import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.game.CombatTimer;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class PitScoreboardManager {
    private static PitScoreboardManager instance;

    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard scoreboard = manager.getNewScoreboard();

    private HashMap<UUID, String> playerObjectives = new HashMap<>();

    private HashMap<Integer, Team> tablistSortTeams = new HashMap<>();

    private PitScoreboardManager() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.instance, this::updateStandardScoreboard, 0L, 20L);
        createSortedTeams(scoreboard);
    }

    public static PitScoreboardManager getInstance() {
        if (instance == null) instance = new PitScoreboardManager();

        return instance;
    }

    public void sort(Player player) {
        updateStandardScoreboard();
        tablistSortTeams.get(GrindingSystem.getInstance().getPlayerLevel(player)).addEntry(player.getName());
    }

    private void updateStandardScoreboard() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Scoreboard board = scoreboard;

            Objective objective = null;

            if (playerObjectives.containsKey(player.getUniqueId())) {
                objective = board.getObjective(playerObjectives.get(player.getUniqueId()));
            } else {
                objective = board.registerNewObjective(player.getName(), "dummy");
                playerObjectives.put(player.getUniqueId(), objective.getName());
            }

            objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE HYPIXEL PIT");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            int index = 11;

            if (CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)).equalsIgnoreCase("None")) index -= 1;

            //TODO Dynamic date
            Score dataAndInstance = objective.getScore(ChatColor.GRAY + "01/26/20 " + ChatColor.DARK_GRAY + "mega13E");
            dataAndInstance.setScore(index);
            index--;

            Score space1 = objective.getScore(" ");
            space1.setScore(index);
            index--;

            if (!CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)).equalsIgnoreCase("None")) {
                Score prestige = objective.getScore(ChatColor.WHITE + "Prestige: " + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)));
                prestige.setScore(index);
                index--;
            }

            Score level = objective.getScore(ChatColor.WHITE + "Level: " + GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player));
            level.setScore(index);
            index--;

            Score xp = objective.getScore(ChatColor.WHITE + "XP: " + ChatColor.AQUA + "MAXED!");
            xp.setScore(index);
            index--;

            Score space2 = objective.getScore("  ");
            space2.setScore(index);
            index--;

            Score gold = objective.getScore(ChatColor.WHITE + "Gold: " + ChatColor.GOLD + GrindingSystem.getInstance().getFormattedPlayerGold(player));
            gold.setScore(index);
            index--;

            Score space3 = objective.getScore("   ");
            space3.setScore(index);
            index--;

            //TODO Get status w/ combat timer and change color accordingly
            Score status = objective.getScore(ChatColor.WHITE + "Status: " + (!CombatTimer.getInstance().playerIsInCombat(player) ? ChatColor.GREEN + "Idling" : ChatColor.RED + "Fighting " + ChatColor.GRAY + "(" + CombatTimer.getInstance().getCombatTime(player) + ")"));
            status.setScore(index);
            index--;

            //TODO If player has bounty... add line

            Score space4 = objective.getScore("    ");
            space4.setScore(index);
            index--;

            Score serverinfo = objective.getScore(ChatColor.YELLOW + "bluehats.ddns.net");
            serverinfo.setScore(index);
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
                sortingCharachter = "4" + alphabet.split("")[index - 36].toUpperCase();
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
