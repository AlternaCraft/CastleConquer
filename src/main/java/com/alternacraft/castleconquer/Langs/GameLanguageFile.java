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
public enum GameLanguageFile implements LangInterface {
    // <editor-fold defaultstate="collapsed" desc="Language values">
    GAME_SIGN_TEXT_UNINITIALIZED("&4Uninitialized"),
    GAME_SIGN_TEXT_JOIN("&6Join to match"),
    GAME_SIGN_PLACED("&2The game sign has been placed successfully!"),
    GAME_SIGN_DELETED("&2The game sign has been deleted!"),
    GAME_SIGN_ALREADY_PLACED("&2The game sign is already placed for this game instance!"),
    GAME_JOINING_NOT_INITIALIZED("&4You cannot join to the game because it's not initialized!"),
    GAME_ALREADY_IN_QUEUE("&4You are already waiting for playing! Use '/cc leave' to leave the current game"),
    GAME_NOT_IN_QUEUE("&4You are not in any game queue!"),
    GAME_JOINED_AS_ATTACKER("&2You joined the game queue as attacker"),
    GAME_JOINED_AS_DEFENDER("&2You joined the game queue as defender"),
    GAME_STARTING_COUNTDOWN("&6ATENTION! &eThe game will start in 5 seconds"),
    GAME_COUNTDOWN("&6%t% &eseconds left to start the match!"),
    GAME_COUNTDOWN_PLAYER_LEFT_QUEUE("&4The countdown has been cancelled due a player has disconnected the queue!"),
    GAME_STARTED("&2The game started! Destroy the enemy!"),
    GAME_LEFT_QUEUE("&4You left from the waiting queue of the game!"),
    GAME_LEFT("&4You abandoned the game!");
    // </editor-fold>

    public final HashMap<Langs, String> locales = new HashMap();

    /**
     * Define the default languages to load
     *
     * @param en English
     */
    private GameLanguageFile(String en) {
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
