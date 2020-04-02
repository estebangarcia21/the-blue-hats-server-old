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

    private boolean condition = true;
    private int lineIndex;

    private ChatColor color = ChatColor.GRAY;

    public DescriptionBuilder() {
        description.add("");
    }

    public DescriptionBuilder addVariable(String... values) {
        this.parameters.add(values);

        return this;
    }

    public DescriptionBuilder writeVariable(int id, int level) {
        if (parameters.get(id) != null) {
            try {
                if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + parameters.get(id)[level - 1]);
            } catch (NullPointerException ignored) {

            }
        }

        return this;
    }

    public DescriptionBuilder writeVariable(ChatColor color, int id, int level) {
        if (parameters.get(id) != null) {
            if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + parameters.get(id)[level - 1]);
        }

        return this;
    }

    public DescriptionBuilder nextLine() {
        if (condition) {
            lineIndex++;
            description.add("");
        }

        return this;
    }

    public DescriptionBuilder write(String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public DescriptionBuilder write(ChatColor color, String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public DescriptionBuilder setColor(ChatColor color) {
        this.color = color;

        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString());

        return this;
    }

    public DescriptionBuilder resetColor() {
        this.color = ChatColor.GRAY;

        return this;
    }

    public DescriptionBuilder writeOnlyIf(boolean condition, String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public DescriptionBuilder setWriteCondition(boolean condition) {
        this.condition = condition;

        return this;
    }

    public DescriptionBuilder resetCondition() {
        condition = true;

        return this;
    }

    public ArrayList<String> build() {
        return description;
    }
}
