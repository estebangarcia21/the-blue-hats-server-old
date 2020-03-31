package me.stevemmmmm.thehypixelpit.managers.enchants;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DescriptionBuilder {
    private ArrayList<String> description = new ArrayList<>();

    private List<String[]> parameters = new ArrayList<>();

    private int lineIndex;

    public DescriptionBuilder declareVariable(String... values) {
        this.parameters.add(values);

        return this;
    }

    public DescriptionBuilder writeVariable(int id, int level) {
        if (parameters.get(id) != null) {
            description.set(lineIndex, description.get(lineIndex) + parameters.get(id)[level - 1]);
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
