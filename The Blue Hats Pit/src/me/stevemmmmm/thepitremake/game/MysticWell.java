package me.stevemmmmm.thepitremake.game;

import me.stevemmmmm.animationapi.core.Sequence;
import me.stevemmmmm.animationapi.core.SequenceAPI;
import me.stevemmmmm.animationapi.core.SequenceActions;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thepitremake.managers.enchants.EnchantGroup;
import me.stevemmmmm.thepitremake.managers.other.GrindingSystem;
import me.stevemmmmm.thepitremake.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
 * This class is lazily coded.
 */

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

public class MysticWell implements Listener {
    private final HashMap<UUID, Inventory> playerGuis = new HashMap<>();

    private final HashMap<UUID, MysticWellAnimation> mysticWellStates = new HashMap<>();
    private final HashMap<UUID, Sequence> mysticWellSequences = new HashMap<>();

    private final ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private final ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    private final int[] glassPanes = new int[] { 10, 11, 12, 21, 30, 29, 28, 19 };

    public MysticWell() {
        ItemMeta etMeta = enchantmentTableInfoIdle.getItemMeta();

        etMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        etMeta.setLore(new ArrayList<String>() {
            {
                add(ChatColor.GRAY + "Find a " + ChatColor.AQUA + "Mystic Bow" + ChatColor.GRAY + ", "
                        + ChatColor.YELLOW + "Mystic");
                add(ChatColor.YELLOW + "Sword" + ChatColor.GRAY + " or " + ChatColor.RED + "P" + ChatColor.GOLD + "a"
                        + ChatColor.YELLOW + "n" + ChatColor.GREEN + "t" + ChatColor.BLUE + "s" + ChatColor.GRAY
                        + " from");
                add(ChatColor.GRAY + "killing players");
                add(" ");
                add(ChatColor.GRAY + "Enchant these items in the well");
                add(ChatColor.GRAY + "for tons of buffs.");
                add(" ");
                add(ChatColor.LIGHT_PURPLE + "Click an item in your inventory!");
            }
        });

        enchantmentTableInfoIdle.setItemMeta(etMeta);

        ItemMeta et1Meta = enchantmentTableInfoT1.getItemMeta();

        et1Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et1Meta.setLore(new ArrayList<String>() {
            {
                add(ChatColor.GRAY + "Upgrade:" + ChatColor.GREEN + " Tier I");
                add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 1,000g");
                add(" ");
                add(ChatColor.YELLOW + "Click to upgrade!");
            }
        });

        enchantmentTableInfoT1.setItemMeta(et1Meta);

        ItemMeta et2Meta = enchantmentTableInfoT1.getItemMeta();

        et2Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et2Meta.setLore(new ArrayList<String>() {
            {
                add(ChatColor.GRAY + "Upgrade:" + ChatColor.YELLOW + " Tier II");
                add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 4,000g");
                add(" ");
                add(ChatColor.YELLOW + "Click to upgrade!");
            }
        });

        enchantmentTableInfoT2.setItemMeta(et2Meta);

        ItemMeta et3Meta = enchantmentTableInfoT1.getItemMeta();

        et3Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et3Meta.setLore(new ArrayList<String>() {
            {
                add(ChatColor.GRAY + "Upgrade:" + ChatColor.RED + " Tier III");
                add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 8,000g");
                add(" ");
                add(ChatColor.YELLOW + "Click to upgrade!");
            }
        });

        enchantmentTableInfoT3.setItemMeta(et3Meta);

        ItemMeta enirMeta = enchantmentTableInfoItsRollin.getItemMeta();
        enirMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Its' rollin!");
        enchantmentTableInfoItsRollin.setItemMeta(enirMeta);

        ItemMeta etMax = enchantmentTableInfoMaxTier.getItemMeta();

        etMax.setDisplayName(ChatColor.RED + "Mystic Well");
        etMax.setLore(new ArrayList<String>() {
            {
                add(ChatColor.GRAY + "This item cannot be");
                add(ChatColor.GRAY + "upgraded any further");
                add(" ");
                add(ChatColor.RED + "Maxed out upgrade tier!");
            }
        });

        enchantmentTableInfoMaxTier.setItemMeta(etMax);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerGuis.containsKey(event.getPlayer().getUniqueId()))
            playerGuis.put(event.getPlayer().getUniqueId(), createMysticWell());
        // TODO Read from inventory API
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            event.setCancelled(true);

