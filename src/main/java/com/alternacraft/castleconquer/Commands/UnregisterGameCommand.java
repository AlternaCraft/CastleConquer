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
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class UnregisterGameCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();

        GamesRegister greg = Manager.getGamesRegister();

        World world;

        if (cs instanceof Player) {
            Player player = (Player) cs;
            if (args.length == 1) {
                world = player.getWorld();
            } else {
                world = Bukkit.getWorld(args[1]);

                if (world == null) {
                    MessageManager.sendCommandSender(cs,
                            MainLanguageFile.WORLD_NOT_EXIST
                            .getText(Localizer.getLocale(player))
                    );
                    return true;
                }
            }

            if (greg.isRegistered(world)) {
                unregister(cs, world);
            } else {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                        .getText(Localizer.getLocale(player))
                );
            }
        } else if (args.length >= 2) {
            world = Bukkit.getWorld(args[1]);

            if (world != null) {
                if (greg.isRegistered(world)) {
                    unregister(cs, world);
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
                    ManageLanguageFile.GAME_WORLD_REGISTER_CONSOLE
                    .getText(Langs.EN)
                    .replace("%cmd_usage%", usage)
            );
        }
        return true;
    }

    private void unregister(CommandSender cs, World world) {
        GamesRegisterer.GamesRegistererResult result = Manager
                .getGamesRegisterer().unregister(world);

        if (!result.error) {
            if (cs instanceof Player) {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_UNREGISTERED
                        .getText(Localizer.getLocale((Player) cs))
                );
            } else {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_UNREGISTERED
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
