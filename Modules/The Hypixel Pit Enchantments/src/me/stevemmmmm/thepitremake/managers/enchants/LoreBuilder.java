package me.stevemmmmm.thepitremake.managers.enchants;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class LoreBuilder {
    private final ArrayList<String> description = new ArrayList<>();
    private final List<String[]> parameters = new ArrayList<>();

    private boolean condition = true;
    private int lineIndex;

    private ChatColor color = ChatColor.GRAY;

    public LoreBuilder() {
        description.add("");
    }

    public LoreBuilder addVariable(String... values) {
        this.parameters.add(values);

        return this;
    }

    public LoreBuilder writeVariable(int id, int level) {
        if (parameters.get(id) != null) {
            try {
                if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + parameters.get(id)[level - 1]);
            } catch (NullPointerException | IndexOutOfBoundsException ignored) {

            }
        }

        return this;
    }

    public LoreBuilder writeVariable(ChatColor color, int id, int level) {
        if (parameters.get(id) != null) {
            if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + parameters.get(id)[level - 1]);
        }

        return this;
    }

    public LoreBuilder nextLine() {
        if (condition) {
            lineIndex++;
            description.add("");
        }

        return this;
    }

    public LoreBuilder write(String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public LoreBuilder write(ChatColor color, String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public LoreBuilder setColor(ChatColor color) {
        this.color = color;

        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString());

        return this;
    }

    public LoreBuilder resetColor() {
        this.color = ChatColor.GRAY;

        return this;
    }

    public LoreBuilder writeOnlyIf(boolean condition, String value) {
        if (condition) description.set(lineIndex, description.get(lineIndex) + color.toString() + value);

        return this;
    }

    public LoreBuilder setWriteCondition(boolean condition) {
        this.condition = condition;

        return this;
    }

    public LoreBuilder resetCondition() {
        condition = true;

        return this;
    }

    public ArrayList<String> build() {
        return description;
    }
}
