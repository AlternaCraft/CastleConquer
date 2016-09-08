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
package com.alternacraft.castleconquer.Game;

import com.alternacraft.castleconquer.Teams.TeamMember;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * This class contains all the game instances.
 *
 * @author AlternaCraft
 */
public final class GamesRegister {
    private final List<GameInstance> games = new ArrayList<>();

    /**
     * Registers a new game in memory.
     *
     * @param game
     */
    public final void registerGame(GameInstance game) {
        games.add(game);
    }

    /**
     * Unregisters a game from memory.
     *
     * @param game
     */
    public final void unregisterGame(GameInstance game) {
        games.remove(game);
    }

    /**
     * Returns if a world is registered as game world in memory.
     *
     * @param world
     * @return
     */
    public final boolean isRegistered(World world) {
        return seekGameByWorld(world) != null;
    }

    /**
     * Returns a game by world.
     *
     * @param world
     * @return
     */
    public GameInstance seekGameByWorld(World world) {
        for (GameInstance game : games) {
            if (game.getWorld().getName().equals(world.getName())) {
                return game;
            }
        }
        return null;
    }

    /**
     * Returns a game by player.
     *
     * @param player
     * @return
     */
    public GameInstance seekGameByPlayer(Player player) {
        for (GameInstance game : games) {
            for (TeamMember member : game.getTeamsManager().getAttackers()
                    .getMembers()) {
                if (member.getUUID().equals(player.getUniqueId())) {
                    return game;
                }
            }
            for (TeamMember member : game.getTeamsManager().getDefenders()
                    .getMembers()) {
                if (member.getUUID().equals(player.getUniqueId())) {
                    return game;
                }
            }
        }
        return null;
    }

    /**
     * Returns all games registered in memory.
     *
     * @return
     */
    public final List<GameInstance> getGames() {
        return games;
    }
}
