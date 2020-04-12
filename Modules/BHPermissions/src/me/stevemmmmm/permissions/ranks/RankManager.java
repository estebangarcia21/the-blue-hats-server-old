package me.stevemmmmm.permissions.ranks;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import me.stevemmmmm.permissions.core.PermissionsManager;
import me.stevemmmmm.permissions.core.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankManager {
    private static RankManager instance;

    private List<Rank> ranks = new ArrayList<>();

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
