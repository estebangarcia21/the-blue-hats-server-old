package me.stevemmmmm.thehypixelpit.managers.other;

import me.stevemmmmm.permissions.core.PermissionsManager;
import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.game.CombatManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class PitScoreboardManager implements Listener {
    private static PitScoreboardManager instance;

    private final HashMap<UUID, Integer> scoreboardTasks = new HashMap<>();
    private final HashMap<UUID, Scoreboard> playerToScoreboard = new HashMap<>();

    public static PitScoreboardManager getInstance() {
        if (instance == null) instance = new PitScoreboardManager();

        return instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!scoreboardTasks.containsKey(player.getUniqueId())) {
            playerToScoreboard.put(player.getUniqueId(), Bukkit.getScoreboardManager().getNewScoreboard());
            player.setScoreboard(playerToScoreboard.get(player.getUniqueId()));

            scoreboardTasks.put(player.getUniqueId(), Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, () -> {
                updateScoreboard(player);
            }, 20L, 20L));
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (scoreboardTasks.containsKey(event.getPlayer().getUniqueId())) {
            Bukkit.getServer().getScheduler().cancelTask(scoreboardTasks.get(event.getPlayer().getUniqueId()));
            scoreboardTasks.remove(event.getPlayer().getUniqueId());
        }
    }

    private void createScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = getScoreboardObjective(board, player);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
    }

    private void updateScoreboard(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = board.registerNewObjective("test", "dummy");

        objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE BLUE HATS PIT");

        int index = 11;

        if (CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)).equalsIgnoreCase("None")) index -= 1;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        Score dataAndInstance = objective.getScore(ChatColor.GRAY + simpleDateFormat.format(date) + ChatColor.DARK_GRAY + " mega69L");
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

        Score xp = objective.getScore(ChatColor.WHITE + "XP: " + ChatColor.AQUA + "MAXED!!!");
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

        Score status = objective.getScore(ChatColor.WHITE + "Status: " + (!CombatManager.getInstance().playerIsInCombat(player) ? ChatColor.GREEN + "Idling" : ChatColor.RED + "Fighting " + ChatColor.GRAY + "(" + CombatManager.getInstance().getCombatTime(player) + ")"));
        status.setScore(index);
        index--;

        Score space4 = objective.getScore("    ");
        space4.setScore(index);
        index--;

        Score serverinfo = objective.getScore(ChatColor.YELLOW + "play.thebluehats.net");
        serverinfo.setScore(index);

        updateNametag(player, board);

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(board);
    }

    public Objective getScoreboardObjective(Scoreboard board, Player player) {
        Objective objective;

//        if (!objectiveBuffer.containsKey(player.getUniqueId())) {
//            objectiveBuffer.put(player.getUniqueId(), "main");
//        }
//
//        if (objectiveBuffer.get(player.getUniqueId()).equalsIgnoreCase("main")) {
//            objective = board.registerNewObjective("buffer", "dummy");
//            objectiveBuffer.put(player.getUniqueId(), "buffer");
//        } else {
//            objective = board.registerNewObjective("main", "dummy");
//            objectiveBuffer.put(player.getUniqueId(), "main");
//        }

        if (board.getObjective("test") != null) {
            board.getObjective("test").unregister();
        }

        objective = board.registerNewObjective("test", "dummy");

        objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "THE BLUE HATS PIT");

        int index = 11;

        if (CustomEnchantManager.getInstance().convertToRomanNumeral(GrindingSystem.getInstance().getPlayerPrestige(player)).equalsIgnoreCase("None")) index -= 1;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();

        Score dataAndInstance = objective.getScore(ChatColor.GRAY + simpleDateFormat.format(date) + ChatColor.DARK_GRAY + " mega69L");
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
        Score status = objective.getScore(ChatColor.WHITE + "Status: " + (!CombatManager.getInstance().playerIsInCombat(player) ? ChatColor.GREEN + "Idling" : ChatColor.RED + "Fighting " + ChatColor.GRAY + "(" + CombatManager.getInstance().getCombatTime(player) + ")"));
        status.setScore(index);
        index--;

        //TODO If player has bounty... add line

        Score space4 = objective.getScore("    ");
        space4.setScore(index);
        index--;

        Score serverinfo = objective.getScore(ChatColor.YELLOW + "play.thebluehats.net");
        serverinfo.setScore(index);

        return objective;
    }

    @SuppressWarnings("deprecation")
    private void updateNametag(Player p, Scoreboard board) {
        for (Player player : p.getWorld().getPlayers()) {
            Team team;

            if (board.getTeam(player.getName()) != null) {
                board.getTeam(player.getName()).unregister();
            }

            team = board.registerNewTeam(player.getName());

            team.setPrefix(GrindingSystem.getInstance().getFormattedPlayerLevelWithoutPrestige(player) + " " + PermissionsManager.getInstance().getPlayerRank(player).getNameColor());

            team.addPlayer(player);
        }
    }
}
