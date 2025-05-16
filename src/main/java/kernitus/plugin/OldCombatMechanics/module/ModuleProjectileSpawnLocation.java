/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ModuleProjectileSpawnLocation extends OCMModule {

    public ModuleProjectileSpawnLocation(OCMMain plugin) {
        super(plugin, "old-projectile-spawn-location");
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        if (shooter instanceof Player player) {
            if (!isEnabled(player)) {
                return;
            }
            Location location = player.getEyeLocation().clone();
            location.subtract(
                    Math.cos(location.getYaw() / 180.0F * 3.1415927F) * 0.16F,
                    0.10000000149011612D,
                    Math.sin(location.getYaw() / 180.0F * 3.1415927F) * 0.16F
            );

            Projectile projectile = event.getEntity();
            Vector velocity = projectile.getVelocity();
            location.setDirection(projectile.getLocation().getDirection());
            projectile.teleport(location);
            projectile.setVelocity(velocity);
        }
    }
}