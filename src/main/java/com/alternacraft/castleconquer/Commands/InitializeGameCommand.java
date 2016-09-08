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
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InitializeGameCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegister greg = Manager.getGamesRegister();
        GamesRegisterer greger = Manager.getGamesRegisterer();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();

        if (cs instanceof Player) {
            Player player = (Player) cs;
            World world = player.getWorld();

            if (args.length >= 2) {
                world = Bukkit.getWorld(args[2]);
            }

            if (world != null) {
                if (greg.isRegistered(world)) {
                    GameInstance gi = greg.seekGameByWorld(world);

                    if (!gi.isInitialized()) {
                        if (gi.canInitialize()) {
                            initializeGame(gi, cs);
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    ManageLanguageFile.GAME_WORLD_CANNOT_INITIALIZE
                                    .getText(Localizer.getLocale(player))
                            );
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.GAME_WORLD_ALREADY_INITIALIZED
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
                        MainLanguageFile.WORLD_NOT_EXIST
                        .getText(Localizer.getLocale(player))
                );
            }
        } else if (args.length >= 2) {
            World world = Bukkit.getWorld(args[1]);

            if (world != null) {
                if (greg.isRegistered(world)) {
                    String team = args[1];

                    GameInstance gi = greg.seekGameByWorld(world);

                    if (gi.isInitialized()) {
                        if (gi.canInitialize()) {
                            initializeGame(gi, cs);
                        } else {
                            MessageManager.sendCommandSender(cs,
                                    ManageLanguageFile.GAME_WORLD_CANNOT_INITIALIZE
                                    .getText(Langs.EN)
                            );
                        }
                    } else {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.GAME_WORLD_ALREADY_INITIALIZED
                                .getText(Langs.EN)
                        );
                    }
                } else {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                            .getText(Langs.EN)
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
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(Langs.EN)
            );
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_INITIALIZE_CONSOLE
                    .getText(Langs.EN)
            );
        }
        return true;
    }

    private void initializeGame(GameInstance gi, CommandSender cs) {
        GamesRegisterer greger = Manager.getGamesRegisterer();
        gi.initialize();

        greger.saveGameInstance(gi);

        if (gi.getSign() != null) {
            Sign sign = (Sign) gi.getSign().getState();
            sign.setLine(3, StringsUtils
                    .translateColors("&d0&5/"
                            + (gi.getMaxPlayersPerTeam() * 2)));
            sign.update();
        }

        if (cs instanceof Player) {
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_INITIALIZED
                    .getText(Localizer.getLocale((Player) cs))
            );
        } else {
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_INITIALIZED
                    .getText(Langs.EN)
            );
        }
    }
}
