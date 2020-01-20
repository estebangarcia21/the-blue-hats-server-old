package me.stevemmmmm.thehypixelpit.managers;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PitScoreboardManager {
    private static PitScoreboardManager instance;

    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board = manager.getNewScoreboard();

    private HashMap<Integer, Team> tablistSortTeams = new HashMap<>();

    private PitScoreboardManager() {
        init();
    }

    public static PitScoreboardManager getInstance() {
        if (instance == null) instance = new PitScoreboardManager();

        return instance;
    }

    @SuppressWarnings("deprecated")
    public void sort(Player player) {
        player.setScoreboard(board);
        tablistSortTeams.get(GrindingSystem.getInstance().getPlayerLevel(player)).addEntry(player.getName());
    }

    private void init() {
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

        Objective objective = board.registerNewObjective("test", "dummy");

        objective.setDisplayName("The Pit");

        Score score = objective.getScore("Aye");
        score.setScore(0);
    }
}
