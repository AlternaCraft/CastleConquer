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
 * This class manages the language messages about managing the game.
 *
 * @author AlternaCraft
 */
public enum ManageLanguageFile implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="Language values">
    GAME_WORLD_REGISTER_CONSOLE("&fYou can register any world by using '/cc reg <max_players_per_team> <world_name>'"),
    GAME_WORLD_UNREGISTER_CONSOLE("&fYou can unregister any world by using '/cc unreg <world_name>'"),
    GAME_WORLD_ALREADY_REGISTERED("&4The world is already registered as game world!"),
    GAME_WORLD_NOT_REGISTERED("&4The world is not registered as game world!"),
    GAME_WORLD_REGISTERED("&2The world has been registered as game world!"),
    GAME_WORLD_UNREGISTERED("&2The world has been unregistered as game world!"),
    GAME_WORLD_CANNOT_INITIALIZE("&4Before initialize, place the flags of both sides!"),
    GAME_WORLD_NOT_INITIALIZED("&4The game in the world is not initialized yet!"),
    GAME_WORLD_INITIALIZE_MAX_PLAYERS("&4The maximum players per team must be between 1 and 24!"),
    GAME_WORLD_INITIALIZE_MAX_PLAYERS_EVEN("&4The maximum players number must be an even one!"),
    GAME_WORLD_ALREADY_INITIALIZED("&4The game in the world is already initialized!"),
    GAME_WORLD_INITIALIZE_CONSOLE("&fYou can initialize any game in any world by using '/cc init <max_players_per_team> <world_name>'"),
    GAME_WORLD_UNINITIALIZE_CONSOLE("&fYou can uninitialize any game in any world by using '/cc uninit <world_name>'"),
    GAME_WORLD_INITIALIZED("&2The game in the world has been initialized!"),
    GAME_WORLD_UNINITIALIZED("&2The game in the world has been uninitialized!"),
    FLAG_BANNER_NOT_IN_HAND("&4You must have a banner in your hand first!"),
    FLAG_BANNER_DEFENDERS_ALREADY_PLACED("&4The defenders flag is already placed!"),
    FLAG_BANNER_ATTACKERS_ALREADY_PLACED("&4The attackers flag is already placed!"),
    FLAG_BANNER_DEFENDERS_PLACED("&2The defenders flag has been placed!"),
    FLAG_BANNER_ATTACKERS_PLACED("&2The attackers flag has been placed!"),
    FLAG_BANNER_DELETE_CONSOLE("&fYou can delete any flag in any world by using '/cc delflag "
            + "[def|att] <world_name>'"),
    FLAG_BANNER_DEFENDERS_NOT_PLACED("&The defenders flag is not placed yet!"),
    FLAG_BANNER_ATTACKERS_NOT_PLACED("&The attackers flag is not placed yet!"),
    FLAG_BANNER_DEFENDERS_DELETED("&The defenders flag has been deleted!"),
    FLAG_BANNER_ATTACKERS_DELETED("&The attackers flag has been deleted!");
    // </editor-fold>

    public final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     */
    private ManageLanguageFile(String en) {
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
