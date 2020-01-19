package me.stevemmmmm.thehypixelpit.core;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.thehypixelpit.chat.Prestiges;
import me.stevemmmmm.thehypixelpit.commands.EnchantCommand;
import me.stevemmmmm.thehypixelpit.commands.MysticEnchantsCommand;
import me.stevemmmmm.thehypixelpit.commands.PitAboutCommand;
import me.stevemmmmm.thehypixelpit.enchants.*;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchantManager;
import me.stevemmmmm.thehypixelpit.perks.Vampire;
import me.stevemmmmm.thehypixelpit.world.AntiFall;
import me.stevemmmmm.thehypixelpit.world.AutoRespawn;
import me.stevemmmmm.thehypixelpit.world.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Main extends JavaPlugin {
    public static Main instance;

    public static String version = "v1.0";

    @Override
    public void onEnable() {
        instance = this;

        ConfigAPI.setPlugin(this, "Ranks",
                "Gold", "XP", "Prestige");

        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("The Hypixel Pit Enchantments by Stevemmmmm");
        log.info("------------------------------------------");

        //Initialization
        registerEnchants();
        registerPerks();

        //Utility
        getServer().getPluginManager().registerEvents(new AntiFall(), this);
        getServer().getPluginManager().registerEvents(new AutoRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerUtility(), this);

        //Commands
        getCommand("pitenchant").setExecutor(new EnchantCommand());
        getCommand("mysticenchants").setExecutor(new MysticEnchantsCommand());
        getCommand("pitabout").setExecutor(new PitAboutCommand());

        //Game logic
        getServer().getPluginManager().registerEvents(new Prestiges(), this);
    }

    @Override
    public void onDisable() {

    }

    private void registerPerks() {
        getServer().getPluginManager().registerEvents(new Vampire(), this);
    }

    private void registerEnchants() {
        //Weapons
        CustomEnchantManager.getInstance().registerEnchant(new Billionaire());
        CustomEnchantManager.getInstance().registerEnchant(new Healer());
        CustomEnchantManager.getInstance().registerEnchant(new Perun());
        CustomEnchantManager.getInstance().registerEnchant(new Stun());

        //Bows
        CustomEnchantManager.getInstance().registerEnchant(new Robinhood());
        CustomEnchantManager.getInstance().registerEnchant(new Telebow());
        CustomEnchantManager.getInstance().registerEnchant(new Megalongbow());
        CustomEnchantManager.getInstance().registerEnchant(new Volley());
        CustomEnchantManager.getInstance().registerEnchant(new LuckyShot());

        //Pants
        CustomEnchantManager.getInstance().registerEnchant(new Mirror());
        CustomEnchantManager.getInstance().registerEnchant(new Solitude());
        CustomEnchantManager.getInstance().registerEnchant(new Assassin());
        CustomEnchantManager.getInstance().registerEnchant(new CriticallyFunky());
    }
}
