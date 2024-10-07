/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import kernitus.plugin.OldCombatMechanics.utilities.MathHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public class ModuleProjectileRelativeVelocity extends OCMModule {

    public ModuleProjectileRelativeVelocity(OCMMain plugin) {
        super(plugin, "projectile-relative-velocity-transfer");
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        if (shooter instanceof Player player) {
            if (!isEnabled(player)) return;

            Projectile projectile = event.getEntity();
            Vector velocity = projectile.getVelocity();
            Vector shooterVelocity = player.getVelocity();
            Vector relativeVelocity = new Vector(shooterVelocity.getX(), player.isOnGround() ? 0.0D : shooterVelocity.getY(), shooterVelocity.getZ());
            projectile.setVelocity(velocity.add(relativeVelocity));
        }
    }
}