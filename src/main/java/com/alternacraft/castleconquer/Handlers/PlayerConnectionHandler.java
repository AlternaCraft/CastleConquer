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

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class manages the player connection stuff.
 *
 * @author AlternaCraft
 */
public class PlayerConnectionHandler implements Listener {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @EventHandler
    public void OnPlayerDisconnects(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata(MetadataValues.GAME_INSTANCE.key)) {
            GamesRegisterer greger = Manager.getGamesRegisterer();

            GameInstance gi = (GameInstance) player
                    .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                    .value();

            if (gi.getPlayersInQueue().contains(player)) {
                gi.removePlayerFromQueue(player);
            }
            if (gi.getPlayersInGame().contains(player)) {
                gi.removePlayerFromGame(player);
            }
            greger.saveGameInstance(gi);

            player.removeMetadata(MetadataValues.GAME_INSTANCE.key, plugin);
            player.removeMetadata(MetadataValues.TEAM_MEMBER.key, plugin);
        }
    }
}
