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
package com.alternacraft.castleconquer.Handlers;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Teams.Team;
import com.alternacraft.castleconquer.Teams.Team.TeamType;
import com.alternacraft.castleconquer.Teams.TeamMember;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This class will manage the conquering stuff.
 *
 * @author AlternaCraft
 */
public final class TeamConquerHandler implements Listener {
    private int countingSchedule;

    private boolean counting;

    @EventHandler
    public void OnPlayerNearFlag(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
            GameInstance gi = (GameInstance) player
                    .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                    .value();

            if (gi.isMatchStarted()) {
                TeamMember tm = (TeamMember) player
                        .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0)
                        .value();
                Team team = tm.getTeam();
                Block enemyFlag;

                if (team.getTeamType().equals(TeamType.ATTACKERS)) {
                    enemyFlag = gi.getDefendersFlag();
                } else {
                    enemyFlag = gi.getAttackersFlag();
                }

                Location flagLoc = enemyFlag.getLocation();
                double distance = flagLoc.distance(player.getLocation());

                if (distance <= 5.0) {
                    if (!counting) {
                        startCounting(team.getTeamType(), gi);
                    }
                } else if (counting) {
                    stopCounting();
                }
            }
        }
    }

    /**
     * Starts the counting task.
     *
     * @param conquerorTeam
     * @param gi
     */
    public void startCounting(final TeamType conquerorTeam,
            final GameInstance gi) {
        if (!counting) {
            CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();
            counting = true;

            countingSchedule = plugin.getServer().getScheduler()
                    .scheduleSyncRepeatingTask(plugin, new Runnable() {
                        int count = 0;
                        int timeToConquer = 60;

                        @Override
                        public void run() {
                            if (count >= timeToConquer) {
                                stopCounting();
                                gi.endMatch(conquerorTeam);
                            }
                            count++;
                        }
                    }, 0L, 20L);
        }
    }

    /**
     * Stops the counting task.
     */
    private void stopCounting() {
        if (counting) {
            CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();
            counting = false;
            plugin.getServer().getScheduler().cancelTask(countingSchedule);
        }
    }
}
