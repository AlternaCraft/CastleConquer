/*
 * Copyright (C) 2016 AlternaCraft
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.alternacraft.castleconquer.Handlers;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.Team.TeamType;
import com.alternacraft.castleconquer.Teams.TeamMember;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;

/**
 * This class handles all the events in relation with joining a game, like
 * selecting a team or selecting a kit.
 *
 * @author AlternaCraft
 */
public final class GameJoiningHandler implements Listener {
    private final CastleConquer plugin;
    private World world;

    public GameJoiningHandler(CastleConquer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlayerClicksGameSign(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (block != null) {
            if (block.getType().equals(Material.WALL_SIGN)
                    || block.getType().equals(Material.SIGN_POST)) {
                Player player = event.getPlayer();
                GamesRegister greg = Manager.getGamesRegister();
                Sign sign = (Sign) event.getClickedBlock().getState();

                if (StringsUtils.stripColors(sign.getLine(0))
                        .equalsIgnoreCase("[CastleConquer]")) {
                    world = Bukkit.getWorld(StringsUtils
                            .stripColors(sign.getLine(2)));

                    if (world == null) {
                        MessageManager.sendCommandSender(
                                player,
                                MainLanguageFile.WORLD_NOT_EXIST
                                .getText(Localizer.getLocale(player))
                        );
                        return;
                    }

                    if (greg.isRegistered(world)) {
                        GameInstance gi = (GameInstance) world.getMetadata(
                                MetadataValues.WORLD_GAME_INSTANCE.key
                        ).get(0).value();

                        if (gi.getSign() != null) {
                            if (gi.getSign().getLocation().equals(block.getLocation())) {
                                if (gi.isInitialized()) {
                                    if (sign.getLine(1).equalsIgnoreCase(StringsUtils
                                            .translateColors("&6Join to match"))) {
                                        if (!gi.isPlayerInQueue(player)) {
                                            startSelecting(gi, player);
                                        } else {
                                            MessageManager.sendCommandSender(
                                                    player,
                                                    GameLanguageFile.GAME_ALREADY_IN_QUEUE
                                                    .getText(Localizer.getLocale(player))
                                            );
                                        }
                                    }
                                } else {
                                    MessageManager.sendPlayer(player,
                                            GameLanguageFile.GAME_JOINING_NOT_INITIALIZED
                                            .getText(Localizer.getLocale(player))
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void startSelecting(GameInstance gi, Player player) {
        Inventory teamSelectorInv = Bukkit.createInventory(player, 9,
                StringsUtils.translateColors("&1[&fCC&1] &0Team selector"));

        ItemStack defensorsItem = new ItemStack(Material.BANNER);
        ItemStack attackersItem = new ItemStack(Material.BANNER);

        BannerMeta defensorsItemMeta = (BannerMeta) defensorsItem.getItemMeta();
        Banner defensorsBanner = (Banner) gi.getDefendersFlag().getState();

        defensorsItemMeta.setBaseColor(defensorsBanner.getBaseColor());
        defensorsItemMeta.setPatterns(defensorsBanner.getPatterns());

        defensorsItemMeta.getItemFlags().add(ItemFlag.HIDE_ATTRIBUTES);

        defensorsItemMeta.setDisplayName(StringsUtils
                .translateColors("&2Join defenders"));

        BannerMeta attackersItemMeta = (BannerMeta) defensorsItem.getItemMeta();
        Banner attackersBanner = (Banner) gi.getAttackersFlag().getState();

        attackersItemMeta.setBaseColor(attackersBanner.getBaseColor());
        attackersItemMeta.setPatterns(attackersBanner.getPatterns());

        attackersItemMeta.setDisplayName(StringsUtils
                .translateColors("&4Join attackers"));

        defensorsItem.setItemMeta(defensorsItemMeta);
        attackersItem.setItemMeta(attackersItemMeta);

        teamSelectorInv.setItem(3, defensorsItem);
        teamSelectorInv.setItem(5, attackersItem);

        for (int i = 0; i < teamSelectorInv.getSize(); i++) {
            if (teamSelectorInv.getItem(i) == null) {
                ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE);
                pane.setDurability((short) 15);
                if (pane.getItemMeta() == null) {
                    System.out.println("null");
                }

                ItemMeta paneItemMeta = pane.getItemMeta();
                paneItemMeta.setDisplayName("");
                pane.setItemMeta(paneItemMeta);

                teamSelectorInv.setItem(i, pane);
            }
        }

        player.openInventory(teamSelectorInv);
    }

    @EventHandler
    public void OnPlayerSelects(InventoryClickEvent event) {
        Inventory inv = event.getInventory();

        if (inv.getName().equalsIgnoreCase(StringsUtils
                .translateColors("&1[&fCC&1] &0Team selector"))) {
            GamesRegister greg = Manager.getGamesRegister();

            if (greg.isRegistered(world)) {
                ItemStack clickedItem = event.getCurrentItem();
                ItemMeta clickedItemMeta = clickedItem.getItemMeta();
                GameInstance gi = (GameInstance) world.getMetadata(
                        MetadataValues.WORLD_GAME_INSTANCE.key
                ).get(0).value();
                Player player = (Player) event.getWhoClicked();

                if (clickedItemMeta != null) {
                    if (clickedItemMeta.getDisplayName() != null) {
                        if (clickedItemMeta.getDisplayName().equalsIgnoreCase(StringsUtils.translateColors("&4Join attackers"))) {
                            joinToTeam(player, gi, TeamType.ATTACKERS);
                        } else if (clickedItem.getItemMeta().getDisplayName()
                                .equalsIgnoreCase(StringsUtils
                                        .translateColors("&2Join defenders"))) {
                            joinToTeam(player, gi, TeamType.DEFENDERS);
                        }
                    }
                }

                event.setCancelled(true);
            }
        }
    }

    private void joinToTeam(Player player, GameInstance gi, TeamType tt) {
        TeamMember tm = new TeamMember(player.getUniqueId());
        if (tt.equals(TeamType.ATTACKERS)) {
            gi.getTeamsManager().addMemberToAttackers(tm);

            MessageManager.sendPlayer(player,
                    GameLanguageFile.GAME_JOINED_AS_ATTACKER
                    .getText(Localizer.getLocale(player))
            );
        } else {
            gi.getTeamsManager().addMemberToDefenders(tm);

            MessageManager.sendPlayer(player,
                    GameLanguageFile.GAME_JOINED_AS_DEFENDER
                    .getText(Localizer.getLocale(player))
            );
        }
        gi.addPlayerToQueue(player);
        player.closeInventory();
    }
}
