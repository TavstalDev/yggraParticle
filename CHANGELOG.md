# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-11-17

### Added

- Initial release of yggraParticle.
- Public `IParticle` API for spawning particle effects by name.
- `YggraParticle` bootstrap facade with runtime server version detection.
- Bukkit particle API implementation for Minecraft 1.9.4 and newer.
- Reflective NMS packet implementation for Minecraft 1.8.x to 1.9.3 (`v1_8_R3`).
- Shaded bootstrap artifact bundling the API and bundled implementations.
- Example `plugin-test` module demonstrating library usage.
