# Hedgehog Firewall

An 3D tower defense game for all ages, rendered with a fixed isometric camera.

Hedgehog agents of the E.R.I.Z.O. agency defend the Central Node, a tree house full of
servers, from friendly malware that arrives in waves along the network cables. You place
and upgrade security towers along the path. Malware is never destroyed, only disinfected.
Between waves, short spy missions introduce real security concepts.

The simulation is deterministic and tick based: the same seed and the same list of
commands always produce the same result.

## Features

- One map with eight waves of enemies.
- Four towers: Spike Wall, Scanner Lens, Honeypot and Traffic Valve, each with upgrades and selling.
- Four enemies: Wormy, Trojy, Phisher and Buzz Cloud, each with its own behavior.
- Two spy missions between waves: Intercept the Message and Is This Seal Real?
- A daily challenge shared by everyone, with score, weekly and group leaderboards.
- A player profile with active playtime and achievements.
- Waves and missions are served as data, so content can grow without code changes.

## Modules

The project uses a hexagonal architecture with dependencies pointing inward only.

| Module            | Responsibility                                                        |
|-------------------|-----------------------------------------------------------------------|
| `domain`          | Pure game rules: map, towers, enemies, waves, ticks, missions, score. |
| `application`     | Use cases built on the domain.                                        |
| `infrastructure`  | Adapters for content files, storage and HTTP.                         |
| `client`          | libGDX 3D desktop client with a fixed isometric camera.               |
| `api`             | REST service that serves content and validates scores.               |
| `match-validator` | Service that re-simulates matches to confirm reported scores.         |

## Requirements

- Java 21
- No separate Gradle install needed; use the included wrapper.

## Build and run

Build everything and run the tests:

```
./gradlew build
```

Run the desktop client:

```
./gradlew :client:run
```

Run the API:

```
./gradlew :api:bootRun
```

## License

See the repository for license details.
