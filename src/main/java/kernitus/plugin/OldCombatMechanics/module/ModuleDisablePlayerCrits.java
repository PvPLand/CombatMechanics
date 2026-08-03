/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ModuleDisablePlayerCrits extends OCMModule {

    public ModuleDisablePlayerCrits(OCMMain plugin) {
        super(plugin, "disable-player-crits");
    }

    @Override
    public void onModesetChange(Player player) {
        Level level = ((CraftPlayer) player).getHandle().level();

        // Paper stores this setting on the Level, not on the player. Recompute it
        // from all players in the world so switching one player to the new
        // modeset cannot leave critical hits disabled for everyone indefinitely.
        boolean disablePlayerCrits = Bukkit.getOnlinePlayers().stream()
                .filter(other -> other.getWorld().equals(player.getWorld()))
                .anyMatch(this::isEnabled);

        level.paperConfig().entities.behavior.disablePlayerCrits = disablePlayerCrits;
    }
}
