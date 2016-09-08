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

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * This class is the main object of the teams.
 *
 * @author AlternaCraft
 */
public class Team {
    public enum TeamType {
        ATTACKERS, DEFENDERS
    }

    private final List<TeamMember> members = new ArrayList<>();
    private final TeamType type;

    public Team(TeamType type) {
        this.type = type;
    }

    /**
     * Returns the type of team.
     *
     * @return
     */
    public final TeamType getType() {
        return type;
    }

    /**
     * Adds a member to the team.
     *
     * @param member
     */
    public final void addMember(TeamMember member) {
        members.add(member);
    }

    /**
     * Removes a member from the team.
     *
     * @param member
     */
    public final void removeMember(TeamMember member) {
        members.remove(member);
    }

    /**
     * Checks if a player is a TeamMember instance.
     *
     * @param player
     * @return
     */
    public final boolean playerIsTeamMember(Player player) {
        for (TeamMember tm : members) {
            if (tm.getUUID().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if this team contains a defined TeamMember.
     *
     * @param member
     * @return
     */
    public final boolean containsMember(TeamMember member) {
        return members.contains(member);
    }

    /**
     * Returns all members as list.
     *
     * @return
     */
    public final List<TeamMember> getMembers() {
        return members;
    }
}
