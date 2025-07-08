/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package kernitus.plugin.OldCombatMechanics.module;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import kernitus.plugin.OldCombatMechanics.OCMMain;
import kernitus.plugin.OldCombatMechanics.utilities.Messenger;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A module to disable the new attack sounds.
 */
public class ModuleAttackSounds extends OCMModule {

    private final PacketEventsAPI<?> protocolManager = plugin.getProtocolManager();
    private final SoundListener soundListener = new SoundListener();
    private final Set<String> blockedSounds = new HashSet<>(getBlockedSounds());

    public ModuleAttackSounds(OCMMain plugin) {
        super(plugin, "disable-attack-sounds");

        reload();
    }

    @Override
    public void reload() {
        blockedSounds.clear();
        blockedSounds.addAll(getBlockedSounds());

        if (isEnabled())
            protocolManager.getEventManager().registerListener(soundListener);
        else
            protocolManager.getEventManager().unregisterListener(soundListener);
    }

    private Collection<String> getBlockedSounds() {
        return module().getStringList("blocked-sound-names");
    }

    /**
     * Disables attack sounds.
     */
    private class SoundListener extends SimplePacketListenerAbstract {
        private boolean disabledDueToError;

        @Override
        public void onPacketPlaySend(PacketPlaySendEvent event) {
            if (disabledDueToError || !isEnabled((HumanEntity) event.getPlayer())) return;
            if (event.getPacketType() != PacketType.Play.Server.SOUND_EFFECT) {
                return;
            }

            try {
                WrapperPlayServerSoundEffect wrapper = new WrapperPlayServerSoundEffect(event);
                Sound sound = wrapper.getSound();
                String soundName = sound.getSoundId().toString(); // Works for both string and namespaced key

                if (blockedSounds.contains(soundName)) {
                    event.setCancelled(true);
                    debug("Blocked sound " + soundName, event.getPlayer());
                }
            } catch (Exception | ExceptionInInitializerError e) {
                disabledDueToError = true;
                Messenger.warn(
                        e,
                        "Error detecting sound packets. Please report it along with the following exception " +
                                "on github."
                );
            }
        }
    }
}
