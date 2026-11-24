package com.github.TavstalDev.yggraParticle.impl.v1_8_8;

import com.github.TavstalDev.yggraParticle.api.IParticle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Particle implementation for Minecraft 1.8.x servers.
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
    private static final HashMap<String, String> particleMap = new HashMap<String, String>() {{
        put("HAPPY_VILLAGER", "VILLAGER_HAPPY");
        put("VILLAGER_THUNDERCLOUD", "VILLAGER_ANGRY");
        put("SMOKE", "SMOKE_NORMAL");
        put("SMALL_SMOKE", "SMOKE_NORMAL");
        put("LARGE_SMOKE", "SMOKE_LARGE");
        put("MAGIC_CRIT", "CRIT_MAGIC");
        put("WITCH_MAGIC", "SPELL_WITCH");
        put("INSTANT_SPELL", "SPELL_INSTANT");
        put("POTION_SWIRL", "SPELL_MOB");
        put("POTION_SWIRL_TRANSPARENT", "SPELL_MOB_AMBIENT");
        put("FLYING_GLYPH", "ENCHANTMENT_TABLE");
        put("LAVA_POP", "LAVA");
        put("WATERDRIP", "DRIP_WATER");
        put("LAVADRIP", "DRIP_LAVA");
        put("COLOURED_DUST", "REDSTONE");
        put("SNOWBALL_BREAK", "SNOWBALL");
        put("EXPLOSION", "EXPLOSION_NORMAL");
        put("VOID_FOG", "TOWN_AURA");
        put("SPLASH", "WATER_SPLASH");

        // Common aliases people try that don't exist in either enum
        put("FIREWORK", "FIREWORKS_SPARK");
        put("WATER_DRIP", "DRIP_WATER");
        put("LAVA_DRIP", "DRIP_LAVA");
    }};

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

    private static String mapParticle(String input) {
        if (input == null || input.isEmpty())
            return input;

        String key = input.toUpperCase();
        if (!particleMap.containsKey(key))
            return key;
        return particleMap.get(key);
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
            String particle = mapParticle(particleName);
            Object enumParticle = Enum.valueOf((Class<Enum>) enumParticleClass, particle);
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
