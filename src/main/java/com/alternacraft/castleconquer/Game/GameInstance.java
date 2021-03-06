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
package com.alternacraft.castleconquer.Game;

import com.alternacraft.aclib.MessageManager;
import com.alternacraft.castleconquer.Teams.TeamMember;
import com.alternacraft.castleconquer.Teams.TeamsManager;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.langs.Langs;
import com.alternacraft.aclib.utils.Localizer;
import com.alternacraft.aclib.utils.LocationUtils;
import com.alternacraft.aclib.utils.StringsUtils;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Files.GamesRegisterer;
import com.alternacraft.castleconquer.Langs.GameLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.Team.TeamType;
import org.bukkit.block.Sign;
import org.bukkit.plugin.Plugin;

/**
 * This class is the main object of a game instance.
 *
 * @author AlternaCraft
 */
public final class GameInstance {
    private final CastleConquer plugin = (CastleConquer) Manager.getPlugin();

    private final World world;
    private final TeamsManager teamsManager = new TeamsManager();

    private List<Player> playersInQueue = new ArrayList<>();
    private List<Player> playersInGame = new ArrayList<>();

    private Block defendersFlag;
    private Block attackersFlag;
    private Block gameInstanceSign;

    private int maxPlayersPerTeam;

    private boolean initialized;
    private boolean countdownStarted;
    private boolean matchStarted;

    private int countdownSchedule;

    public GameInstance(World world, int maxPlayersPerTeam) {
        this.world = world;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    /**
     * Returns the game instance world.
     *
     * @return
     */
    public final World getWorld() {
        return world;
    }

    /**
     * Defines a player into the game.
     *
     * @param player
     */
    public final void addPlayerToGame(Player player) {
        playersInGame.add(player);
    }

    /**
     * Removes a player defined as a player into the game.
     *
     * @param player
     */
    public final void removePlayerFromGame(Player player) {
        playersInGame.remove(player);
        TeamMember tm = (TeamMember) player
                .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0).value();
        if (matchStarted && tm.getTeam().getMembers().isEmpty()) {
            endMatch(tm.getTeam().getOpponentTeam().getTeamType());
        }
    }

    /**
     * Adds a player to game queue.
     *
     * @param player
     */
    public void addPlayerToQueue(Player player) {
        playersInQueue.add(player);
        updateSignData();
        System.out.println(playersInQueue.size());
        System.out.println(maxPlayersPerTeam * 2);
        if (playersInQueue.size() == maxPlayersPerTeam * 2) {
            startCountdown();
        }
    }

    /**
     * Removes a player from game queue.
     *
     * @param player
     */
    public void removePlayerFromQueue(Player player) {
        playersInQueue.remove(player);
        updateSignData();
    }

    /**
     * Checks if a player is in queue.
     *
     * @param player
     * @return
     */
    public boolean isPlayerInQueue(Player player) {
        return playersInQueue.contains(player);
    }

    /**
     * Returns all players in queue.
     *
     * @return
     */
    public List<Player> getPlayersInQueue() {
        return playersInQueue;
    }

    /**
     * Returns all players in game.
     *
     * @return
     */
    public List<Player> getPlayersInGame() {
        return playersInGame;
    }

    /**
     * Defines the max amount of players per team.
     *
     * @param value
     */
    public void setMaxPlayersPerTeam(int value) {
        this.maxPlayersPerTeam = value;
    }

    /**
     * Returns the max players per team.
     *
     * @return
     */
    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    /**
     * Defines a block as defensors flag.
     *
     * @param flagBlock
     */
    public void setDefensorsFlag(Block flagBlock) {
        this.defendersFlag = flagBlock;
    }

    /**
     * Returns the defensors flag block.
     *
     * @return
     */
    public Block getDefendersFlag() {
        return defendersFlag;
    }

    /**
     * Defines a block as attackers flag.
     *
     * @param flagBlock
     */
    public void setAttackersFlag(Block flagBlock) {
        this.attackersFlag = flagBlock;
    }

    /**
     * Returns the attackers flag block.
     *
     * @return
     */
    public Block getAttackersFlag() {
        return attackersFlag;
    }

    /**
     * Declares this game instance initialized, ready to be played.
     */
    public void initialize() {
        this.initialized = true;
        updateSignData();
    }

    /**
     * Declares this game instance uninitialized, not ready to be played.
     */
    public void uninitialize() {
        this.initialized = false;
        updateSignData();
    }

    /**
     * Returns if this game instance is initialized.
     *
     * @return
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Checks if this game instance can be initialized.
     *
     * @return
     */
    public boolean canInitialize() {
        return (defendersFlag != null || attackersFlag != null);
    }

    /**
     * Sets the value of match started boolean attribute.
     *
     * @param value
     */
    public void setMatchStarted(boolean value) {
        this.matchStarted = value;
    }

