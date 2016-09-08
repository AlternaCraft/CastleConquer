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
 * This class creates, loads and manages the language file.
 *
 * @author AlternaCraft
 */
public enum MainLanguageFile implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="Language values">
    MUST_BE_PLAYER("&4You must be a player to execute this command"),
    EXCEPTION_ERROR("&4Unknown exception error happened! (see console)"),
    WORLD_NOT_EXIST("&4The specified world does not exist!"),
    INSUFFICIENT_ARGUMENTS("&4Insufficient arguments specified for this command!"),
    INVALID_ARGUMENTS("&4Invalid arguments specified for this command!"),
    COMMAND_USAGE("&6Command usage: &e%cmd_usage%");
    // </editor-fold>

    public final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     */
    private MainLanguageFile(String en) {
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

        String v = LangManager.getDefaultText(lang, this);

        return (v == null) ? value : v;
    }
}
