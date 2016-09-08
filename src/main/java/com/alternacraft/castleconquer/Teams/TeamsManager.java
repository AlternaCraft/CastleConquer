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

/**
 * This class manages the game teams.
 *
 * @author AlternaCraft
 */
public final class TeamsManager {
    private final Team defenders = new Team(Team.TeamType.DEFENDERS);
    private final Team attackers = new Team(Team.TeamType.ATTACKERS);

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
}
