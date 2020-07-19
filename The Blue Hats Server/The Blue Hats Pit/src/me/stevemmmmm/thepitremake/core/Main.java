package me.stevemmmmm.thepitremake.core;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.servercore.core.ServerGame;
import me.stevemmmmm.servercore.core.WorldType;
import me.stevemmmmm.thepitremake.chat.LevelChatFormatting;
import me.stevemmmmm.thepitremake.commands.*;
import me.stevemmmmm.thepitremake.enchants.*;
import me.stevemmmmm.thepitremake.game.*;
import me.stevemmmmm.thepitremake.game.duels.DuelingManager;
import me.stevemmmmm.thepitremake.game.duels.GameUtility;
import me.stevemmmmm.thepitremake.managers.ServerMOTDInitializer;
import me.stevemmmmm.thepitremake.managers.enchants.BowManager;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thepitremake.managers.enchants.DamageManager;
import me.stevemmmmm.thepitremake.managers.other.GrindingSystem;
import me.stevemmmmm.thepitremake.managers.other.PitScoreboardManager;
import me.stevemmmmm.thepitremake.perks.Vampire;
import me.stevemmmmm.thepitremake.utils.DevelopmentMode;
import me.stevemmmmm.thepitremake.world.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Logger;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class Main extends JavaPlugin implements ServerGame {
    public static Main INSTANCE;
//    public static ProtocolManager protocolManager;

    public static String version = "v1.0";

    @Override
    public void onEnable() {
        INSTANCE = this;
//        protocolManager = ProtocolLibrary.getProtocolManager();

        ConfigAPI.registerConfigWriteLocations(this, new HashMap<String, String>() {{
            put("Gold", "stats.gold");
            put("XP", "stats.xp");
            put("Prestiges", "stats.prestige");
            put("Levels", "stats.level");
        }});

        ConfigAPI.registerConfigWriter(GrindingSystem.getInstance());
        ConfigAPI.registerConfigReader(GrindingSystem.getInstance());

        Logger log = Bukkit.getLogger();
        log.info("------------------------------------------");
        log.info("The Hypixel Pit Remake by Stevemmmmm");
        log.info("------------------------------------------");

        //Initialization
        registerEnchants();
        registerPerks();

        //Utility
        getServer().getPluginManager().registerEvents(new GameUtility(), this);
        getServer().getPluginManager().registerEvents(new ClearArrows(), this);
        getServer().getPluginManager().registerEvents(new AntiFall(), this);
        getServer().getPluginManager().registerEvents(new AutoRespawn(), this);
        getServer().getPluginManager().registerEvents(new PlayerUtility(), this);
        getServer().getPluginManager().registerEvents(new DeveloperUpdates(), this);
        getServer().getPluginManager().registerEvents(new WorldProtection(), this);
        getServer().getPluginManager().registerEvents(RegionManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new DevelopmentMode(), this);
        getServer().getPluginManager().registerEvents(new ServerMOTDInitializer(), this);
        getServer().getPluginManager().registerEvents(new ChatManagement(), this);
        getServer().getPluginManager().registerEvents(new TogglePvPCommand(), this);

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
        getCommand("unenchant").setExecutor(new UnenchantCommand());
        getCommand("togglepvp").setExecutor(new TogglePvPCommand());
        getCommand("selectworld").setExecutor(new SelectWorldCommand());

        SpawnCommand spawnCommand = new SpawnCommand();
        getCommand("spawn").setExecutor(spawnCommand);
        getCommand("respawn").setExecutor(spawnCommand);

        //Game
        getServer().getPluginManager().registerEvents(new NewMysticWell(), this);
        System.out.println("Using the new mystic well!");

        getServer().getPluginManager().registerEvents(PitScoreboardManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DuelingManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DamageIndicator.getInstance(), this);
        getServer().getPluginManager().registerEvents(CombatManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(DamageManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(BowManager.getInstance(), this);
        getServer().getPluginManager().registerEvents(new LevelChatFormatting(), this);
        getServer().getPluginManager().registerEvents(new Bread(), this);
        getServer().getPluginManager().registerEvents(GrindingSystem.getInstance(), this);
        getServer().getPluginManager().registerEvents(Obsidian.getInstance(), this);
        getServer().getPluginManager().registerEvents(new PlayableArea(), this);
        getServer().getPluginManager().registerEvents(new StopLiquidFlow(), this);
        getServer().getPluginManager().registerEvents(WorldSelection.getInstance(), this);
        getServer().getPluginManager().registerEvents(EnderChest.getInstance(), this);
//        getServer().getPluginManager().registerEvents(new MonsterBlob(), this);
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).disconnect("Server restart!");
        }

//        GrindingSystem.getInstance().writeToConfig();

//        EnderChest.getInstance().storeEnderChests();

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.isValid()) {
                    entity.remove();
                }
            }
        }

        Obsidian.getInstance().removeObsidian();
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
        CustomEnchantManager.getInstance().registerEnchant(new Bruiser());
        CustomEnchantManager.getInstance().registerEnchant(new Frostbite());
        CustomEnchantManager.getInstance().registerEnchant(new Executioner());
        CustomEnchantManager.getInstance().registerEnchant(new BeatTheSpammers());
        CustomEnchantManager.getInstance().registerEnchant(new ComboHeal());
        CustomEnchantManager.getInstance().registerEnchant(new Sweaty());
        CustomEnchantManager.getInstance().registerEnchant(new Knockback());

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
        CustomEnchantManager.getInstance().registerEnchant(new Chipping());
        CustomEnchantManager.getInstance().registerEnchant(new Fletching());
        CustomEnchantManager.getInstance().registerEnchant(new SprintDrain());

        //Pants
        CustomEnchantManager.getInstance().registerEnchant(new Mirror());
        CustomEnchantManager.getInstance().registerEnchant(new Solitude());
        CustomEnchantManager.getInstance().registerEnchant(new Assassin());
        CustomEnchantManager.getInstance().registerEnchant(new CriticallyFunky());
        CustomEnchantManager.getInstance().registerEnchant(new LastStand());
        CustomEnchantManager.getInstance().registerEnchant(new Peroxide());
        CustomEnchantManager.getInstance().registerEnchant(new BooBoo());
        CustomEnchantManager.getInstance().registerEnchant(new DoubleJump());
        CustomEnchantManager.getInstance().registerEnchant(new FractionalReserve());
        CustomEnchantManager.getInstance().registerEnchant(new RingArmor());
    }

    @Override
    public String getGameName() {
        return "The Blue Hats Pit";
    }

    @Override
    public String getReferenceName() {
        return "ThePit";
    }

    @Override
    public World getGameMap() {
        return Bukkit.getWorld("world");
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.MONITORED;
    }
}
