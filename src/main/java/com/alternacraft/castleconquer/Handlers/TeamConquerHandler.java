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
import com.alternacraft.castleconquer.Main.CastleConquer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * This class will manage the conquering stuff.
 *
 * @author AlternaCraft
 */
public final class TeamConquerHandler implements Listener {
    private int countingSchedule;

    private boolean counting;

    @EventHandler
    public void OnPlayerNearFlag(PlayerMoveEvent event) {

    }

    /**
     * Starts the counting task.
     */
    public void startCounting() {
        if (!counting) {
            CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();
            counting = true;

            plugin.getServer().getScheduler()
                    .scheduleSyncRepeatingTask(plugin, new Runnable() {
                        int count = 0;

                        @Override
                        public void run() {

                        }
                    }, 0L, 20L);
        }
    }

    /**
     * Stops the counting task.
     */
    private void stopCounting() {
        if (counting) {
            CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();
            counting = false;
            plugin.getServer().getScheduler().cancelTask(countingSchedule);
        }
    }
}
