package me.stevemmmmm.thepitremake.managers.other;

import me.stevemmmmm.thepitremake.managers.CustomEnchant;
import org.bukkit.event.Event;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public abstract class CancelerEnchant<T extends Event> extends CustomEnchant {
    private CancelerEnchant<T> caller;
    private boolean enchantsWereCancelled = false;

    public void attemptEnchantCancellation(T event) {
        caller = null;
        enchantsWereCancelled = false;
    }

    public void cancelEnchants(CancelerEnchant<T> caller) {
        enchantsWereCancelled = true;
        this.caller = caller;
    }

    public boolean cancelledEnchants() {
        return enchantsWereCancelled;
    }

    public CancelerEnchant<T> getCaller() {
        return caller;
    }
}
