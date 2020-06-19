package me.stevemmmmm.thepitremake.game;

/*
 * Copyright (c) 2020. Created by Stevemmmmm.
 */

import me.stevemmmmm.animationapi.core.Sequence;
import me.stevemmmmm.animationapi.core.SequenceAPI;
import me.stevemmmmm.animationapi.core.SequenceActions;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchant;
import me.stevemmmmm.thepitremake.managers.enchants.CustomEnchantManager;
import me.stevemmmmm.thepitremake.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class NewMysticWell implements Listener {
    private final HashMap<UUID, MysticWellInventory> playerMysticWellInventorys = new HashMap<>();

    private final HashMap<UUID, MysticWell.MysticWellAnimation> mysticWellStates = new HashMap<>();
    private final HashMap<UUID, Sequence> mysticWellSequences = new HashMap<>();

    private static final ItemStack enchantmentTableInfoIdle = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT1 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT2 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoT3 = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoItsRollin = new ItemStack(Material.ENCHANTMENT_TABLE);
    private static final ItemStack enchantmentTableInfoMaxTier = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);

    private final int[] glassPanePositions = new int[] { 10, 11, 12, 21, 30, 29, 28, 19 };

    public NewMysticWell() {
        init();
    }

    public void init() {
        ItemMeta etMeta = enchantmentTableInfoIdle.getItemMeta();

        //This was before my LoreBuilder class
        etMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        etMeta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Find a " + ChatColor.AQUA + "Mystic Bow" + ChatColor.GRAY + ", " + ChatColor.YELLOW + "Mystic");
            add(ChatColor.YELLOW + "Sword" + ChatColor.GRAY + " or " + ChatColor.RED + "P" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "n" + ChatColor.GREEN + "t" + ChatColor.BLUE + "s" + ChatColor.GRAY + " from");
            add(ChatColor.GRAY + "killing players");
            add(" ");
            add(ChatColor.GRAY + "Enchant these items in the well");
            add(ChatColor.GRAY + "for tons of buffs.");
            add(" ");
            add(ChatColor.LIGHT_PURPLE + "Click an item in your inventory!");
        }});

        enchantmentTableInfoIdle.setItemMeta(etMeta);

        ItemMeta et1Meta = enchantmentTableInfoT1.getItemMeta();

        et1Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et1Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.GREEN + " Tier I");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 1,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT1.setItemMeta(et1Meta);

        ItemMeta et2Meta = enchantmentTableInfoT1.getItemMeta();

        et2Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et2Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.YELLOW + " Tier II");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 4,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT2.setItemMeta(et2Meta);

        ItemMeta et3Meta = enchantmentTableInfoT1.getItemMeta();

        et3Meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Mystic Well");
        et3Meta.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "Upgrade:" + ChatColor.RED + " Tier III");
            add(ChatColor.GRAY + "Cost:" + ChatColor.GOLD + " 8,000g");
            add(" ");
            add(ChatColor.YELLOW + "Click to upgrade!");
        }});

        enchantmentTableInfoT3.setItemMeta(et3Meta);

        ItemMeta enirMeta = enchantmentTableInfoItsRollin.getItemMeta();
        enirMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Its' rollin!");
        enchantmentTableInfoItsRollin.setItemMeta(enirMeta);

        ItemMeta etMax = enchantmentTableInfoMaxTier.getItemMeta();

        etMax.setDisplayName(ChatColor.RED + "Mystic Well");
        etMax.setLore(new ArrayList<String>() {{
            add(ChatColor.GRAY + "This item cannot be");
            add(ChatColor.GRAY + "upgraded any further");
            add(" ");
            add(ChatColor.RED + "Maxed out upgrade tier!");
        }});

        enchantmentTableInfoMaxTier.setItemMeta(etMax);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!playerMysticWellInventorys.containsKey(event.getPlayer().getUniqueId())) playerMysticWellInventorys.put(event.getPlayer().getUniqueId(), MysticWellInventory.newInventory(event.getPlayer()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.GRAY + "Mystic Well") || event.getSlotType() != InventoryType.SlotType.OUTSIDE) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();

        if (MysticWellInventory.pressedEnchantButton(event) && playerMysticWellInventorys.get(player.getUniqueId()).canEnchant()) {
            startEnchantmentProcess(player);
        }

        updateGui(event);
    }

    private void startEnchantmentProcess(Player player) {
        MysticWellInventory inventory = playerMysticWellInventorys.get(player.getUniqueId());
        ItemStack targetItem = inventory.getItemInEnchantmentSlot();

        ItemStack[] animationItems = new ItemStack[] { new ItemStack(Material.INK_SACK, 1, (byte) 0), new ItemStack(Material.INK_SACK, 1, (byte) 1), new ItemStack(Material.INK_SACK, 1, (byte) 2), new ItemStack(Material.INK_SACK, 1, (byte) 3), new ItemStack(Material.INK_SACK, 1, (byte) 4), new ItemStack(Material.INK_SACK, 1, (byte) 5), new ItemStack(Material.INK_SACK, 1, (byte) 6), new ItemStack(Material.INK_SACK, 1, (byte) 7), new ItemStack(Material.INK_SACK, 1, (byte) 8), new ItemStack(Material.INK_SACK, 1, (byte) 9), new ItemStack(Material.INK_SACK, 1, (byte) 10), new ItemStack(Material.INK_SACK, 1, (byte) 11), new ItemStack(Material.INK_SACK, 1, (byte) 12), new ItemStack(Material.INK_SACK, 1, (byte) 13), new ItemStack(Material.INK_SACK, 1, (byte) 14) };
        final int[] glassPaneIndex = { 0 };

        Sequence enchantmentSequence = new Sequence()
                .addKeyFrame(0, () -> setGlassPanesToColor(player, "Green"))
                .addKeyFrame(2, () -> setGlassPanesToColor(player, "Gray"))
                .addKeyFrame(4, () -> setGlassPanesToColor(player, "Green"))
                .addKeyFrame(6, () -> setGlassPanesToColor(player, "Gray"))
                .repeatAddKeyFrame(() -> {
                    setPaneToPink(player, glassPaneIndex[0]);
                    inventory.getRawInventory().setItem(20, animationItems[ThreadLocalRandom.current().nextInt(animationItems.length)]);

                    glassPaneIndex[0]++;

                    if (glassPaneIndex[0] + 1 > glassPanePositions.length) {
                        glassPaneIndex[0] = 0;
                    }
                }, 8, 2, 40);


        enchantmentSequence.setAnimationActions(new SequenceActions() {
            @Override
            public void onSequenceStart() {
                playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory().setItem(24, enchantmentTableInfoItsRollin);
            }

            @Override
            public void onSequenceEnd() {
                updateEnchants(targetItem);
                inventory.getRawInventory().setItem(20, targetItem);
                setMysticWellToIdle(player);
//                inventory.getRawInventory().setItem(24, getInfoFromTier(getItemTier(itemInput)));
            }
        });

        mysticWellSequences.put(player.getUniqueId(), enchantmentSequence);

        SequenceAPI.startSequence(enchantmentSequence);
    }

    private void setPaneToPink(Player player, int index) {
        setGlassPanesToColor(player, "Gray");

        playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory().setItem(glassPanePositions[index], new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
    }

    private void setMysticWellToIdle(Player player) {
        Inventory gui = playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory();

        if (mysticWellStates.containsKey(player.getUniqueId())) {
            if (mysticWellStates.get(player.getUniqueId()) == MysticWell.MysticWellAnimation.IDLE) return;
        }

        final int[] position = { 0 };

        Sequence idleSequence = new Sequence()
                .repeatAddKeyFrame(() -> {
                    setPaneToPink(player, position[0]);
                    position[0]++;

                    if (position[0] + 1 > glassPanePositions.length) {
                        position[0] = 0;
                    }
                }, 0, 2, 50)
                .loop();

        mysticWellStates.put(player.getUniqueId(), MysticWell.MysticWellAnimation.IDLE);
        mysticWellSequences.put(player.getUniqueId(), idleSequence);

        SequenceAPI.startSequence(idleSequence);
    }

    private void setGlassPanesToColor(Player player, String color) {
        if (color.equalsIgnoreCase("Green")) {
            for (int glassPane : glassPanePositions) {
                playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory().setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 13));
            }
        }

        if (color.equalsIgnoreCase("Gray")) {
            for (int glassPane : glassPanePositions) {
                playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory().setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));
            }
        }

        if (color.equalsIgnoreCase("Pink")) {
            for (int glassPane : glassPanePositions) {
                playerMysticWellInventorys.get(player.getUniqueId()).getRawInventory().setItem(glassPane, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 6));
            }
        }
    }

    private ItemStack getIconFromTier(int tier) {
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

    private void updateEnchants(ItemStack item) {
        //TODO Rethink process here
    }

    private void addEnchantsToItem(ItemStack item, int minLives, int maxLives, double livesBias, int maxToken, CustomEnchant... enchants) {
        for (CustomEnchant enchant : enchants) {
            if (CustomEnchantManager.getInstance().itemContainsEnchant(item, enchant)) {
                updateEnchants(item);
                return;
            }
        }

        int lives = CustomEnchantManager.getInstance().getItemLives(item) + MathUtils.biasedRandomness(minLives, maxLives, livesBias);

        CustomEnchantManager.getInstance().addEnchants(item, MathUtils.biasedRandomness(1, maxToken, 3.5), enchants);
        CustomEnchantManager.getInstance().setItemLives(item, lives);
        CustomEnchantManager.getInstance().setMaximumItemLives(item, lives);
    }

    private void updateGui(InventoryClickEvent event) {

    }
}
