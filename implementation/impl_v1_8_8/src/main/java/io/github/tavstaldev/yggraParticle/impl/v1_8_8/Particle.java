package io.github.tavstaldev.yggraParticle.impl.v1_8_8;

import io.github.tavstaldev.yggraParticle.api.IParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

/**
 * Particle implementation for Minecraft 1.8.x - 1.10.x servers.
 * <p>
 * This implementation sends {@code PacketPlayOutWorldParticles} packets via reflection against the
 * {@code v1_8_R3} NMS classes and delivers them to nearby players within a 32 block radius.
 */
public class Particle implements IParticle {
    private final Logger logger;
    private static Class<?> enumParticleClass;
    private static Class<?> playerPacketClass;
    private static Constructor<?> constructor;
    private static boolean ready;

    static {
        try {
            Class<?> packetClass = Class.forName("net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles");
            enumParticleClass = Class.forName("net.minecraft.server.v1_8_R3.EnumParticle");

            constructor = packetClass.getConstructor(
                    enumParticleClass, boolean.class, float.class, float.class, float.class,
                    float.class, float.class, float.class, float.class, int.class, int[].class
            );
            playerPacketClass = Class.forName("net.minecraft.server.v1_8_R3.Packet");

            ready = true;
        }
        catch (Exception ex) {
            //noinspection CallToPrintStackTrace
            ex.printStackTrace();
        }
    }

    /**
     * Creates a new legacy particle implementation.
     *
     * @param plugin the plugin instance used for logging
     */
    public Particle(JavaPlugin plugin) {
        logger = plugin.getLogger();
    }

    @Override
    public void spawn(Location location, String particleName) {
        spawn(location, particleName, 1);
    }

    @Override
    public void spawn(Location location, String particleName, int count) {
        spawn(location, particleName, count, 0, 1);
    }

    @Override
    public void spawn(Location location, String particleName, int count, double offset, double speed) {
        spawn(location, particleName, count, offset, offset, offset, speed);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void spawn(Location location, String particleName, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        try {
            if (!ready)
                return;

            Object enumParticle = Enum.valueOf((Class<Enum>) enumParticleClass, particleName);
            Object packet = constructor.newInstance(
                    enumParticle, true,
                    (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                    (float)offsetX, (float)offsetY, (float)offsetZ,
                    (float)speed,
                    count,
                    new int[0]
            );

            for (Player player : location.getWorld().getPlayers()) {
                if (location.distance(player.getLocation()) > 32)
                    continue;
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", playerPacketClass).invoke(playerConnection, packet);
            }
        }
        catch (Exception ex) {
            logger.warning(String.format("Particle '%s' was not found.\n%s", particleName, ex));
        }
    }
}
