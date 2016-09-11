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
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public final class RegisterGameCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegisterer greger = Manager.getGamesRegisterer();
        GamesRegister greg = Manager.getGamesRegister();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();
        Langs lang = (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();

        int maxPlayersPerTeam;
        World world;

        if (args.length == 1 || !NumbersUtils.isInteger(args[1])) {
            if (args.length == 1) {
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
        } else if (!(cs instanceof Player) && args.length == 2) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(lang)
            );
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_REGISTER_CONSOLE
                    .getText(lang)
            );
        } else {
            maxPlayersPerTeam = Integer.valueOf(args[1]);
            
            if (maxPlayersPerTeam < 1 || maxPlayersPerTeam > 24) {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_INITIALIZE_MAX_PLAYERS
                        .getText(lang)
                );
            } else {
                if (args.length >= 3) {
                    world = Bukkit.getWorld(args[2]);
                } else {
                    Player player = (Player) cs;
                    world = player.getWorld();
                }

                if (world == null) {
                    MessageManager.sendCommandSender(cs,
                            MainLanguageFile.WORLD_NOT_EXIST
                            .getText(lang)
                    );
                } else if (world.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_ALREADY_REGISTERED
                            .getText(lang)
                    );
                } else {
                    GamesRegisterer.GameRegisterResult result = greger
                            .registerGame(world, maxPlayersPerTeam);

                    if (!result.error) {
                        GameInstance gi = result.gameinstance;
                        world.setMetadata(MetadataValues.GAME_INSTANCE.key,
                                new FixedMetadataValue(plugin, gi));
                        greg.registerGame(gi);
                    }

                    MessageManager.sendCommandSender(cs,
                            result.message
                            .getText(lang)
                    );
                }
            }
        }

        return true;
    }
}
