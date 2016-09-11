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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UninitializeGameCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        Langs lang = (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();

        World world;

        if (!(cs instanceof Player) && args.length == 1) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(lang)
            );
            MessageManager.sendCommandSender(cs,
                    ManageLanguageFile.GAME_WORLD_UNINITIALIZE_CONSOLE
                    .getText(lang)
            );
        } else {
            if (args.length >= 2) {
                world = Bukkit.getWorld(args[1]);
            } else {
                Player player = (Player) cs;
                world = player.getWorld();
            }

            if (world == null) {
                MessageManager.sendCommandSender(cs,
                        MainLanguageFile.WORLD_NOT_EXIST
                        .getText(lang)
                );
            } else if (!world.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
                MessageManager.sendCommandSender(cs,
                        ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                        .getText(lang)
                );
            } else {
                GameInstance gi = (GameInstance) world
                        .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                        .value();

                if (!gi.isInitialized()) {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_NOT_INITIALIZED
                            .getText(lang)
                    );
                } else {
                    gi.uninitialize();
                    gi.updateSignData();
                    greger.saveGameInstance(gi);

                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_UNINITIALIZED
                            .getText(lang)
                    );
                }
            }
        }

        return true;
    }
}
