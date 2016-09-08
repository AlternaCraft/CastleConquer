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
package com.alternacraft.castleconquer.Main;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.castleconquer.Handlers.GameJoiningHandler;
import com.alternacraft.castleconquer.Handlers.GameSignHandler;
import com.alternacraft.castleconquer.Handlers.TeamConquerHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class of the plugin.
 *
 * @author AlternaCraft
 * @version 0.0.1-ALPHA
 */
public final class CastleConquer extends JavaPlugin {
    private final PluginBase pb = PluginBase.INSTANCE;

    @Override
    public void onEnable() {
        // Defines the prefix of the plugin, and initializes the plugin manager
        pb.definePluginPrefix("&1[&fCastleConquer&1] &f");
        pb.init(this);

        // Initializes the manager, which initializes and manages all classes
        Manager manager = new Manager(this);

        // Registers the plugin events
        getServer().getPluginManager().registerEvents(new GameSignHandler(this), this);
        getServer().getPluginManager().registerEvents(new GameJoiningHandler(this), this);
        getServer().getPluginManager().registerEvents(new TeamConquerHandler(), this);
    }

    @Override
    public void onDisable() {
        // Sends the disabled message
        MessageManager.log("The plugin has been disabled!");
    }

    /**
     * Returns the ACLIB plugin manager class.
     *
     * @return
     */
    public PluginBase getPluginManager() {
        return pb;
    }
}
