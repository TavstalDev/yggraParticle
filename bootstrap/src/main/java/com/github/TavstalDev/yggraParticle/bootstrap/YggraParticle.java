package com.github.TavstalDev.yggraParticle.bootstrap;

import com.github.TavstalDev.yggraParticle.api.IParticle;
import com.github.TavstalDev.yggraParticle.bootstrap.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Runtime facade for spawning particles across supported Minecraft server versions.
 * <p>
 * When constructed, this class selects the appropriate version-specific implementation for the
 * running server. If no compatible implementation can be loaded, a warning is logged and spawn
 * calls are ignored so the host plugin can continue running.
 */
public class YggraParticle implements IParticle {
    private IParticle _particle;

    /**
     * Creates a new particle facade for the given plugin and resolves the runtime implementation.
     *
     * @param plugin the plugin instance used for logging and implementation initialization
     */
    public YggraParticle(JavaPlugin plugin) {
        try {
            String implClassName = getImplClassName();
            _particle = (IParticle) Class.forName(implClassName)
                    .getDeclaredConstructor(JavaPlugin.class).newInstance(plugin);
        }
        catch (Exception | LinkageError ex) {
            plugin.getLogger().warning("Failed to get the right particle implementation for this server version.\n" + ex);
        }
    }

    @Override
    public void spawn(Location location, String particleName) {
        if (_particle == null)
            return;
        _particle.spawn(location, particleName);
    }

    @Override
    public void spawn(Location location, String particleName, int count) {
        if (_particle == null)
            return;
        _particle.spawn(location, particleName, count);
    }

    @Override
    public void spawn(Location location, String particleName, int count, double offset, double speed) {
        if (_particle == null)
            return;
        _particle.spawn(location, particleName, count, offset, speed);
    }

    @Override
    public void spawn(Location location, String particleName, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        if (_particle == null)
            return;
        _particle.spawn(location, particleName, count, offsetX, offsetY, offsetZ, speed);
    }

    private static @NotNull String getImplClassName() {
        String implClassName;

        if (VersionUtils.isAtLeast(1, 9, 4))
            implClassName = "com.github.TavstalDev.yggraParticle.impl.v1_9_4.Particle";
        else if (VersionUtils.isAtLeast(1, 8))
            implClassName = "com.github.TavstalDev.yggraParticle.impl.v1_8_8.Particle";
        else
            throw new RuntimeException("Unsupported Minecraft version: " + Bukkit.getBukkitVersion());

        return implClassName;
    }
}
