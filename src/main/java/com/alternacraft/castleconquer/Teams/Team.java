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
    private final TeamsManager tman;

    public Team(TeamsManager tman, TeamType type) {
        this.tman = tman;
        this.type = type;
    }

    /**
     * Returns the type of team.
     *
     * @return
     */
    public final TeamType getTeamType() {
        return type;
    }
    
    /**
     * Returns the opponent Team object.
     * 
     * @return 
     */
    public Team getOpponentTeam() {
        if(type.equals(TeamType.ATTACKERS)) {
            return tman.getDefenders();
        } else {
            return tman.getAttackers();
        }
    }

    /**
     * Returns this Team manager.
     *
     * @return
     */
    public final TeamsManager getTeamManager() {
        return tman;
    }

    /**
     * Adds a member to the team.
     *
     * @param member
     */
    public final void addMember(TeamMember member) {
        member.defineTeam(this);
        members.add(member);
    }

    /**
     * Removes a member from the team.
     *
     * @param member
     */
    public final void removeMember(TeamMember member) {
        member.defineTeam(this);
        members.remove(member);
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
