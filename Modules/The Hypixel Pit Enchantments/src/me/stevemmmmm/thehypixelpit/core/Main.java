package me.stevemmmmm.thehypixelpit.core;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.thehypixelpit.chat.LevelChatFormatting;
import me.stevemmmmm.thehypixelpit.commands.*;
import me.stevemmmmm.thehypixelpit.enchants.*;
import me.stevemmmmm.thehypixelpit.game.*;
import me.stevemmmmm.thehypixelpit.game.duels.DuelingManager;
import me.stevemmmmm.thehypixelpit.game.duels.GameUtility;
import me.stevemmmmm.thehypixelpit.managers.ServerMOTDInitializer;
import me.stevemmmmm.thehypixelpit.managers.enchants.BowManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thehypixelpit.managers.enchants.DamageManager;
import me.stevemmmmm.thehypixelpit.managers.other.GrindingSystem;
import me.stevemmmmm.thehypixelpit.managers.other.PitScoreboardManager;
import me.stevemmmmm.thehypixelpit.perks.Vampire;
import me.stevemmmmm.thehypixelpit.utils.DevelopmentMode;
import me.stevemmmmm.thehypixelpit.world.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class Main extends JavaPlugin {
    public static Main INSTANCE;
    public static ProtocolManager protocolManager;

    public static String version = "v1.0";

    @Override
    public void onEnable() {
        INSTANCE = this;
        protocolManager = ProtocolLibrary.getProtocolManager();

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
        getServer().getPluginManager().registerEvents(new GameUtility(), this);
        getServer().getPluginManager().registerEvents(new AntiFall(), this);
        getServer().getPluginManager().registerEvents(new AutoRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerUtility(), this);
        getServer().getPluginManager().registerEvents(new DeveloperUpdates(), this);
        getServer().getPluginManager().registerEvents(new WorldProtection(), this);
        getServer().getPluginManager().registerEvents(RegionManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new DevelopmentMode(), this);
        getServer().getPluginManager().registerEvents(new ServerMOTDInitializer(), this);

        //Commands
        getCommand("pitenchant").setExecutor(new EnchantCommand());
        getCommand("mysticenchants").setExecutor(new MysticEnchantsCommand());
        getCommand("pitabout").setExecutor(new PitAboutCommand());
        getCommand("givefreshitem").setExecutor(new GiveFreshItemCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        getCommand("giveprot").setExecutor(new GiveProtCommand());
        getCommand("setgold").setExecutor(new SetGoldCommand());
        getCommand("givebread").setExecutor(new GiveBreadCommand());
        getCommand("givearrows").setExecutor(new GiveArrowCommand());
        getCommand("giveobsidian").setExecutor(new GiveObsidianCommand());

        SpawnCommand spawnCommand = new SpawnCommand();
        getCommand("spawn").setExecutor(spawnCommand);
        getCommand("respawn").setExecutor(spawnCommand);

        //Game
        //getServer().getPluginManager().registerEvents(new MysticWell(), this);
        getServer().getPluginManager().registerEvents(PitScoreboardManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DuelingManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DamageIndicator.getInstance(), this);
        getServer().getPluginManager().registerEvents(CombatManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DamageManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(BowManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new LevelChatFormatting(), this);
        getServer().getPluginManager().registerEvents(new Bread(), this);
        getServer().getPluginManager().registerEvents(GrindingSystem.getInstance(), this);
        getServer().getPluginManager().registerEvents(new Obsidian(), this);
    }

    @Override
    public void onDisable() {
        GrindingSystem.getInstance().writeToConfig();
    }

    private void registerPerks() {
        getServer().getPluginManager().registerEvents(new Vampire(), this);
    }

    private void registerEnchants() {
        //Swords
        CustomEnchantManager.getInstance().registerEnchant(new Billionaire());
        CustomEnchantManager.getInstance().registerEnchant(new Healer());
        CustomEnchantManager.getInstance().registerEnchant(new Perun());
        CustomEnchantManager.getInstance().registerEnchant(new ComboStun());
        CustomEnchantManager.getInstance().registerEnchant(new Lifesteal());
        CustomEnchantManager.getInstance().registerEnchant(new DiamondStomp());
        CustomEnchantManager.getInstance().registerEnchant(new BulletTime());
        CustomEnchantManager.getInstance().registerEnchant(new ComboDamage());
        CustomEnchantManager.getInstance().registerEnchant(new PainFocus());
        CustomEnchantManager.getInstance().registerEnchant(new KingBuster());
        CustomEnchantManager.getInstance().registerEnchant(new Punisher());
        CustomEnchantManager.getInstance().registerEnchant(new ComboSwift());

        //Bows
        CustomEnchantManager.getInstance().registerEnchant(new Robinhood());
        CustomEnchantManager.getInstance().registerEnchant(new Telebow());
        CustomEnchantManager.getInstance().registerEnchant(new Megalongbow());
        CustomEnchantManager.getInstance().registerEnchant(new Volley());
        CustomEnchantManager.getInstance().registerEnchant(new LuckyShot());
        CustomEnchantManager.getInstance().registerEnchant(new DevilChicks());
        CustomEnchantManager.getInstance().registerEnchant(new Explosive());
        CustomEnchantManager.getInstance().registerEnchant(new Wasp());
        CustomEnchantManager.getInstance().registerEnchant(new Parasite());
        CustomEnchantManager.getInstance().registerEnchant(new PushComesToShove());

        //Pants
        CustomEnchantManager.getInstance().registerEnchant(new Mirror());
        CustomEnchantManager.getInstance().registerEnchant(new Solitude());
        CustomEnchantManager.getInstance().registerEnchant(new Assassin());
        CustomEnchantManager.getInstance().registerEnchant(new CriticallyFunky());
        CustomEnchantManager.getInstance().registerEnchant(new LastStand());
    }
}
