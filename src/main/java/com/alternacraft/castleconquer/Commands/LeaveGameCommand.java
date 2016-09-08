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
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.TeamMember;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveGameCommand implements ArgumentExecutor {
    @Override
    public boolean execute(CommandSender cs, String[] args) {
        if (!(cs instanceof Player)) {
            MessageManager.sendCommandSender(
                    cs,
                    MainLanguageFile.MUST_BE_PLAYER
                    .getText(Langs.EN)
            );
        } else {
            GamesRegister greg = Manager.getGamesRegister();
            Player player = (Player) cs;
            GameInstance gi = GameInstance.getGameInstanceByPlayer(player);

            if (gi == null) {
                MessageManager.sendCommandSender(
                        cs,
                        GameLanguageFile.GAME_NOT_IN_QUEUE
                        .getText(Localizer.getLocale(player))
                );
            } else {
                TeamMember tm = TeamMember.getTeamMemberFromPlayer(player);
                tm.getTeam().removeMember(tm);

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
                MessageManager.sendCommandSender(
                        cs,
                        GameLanguageFile.GAME_LEFT_QUEUE
                        .getText(Localizer.getLocale(player))
                );
            }
        }
        return true;
    }
}
