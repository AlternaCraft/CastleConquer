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
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DeleteFlagCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegister greg = Manager.getGamesRegister();
        GamesRegisterer greger = Manager.getGamesRegisterer();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();

        if (cs instanceof Player) {
            Player player = (Player) cs;

            if (args.length >= 2) {
                World world = player.getWorld();
                String team = args[1];

                if (args.length >= 3) {
                    world = Bukkit.getWorld(args[2]);
                }

                if (world != null) {
                    if (team.equalsIgnoreCase("def")
                            || team.equalsIgnoreCase("att")) {
                        if (greg.isRegistered(world)) {
                            GameInstance gi = greg.seekGameByWorld(world);
                            deleteFlag(cs, gi, team);
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
                    MessageManager.sendCommandSender(cs,
                            MainLanguageFile.WORLD_NOT_EXIST
                            .getText(Localizer.getLocale(player))
                    );
                }
            }
        } else {

        }
        return true;
    }

    private void deleteFlag(CommandSender cs, GameInstance gi, String team) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        if (team.equalsIgnoreCase("def")) {
            gi.setDefensorsFlag(null);

            if (cs instanceof Player) {
                Player player = (Player) cs;

                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.FLAG_BANNER_DEFENDERS_PLACED
                        .getText(Localizer.getLocale(player))
                );
            } else {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.FLAG_BANNER_DEFENDERS_PLACED
                        .getText(Langs.EN)
                );
            }
        } else {
            gi.setAttackersFlag(null);

            if (cs instanceof Player) {
                Player player = (Player) cs;

                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.FLAG_BANNER_ATTACKERS_PLACED
                        .getText(Localizer.getLocale(player))
                );
            } else {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.FLAG_BANNER_ATTACKERS_PLACED
                        .getText(Langs.EN)
                );
            }
        }

        greger.saveGameInstance(gi);
    }
}
