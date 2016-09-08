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
package com.alternacraft.castleconquer.Data;

import com.alternacraft.aclib.PluginBase;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Contains the plugin metadata values.
 *
 * @author AlternaCraft
 */
public enum MetadataValues {
    // <editor-fold defaultstate="collapsed" desc="Values">
    PLAYING("castleconquer:playing"),
    IN_QUEUE("castleconquer:in-queue"),
    TEAM_MEMBER("castleconquer:team-member"),
    GAME_INSTANCE("castleconquer:game-instance");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Internal stuff">
    public String key;

    MetadataValues(String key) {
        this.key = key;
    }
    // </editor-fold>

    /**
     * Registers the metadata values to the corresponding objects.
     */
    public static void register() {
        CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setMetadata(PLAYING.key,
                    new FixedMetadataValue(plugin, false));
            player.setMetadata(IN_QUEUE.key,
                    new FixedMetadataValue(plugin, false));
        }

        for (World world : Bukkit.getWorlds()) {
            GameInstance gi = Manager.getGamesRegister().seekGameByWorld(world);
            if (gi != null) {
                world.setMetadata(GAME_INSTANCE.key,
                        new FixedMetadataValue(plugin, gi));
            }
        }
    }
}
