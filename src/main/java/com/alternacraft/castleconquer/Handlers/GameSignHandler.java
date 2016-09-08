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

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Game.GamesRegister;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 * This class handles all the events in relation with managing the game signs.
 *
 * @author AlternaCraft
 */
public class GameSignHandler implements Listener {
    private final CastleConquer plugin;

    public GameSignHandler(CastleConquer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnPlaceSign(SignChangeEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasPermission("cc.sign")) {
            String[] lines = event.getLines();
            if ((lines[0].equalsIgnoreCase("[CastleConquer]")
                    || lines[0].equalsIgnoreCase("[CC]"))
                    && lines[1].equalsIgnoreCase("join")) {
                World world = player.getWorld();
                if (!lines[2].isEmpty()) {
                    world = Bukkit.getWorld(lines[2]);
                }

                GamesRegisterer greger = Manager.getGamesRegisterer();
                GamesRegister greg = Manager.getGamesRegister();
                GameInstance gi = greg.seekGameByWorld(world);
                if (gi == null) {
                    MessageManager.sendPlayer(player,
                            ManageLanguageFile.GAME_WORLD_NOT_REGISTERED
                            .getText(Localizer.getLocale(player))
                    );
                    event.getBlock().setType(Material.AIR);
                } else if (gi.getSign() != null) {
                    MessageManager.sendPlayer(player,
                            GameLanguageFile.GAME_SIGN_ALREADY_PLACED
                            .getText(Localizer.getLocale(player))
                    );
                    event.getBlock().setType(Material.AIR);
                } else {
                    event.setLine(0, StringsUtils
                            .translateColors("&1[&fCastleConquer&1]"));
                    event.setLine(1, StringsUtils
                            .translateColors(GameLanguageFile.GAME_SIGN_TEXT_JOIN
                                    .getText(Localizer.getLocale(player))
                            ));
                    event.setLine(2, StringsUtils
                            .translateColors("&e" + player.getWorld()
                                    .getName()));
                    if (gi.isInitialized()) {
                        event.setLine(3, StringsUtils
                                .translateColors("&d0&5/"
                                        + (gi.getMaxPlayersPerTeam() * 2)));
                    } else {
                        event.setLine(3, StringsUtils
                                .translateColors(GameLanguageFile.GAME_SIGN_TEXT_UNINITIALIZED
                                        .getText(Localizer.getLocale(player))
                                ));
                    }

                    gi.defineSign(event.getBlock());
                    greger.saveGameInstance(gi);

                    MessageManager.sendPlayer(player,
                            GameLanguageFile.GAME_SIGN_PLACED
                            .getText(Localizer.getLocale(player))
                    );
                }
            }
        }
    }

    @EventHandler
    public void OnDeleteSign(BlockBreakEvent event) {
        Block block = event.getBlock();

        if (block.getType().equals(Material.WALL_SIGN)
                || block.getType().equals(Material.SIGN_POST)) {
            Player player = event.getPlayer();
            GamesRegister greg = Manager.getGamesRegister();
            if (greg.isRegistered(player.getWorld())) {
                GameInstance gi = greg.seekGameByWorld(player.getWorld());
                if (gi.getSign() != null) {
                    if (gi.getSign().getLocation().equals(block.getLocation())) {
                        GamesRegisterer greger = Manager.getGamesRegisterer();

                        gi.defineSign(null);
                        greger.saveGameInstance(gi);

                        MessageManager.sendPlayer(player,
                                GameLanguageFile.GAME_SIGN_DELETED
                                .getText(Localizer.getLocale(player))
                        );
                    }
                }
            }
        }
    }
}
