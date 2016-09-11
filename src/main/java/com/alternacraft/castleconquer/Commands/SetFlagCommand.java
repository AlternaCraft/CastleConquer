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
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.Team.TeamType;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BannerMeta;

public final class SetFlagCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();
        Langs lang = (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();

        TeamType teamType = null;

        if (!(cs instanceof Player)) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(lang)
            );
        } else if (args.length < 2 || !(args[1].equalsIgnoreCase("def")
                || args[1].equalsIgnoreCase("att"))) {
            if (args.length < 2) {
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.INSUFFICIENT_ARGUMENTS
                        .getText(lang)
                );
            } else {
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.INVALID_ARGUMENTS
                        .getText(lang)
                );
            }
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.COMMAND_USAGE
                    .getText(lang)
                    .replace("%cmd_usage%", usage)
            );
        } else {
            Player player = (Player) cs;
            World world = player.getWorld();
            String team = args[1];

            if (!world.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                        .getText(lang)
                );
            } else {
                GameInstance gi = (GameInstance) world
                        .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                        .value();

                if (team.equalsIgnoreCase("def")) {
                    if (gi.getDefendersFlag() != null) {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.FLAG_BANNER_DEFENDERS_ALREADY_PLACED
                                .getText(lang)
                        );
                    } else {
                        teamType = TeamType.DEFENDERS;
                    }
                } else if (gi.getAttackersFlag() != null) {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.FLAG_BANNER_ATTACKERS_ALREADY_PLACED
                            .getText(lang)
                    );
                } else {
                    teamType = TeamType.ATTACKERS;
                }

                setFlag(gi, player, teamType);
            }
        }

        return true;
    }

    private void setFlag(GameInstance gi, Player player, TeamType teamType) {
        PlayerInventory playerInv = player.getInventory();
        ItemStack itemInHand = playerInv.getItemInMainHand();

        if (!itemInHand.getType().equals(Material.BANNER)) {
            MessageManager.sendPlayer(player,
                    ManageLanguageFile.FLAG_BANNER_NOT_IN_HAND
                    .getText(Localizer.getLocale(player))
            );
        } else {
            GamesRegisterer greger = Manager.getGamesRegisterer();
            Block flagBlock = player.getWorld().getBlockAt(player.getLocation());

            flagBlock.setType(Material.STANDING_BANNER);

            BannerMeta bannerMeta = (BannerMeta) itemInHand.getItemMeta();

            Banner banner = (Banner) flagBlock.getState();
            org.bukkit.material.Banner bannerMat
                    = (org.bukkit.material.Banner) banner.getData();

            bannerMat.setFacingDirection(yawToFace(player.getLocation().getYaw()));
            banner.setData(bannerMat);

            banner.setBaseColor(bannerMeta.getBaseColor());
            banner.setPatterns(bannerMeta.getPatterns());

            banner.update();

            if (teamType.equals(TeamType.DEFENDERS)) {
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

    /**
     * Returns the facing direction from an specified yaw.
     *
     * @param yaw
     * @return
     */
    private BlockFace yawToFace(float yaw) {
        double rotation = yaw % 360;

        if (rotation < 0) {
            rotation += 360.0;
        }

        if (rotation > 337.5 && rotation <= 360.0) {
            return BlockFace.SOUTH;
        } else if (rotation > 22.5 && rotation <= 67.5) {
            return BlockFace.SOUTH_WEST;
        } else if (rotation > 67.5 && rotation <= 112.5) {
            return BlockFace.WEST;
        } else if (rotation > 112.5 && rotation <= 157.5) {
            return BlockFace.NORTH_WEST;
        } else if (rotation > 157.5 && rotation <= 202.5) {
            return BlockFace.NORTH;
        } else if (rotation > 202.5 && rotation <= 247.5) {
            return BlockFace.NORTH_EAST;
        } else if (rotation > 247.5 && rotation <= 292.5) {
            return BlockFace.EAST;
        } else if (rotation > 292.5 && rotation <= 337.5) {
            return BlockFace.SOUTH_EAST;
        } else if (rotation >= 0.0 && rotation <= 22.5) {
            return BlockFace.SOUTH;
        } else {
            return null;
        }
    }
}
