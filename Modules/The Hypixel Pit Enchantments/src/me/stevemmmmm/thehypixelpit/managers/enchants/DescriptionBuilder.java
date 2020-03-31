package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;

public class DescriptionBuilder {
    private ArrayList<String> description = new ArrayList<>();

    private HashMap<String, String[]> parameters = new HashMap<>();

    private int lineIndex;

    public <T> DescriptionBuilder addParamater(String name, String... parameters) {
        this.parameters.put(name, parameters);

        return this;
    }

    public DescriptionBuilder writeParamater(String name, int level) {
        if (parameters.get(name) != null) {
            description.set(lineIndex, description.get(lineIndex) + parameters.get(name)[level - 1]);
        }

        return this;
    }

    public DescriptionBuilder nextLine() {
        lineIndex++;
        description.add("");

        return this;
    }

    public DescriptionBuilder write(String value) {
        if (description.size() == 0) description.add("");

        description.set(lineIndex, description.get(lineIndex) + value);

        return this;
    }

    public DescriptionBuilder setColor(ChatColor color) {
        if (description.size() == 0) description.add("");

        description.set(lineIndex, description.get(lineIndex) + color.toString());

        return this;
    }

    public ArrayList<String> build() {
        return description;
    }
}
