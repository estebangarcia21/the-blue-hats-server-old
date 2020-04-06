package me.stevemmmmm.thehypixelpit.managers.other;

import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import org.bukkit.event.Event;

public abstract class CancelerEnchant<T extends Event> extends CustomEnchant {
    private CancelerEnchant<T> caller;
    private boolean enchantsWereCancelled = false;

    public abstract void attemptEnchantCancellation(T event);

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

    public void reset() {
        caller = null;
        enchantsWereCancelled = false;
    }
}
