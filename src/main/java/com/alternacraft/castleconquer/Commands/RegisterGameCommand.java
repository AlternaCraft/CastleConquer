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
import com.alternacraft.aclib.utils.NumbersUtils;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public final class RegisterGameCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();

        if (cs instanceof Player) {
            Player player = (Player) cs;
            World world = player.getWorld();

            if (args.length >= 2) {
                if (NumbersUtils.isInteger(args[1])) {
                    int maxPlayersPerTeam = Integer.valueOf(args[1]);

                    if (maxPlayersPerTeam >= 1
                            && maxPlayersPerTeam <= 24) {
                        if (args.length >= 3) {
                            world = Bukkit.getWorld(args[2]);
                        }

                        if (world != null) {
                            if (!greger.isRegistered(world)) {
                                register(cs, world, maxPlayersPerTeam);
                            } else {
                                MessageManager.sendCommandSender(cs,
                                        ManageLanguageFile.GAME_WORLD_ALREADY_REGISTERED
                                        .getText(Localizer.getLocale(player))
                                        .replace("%cmd_usage%", usage)
                                );
                            }
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    MainLanguageFile.WORLD_NOT_EXIST
                                    .getText(Localizer.getLocale(player))
                            );
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.GAME_WORLD_INITIALIZE_MAX_PLAYERS
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
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.INSUFFICIENT_ARGUMENTS
                        .getText(Localizer.getLocale(player))
                        .replace("%cmd_usage%", usage)
                );
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.COMMAND_USAGE
                        .getText(Localizer.getLocale(player))
                        .replace("%cmd_usage%", usage)
                );
            }
        } else if (args.length >= 3) {
            if (NumbersUtils.isInteger(args[1])) {
                int maxPlayersPerTeam = Integer.valueOf(args[1]);

                if (maxPlayersPerTeam >= 1
                        && maxPlayersPerTeam <= 24) {
                    World world = Bukkit.getWorld(args[2]);

                    if (world != null) {
                        if (!greger.isRegistered(world)) {
                            register(cs, world, maxPlayersPerTeam);
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    ManageLanguageFile.GAME_WORLD_ALREADY_REGISTERED
                                    .getText(Langs.EN)
                                    .replace("%cmd_usage%", usage)
                            );
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                MainLanguageFile.WORLD_NOT_EXIST
                                .getText(Langs.EN)
                        );
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_INITIALIZE_MAX_PLAYERS
                            .getText(Langs.EN)
                    );
                }
            } else {
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.INVALID_ARGUMENTS
                        .getText(Langs.EN)
                );
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.COMMAND_USAGE
                        .getText(Langs.EN).replace("%cmd_usage%", usage)
                );
            }
        } else {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(Langs.EN)
            );
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_REGISTER_CONSOLE
                    .getText(Langs.EN)
            );
        }
        return true;
    }

    private void register(CommandSender cs, World world,
            int maxPlayersPerTeam) {
        GamesRegisterer greger = Manager.getGamesRegisterer();
        GamesRegisterer.GameRegisterResult result = greger
                .registerGame(world, maxPlayersPerTeam);

        Plugin plugin = PluginBase.INSTANCE.plugin();

        if (!result.error) {
            world.setMetadata(MetadataValues.GAME_INSTANCE.key,
                    new FixedMetadataValue(plugin, result.gameinstance));

            if (cs instanceof Player) {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_REGISTERED
                        .getText(Localizer.getLocale((Player) cs))
                );
            } else {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_REGISTERED
                        .getText(Langs.EN)
                );
            }
        } else if (cs instanceof Player) {
            MessageManager.sendCommandSender(
                    cs,
                    result.message.getText(Localizer.getLocale((Player) cs))
            );
        } else {
            MessageManager.sendCommandSender(
                    cs,
                    result.message.getText(Langs.EN)
            );
        }
    }
}
