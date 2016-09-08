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
package com.alternacraft.castleconquer.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.LocationUtils;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;

public final class SetFlagCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegister greg = Manager.getGamesRegister();
        GamesRegisterer greger = Manager.getGamesRegisterer();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();

        if (cs instanceof Player) {
            if (args.length >= 2) {
                Player player = (Player) cs;
                String team = args[1];

                if (team.equalsIgnoreCase("def")
                        || team.equalsIgnoreCase("att")) {
                    World world = player.getWorld();

                    if (greg.isRegistered(world)) {
                        GameInstance gi = greg.seekGameByWorld(world);

                        if (team.equalsIgnoreCase("def")) {
                            if (gi.getDefendersFlag() == null) {
                                Location flagLoc = player.getLocation();

                                setFlag(gi, player, team);
                            } else {
                                MessageManager.sendCommandSender(cs,
                                        ManageLanguageFile.FLAG_BANNER_DEFENDERS_ALREADY_PLACED
                                        .getText(Localizer.getLocale(player))
                                );
                            }
                        } else if (gi.getAttackersFlag() == null) {
                            Location flagLoc = player.getLocation();

                            setFlag(gi, player, team);
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    ManageLanguageFile.FLAG_BANNER_ATTACKERS_ALREADY_PLACED
                                    .getText(Localizer.getLocale(player))
                            );
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                                .getText(Localizer.getLocale(player))
                        );
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            MainLanguageFile.INVALID_ARGUMENTS
                            .getText(Localizer.getLocale(player))
                    );
                    MessageManager.sendCommandSender(cs,
                            MainLanguageFile.COMMAND_USAGE
                            .getText(Localizer.getLocale(player))
                            .replace("%cmd_usage%", usage)
                    );
                }
            } else {

            }
        } else {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(Langs.EN)
            );
        }

        return true;
    }

    private void setFlag(GameInstance gi, Player player, String team) {
        PlayerInventory playerInv = player.getInventory();
        ItemStack itemInHand = playerInv.getItemInMainHand();

        GamesRegisterer greger = Manager.getGamesRegisterer();
        Block flagBlock = gi.getWorld().getBlockAt(player.getLocation());

        if (!itemInHand.getType().equals(Material.BANNER)) {
            MessageManager.sendPlayer(player,
                    ManageLanguageFile.FLAG_BANNER_NOT_IN_HAND
                    .getText(Localizer.getLocale(player))
            );
            return;
        }

        flagBlock.setType(Material.STANDING_BANNER);

        BannerMeta bannerMeta = (BannerMeta) itemInHand.getItemMeta();

        Banner banner = (Banner) flagBlock.getState();
        org.bukkit.material.Banner bannerMat
                = (org.bukkit.material.Banner) banner.getData();

        bannerMat.setFacingDirection(LocationUtils.yawToFace(player.getLocation().getYaw()));
        banner.setData(bannerMat);

        banner.setBaseColor(bannerMeta.getBaseColor());
        banner.setPatterns(bannerMeta.getPatterns());

        banner.update();

        if (team.equalsIgnoreCase("def")) {
            gi.setDefensorsFlag(flagBlock);

            MessageManager.sendPlayer(player,
                    ManageLanguageFile.FLAG_BANNER_DEFENDERS_PLACED
                    .getText(Localizer.getLocale(player))
            );
        } else {
            gi.setAttackersFlag(flagBlock);

            MessageManager.sendPlayer(player,
                    ManageLanguageFile.FLAG_BANNER_ATTACKERS_PLACED
                    .getText(Localizer.getLocale(player))
            );
        }

        greger.saveGameInstance(gi);
    }
}