            if (mysticWellSequences.containsKey(event.getPlayer().getUniqueId())) {
                if (mysticWellStates.get(event.getPlayer().getUniqueId()) == MysticWellAnimation.ENCHANTING) {
                    SequenceAPI.stopSequence(mysticWellSequences.get(event.getPlayer().getUniqueId()));
                }
            }

            setMysticWellAnimation(((Player) event.getPlayer()), MysticWellAnimation.IDLE);
            event.getPlayer().openInventory(playerGuis.get(event.getPlayer().getUniqueId()));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals(ChatColor.GRAY + "Mystic Well")
                && event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();

            if (playerGuis.get(player.getUniqueId()).getItem(20) != null
                    && playerGuis.get(player.getUniqueId()).getItem(event.getSlot()) != null) {
                if (event.getInventory().getItem(event.getSlot()).getType() == Material.ENCHANTMENT_TABLE) {
                    if (playerGuis.get(player.getUniqueId()).getItem(24).getType() != Material.STAINED_CLAY) {
                        int goldAmount = 0;

                        switch (getItemTier(playerGuis.get(player.getUniqueId()).getItem(20))) {
                            case 0:
                                goldAmount = 1000;
                                break;
                            case 1:
                                goldAmount = 4000;
                                break;
                            case 2:
                                goldAmount = 8000;
                                break;
                        }

                        if (GrindingSystem.getInstance().getPlayerGold(player) >= goldAmount) {
                            GrindingSystem.getInstance().setPlayerGold(player,
                                    Math.max(0, GrindingSystem.getInstance().getPlayerGold(player) - goldAmount));
                            // TODO Not enough gold

                            enchantItem(player, event.getInventory().getItem(20));
                        }
                    }
                }
            }

