package me.stevemmmmm.thehypixelpit.game.duels;

import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class DuelingManager implements Listener {
    private static DuelingManager instance;

    private ArrayList<Duel> activeDuels = new ArrayList<>();
    private ArrayList<Pair<Vector, Vector>> duelPositions = new ArrayList<>();

    private DuelingManager() {
        init();
    }

    public static DuelingManager getInstance() {
        if (instance == null) instance = new DuelingManager();

        return instance;
    }

    private void init() {
        //TODO Maps right here
        duelPositions.add(new Pair<>(new Vector(0, 0, 0), new Vector(0, 0, 0)));
    }

    public void startDuel(Duel duel) {
        activeDuels.add(duel);

        initializeDuel(duel);
    }

    public void stopDuel(Player player) {
        if (getPlayerDuel(player) == null) return;

        deInitializeDuel(getPlayerDuel(player));
    }

    public Duel getPlayerDuel(Player player) {
        for (Duel duel : activeDuels) {
            if (duel.getPlayerA() == player) {
                return duel;
            }
        }

        return null;
    }

    private void initializeDuel(Duel duel) {
        duel.getPlayerA().teleport(new Location(duel.getPlayerA().getWorld(), duel.getPlayerAPos().getX(), duel.getPlayerAPos().getY(), duel.getPlayerAPos().getZ()));
        duel.getPlayerB().teleport(new Location(duel.getPlayerB().getWorld(), duel.getPlayerBPos().getX(), duel.getPlayerBPos().getY(), duel.getPlayerBPos().getZ()));
    }

    private void deInitializeDuel(Duel duel) {

    }
}
