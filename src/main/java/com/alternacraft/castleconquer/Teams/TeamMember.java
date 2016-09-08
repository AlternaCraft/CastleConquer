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

import java.util.UUID;

/**
 * This class is the main object of the team members.
 *
 * @author AlternaCraft
 */
public class TeamMember {
    private final UUID uid;

    public TeamMember(UUID uid) {
        this.uid = uid;
    }

    /**
     * Returns the player UUID.
     *
     * @return
     */
    public final UUID getUUID() {
        return uid;
    }
}
