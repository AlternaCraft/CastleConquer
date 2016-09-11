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
package com.alternacraft.castleconquer.Langs;

import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.aclib.langs.LangManager;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.StringsUtils;
import java.util.HashMap;

/**
 * This contains the arguments description text.
 *
 * @author AlternaCraft
 */
public enum ArgumentsLanguageFile implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="Language values">
    INFO_COMMAND("Shows the basic of the plugin"),
    REGISTER_WORLD_GAME("Registers a world as a game world"),
    UNREGISTER_WORLD_GAME("Unregisters a world from being a game world"),
    INITIALIZE_WORLD_GAME("Makes a world playable"),
    UNINITIALIZE_WORLD_GAME("Makes a world unplayable"),
    SET_FLAG("Sets the flag of the defensors or attackers"),
    DELETE_FLAG("Deletes the flag of the defensors or attackers"),
    LEAVE_GAME("Leaves from the current game");
    // </editor-fold>

    public final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     */
    private ArgumentsLanguageFile(String en) {
        this.locales.put(Langs.EN, en);
    }

    @Override
    public String getText(Langs lang) {
        return StringsUtils.translateColors(getDefaultText(lang));
    }

    @Override
    public String getDefaultText(Langs lang) {
        String value = (this.locales.get(lang) == null)
                ? this.locales.get(Langs.EN) : this.locales.get(lang);

        String v = LangManager.getValueFromFile(lang, this);

        return (v == null) ? value : v;
    }

}