            if (event.getRawSlot() == 20) {
                if (event.getCurrentItem().getType() != Material.AIR) {
                    if (event.getCurrentItem().getType() == Material.GOLD_SWORD
                            || event.getCurrentItem().getType() == Material.BOW
                            || event.getCurrentItem().getType() == Material.LEATHER_LEGGINGS) {
                        for (int i = 0; i < player.getInventory().getSize(); i++) {
                            if (event.getWhoClicked().getInventory().getItem(i) == null) {
                                playerGuis.get(player.getUniqueId()).setItem(24, enchantmentTableInfoIdle);
                                player.getInventory().setItem(i, event.getCurrentItem());
                                playerGuis.get(player.getUniqueId()).setItem(event.getSlot(),
                                        new ItemStack(Material.AIR));
                                break;
                            }
                        }
                    }
                }
            } else if (event.getCurrentItem().getType() != Material.AIR && event.getRawSlot() > 36
                    && playerGuis.get(player.getUniqueId()).getItem(20) == null
                    && mysticWellStates.get(player.getUniqueId()).equals(MysticWellAnimation.IDLE)) {
                if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                    String[] itemTokens = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName())
                            .split(" ");
                    if (itemTokens[0].equalsIgnoreCase("Fresh") || itemTokens[0].equalsIgnoreCase("Mystic")
                            || itemTokens[0].equalsIgnoreCase("Tier")) {
                        playerGuis.get(player.getUniqueId()).setItem(24,
                                getInfoFromTier(getItemTier(event.getCurrentItem())));
                        playerGuis.get(player.getUniqueId()).setItem(20, event.getCurrentItem());
                        player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    private void enchantItem(Player player, ItemStack item) {
        if (item == null)
            return;

        if (mysticWellStates.get(player.getUniqueId()) == MysticWellAnimation.ENCHANTING)
            return;

        SequenceAPI.stopSequence(mysticWellSequences.get(player.getUniqueId()));

        mysticWellStates.put(player.getUniqueId(), MysticWellAnimation.ENCHANTING);

        Inventory gui = playerGuis.get(player.getUniqueId());

        ItemStack itemInput = gui.getItem(20);

        ItemStack[] animationItems = new ItemStack[] { new ItemStack(Material.INK_SACK, 1, (byte) 0),
                new ItemStack(Material.INK_SACK, 1, (byte) 1), new ItemStack(Material.INK_SACK, 1, (byte) 2),
                new ItemStack(Material.INK_SACK, 1, (byte) 3), new ItemStack(Material.INK_SACK, 1, (byte) 4),
                new ItemStack(Material.INK_SACK, 1, (byte) 5), new ItemStack(Material.INK_SACK, 1, (byte) 6),
                new ItemStack(Material.INK_SACK, 1, (byte) 7), new ItemStack(Material.INK_SACK, 1, (byte) 8),
                new ItemStack(Material.INK_SACK, 1, (byte) 9), new ItemStack(Material.INK_SACK, 1, (byte) 10),
                new ItemStack(Material.INK_SACK, 1, (byte) 11), new ItemStack(Material.INK_SACK, 1, (byte) 12),
                new ItemStack(Material.INK_SACK, 1, (byte) 13), new ItemStack(Material.INK_SACK, 1, (byte) 14) };
        final int[] position = { 0 };

        Sequence enchantmentSequence = new Sequence().addKeyFrame(0, () -> setGlassPanesToColor(player, "Green"))
                .addKeyFrame(2, () -> setGlassPanesToColor(player, "Gray"))
                .addKeyFrame(4, () -> setGlassPanesToColor(player, "Green"))
                .addKeyFrame(6, () -> setGlassPanesToColor(player, "Gray")).repeatAddKeyFrame(() -> {
                    setPaneToPink(player, position[0]);
                    gui.setItem(20, animationItems[ThreadLocalRandom.current().nextInt(animationItems.length)]);

                    position[0]++;

                    if (position[0] + 1 > glassPanes.length) {
                        position[0] = 0;
                    }
                }, 8, 2, 40).setAnimationActions(new SequenceActions() {

                    @Override
                    public void onSequenceStart() {
                        playerGuis.get(player.getUniqueId()).setItem(24, enchantmentTableInfoItsRollin);
                    }

                    @Override
                    public void onSequenceEnd() {
                        addNewEnchantsToItem(itemInput);
                        gui.setItem(20, itemInput);
                        setMysticWellAnimation(player, MysticWellAnimation.IDLE);
                        playerGuis.get(player.getUniqueId()).setItem(24, getInfoFromTier(getItemTier(itemInput)));
                    }
                });

        mysticWellSequences.put(player.getUniqueId(), enchantmentSequence);

        SequenceAPI.startSequence(enchantmentSequence);
    }

    private void addNewEnchantsToItem(ItemStack item) {
        if (getItemTier(item) == 0) {
            int determinant = ThreadLocalRandom.current().nextInt(0, 10) + 1;

            if (determinant != 1) {
                CustomEnchant enchant = getRandomEnchantFromGroup(EnchantGroup.A, item.getType());

                addEnchantsToItem(item, 5, 10, 2.25, 2, enchant);
            } else {
                CustomEnchant enchantA = getRandomEnchantFromGroup(EnchantGroup.A, item.getType());
                CustomEnchant enchantC = getRandomEnchantFromGroup(EnchantGroup.C, item.getType());

                addEnchantsToItem(item, 5, 10, 2.25, 1, enchantA, enchantC);
            }

            return;
        }

        if (getItemTier(item) == 1) {
            int determinant = ThreadLocalRandom.current().nextInt(0, 10) + 1;

            CustomEnchant[] buffer = new CustomEnchant[2];

            if (determinant <= 7) {
                int tokenDeterminant = ThreadLocalRandom.current().nextInt(0, 10) + 1;
                boolean addRare = ThreadLocalRandom.current().nextInt(0, 150) == 0;

                if (tokenDeterminant <= 4) {
                    buffer[0] = getRandomEnchantFromGroup(EnchantGroup.A, item.getType());
                }

            } else {
                int order = ThreadLocalRandom.current().nextInt(0, 1);

                buffer[order] = getRandomEnchantFromGroup(EnchantGroup.A, item.getType());
                buffer[order - 1 != 0 ? 1 : 0] = getRandomEnchantFromGroup(EnchantGroup.C, item.getType());
            }

            buffer = Arrays.stream(buffer).filter(Objects::nonNull).toArray(CustomEnchant[]::new);

            addEnchantsToItem(item, 2, 20, 2.35, 3, buffer);
            return;
        }

        if (getItemTier(item) == 2) {
            CustomEnchant enchant = getRandomEnchantFromGroup(EnchantGroup.B, item.getType());

            addEnchantsToItem(item, 5, 25, 2.5, 3, enchant);
        }
    }

    private void addEnchantsToItem(ItemStack item, int minLives, int maxLives, double livesBias, int maxToken,
            CustomEnchant... enchants) {
        for (CustomEnchant enchant : enchants) {
            if (CustomEnchantManager.getInstance().itemContainsEnchant(item, enchant)) {
                addNewEnchantsToItem(item);
                return;
            }
        }

        int lives = CustomEnchantManager.getInstance().getItemLives(item)
                + MathUtils.biasedRandomness(minLives, maxLives, livesBias);

        CustomEnchantManager.getInstance().addEnchants(item, MathUtils.biasedRandomness(1, maxToken, 3.5), enchants);
        CustomEnchantManager.getInstance().setItemLives(item, lives);
        CustomEnchantManager.getInstance().setMaximumItemLives(item, lives);
    }

    private CustomEnchant getRandomEnchantFromGroup(EnchantGroup group, Material type) {
        ArrayList<CustomEnchant> groupEnchants = new ArrayList<>();

        for (CustomEnchant enchant : CustomEnchantManager.getInstance().getEnchants()) {
            if (enchant.getEnchantGroup() == group && enchant.isCompatibleWith(type)) {
                groupEnchants.add(enchant);
            }
        }

        return groupEnchants.get(ThreadLocalRandom.current().nextInt(groupEnchants.size()));
    }

    private void setMysticWellAnimation(Player player, MysticWellAnimation animation) {
        Inventory gui = playerGuis.get(player.getUniqueId());

        if (animation == MysticWellAnimation.IDLE) {
            if (mysticWellStates.containsKey(player.getUniqueId())) {
                if (mysticWellStates.get(player.getUniqueId()) == MysticWellAnimation.IDLE)
                    return;
            }

            final int[] position = { 0 };

            Sequence idleSequence = new Sequence().repeatAddKeyFrame(() -> {
                setPaneToPink(player, position[0]);
                position[0]++;

                if (position[0] + 1 > glassPanes.length) {
                    position[0] = 0;
                }
            }, 0, 2, 50).loop();

            mysticWellStates.put(player.getUniqueId(), MysticWellAnimation.IDLE);
            mysticWellSequences.put(player.getUniqueId(), idleSequence);

            SequenceAPI.startSequence(idleSequence);
        }
    }

    public static int getItemTier(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList(ChatColor.stripColor(meta.getDisplayName()).split(" ")));

        if (tokens.contains("I")) {
            return 1;
        } else if (tokens.contains("II")) {
            return 2;
        } else if (tokens.contains("III")) {
            return 3;
        } else if (tokens.contains("Fresh") || tokens.contains("Mystic")) {
            return 0;
        }

        return -1;
    }

