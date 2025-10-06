/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package kernitus.plugin.OldCombatMechanics.utilities.storage;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import org.bson.*;
import org.bson.codecs.*;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.io.BasicOutputBuffer;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 * Stores data associated with players to disk, persisting across server restarts.
 */
public class PlayerStorage {

    private static OCMMain plugin;
    private static Map<UUID, PlayerData> dataMap;

    public static void initialise(OCMMain plugin) {
        PlayerStorage.plugin = plugin;
        dataMap = new HashMap<>();
    }

    public static void scheduleSave() {
    }

    public static PlayerData getPlayerData(UUID uuid) {
        PlayerData playerData = dataMap.get(uuid);
        if (playerData == null) {
            playerData = new PlayerData();
            dataMap.put(uuid, playerData);
            return playerData;
        } else {
            return playerData;
        }
    }

    public static void clearPlayerData(UUID uuid) {
        dataMap.remove(uuid);
    }

    public static void setPlayerData(UUID uuid, PlayerData playerData) {
        dataMap.put(uuid, playerData);
    }
}