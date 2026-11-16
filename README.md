# yggraParticle

![Release (latest by date)](https://img.shields.io/github/v/release/TavstalDev/yggraParticle?style=plastic-square)
![Workflow Status](https://img.shields.io/github/actions/workflow/status/TavstalDev/yggraParticle/release.yml?branch=stable&label=build&style=plastic-square)
![License](https://img.shields.io/github/license/TavstalDev/yggraParticle?style=plastic-square)
![Downloads](https://img.shields.io/github/downloads/TavstalDev/yggraParticle/total?style=plastic-square)
![Stars](https://img.shields.io/github/stars/TavstalDev/yggraParticle?style=plastic-square)
![Issues](https://img.shields.io/github/issues/TavstalDev/yggraParticle?style=plastic-square)
![Forks](https://img.shields.io/github/forks/TavstalDev/yggraParticle?style=plastic-square)

A small, version-aware library for spawning particle effects from Spigot/Paper plugins. It selects
the appropriate implementation for the running server at runtime, so your plugin can use one API
without maintaining its own version-specific particle code.

## Features

- One API for spawning particles across supported Minecraft server versions.
- Overloads for particle count, uniform offset, per-axis offsets, and speed.
- Automatic server version detection with a graceful fallback when no compatible implementation is
  available.
- A single shaded bootstrap JAR that contains the API and the bundled implementations.

## Supported versions

| Minecraft version | Implementation | How it works |
| --- | --- | --- |
| 1.9.4+ | `impl_v1_9_4` | Bukkit `World#spawnParticle(Particle, ...)` API |
| 1.8.x – 1.9.3 | `impl_v1_8_8` | Reflective NMS `PacketPlayOutWorldParticles` packets (`v1_8_R3`) |

The implementation is selected once when `YggraParticle` is constructed. If the server version is
unsupported or the implementation cannot be loaded, a warning is logged and spawn calls are ignored
instead of crashing your plugin.

> **Note:** Particle names must match the identifiers expected by the selected implementation. On
> 1.9.4+ servers this means the `org.bukkit.Particle` enum name. On 1.8.x servers this means the
> `EnumParticle` name used by `v1_8_R3`.

## Project structure

| Module | Purpose |
| --- | --- |
| `api` | The public `IParticle` interface |
| `bootstrap` | Version detection and the `YggraParticle` facade |
| `implementation/impl_v1_8_8` | 1.8.x – 1.9.3 implementation |
| `implementation/impl_v1_9_4` | 1.9.4+ implementation |
| `plugin-test` | Example plugin showing runtime usage |

## Requirements

- A Spigot or Paper server running a supported Minecraft version.
- **Java 8+** at runtime for the published library modules.
- **JDK 25** with the included Gradle wrapper to build the project.

## Building from source

```bash
./gradlew build
```

The library JAR is written to:

```text
bootstrap/build/libs/yggraParticle-0.1.0.jar
```

This JAR contains the API, the bootstrap facade, and both bundled implementations.

To build the example plugin:

```bash
./gradlew :plugin-test:shadowJar
```

The result is `plugin-test/build/libs/yggraParticle-test-1.0.0.jar`.

## Usage

### 1. Add the library to your plugin

Shade the produced library JAR into your plugin. If you build this repository as part of your
project, you can depend on the shaded bootstrap artifact directly:

```kotlin
dependencies {
    implementation(project(":bootstrap", configuration = "shadow")) {
        isTransitive = false
    }
}
```

If you use the pre-built JAR, add it as a local dependency:

```kotlin
dependencies {
    implementation(files("libs/yggraParticle-0.1.0.jar"))
}
```

### 2. Create the facade

Create one `YggraParticle` instance during `onEnable` and reuse it for the lifetime of the plugin:

```java
import io.github.tavstaldev.yggraParticle.api.IParticle;
import io.github.tavstaldev.yggraParticle.bootstrap.YggraParticle;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyPlugin extends JavaPlugin {

    private IParticle particles;

    @Override
    public void onEnable() {
        particles = new YggraParticle(this);
    }
}
```

`YggraParticle` implements `IParticle`, so you can store it as either type.

### 3. Spawn particles

```java
import org.bukkit.Location;
import org.bukkit.entity.Player;

public void showEffect(Player player) {
    Location location = player.getLocation();

    // Spawn a single particle.
    particles.spawn(location, "HEART");

    // Spawn 10 particles.
    particles.spawn(location, "HEART", 10);

    // Spawn 10 particles with a uniform offset and speed.
    particles.spawn(location, "HEART", 10, 1.0, 0.5);

    // Spawn 10 particles with per-axis offsets and speed.
    particles.spawn(location, "HEART", 10, 1.0, 0.5, 1.0, 0.2);
}
```

## API overview

```java
public interface IParticle {

    void spawn(Location location, String particleName);

    void spawn(Location location, String particleName, int count);

    void spawn(Location location, String particleName, int count, double offset, double speed);

    void spawn(Location location, String particleName, int count,
               double offsetX, double offsetY, double offsetZ, double speed);
}
```

| Method | Description |
| --- | --- |
| `spawn(Location, String)` | Spawns a single particle. |
| `spawn(Location, String, int)` | Spawns the specified number of particles. |
| `spawn(Location, String, int, double, double)` | Spawns particles with a uniform offset on each axis and a speed value. |
| `spawn(Location, String, int, double, double, double, double)` | Spawns particles with per-axis offsets and a speed value. |

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for more details.

## Contact

For issues or feature requests, use the
[GitHub issue tracker](https://github.com/TavstalDev/yggraParticle/issues).
