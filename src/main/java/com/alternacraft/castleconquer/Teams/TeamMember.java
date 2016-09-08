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
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

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
     * Returns a TeamMember object from a player.
     *
     * @param player
     * @return
     */
    public final static TeamMember getTeamMemberFromPlayer(Player player) {
        if (player.hasMetadata(MetadataValues.TEAM_MEMBER.key)) {
            for (MetadataValue mv : player
                    .getMetadata(MetadataValues.TEAM_MEMBER.key)) {
                System.out.println(mv.value());
            }
            return (TeamMember) player
                    .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0).value();
        }
        return null;
    }
}
