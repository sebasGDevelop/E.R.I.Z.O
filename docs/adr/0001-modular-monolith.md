# 1. Modular monolith for Release 1

Date: 2025-01-01

## Status

Accepted

## Context

Hedgehog Firewall is a 3D isometric tower defense. The player builds and upgrades
towers to stop waves of malware reaching the Central Node, with short spy missions
between waves. The simulation is deterministic and tick-based: the same seed and
the same command list always produce the same result, and the server re-simulates
finished matches to validate the scores it puts on the leaderboards.

Release 1 ships one map, eight waves, four towers, four enemies, two spy missions,
a daily challenge, score and leaderboards, and a player profile. The deliverable is
a desktop game client plus a small backend that serves wave and mission content,
accepts finished matches, re-simulates them and publishes rankings.

We need to decide how to split the backend into deployable units. The two candidates:

- **Modular monolith** — one deployable backend, divided internally into modules
  with explicit boundaries (defense simulation, content, community/leaderboards,
  match validation), sharing one process and one build.
- **Microservices** — each capability as its own independently deployable service
  with its own process and its own network boundary, communicating over the wire.

We compared the two against the ISO/IEC 25010 product quality attributes most
relevant to this product and this stage.

| Quality attribute (ISO/IEC 25010) | Modular monolith | Microservices |
|---|---|---|
| Functional suitability | Equal. Either can deliver the Release 1 features. | Equal. |
| Performance efficiency | One process, in-memory calls, no network hops for re-simulation. | Added latency and serialization between services for little gain at this load. |
| Compatibility | Shared types and one contract-first API; no cross-service version skew. | Needs versioned contracts and compatibility handling between services from day one. |
| Usability (operability) | One thing to run, configure and observe. | Many moving parts to run and correlate, heavy for the current team and traffic. |
| Reliability | Fewer failure modes; no partial-failure or distributed-timeout handling. | More independent failure points and the distributed-systems failures that come with them. |
| Security | One trust boundary to defend; the match validator is the one place that needs its own boundary, and it gets one. | A larger attack surface and more service-to-service authentication to secure. |
| Maintainability | Module boundaries give separation of concerns without the cost of a network between modules; refactoring across boundaries is a normal code change. | Boundaries are enforced by the network, but every change that crosses one becomes a cross-service change. |
| Portability | Single artifact, simple to deploy and move. | More deployment and orchestration machinery to carry. |

The decisive attributes at this stage are operability, reliability and
maintainability. Microservices trade those away for independent scaling and
independent deployment, neither of which Release 1 needs: the load is modest and
the whole backend ships together.

The one capability with a genuinely different trust and integrity requirement is
match validation (it must re-run a match with the exact simulation the client used,
and its verdict decides leaderboard scores). We keep it as a separate service with
its own boundary, but that is a single, deliberate split, not a service-per-capability
architecture.

## Decision

Build the Release 1 backend as a **modular monolith**: one deployable backend with
explicit internal module boundaries (defense, content, community, shared), plus a
separate **match validator** service that re-simulates submitted matches across a
guarded boundary.

Keep the module boundaries clean enough that any one module could later be extracted
into its own service if load or team growth ever justifies it.

## Consequences

- One backend to build, run, configure and observe for Release 1; low operational cost.
- In-process module calls: no network latency or serialization inside the backend,
  and re-simulation stays fast.
- Module boundaries must be enforced in the code and the build, because the network
  will not enforce them for us. Dependencies point inward toward the core; modules
  do not reach into each other's internals.
- Match validation keeps its own process and trust boundary, so a split already
  exists and the pattern for extracting a future module is proven.
- If a single capability later needs independent scaling or deployment, the clean
  boundaries make extraction a bounded piece of work rather than a rewrite.
