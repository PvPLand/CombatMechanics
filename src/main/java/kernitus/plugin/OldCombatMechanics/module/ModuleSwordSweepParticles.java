/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import kernitus.plugin.OldCombatMechanics.OCMMain;
import kernitus.plugin.OldCombatMechanics.utilities.Messenger;
import org.bukkit.entity.HumanEntity;

/**
 * A module to disable the sweep attack.
 */
public class ModuleSwordSweepParticles extends OCMModule {

    private final PacketEventsAPI<?> protocolManager = plugin.getProtocolManager();
    private final ParticleListener particleListener = new ParticleListener();

    public ModuleSwordSweepParticles(OCMMain plugin) {
        super(plugin, "disable-sword-sweep-particles");

        reload();
    }

    @Override
    public void reload() {
        if (isEnabled())
            protocolManager.getEventManager().registerListener(particleListener);
        else
            protocolManager.getEventManager().unregisterListener(particleListener);
    }

    /**
     * Hides sweep particles.
     */
    private class ParticleListener extends SimplePacketListenerAbstract {

        private boolean disabledDueToError;

        @Override
        public void onPacketPlaySend(PacketPlaySendEvent event) {
            if (disabledDueToError || !isEnabled((HumanEntity) event.getPlayer())) return;
            if (event.getPacketType() != PacketType.Play.Server.PARTICLE) return;
            try {
                WrapperPlayServerParticle wrapper = new WrapperPlayServerParticle(event);
                if (wrapper.getParticle().getType() == ParticleTypes.SWEEP_ATTACK) {
                    event.setCancelled(true);
                    debug("Cancelled sweep particles", event.getPlayer());
                }
            } catch (Exception | ExceptionInInitializerError e) {
                disabledDueToError = true;
                Messenger.warn(
                        e,
                        "Error detecting sweep packets. Please report it along with the following exception " +
                                "on github." +
                                "Sweep cancellation should still work, but particles might show up."
                );
            }
        }
    }
}
