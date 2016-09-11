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
package com.alternacraft.castleconquer.Teams;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Teams.Team.TeamType;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * This class is the main object of the team members.
 *
 * @author AlternaCraft
 */
public class TeamMember {
    private final Player player;
    private Team team;

    public TeamMember(Player player) {
        player.setMetadata(MetadataValues.TEAM_MEMBER.key,
                new FixedMetadataValue(PluginBase.INSTANCE.plugin(), this));
        this.player = player;
    }

    /**
     * Defines a Team for this TeamMember.
     *
     * @param team
     */
    public final void defineTeam(Team team) {
        this.team = team;
    }

    /**
     * Returns the team of this TeamMember.
     *
     * @return
     */
    public final Team getTeam() {
        return team;
    }

    /**
     * Returns the player UUID.
     *
     * @return
     */
    public final Player getPlayer() {
        return player;
    }

    /**
     * Serializes a TeamMember object.
     *
     * @return
     */
    @Override
    public final String toString() {
        String serialized = new StringBuilder()
                .append("[")
                .append(player.getUniqueId().toString())
                .append(",")
                .append(team.getTeamType().toString())
                .append("]")
                .toString();
        return serialized;
    }

    /**
     * Deserializes a TeamMember object.
     *
     * @param serialized
     * @param gi
     * @return
     */
    public final static TeamMember fromString(String serialized, GameInstance gi) {
        String serializedCleaned = serialized.replace("[", "").replace("]", "");
        String[] serializedSplitted = serializedCleaned.split(",");
        String serializedTeamType = serializedSplitted[1];

        Player pl = Bukkit.getPlayer(UUID.fromString(serializedSplitted[0]));
        Team t;
        if (serializedTeamType.equalsIgnoreCase(TeamType.ATTACKERS.toString())) {
            t = gi.getTeamsManager().getAttackers();
        } else {
            t = gi.getTeamsManager().getDefenders();
        }

        TeamMember tm = new TeamMember(pl);
        tm.defineTeam(t);
        return tm;
    }
}
