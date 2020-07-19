package me.stevemmmmm.thepitremake.enchants;

import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import org.bukkit.Material;

import java.util.ArrayList;

public class GrandMaster extends CustomEnchant {
    @Override
    public void applyEnchant(int level, Object... args) {

    }

    @Override
    public String getName() {
        return "Lmao";
    }

    @Override
    public String getEnchantReferenceName() {
        return "";
    }

    @Override
    public ArrayList<String> getDescription(int level) {
        return null;
    }

    @Override
    public boolean isDisabledOnPassiveWorld() {
        return false;
    }

    @Override
    public EnchantGroup getEnchantGroup() {
        return null;
    }

    @Override
    public boolean isRareEnchant() {
        return false;
    }

    @Override
    public Material[] getEnchantItemTypes() {
        return new Material[0];
    }
}
