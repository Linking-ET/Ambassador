package org.adde0109.ambassador.forge;

import org.adde0109.ambassador.forge.packet.Context;
import org.adde0109.ambassador.forge.packet.GenericForgeLoginWrapperPacket;
import org.adde0109.ambassador.forge.packet.ModListReplyPacket;
import org.adde0109.ambassador.forge.packet.RegistryPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ForgeHandshake {
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(ForgeHandshake.class);
    private volatile boolean channelsLogged = false;
    private ModListReplyPacket modListReplyPacket;
    private final Map<String, Long> registries = new HashMap<>();
    public GenericForgeLoginWrapperPacket<Context.ClientContext> zetaFlagsPacket;

    public ForgeHandshake() {
    }

    public ModListReplyPacket getModListReplyPacket() {
        return modListReplyPacket;
    }

    public void setModListReplyPacket(ModListReplyPacket modListReplyPacket) {
        this.modListReplyPacket = modListReplyPacket;
    }

    public void addRegistry(RegistryPacket packet) {
        Checksum registryChecksum = new Adler32();
        registryChecksum.update(packet.getSnapshot());
        registries.put(packet.getRegistryName(), registryChecksum.getValue());
        // --------- debug 打印 ---------
        if (!channelsLogged &&
                Boolean.getBoolean("ambassador.debug")) {    // -Dambassador.debug=true
            channelsLogged = true;
            log.info("[ambassador-debug] connection channels: {}", registries.keySet());
        }
    }

    public Map<String, Long> getRegistries() {
        return registries;
    }

    public boolean isCompatible(ForgeHandshake handshake) {
        return this.registries.equals(handshake.registries);
    }
}
