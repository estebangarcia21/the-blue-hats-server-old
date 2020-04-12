package me.stevemmmmm.thehypixelpit.managers.other;

import me.stevemmmmm.configapi.core.ConfigAPI;
import me.stevemmmmm.configapi.core.ConfigWriter;
import me.stevemmmmm.permissions.core.PermissionsManager;
import me.stevemmmmm.thehypixelpit.core.Main;
import me.stevemmmmm.thehypixelpit.managers.enchants.CustomEnchantManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Copyright (c) 2020. Created by the Pit Player: Stevemmmmm.
 */

public class GrindingSystem implements Listener, ConfigWriter {
    private static GrindingSystem instance;

    private HashMap<Integer, Integer> xpPerLevel = new HashMap<>();
    private HashMap<Integer, Float> prestigeMultiplier = new HashMap<>();

    private HashMap<UUID, Integer> playerPrestiges = new HashMap<>();
    private HashMap<UUID, Integer> playerLevels = new HashMap<>();
    private HashMap<UUID, Integer> playerXP = new HashMap<>();
    private HashMap<UUID, Double> playerGold = new HashMap<>();

    private GrindingSystem() {
        initializeMaps();
    }

    public static GrindingSystem getInstance() {
        if (instance == null) instance = new GrindingSystem();

        return instance;
    }

