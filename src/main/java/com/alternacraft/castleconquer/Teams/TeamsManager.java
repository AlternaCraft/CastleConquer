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

import org.bukkit.entity.Player;

/**
 * This class manages the game teams.
 *
 * @author AlternaCraft
 */
public final class TeamsManager {
    private final Team defenders = new Team(Team.TeamType.DEFENDERS);
    private final Team attackers = new Team(Team.TeamType.ATTACKERS);

    /**
     * Adds a member to defenders team.
     *
     * @param tm
     */
    public final void addMemberToDefenders(TeamMember tm) {
        defenders.addMember(tm);
    }

    /**
     * Removes a member from defenders team.
     *
     * @param tm
     */
    public final void removeMemberFromDefenders(TeamMember tm) {
        defenders.removeMember(tm);
    }

    /**
     * Adds a member to attackers team.
     *
     * @param tm
     */
    public final void addMemberToAttackers(TeamMember tm) {
        attackers.addMember(tm);
    }

    /**
     * Removes a member from attackers team.
     *
     * @param tm
     */
    public final void removeMemberFromAttackers(TeamMember tm) {
        attackers.removeMember(tm);
    }

    /**
     * Returns the defenders team.
     *
     * @return
     */
    public final Team getDefenders() {
        return defenders;
    }

    /**
     * Returns the attackers team.
     *
     * @return
     */
    public final Team getAttackers() {
        return attackers;
    }

    /**
     * Checks if a TeamMember is in attackers team.
     *
     * @param member
     * @return
     */
    public final boolean isTeamMemberInAttackers(TeamMember member) {
        for (TeamMember tm : attackers.getMembers()) {
            if (tm.equals(member)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a TeamMemebr is in defenders team.
     *
     * @param member
     * @return
     */
    public final boolean isTeamMemberInDefenders(TeamMember member) {
        for (TeamMember tm : defenders.getMembers()) {
            if (tm.equals(member)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a TeamMember from a player, if is in some team.
     *
     * @param player
     * @return
     */
    public final TeamMember getTeamMemberByPlayer(Player player) {
        for (TeamMember tm : attackers.getMembers()) {
            if (tm.getUUID().equals(player.getUniqueId())) {
                return tm;
            }
        }
        for (TeamMember tm : defenders.getMembers()) {
            if (tm.getUUID().equals(player.getUniqueId())) {
                return tm;
            }
        }
        return null;
    }
}
