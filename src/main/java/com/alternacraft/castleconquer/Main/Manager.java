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

import com.alternacraft.aclib.langs.DefaultMessages;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.castleconquer.Commands.ArgumentsRegisterer;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Files.FilesRegisterer;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;

/**
 * This class initializes and manages all the plugin classes.
 *
 * @author AlternaCraft
 */
public final class Manager {
    private static CastleConquer pl;
    private static ArgumentsRegisterer argsReg;
    private static FilesRegisterer filesReg;
    private static GamesRegisterer gamesReger;
    private static GamesRegister gamesReg;

    public Manager(CastleConquer plugin) {
        pl = plugin;

        // Initializes the ArgumentsRegisterer class, which registers the commands
        argsReg = new ArgumentsRegisterer(plugin);

        // Saves the ManageLanguageFile into the language files
        LangManager.setKeys(Langs.EN);
        LangManager.saveMessages(LangManager.DIRECTORY + "GlobalMessages",
                DefaultMessages.class, MainLanguageFile.class);
        LangManager.saveMessages(LangManager.DIRECTORY + "ManagingMessages",
                ManageLanguageFile.class);
        LangManager.saveMessages(LangManager.DIRECTORY + "GamesMessages",
                GameLanguageFile.class);
        LangManager.loadMessages();

        // Initializes the FilesRegisterer class, which registers the files
        filesReg = new FilesRegisterer();

        // Initializes the GameRegister, which contains all games instances
        gamesReg = new GamesRegister();

        // Initializes the GameRegisterer class, which manages the games register
        // Then, loads the games in memory, into the GameRegister class
        gamesReger = new GamesRegisterer(plugin);
        gamesReger.loadGames();

        MetadataValues.register();
    }

    public static CastleConquer getPlugin() {
        return pl;
    }

    /**
     * Returns the ArgumentsRegisterer class.
     *
     * @return
     */
    public static ArgumentsRegisterer getArgumentsRegisterer() {
        return argsReg;
    }

    /**
     * Returns the FilesRegisterer class.
     *
     * @return
     */
    public static FilesRegisterer getFilesRegisterer() {
        return filesReg;
    }

    /**
     * Returns the GamesRegisterer class.
     *
     * @return
     */
    public static GamesRegisterer getGamesRegisterer() {
        return gamesReger;
    }

    /**
     * Returns the GamesRegister class.
     *
     * @return
     */
    public static GamesRegister getGamesRegister() {
        return gamesReg;
    }
}
