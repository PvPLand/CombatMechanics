/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ModuleDisablePlayerCrits extends OCMModule {

    public ModuleDisablePlayerCrits(OCMMain plugin) {
        super(plugin, "disable-player-crits");
    }

    @Override
    public void onModesetChange(Player player) {
        if (!isEnabled(player)) {
            return;
        }

        Level level = ((CraftPlayer) player).getHandle().level();
        level.paperConfig().entities.behavior.disablePlayerCrits = true;
    }
}