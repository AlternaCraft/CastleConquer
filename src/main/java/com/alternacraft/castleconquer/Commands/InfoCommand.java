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
package com.alternacraft.castleconquer.Commands;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.castleconquer.Main.CastleConquer;
import org.bukkit.command.CommandSender;

public class InfoCommand implements ArgumentExecutor {
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    @Override
    public boolean execute(CommandSender cs, String[] args) {
        MessageManager.sendCommandSender(
                cs,
                "&fPlugin developed by &9AlternaCraft"
        );
        MessageManager.sendCommandSender(
                cs,
                "&6Version: &e" + plugin.getDescription().getVersion()
        );
        MessageManager.sendCommandSender(
                cs,
                "&fMore info in &9" + plugin.getDescription().getWebsite()
        );
        return true;
    }
}
