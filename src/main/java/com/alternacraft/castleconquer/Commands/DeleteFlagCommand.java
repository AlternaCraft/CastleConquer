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
import com.alternacraft.castleconquer.Teams.Team;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class DeleteFlagCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        String usage = Manager.getArgumentsRegisterer()
                .getArgument(this).getUsage();
        Langs lang = (cs instanceof Player) ? Localizer.getLocale((Player) cs)
                : PluginBase.INSTANCE.getMainLanguage();

        Team.TeamType teamType = null;

        if (!(cs instanceof Player)) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(lang)
            );
        } else if (args.length < 2) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.INSUFFICIENT_ARGUMENTS
                    .getText(lang)
            );
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.COMMAND_USAGE
                    .getText(lang)
                    .replace("%cmd_usage%", usage)
            );
        } else if (!(args[1].equalsIgnoreCase("def")
                || args[1].equalsIgnoreCase("att"))) {
            MessageManager.sendCommandSender(cs,
                    MainLanguageFile.INVALID_ARGUMENTS
                    .getText(lang)
            );
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
                    if (gi.getDefendersFlag() == null) {
                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                                .getText(lang)
                        );
                    } else {
                        gi.setDefensorsFlag(null);

                        MessageManager.sendCommandSender(cs,
                                ManageLanguageFile.FLAG_BANNER_DEFENDERS_DELETED
                                .getText(lang)
                        );
                    }
                } else if (gi.getAttackersFlag() == null) {
                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                            .getText(lang)
                    );
                } else {
                    gi.setAttackersFlag(null);

                    MessageManager.sendCommandSender(cs,
                            ManageLanguageFile.FLAG_BANNER_ATTACKERS_DELETED
                            .getText(lang)
                    );
                }
                
                greger.saveGameInstance(gi);
            }
        }

        return true;
    }
}
