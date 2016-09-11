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
package com.alternacraft.castleconquer.Files;

import com.alternacraft.castleconquer.Langs.ManageLanguageFile;
import com.alternacraft.aclib.MessageManager;
import com.alternacraft.aclib.PluginBase;
import com.alternacraft.aclib.langs.LangInterface;
import com.alternacraft.castleconquer.Data.MetadataValues;
import com.alternacraft.castleconquer.Game.GameInstance;
import com.alternacraft.castleconquer.Langs.MainLanguageFile;
import com.alternacraft.castleconquer.Main.CastleConquer;
import com.alternacraft.castleconquer.Main.Manager;
import com.alternacraft.castleconquer.Teams.TeamMember;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public final class GamesRegisterer {
    public class GameRegisterResult extends GameUnregisterResult {
        public final GameInstance gameinstance;

        public GameRegisterResult(GameInstance gi, LangInterface message,
                boolean error) {
            super(message, error);
            this.gameinstance = gi;
        }
    }

    public class GameUnregisterResult {
        public final LangInterface message;
        public final boolean error;

        public GameUnregisterResult(LangInterface message,
                boolean error) {
            this.message = message;
            this.error = error;
        }
    }

    private final String folderPath;
    private final CastleConquer plugin = (CastleConquer) PluginBase.INSTANCE.plugin();

    public GamesRegisterer(CastleConquer plugin) {
        this.folderPath = plugin.getDataFolder() + "/games/";

        if (!new File(folderPath).exists()) {
            new File(folderPath).mkdir();
        }
    }

    /**
     * Register a new game into the games register file.
     *
     * @param world
     * @param maxPlayersPerTeam
     * @return
     */
    public GameRegisterResult registerGame(World world,
            int maxPlayersPerTeam) {
        GameRegisterResult result = null;

        try {
            String worldUID = world.getUID().toString();
            File file = new File(folderPath + worldUID + ".yml");
            YamlConfiguration fileYAML = new YamlConfiguration();

            if (!file.exists()) {
                file.createNewFile();

                fileYAML.options().header("Auto-generated game instance file\n"
                        + "World UUID: " + worldUID + "\n"
                        + "World name: " + world.getName() + "\n");

                fileYAML.set("initialized", false);
                fileYAML.set("max-players-per-team", maxPlayersPerTeam);

                fileYAML.save(file);

                GameInstance gi = new GameInstance(world, maxPlayersPerTeam);
                Manager.getGamesRegister().registerGame(gi);

                result = new GameRegisterResult(gi,
                        ManageLanguageFile.GAME_WORLD_REGISTERED, false
                );
            }
        } catch (IOException ex) {
            result = new GameRegisterResult(null,
                    MainLanguageFile.EXCEPTION_ERROR, true
            );
            MessageManager.logError(ex.getMessage());
        }

        return result;
    }

    /**
     * Unregisters a game from games register file.
     *
     * @param world
     * @return
     */
    public GameUnregisterResult unregister(World world) {
        String worldUID = world.getUID().toString();
        File file = new File(folderPath + worldUID + ".yml");

        GameUnregisterResult result;

        if (!file.exists()) {
            result = new GameUnregisterResult(
                    ManageLanguageFile.GAME_WORLD_NOT_REGISTERED, true
            );
        } else {
            file.delete();

            GameInstance gi = (GameInstance) world
                    .getMetadata(MetadataValues.GAME_INSTANCE.key).get(0)
                    .value();

            if (gi.getDefendersFlag() != null) {
                gi.getDefendersFlag().setType(Material.AIR);
            }
            if (gi.getAttackersFlag() != null) {
                gi.getAttackersFlag().setType(Material.AIR);
            }
            if (gi.getSign() != null) {
                gi.getSign().setType(Material.AIR);
            }

            Manager.getGamesRegister().unregisterGame(gi);

            result = new GameUnregisterResult(
                    ManageLanguageFile.GAME_WORLD_UNREGISTERED, false
            );
        }

        return result;
    }

    /**
     * Loads all games in memory.
     */
    public void loadGames() {
        for (File file : new File(folderPath).listFiles()) {
            UUID worldUUID = UUID.fromString(file.getName().replace(".yml", ""));
            World world = Bukkit.getWorld(worldUUID);

            GameInstance gi = getGameInstance(world);
            Manager.getGamesRegister().registerGame(gi);
        }
    }

    /**
     * Checks if a world is registered as a game world.
     *
     * @param world
     * @return
     */
    public boolean isRegistered(World world) {
        String worldUID = world.getUID().toString();
        File folder = new File(folderPath);
        File file = new File(folderPath + worldUID + ".yml");

        return Arrays.asList(folder.listFiles()).contains(file);
    }

    /**
     * Returns the game instance from a game world registered.
     *
     * @param world
     * @return
     */
    public GameInstance getGameInstance(World world) {
        if (!isRegistered(world)) {
            return null;
        }

        GameInstance gi = new GameInstance(world, 2);

        String worldUID = gi.getWorld().getUID().toString();
        File file = new File(folderPath + worldUID + ".yml");
        YamlConfiguration fileYAML = YamlConfiguration.loadConfiguration(file);

        // <editor-fold defaultstate="collapsed" desc="Defensors flag">
        if (fileYAML.contains("defensors-flag")) {
            List<Double> defFlagPos_serialized = (List<Double>) fileYAML
                    .getList("defensors-flag");
            Location defFlagPos = new Location(world,
                    defFlagPos_serialized.get(0),
                    defFlagPos_serialized.get(1),
                    defFlagPos_serialized.get(2));

            Block defFlagBlock = world.getBlockAt(defFlagPos);
            gi.setDefensorsFlag(defFlagBlock);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Attackers flag">
        if (fileYAML.contains("attackers-flag")) {
            List<Double> attFlagPos_serialized = (List<Double>) fileYAML
                    .getList("attackers-flag");
            Location attFlagPos = new Location(world,
                    attFlagPos_serialized.get(0),
                    attFlagPos_serialized.get(1),
                    attFlagPos_serialized.get(2));

            Block attFlagBlock = world.getBlockAt(attFlagPos);
            gi.setAttackersFlag(attFlagBlock);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Initialized">
        if (fileYAML.getBoolean("initialized")) {
            gi.initialize();
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Started">
        if (fileYAML.getBoolean("match-started")) {
            gi.setMatchStarted(true);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Max players">
        gi.setMaxPlayersPerTeam(fileYAML.getInt("max-players-per-team"));
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Game instance sign">
        if (fileYAML.contains("game-sign")) {
            List<Double> gameInstanceSign_serialized = (List<Double>) fileYAML
                    .getList("game-sign");
            Location gameInstanceSignPos = new Location(world,
                    gameInstanceSign_serialized.get(0),
                    gameInstanceSign_serialized.get(1),
                    gameInstanceSign_serialized.get(2));

            Block gameInstanceSign = world.getBlockAt(gameInstanceSignPos);
            gi.defineSign(gameInstanceSign);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="In-queue players">
        if (fileYAML.contains("in-queue")) {
            List<String> inQueueSerialized = fileYAML.getStringList("in-queue");

            for (String serialized : inQueueSerialized) {
                TeamMember tm = TeamMember.fromString(serialized, gi);
                gi.addPlayerToQueue(tm.getPlayer());

                tm.getPlayer().setMetadata(MetadataValues.GAME_INSTANCE.key,
                        new FixedMetadataValue(plugin, gi));
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="In-game players">
        if (fileYAML.contains("in-game")) {
            List<String> inGameSerialized = fileYAML.getStringList("in-game");

            for (String serialized : inGameSerialized) {
                TeamMember tm = TeamMember.fromString(serialized, gi);
                gi.addPlayerToQueue(tm.getPlayer());

                tm.getPlayer().setMetadata(MetadataValues.GAME_INSTANCE.key,
                        new FixedMetadataValue(plugin, gi));
            }
        }
        // </editor-fold>

        return gi;
    }

    /**
     * Saves into the game register file a provided game instance.
     *
     * @param gi
     * @return
     */
    public boolean saveGameInstance(GameInstance gi) {
        if (!isRegistered(gi.getWorld())) {
            return false;
        }

        try {
            String worldUID = gi.getWorld().getUID().toString();
            File file = new File(folderPath + worldUID + ".yml");
            YamlConfiguration fileYAML = YamlConfiguration.loadConfiguration(file);

            // <editor-fold defaultstate="collapsed" desc="Defensors flag">
            if (gi.getDefendersFlag() != null) {
                Location defFlagPos = gi.getDefendersFlag().getLocation();
                List<Double> defFlagPos_serialized = new ArrayList<>();

                defFlagPos_serialized.add(defFlagPos.getX());
                defFlagPos_serialized.add(defFlagPos.getY());
                defFlagPos_serialized.add(defFlagPos.getZ());

                fileYAML.set("defensors-flag", defFlagPos_serialized);
            } else {
                fileYAML.set("defensors-flag", null);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Attackers flag">
            if (gi.getAttackersFlag() != null) {
                Location attFlagPos = gi.getAttackersFlag().getLocation();
                List<Double> attFlagPos_serialized = new ArrayList<>();

                attFlagPos_serialized.add(attFlagPos.getX());
                attFlagPos_serialized.add(attFlagPos.getY());
                attFlagPos_serialized.add(attFlagPos.getZ());

                fileYAML.set("attackers-flag", attFlagPos_serialized);
            } else {
                fileYAML.set("attackers-flag", null);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Initialized">
            fileYAML.set("initialized", gi.isInitialized());
            // </editor-fold>        

            // <editor-fold defaultstate="collapsed" desc="Started">
            fileYAML.set("match-started", gi.isMatchStarted());
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Game instance sign">
            if (gi.getSign() != null) {
                Location gameSignPos = gi.getSign().getLocation();
                List<Double> gameSignPos_serialized = new ArrayList<>();

                gameSignPos_serialized.add(gameSignPos.getX());
                gameSignPos_serialized.add(gameSignPos.getY());
                gameSignPos_serialized.add(gameSignPos.getZ());

                fileYAML.set("game-sign", gameSignPos_serialized);
            } else {
                fileYAML.set("game-sign", null);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="In-queue players">
            List<String> inQueueSerialized = new ArrayList<>();

            if (!gi.getPlayersInQueue().isEmpty()) {
                for (Player player : gi.getPlayersInQueue()) {
                    TeamMember tm = (TeamMember) player
                            .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0)
                            .value();
                    inQueueSerialized.add(tm.toString());
                }
                fileYAML.set("in-queue", inQueueSerialized);
            } else {
                fileYAML.set("in-queue", null);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="In-game players">
            List<String> inGameSerialized = new ArrayList<>();

            if (!gi.getPlayersInGame().isEmpty()) {
                for (Player player : gi.getPlayersInGame()) {
                    TeamMember tm = (TeamMember) player
                            .getMetadata(MetadataValues.TEAM_MEMBER.key).get(0)
                            .value();
                    inGameSerialized.add(tm.toString());
                }
                fileYAML.set("in-game", inGameSerialized);
            } else {
                fileYAML.set("in-game", null);
            }
            // </editor-fold>

            fileYAML.save(file);

            return true;
        } catch (IOException ex) {
            MessageManager.logError(ex.getMessage());
            return false;
        }
    }
}
