package me.stevemmmmm.permissions.ranks;

import me.stevemmmmm.permissions.core.Rank;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class RankManager {
    private static RankManager instance;

    private final List<Rank> ranks = new ArrayList<>();

    public static RankManager getInstance() {
        if (instance == null) instance = new RankManager();

        return instance;
    }

    public void registerRank(Rank rank) {
        ranks.add(rank);
    }

    public <T extends Rank> Rank getRank(Class<T> type) {
        for (Rank rank : ranks) {
            if (type == rank.getClass()) {
                return rank;
            }
        }

        return null;
    }

    public Rank getRankByName(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }
}
