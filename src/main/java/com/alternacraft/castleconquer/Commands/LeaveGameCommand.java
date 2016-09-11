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
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.TeamMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        if (!(cs instanceof Player)) {
            MessageManager.sendCommandSender(
                    cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(Langs.EN)
            );
        } else {
            Player player = (Player) cs;

            if (!player.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
                MessageManager.sendCommandSender(
                        cs,
                        GameLanguageFile.GAME_NOT_IN_QUEUE
                        .getText(Localizer.getLocale(player))
                );
            } else {
                GameInstance gi = (GameInstance) player
                        .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                        .value();
                TeamMember tm = (TeamMember) player
                        .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0)
                        .value();

                tm.getTeam().removeMember(tm);

                if (gi.isPlayerInQueue(player)) {
                    if (gi.isCountdownStarted()) {
                        gi.stopCountdown();
                        for (Player pl : gi.getPlayersInQueue()) {
                            MessageManager.sendPlayer(
                                    pl,
                                    GameLanguageFile.GAME_COUNTDOWN_PLAYER_LEFT_QUEUE
                                    .getText(Localizer.getLocale(pl))
                            );
                        }
                    }

                    gi.removePlayerFromQueue(player);
                } else {
                    gi.removePlayerFromGame(player);
                }

                MessageManager.sendCommandSender(
                        cs,
                        GameLanguageFile.GAME_LEFT_QUEUE
                        .getText(Localizer.getLocale(player))
                );

                greger.saveGameInstance(gi);

                player.removeMetadata(MetadataValues.GAME_INSTANCE.key, plugin);
                player.removeMetadata(MetadataValues.TEAM_MEMBER.key, plugin);
            }
        }

        return true;
    }
}