    /**
     * Starts a match in this game instance.
     */
    public void startMatch() {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        if (matchStarted) {
            return;
        }

        stopCountdown();

        matchStarted = true;

        for (Player player : playersInQueue) {
            TeamMember tm = (TeamMember) player
                    .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0).value();
            Location tpDestiny;

            if (tm.getTeam().getTeamType().equals(TeamType.ATTACKERS)) {
                Location flagLoc = attackersFlag.getLocation();
                org.bukkit.material.Banner bannerMat
                        = (org.bukkit.material.Banner) attackersFlag.getState()
                        .getData();
                float yaw = LocationUtils.blockFaceToYaw(bannerMat.getFacing());
                tpDestiny = new Location(
                        flagLoc.getWorld(),
                        flagLoc.getBlockX() + 0.5f,
                        flagLoc.getBlockY(),
                        flagLoc.getBlockZ() + 0.5f,
                        yaw,
                        0f
                );
            } else {
                Location flagLoc = defendersFlag.getLocation();
                org.bukkit.material.Banner bannerMat
                        = (org.bukkit.material.Banner) defendersFlag.getState()
                        .getData();
                float yaw = LocationUtils.blockFaceToYaw(bannerMat.getFacing());
                tpDestiny = new Location(
                        flagLoc.getWorld(),
                        flagLoc.getBlockX() + 0.5f,
                        flagLoc.getBlockY(),
                        flagLoc.getBlockZ() + 0.5f,
                        yaw,
                        0f
                );

            }
            player.teleport(tpDestiny);

            MessageManager.sendPlayer(
                    player,
                    GameLanguageFile.GAME_STARTED
                    .getText(Localizer.getLocale(player))
            );
        }

        playersInGame.addAll(playersInQueue);
        playersInQueue.clear();
        greger.saveGameInstance(this);
    }

    /**
     * Ends the match in this game instance.
     *
     * @param winnerType
     */
    public void endMatch(TeamType winnerType) {
        GamesRegisterer greger = Manager.getGamesRegisterer();

        if (!matchStarted) {
            return;
        }

        matchStarted = false;
        GameLanguageFile message;

        if (winnerType.equals(TeamType.ATTACKERS)) {
            message = GameLanguageFile.GAME_ATTACKERS_WON;
        } else {
            message = GameLanguageFile.GAME_DEFENDERS_WON;
        }

        for (Player player : playersInGame) {
            player.teleport(player.getWorld().getSpawnLocation());
            MessageManager.sendPlayer(
                    player,
                    GameLanguageFile.GAME_FINISHED
                    .getText(Localizer.getLocale(player))
            );
            MessageManager.sendPlayer(
                    player,
                    message.getText(Localizer.getLocale(player))
            );

            player.removeMetadata(MetadataValues.GAME_INSTANCE.key, plugin);
            player.removeMetadata(MetadataValues.TEAM_MEMBER.key, plugin);
        }

        playersInGame.clear();
        greger.saveGameInstance(this);

        updateSignData();
    }

    /**
     * Returns if match started.
     *
     * @return
     */
    public boolean isMatchStarted() {
        return matchStarted;
    }

    /**
     * Returns the game instance TeamsManager class.
     *
     * @return
     */
    public TeamsManager getTeamsManager() {
        return teamsManager;
    }

    /**
     * Defines a sign block as game instance sign.
     *
     * @param sign
     */
    public void defineSign(Block sign) {
        this.gameInstanceSign = sign;
    }

    /**
     * Returns the game instance sign.
     *
     * @return
     */
    public Block getSign() {
        return gameInstanceSign;
    }

    /**
     * Updates the players information in the game instance sign.
     */
    public void updateSignData() {
        if (gameInstanceSign != null) {
            Sign sign = (Sign) gameInstanceSign.getState();
            if (initialized) {
                sign.setLine(3, StringsUtils.translateColors("&d"
                        + playersInQueue.size() + "&5/" + maxPlayersPerTeam * 2));
            } else {
                sign.setLine(3, StringsUtils.translateColors(
                        GameLanguageFile.GAME_SIGN_TEXT_UNINITIALIZED
                        .getText(Langs.EN)));
            }
            sign.update();
        }
    }

    /**
     * Starts the countdown to start the match.
     */
    private void startCountdown() {
        if (countdownStarted || matchStarted) {
            return;
        }

        final Plugin plugin = (Plugin) PluginBase.INSTANCE.plugin();

        countdownStarted = true;

        for (Player player : playersInQueue) {
            MessageManager.sendPlayer(
                    player,
                    GameLanguageFile.GAME_STARTING_COUNTDOWN
                    .getText(Localizer.getLocale(player))
            );
        }

        countdownSchedule = plugin.getServer().getScheduler()
                .scheduleSyncRepeatingTask(plugin, new Runnable() {
                    private int currentTime = 5;

                    @Override
                    public void run() {
                        if (currentTime == 0) {
                            startMatch();
                            return;
                        }

                        for (Player player : playersInQueue) {
                            MessageManager.sendPlayer(
                                    player,
                                    GameLanguageFile.GAME_COUNTDOWN
                                    .getText(Localizer.getLocale(player))
                                    .replace("%t%", String.valueOf(currentTime))
                            );
                        }
                        currentTime--;
                    }
                }, 0L, 20L);
    }

    /**
     * Cancels the countdown.
     */
    public void stopCountdown() {
        if (countdownStarted) {
            plugin.getServer().getScheduler().cancelTask(countdownSchedule);

            countdownStarted = false;
        }
    }

    /**
     * Returns if the countdown is started or not.
     *
     * @return
     */
    public boolean isCountdownStarted() {
        return countdownStarted;
    }
}