    public void updateLevel(Player player) {
        if (playerLevels.get(player.getUniqueId()) == 120) return;

        if (playerXP.get(player.getUniqueId()) >= xpPerLevel.get(playerLevels.get(player.getUniqueId()) + 1) * prestigeMultiplier.getOrDefault(playerPrestiges.get(player.getUniqueId()), 1f)) {
            IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.AQUA + "LEVEL UP!" + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
            PacketPlayOutTitle length = new PacketPlayOutTitle(20, 20, 20);

            IChatBaseComponent chatSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + ChatColor.YELLOW + getPlayerLevel(player) + " ⇢ " + (getPlayerLevel(player) + 1) + "\",color:" + ChatColor.GOLD.name().toLowerCase() + "}");

            PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, chatSubTitle);
            PacketPlayOutTitle subTitleLength = new PacketPlayOutTitle(20, 20, 20);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitle);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subTitleLength);

            playerLevels.put(player.getUniqueId(), playerLevels.get(player.getUniqueId()) + 1);

            ((CraftPlayer) player).getHandle().listName = CraftChatMessage.fromString(getFormattedPlayerLevelWithoutPrestige(player) + ChatColor.GOLD + " " + player.getName())[0];
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));

            for (Player p : player.getWorld().getPlayers()) {
                ((CraftPlayer) p).getHandle().listName = CraftChatMessage.fromString(getFormattedPlayerLevelWithoutPrestige(player) + ChatColor.GOLD + " " + player.getName())[0];
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, ((CraftPlayer) player).getHandle()));
            }
        }
    }

    public void giveXP(Player player, int amount) {
        playerXP.put(player.getUniqueId(), playerXP.get(player.getUniqueId()) + amount);
    }

    public int giveRandomXP(Player player) {
        int xp = ThreadLocalRandom.current().nextInt(22, 35);
        playerXP.put(player.getUniqueId(), playerXP.get(player.getUniqueId()) + xp);

        return xp;
    }

    public void giveGold(Player player, int amount) {
        playerGold.put(player.getUniqueId(), playerGold.get(player.getUniqueId()) + amount);
    }

    public double giveRandomGold(Player player) {
        DecimalFormat df = new DecimalFormat("###.##");
        double gold = ThreadLocalRandom.current().nextDouble(10, 25);
        playerGold.put(player.getUniqueId(), Double.parseDouble(df.format(playerGold.get(player.getUniqueId()) + gold)));

        return gold;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!playerPrestiges.containsKey(event.getPlayer().getUniqueId())) playerPrestiges.put(event.getPlayer().getUniqueId(), 0);
        if (!playerXP.containsKey(event.getPlayer().getUniqueId())) playerXP.put(event.getPlayer().getUniqueId(), 0);
        if (!playerGold.containsKey(event.getPlayer().getUniqueId())) playerGold.put(event.getPlayer().getUniqueId(), 0D);
        if (!playerLevels.containsKey(event.getPlayer().getUniqueId())) playerLevels.put(event.getPlayer().getUniqueId(), 0);

        readConfig(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            event.getEntity().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "DEATH!");
            return;
        }

        if (event.getEntity().getName().equalsIgnoreCase(event.getEntity().getKiller().getName())) {
            event.getEntity().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "DEATH!");
            return;
        }

        DecimalFormat df = new DecimalFormat("##0.00");
        event.getEntity().getKiller().sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "KILL!" + ChatColor.GRAY + " on " + PermissionsManager.getInstance().getPlayerRank(event.getEntity()).getNameColor() + event.getEntity().getName() + ChatColor.AQUA + " +" + giveRandomXP(event.getEntity().getKiller()) + "XP" + ChatColor.GOLD + " +" + df.format(giveRandomGold(event.getEntity().getKiller())) + "g");
        event.getEntity().sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "DEATH! " + ChatColor.GRAY + "by " + getFormattedPlayerLevelWithoutPrestige(event.getEntity().getKiller()) + " " + PermissionsManager.getInstance().getPlayerRank(event.getEntity().getKiller()).getNameColor() + event.getEntity().getKiller().getName() + " " + ChatColor.YELLOW.toString() + ChatColor.BOLD + "VIEW RECAP");
        updateLevel(event.getEntity().getKiller());
    }

    private void initializeMaps() {
        prestigeMultiplier.put(0, 1f);
        prestigeMultiplier.put(1, 1.15f);
        prestigeMultiplier.put(2, 1.225f);
        prestigeMultiplier.put(3, 1.5f);

        xpPerLevel.put(1, 20);

        for (int i = 2; i <= 120; i++) {
            xpPerLevel.put(i, xpPerLevel.get(i - 1) + (int) (80 * Math.round(Math.pow(Math.E, (float) i / 20))));
        }
    }

    public int getPlayerLevel(Player player) {
        return playerLevels.getOrDefault(player.getUniqueId(), 0);
    }

    public String getFormattedPlayerLevel(Player player) {
        if (getPlayerPrestige(player) != 0) {
            ChatColor color = ChatColor.GRAY;

            if (getPlayerPrestige(player) < 5) {
                color = ChatColor.BLUE;
            } else if (getPlayerPrestige(player) < 10) {
                color = ChatColor.YELLOW;
            } else if (getPlayerPrestige(player) < 15) {
                color = ChatColor.GOLD;
            } else if (getPlayerPrestige(player) < 20) {
                color = ChatColor.RED;
            } else if (getPlayerPrestige(player) < 25) {
                color = ChatColor.DARK_PURPLE;
            } else if (getPlayerPrestige(player) < 30) {
                color = ChatColor.LIGHT_PURPLE;
            } else if (getPlayerPrestige(player) < 35) {
                color = ChatColor.WHITE;
            } else if (getPlayerPrestige(player) == 35) {
                color = ChatColor.AQUA;
            }

            if (getPlayerLevel(player) < 10) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.GRAY + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 10 && getPlayerLevel(player) < 20) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.BLUE + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 20 && getPlayerLevel(player) < 30) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.DARK_AQUA + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 30 && getPlayerLevel(player) < 40) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.DARK_GREEN + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 40 && getPlayerLevel(player) < 50) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.GREEN + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 50 && getPlayerLevel(player) < 60) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.YELLOW + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 60 && getPlayerLevel(player) < 70) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.GOLD.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 70 && getPlayerLevel(player) < 80) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 80 && getPlayerLevel(player) < 90) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 90 && getPlayerLevel(player) < 100) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 100 && getPlayerLevel(player) < 110) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 110 && getPlayerLevel(player) < 120) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.WHITE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) == 120) {
                return color + "[" + ChatColor.YELLOW + CustomEnchantManager.getInstance().convertToRomanNumeral(getPlayerPrestige(player)) + color + "-" + ChatColor.AQUA.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }
        } else {
            if (getPlayerLevel(player) < 10) {
                return ChatColor.GRAY + "[" + getPlayerLevel(player) + "]";
            }

            if (getPlayerLevel(player) >= 10 && getPlayerLevel(player) < 20) {
                return ChatColor.GRAY + "[" + ChatColor.BLUE + getPlayerLevel(player) + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 20 && getPlayerLevel(player) < 30) {
                return ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + getPlayerLevel(player) + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 30 && getPlayerLevel(player) < 40) {
                return ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + getPlayerLevel(player) + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 40 && getPlayerLevel(player) < 50) {
                return ChatColor.GRAY + "[" + ChatColor.GREEN + getPlayerLevel(player) + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 50 && getPlayerLevel(player) < 60) {
                return ChatColor.GRAY + "[" + ChatColor.YELLOW + getPlayerLevel(player) + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 60 && getPlayerLevel(player) < 70) {
                return ChatColor.GRAY + "[" + ChatColor.GOLD.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 70 && getPlayerLevel(player) < 80) {
                return ChatColor.GRAY + "[" + ChatColor.RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 80 && getPlayerLevel(player) < 90) {
                return ChatColor.GRAY + "[" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 90 && getPlayerLevel(player) < 100) {
                return ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 100 && getPlayerLevel(player) < 110) {
                return ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) >= 110 && getPlayerLevel(player) < 120) {
                return ChatColor.GRAY + "[" + ChatColor.WHITE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }

            if (getPlayerLevel(player) == 120) {
                return ChatColor.GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
            }
        }

        return null;
    }

    public String getFormattedPlayerLevelWithoutPrestige(Player player) {
        if (getPlayerPrestige(player) != 0) {
            ChatColor color = ChatColor.GRAY;

            if (getPlayerPrestige(player) < 5) {
                color = ChatColor.BLUE;
            } else if (getPlayerPrestige(player) < 10) {
                color = ChatColor.YELLOW;
            } else if (getPlayerPrestige(player) < 15) {
                color = ChatColor.GOLD;
            } else if (getPlayerPrestige(player) < 20) {
                color = ChatColor.RED;
            } else if (getPlayerPrestige(player) < 25) {
                color = ChatColor.DARK_PURPLE;
            } else if (getPlayerPrestige(player) < 30) {
                color = ChatColor.LIGHT_PURPLE;
            } else if (getPlayerPrestige(player) < 35) {
                color = ChatColor.WHITE;
            } else if (getPlayerPrestige(player) == 35) {
                color = ChatColor.AQUA;
            }

            if (getPlayerLevel(player) < 10) {
                return color + "[" + ChatColor.GRAY + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 10 && getPlayerLevel(player) < 20) {
                return color + "[" + ChatColor.BLUE + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 20 && getPlayerLevel(player) < 30) {
                return color + "[" + ChatColor.DARK_AQUA + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 30 && getPlayerLevel(player) < 40) {
                return color + "[" + ChatColor.DARK_GREEN + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 40 && getPlayerLevel(player) < 50) {
                return color + "[" + ChatColor.GREEN + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 50 && getPlayerLevel(player) < 60) {
                return color + "[" + ChatColor.YELLOW + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 60 && getPlayerLevel(player) < 70) {
                return color + "[" + ChatColor.GOLD.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 70 && getPlayerLevel(player) < 80) {
                return color + "[" + ChatColor.RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 80 && getPlayerLevel(player) < 90) {
                return color + "[" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 90 && getPlayerLevel(player) < 100) {
                return color + "[" + ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 100 && getPlayerLevel(player) < 110) {
                return color + "[" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) >= 110 && getPlayerLevel(player) < 120) {
                return color + "[" + ChatColor.WHITE.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }

            if (getPlayerLevel(player) == 120) {
                return color + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD + getPlayerLevel(player) + color + "]";
            }
        }

        if (getPlayerLevel(player) < 10) {
            return ChatColor.GRAY + "[" + getPlayerLevel(player) + "]";
        }

        if (getPlayerLevel(player) >= 10 && getPlayerLevel(player) < 20) {
            return ChatColor.GRAY + "[" + ChatColor.BLUE + getPlayerLevel(player) + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 20 && getPlayerLevel(player) < 30) {
            return ChatColor.GRAY + "[" + ChatColor.DARK_AQUA + getPlayerLevel(player) + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 30 && getPlayerLevel(player) < 40) {
            return ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + getPlayerLevel(player) + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 40 && getPlayerLevel(player) < 50) {
            return ChatColor.GRAY + "[" + ChatColor.GREEN + getPlayerLevel(player) + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 50 && getPlayerLevel(player) < 60) {
            return ChatColor.GRAY + "[" + ChatColor.YELLOW + getPlayerLevel(player) + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 60 && getPlayerLevel(player) < 70) {
            return ChatColor.GRAY + "[" + ChatColor.GOLD.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 70 && getPlayerLevel(player) < 80) {
            return ChatColor.GRAY + "[" + ChatColor.RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 80 && getPlayerLevel(player) < 90) {
            return ChatColor.GRAY + "[" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 90 && getPlayerLevel(player) < 100) {
            return ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 100 && getPlayerLevel(player) < 110) {
            return ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) >= 110 && getPlayerLevel(player) < 120) {
            return ChatColor.GRAY + "[" + ChatColor.WHITE.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        if (getPlayerLevel(player) == 120) {
            return ChatColor.GRAY + "[" + ChatColor.AQUA.toString() + ChatColor.BOLD + getPlayerLevel(player) + ChatColor.RESET + ChatColor.GRAY + "]";
        }

        return null;
    }

    public HashMap<UUID, Integer> getPlayerPrestiges() {
        return playerPrestiges;
    }

    public int getPlayerPrestige(Player player) {
        return playerPrestiges.getOrDefault(player.getUniqueId(), 0);
    }

    public int getPlayerXP(Player player) {
        return playerXP.getOrDefault(player.getUniqueId(), 0);
    }

    public double getPlayerGold(Player player) {
        return playerGold.getOrDefault(player.getUniqueId(), 0.00D);
    }

    public String getFormattedPlayerGold(Player player) {
        return new DecimalFormat("###,###,###,###,###,##0.00").format(playerGold.getOrDefault(player.getUniqueId(), 0D));
    }

    public void setPlayerLevel(Player player, int value) {
        playerLevels.put(player.getUniqueId(), value);
    }

    public void setPlayerGold(Player player, double value) {
        if (value < 0) value = 0;

        playerGold.put(player.getUniqueId(), value);
    }

    public void setPlayerPrestige(Player player, int value) {
        playerPrestiges.put(player.getUniqueId(), value);
    }

    public void readConfig(Player player) {
        playerPrestiges.put(player.getUniqueId(), ConfigAPI.read(Main.INSTANCE, player, "Prestiges", Integer.class));
        playerXP.put(player.getUniqueId(), ConfigAPI.read(Main.INSTANCE, player, "XP", Integer.class));
        playerGold.put(player.getUniqueId(), ConfigAPI.read(Main.INSTANCE, player, "Gold", Double.class));
        playerLevels.put(player.getUniqueId(), ConfigAPI.read(Main.INSTANCE, player, "Levels", Integer.class));
    }

    @Override
    public void writeToConfig() {
        ConfigAPI.write(Main.INSTANCE, "XP", playerXP);
        ConfigAPI.write(Main.INSTANCE, "Gold", playerGold);
        ConfigAPI.write(Main.INSTANCE, "Prestiges", playerPrestiges);
        ConfigAPI.write(Main.INSTANCE, "Levels", playerLevels);
    }
}
