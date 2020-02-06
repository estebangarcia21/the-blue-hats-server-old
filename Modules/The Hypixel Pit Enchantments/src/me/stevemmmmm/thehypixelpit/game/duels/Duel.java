package me.stevemmmmm.thehypixelpit.game.duels;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class Duel {
    private Player playerA;
    private Player playerB;

    private Vector playerAPos;
    private Vector playerBPos;

    public Duel(Player playerA, Player playerB, Vector playerAPos, Vector playerBPos) {
        this.playerA = playerA;
        this.playerB = playerB;

        this.playerAPos = playerAPos;
        this.playerBPos = playerBPos;
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public Vector getPlayerAPos() {
        return playerAPos;
    }

    public Vector getPlayerBPos() {
        return playerBPos;
    }
}