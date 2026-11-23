# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.2] - 2026-11-23

### Added

- Published sources and Javadoc jars with Maven artifacts.

### Changed

- Bumped Gradle wrapper to 9.8.2.
- Ignored Spigot API, Paper API, Gradle, and Shadow updates in Dependabot.

## [0.1.1] - 2026-11-17

### Added

- JitPack publishing support, including `jitpack.yml` and shaded bootstrap publication.
- JitPack dependency examples in the README.

### Changed

- Renamed Java packages from `com.tavstaldev` to `com.github.TavstalDev`.
- Set the Gradle group to `com.github.TavstalDev`.

## [0.1.0] - 2026-11-17

### Added

- Initial release of yggraParticle.
- Public `IParticle` API for spawning particle effects by name.
- `YggraParticle` bootstrap facade with runtime server version detection.
- Bukkit particle API implementation for Minecraft 1.9.4 and newer.
- Reflective NMS packet implementation for Minecraft 1.8.x to 1.9.3 (`v1_8_R3`).
- Shaded bootstrap artifact bundling the API and bundled implementations.
- Example `plugin-test` module demonstrating library usage.

[Unreleased]: https://github.com/TavstalDev/yggraParticle/compare/v0.1.2...HEAD
[0.1.2]: https://github.com/TavstalDev/yggraParticle/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/TavstalDev/yggraParticle/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/TavstalDev/yggraParticle/releases/tag/v0.1.0