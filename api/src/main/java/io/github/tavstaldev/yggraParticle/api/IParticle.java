package io.github.tavstaldev.yggraParticle.api;

import org.bukkit.Location;

/**
 * Defines the particle spawning operations provided by yggraParticle.
 * <p>
 * Implementations resolve the given particle name for the running server version and display the
 * particle effect at the specified location. If a particle cannot be spawned, implementations are
 * expected to fail gracefully rather than throw to the caller.
 */
@SuppressWarnings("unused")
public interface IParticle {

    /**
     * Spawns a single particle at the given location.
     *
     * @param location     the location where the particle effect is displayed
     * @param particleName the name of the particle to spawn
     */
    void spawn(Location location, String particleName);

    /**
     * Spawns particles at the given location.
     *
     * @param location     the location where the particle effect is displayed
     * @param particleName the name of the particle to spawn
     * @param count        the number of particles to spawn
     */
    void spawn(Location location, String particleName, int count);

    /**
     * Spawns particles at the given location using a uniform offset on each axis.
     *
     * @param location     the location where the particle effect is displayed
     * @param particleName the name of the particle to spawn
     * @param count        the number of particles to spawn
     * @param offset       the maximum random offset applied to the X, Y, and Z axes
     * @param speed        the extra particle data/speed value passed to the effect
     */
    void spawn(Location location, String particleName, int count, double offset, double speed);

    /**
     * Spawns particles at the given location.
     *
     * @param location     the location where the particle effect is displayed
     * @param particleName the name of the particle to spawn
     * @param count        the number of particles to spawn
     * @param offsetX      the maximum random offset on the X axis
     * @param offsetY      the maximum random offset on the Y axis
     * @param offsetZ      the maximum random offset on the Z axis
     * @param speed        the extra particle data/speed value passed to the effect
     */
    void spawn(Location location, String particleName, int count, double offsetX, double offsetY, double offsetZ, double speed);
}
