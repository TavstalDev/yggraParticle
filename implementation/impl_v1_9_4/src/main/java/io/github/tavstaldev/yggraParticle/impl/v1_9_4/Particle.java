package io.github.tavstaldev.yggraParticle.impl.v1_9_4;

import io.github.tavstaldev.yggraParticle.api.IParticle;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.logging.Logger;

/**
 * Particle implementation for Minecraft 1.9.4 and newer servers supported by the Bukkit particle API.
 * <p>
 * This implementation resolves the particle by {@link org.bukkit.Particle} enum name and spawns it
 * using {@link org.bukkit.World#spawnParticle(org.bukkit.Particle, Location, int, double, double, double, double)}.
 */
public class Particle implements IParticle {
    private final Logger logger;

    /**
     * Creates a new Bukkit API based particle implementation.
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

    @Override
    public void spawn(Location location, String particleName, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        try {
            org.bukkit.Particle particle = org.bukkit.Particle.valueOf(particleName);
            //noinspection ConstantValue
            if (particle == null) {
                logger.warning(String.format("Particle '%s' was not found.", particleName));
                return;
            }
            location.getWorld().spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, speed);
        }
        catch (Exception ex) {
            logger.warning(String.format("Particle '%s' was not found.\n%s", particleName, ex));
        }
    }
}
