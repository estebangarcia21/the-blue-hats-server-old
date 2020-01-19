package me.stevemmmmm.thehypixelpit.utils;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import java.util.*;

public class MapSorter {
    public static HashMap<UUID, Integer> sortByValue(HashMap<UUID, Integer> hm)
    {
        List<Map.Entry<UUID, Integer>> list = new LinkedList<>(hm.entrySet());

        list.sort(Map.Entry.comparingByValue());

        HashMap<UUID, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<UUID, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        return temp;
    }
}