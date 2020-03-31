package me.stevemmmmm.thehypixelpit.core;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.thehypixelpit.chat.LevelChatFormatting;
import me.stevemmmmm.thehypixelpit.commands.*;
import me.stevemmmmm.thehypixelpit.enchants.*;
import me.stevemmmmm.thehypixelpit.game.CombatTimer;
import me.stevemmmmm.thehypixelpit.game.MysticWell;
import me.stevemmmmm.thehypixelpit.game.duels.DuelingManager;
import me.stevemmmmm.thehypixelpit.managers.CustomEnchant;
import me.stevemmmmm.thehypixelpit.managers.enchants.ArrowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.other.GrindingSystem;
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

        ConfigAPI.setPlugin(this, "Ranks", "Gold", "XP", "Prestiges", "Levels");

        GrindingSystem.getInstance().readConfig();
        ConfigAPI.registerConfigWriter(GrindingSystem.getInstance());

        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("The Hypixel Pit Remake by Stevemmmmm");
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
        getCommand("givefreshitem").setExecutor(new GiveFreshItemCommand());
        getCommand("duel").setExecutor(new DuelCommand());

        //Game logic
        //getServer().getPluginManager().registerEvents(new MysticWell(), this);
        getServer().getPluginManager().registerEvents(DuelingManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(CombatTimer.getInstance(), this);
        getServer().getPluginManager().registerEvents(DamageManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(ArrowManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new LevelChatFormatting(), this);

        getServer().getPluginManager().registerEvents(GrindingSystem.getInstance(), this);
    }

    @Override
    public void onDisable() {
        GrindingSystem.getInstance().writeToConfig();
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
        CustomEnchantManager.getInstance().registerEnchant(new Lifesteal());
        CustomEnchantManager.getInstance().registerEnchant(new DiamondStomp());
        CustomEnchantManager.getInstance().registerEnchant(new BulletTime());

        //Bows
        CustomEnchantManager.getInstance().registerEnchant(new Robinhood());
        CustomEnchantManager.getInstance().registerEnchant(new Telebow());
        CustomEnchantManager.getInstance().registerEnchant(new Megalongbow());
        CustomEnchantManager.getInstance().registerEnchant(new Volley());
        CustomEnchantManager.getInstance().registerEnchant(new LuckyShot());
        CustomEnchantManager.getInstance().registerEnchant(new DevilChicks());
        CustomEnchantManager.getInstance().registerEnchant(new Explosive());

        //Pants
        CustomEnchantManager.getInstance().registerEnchant(new Mirror());
        CustomEnchantManager.getInstance().registerEnchant(new Solitude());
        CustomEnchantManager.getInstance().registerEnchant(new Assassin());
        CustomEnchantManager.getInstance().registerEnchant(new CriticallyFunky());
        CustomEnchantManager.getInstance().registerEnchant(new LastStand());
    }
}