    private void setPaneToPink(Player player, int index) {
        setGlassPanesToColor(player, "Gray");

        playerGuis.get(player.getUniqueId()).setItem(glassPanes[index],
                new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
    }

    private void setGlassPanesToColor(Player player, String color) {
        if (color.equalsIgnoreCase("Green")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane,
                        new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13));
            }
        }

        if (color.equalsIgnoreCase("Gray")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane,
                        new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            }
        }

        if (color.equalsIgnoreCase("Pink")) {
            for (int glassPane : glassPanes) {
                playerGuis.get(player.getUniqueId()).setItem(glassPane,
                        new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
            }
        }
    }

    private Inventory createMysticWell() {
        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.GRAY + "Mystic Well");

        ItemStack grayGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);
        ItemMeta gpMeta = grayGlassPane.getItemMeta();

        gpMeta.setDisplayName(ChatColor.GRAY + "Click an item in your inventory!");

        grayGlassPane.setItemMeta(gpMeta);

        gui.setItem(10, grayGlassPane);
        gui.setItem(11, grayGlassPane);
        gui.setItem(12, grayGlassPane);

        gui.setItem(19, grayGlassPane);
        gui.setItem(21, grayGlassPane);

        gui.setItem(24, enchantmentTableInfoIdle);

        gui.setItem(28, grayGlassPane);
        gui.setItem(29, grayGlassPane);
        gui.setItem(30, grayGlassPane);

        return gui;
    }

    private ItemStack getInfoFromTier(int tier) {
        switch (tier) {
            case 0:
                return enchantmentTableInfoT1;
            case 1:
                return enchantmentTableInfoT2;
            case 2:
                return enchantmentTableInfoT3;
            case 3:
                return enchantmentTableInfoMaxTier;
        }

        return null;
    }

    enum MysticWellAnimation {
        IDLE, ENCHANTING
    }
}
