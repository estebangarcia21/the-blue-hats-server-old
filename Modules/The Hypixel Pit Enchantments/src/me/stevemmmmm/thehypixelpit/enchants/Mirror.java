package me.stevemmmmm.thehypixelpit.enchants;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Mirror extends CustomEnchant {

    @Override
    public boolean executeEnchant(ItemStack sender, Object executedEvent) {
        return false;
    }

    @Override
    public String getName() {
        return "Mirror";
    }

    @Override
    public String getEnchantReferenceName() {
        return "Mirror";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        final String percentAmount = level == 1 ? "" : level == 2 ? "25%" : level == 3 ? "50%" : "";

        return new ArrayList<String>() {{
            if (level == 1) {
                add(ChatColor.GRAY + "You are immune to true damage");
            } else {
                add(ChatColor.GRAY + "You do not take true damage and");
                add(ChatColor.GRAY + "instead reflect " + ChatColor.YELLOW + percentAmount + ChatColor.GRAY + " of it to");
                add(ChatColor.GRAY + "your attacker");
            }
        }};
    }

    @Override
    public boolean isTierTwoEnchant() {
        return false;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }
}
