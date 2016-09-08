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

import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.commands.CommandArgument;
import com.alternacraft.aclib.commands.CommandListener;
import com.alternacraft.castleconquer.Main.CastleConquer;

/**
 * This class registers the plugin commands arguments into the PluginCommands
 * class.
 *
 * @author AlternaCraft
 */
public final class ArgumentsRegisterer {
    public enum Arguments {
        // <editor-fold defaultstate="collapsed" desc="Arguments">
        REGISTER_WORLD_GAME(
                "reg",
                "/cc reg <max_players_per_team> (<world_name>)",
                "Registers a world as a game world",
                new RegisterGameCommand()
        ),
        UNREGISTER_WORLD_GAME(
                "unreg",
                "/cc unreg (<world_name>)",
                "Unregisters a world from being a game world",
                new UnregisterGameCommand()
        ),
        INITIALIZE_WORLD_GAME(
                "init",
                "/cc init (<world_name>)",
                "Makes a world playable",
                new InitializeGameCommand()
        ),
        UNINITIALIZE_WORLD_GAME(
                "uninit",
                "/cc uninit (<world_name>)",
                "Makes a world unplayable",
                new UninitializeGameCommand()
        ),
        SET_FLAG(
                "setflag",
                "/cc setflag [def|att]",
                "Sets the flag of the defensors or attackers",
                new SetFlagCommand()
        ),
        DELETE_FLAG(
                "delflag",
                "/cc delflag [def|att] (<world_name>)",
                "Deletes the flag of the defensors or attackers",
                new DeleteFlagCommand()
        ),
        LEAVE_GAME(
                "leave",
                "/cc leave",
                "Leaves from the current game",
                new LeaveGameCommand()
        );
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Internal stuff">
        public String arg = null;
        public String usage = null;
        public String desc = null;
        public ArgumentExecutor cmdArg = null;

        Arguments(String arg, String usage, String desc,
                ArgumentExecutor cmdArg) {
            this.arg = arg;
            this.usage = usage;
            this.desc = desc;
            this.cmdArg = cmdArg;
        }
        // </editor-fold>
    }

    private final CommandListener cl;

    public ArgumentsRegisterer(CastleConquer plugin) {
        cl = new CommandListener("castleconquer", "cc", plugin);
        cl.addArgument(new CommandArgument("", "", ""),
                new CommandEmpty(plugin));

        for (Arguments cmd : Arguments.values()) {
            CommandArgument cmdArg = new CommandArgument(cmd.arg, cmd.usage,
                    cmd.desc);
            cl.addArgument(cmdArg, cmd.cmdArg);
        }
    }

    /**
     * Returns the CommandArgument class from specified ArgumentExecutor.
     *
     * @param arg
     * @return
     */
    public CommandArgument getArgument(ArgumentExecutor arg) {
        return cl.getCmdArgument(arg);
    }
}
