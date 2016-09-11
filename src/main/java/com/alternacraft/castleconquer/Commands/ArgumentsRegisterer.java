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

import com.alternacraft.aclib.arguments.ArgumentsInterface;
import com.alternacraft.aclib.arguments.ArgumentsRegister;
import com.alternacraft.aclib.commands.ArgumentExecutor;
import com.alternacraft.aclib.commands.CommandArgument;
import com.alternacraft.castleconquer.Langs.ArgumentsLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;

/**
 * This class registers the plugin commands arguments into the PluginCommands
 * class.
 *
 * @author AlternaCraft
 */
public final class ArgumentsRegisterer {
    public enum Arguments implements ArgumentsInterface {
        // <editor-fold defaultstate="collapsed" desc="Arguments">
        EMPTY_COMMAND(
                "", "", null, new CommandEmpty()
        ),
        INFO_COMMAND(
                "info",
                "/cc info",
                ArgumentsLanguageFile.INFO_COMMAND,
                new InfoCommand()
        ),
        REGISTER_WORLD_GAME(
                "reg",
                "/cc reg <max_players_per_team> (<world_name>)",
                ArgumentsLanguageFile.REGISTER_WORLD_GAME,
                new RegisterGameCommand()
        ),
        UNREGISTER_WORLD_GAME(
                "unreg",
                "/cc unreg (<world_name>)",
                ArgumentsLanguageFile.UNREGISTER_WORLD_GAME,
                new UnregisterGameCommand()
        ),
        INITIALIZE_WORLD_GAME(
                "init",
                "/cc init (<world_name>)",
                ArgumentsLanguageFile.INITIALIZE_WORLD_GAME,
                new InitializeGameCommand()
        ),
        UNINITIALIZE_WORLD_GAME(
                "uninit",
                "/cc uninit (<world_name>)",
                ArgumentsLanguageFile.UNINITIALIZE_WORLD_GAME,
                new UninitializeGameCommand()
        ),
        SET_FLAG(
                "setflag",
                "/cc setflag [def|att]",
                ArgumentsLanguageFile.SET_FLAG,
                new SetFlagCommand()
        ),
        DELETE_FLAG(
                "delflag",
                "/cc delflag [def|att] (<world_name>)",
                ArgumentsLanguageFile.DELETE_FLAG,
                new DeleteFlagCommand()
        ),
        LEAVE_GAME(
                "leave",
                "/cc leave",
                ArgumentsLanguageFile.LEAVE_GAME,
                new LeaveGameCommand()
        );
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Internal stuff">
        private String arg = null;
        private String usage = null;
        private Enum desc = null;
        private ArgumentExecutor cmdArg = null;

        Arguments(String arg, String usage, Enum desc,
                ArgumentExecutor cmdArg) {
            this.arg = arg;
            this.usage = usage;
            this.desc = desc;
            this.cmdArg = cmdArg;
        }
        // </editor-fold>

        @Override
        public String getArgument() {
            return arg;
        }

        @Override
        public String getUsage() {
            return usage;
        }

        @Override
        public Enum getDescription() {
            return desc;
        }

        @Override
        public ArgumentExecutor getClazz() {
            return cmdArg;
        }
    }

    private final ArgumentsRegister ar;

    public ArgumentsRegisterer(CastleConquer plugin) {
        ar = new ArgumentsRegister("castleconquer", "cc");
        ar.register(Arguments.class);
    }

    /**
     * Returns the CommandArgument class from specified ArgumentExecutor.
     *
     * @param arg
     * @return
     */
    public CommandArgument getArgument(ArgumentExecutor arg) {
        return ar.cmdListener().getCmdArgument(arg);
    }
}
