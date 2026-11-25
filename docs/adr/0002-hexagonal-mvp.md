# 2. Hexagonal core and a passive-view client

Date: 2025-01-01

## Status

Accepted

Builds on [ADR 0001](0001-modular-monolith.md).

## Context

The game rules are the most valuable and the most long-lived part of this product.
The same rules run in three places: the desktop client plays a live match, the
backend serves content and stores results, and the match validator re-simulates a
finished match to check its score. For the leaderboards to be trustworthy, all three
must compute a match identically from the same seed and the same command list.

That constraint drives two needs:

1. **The rules must not depend on how they are delivered.** They cannot be tangled
   with the 3D renderer, the desktop windowing toolkit, the web framework, file I/O
   or network calls. If they were, we could not run the identical rules inside both
   the client and the validator, and we could not test them quickly and deterministically.
2. **The client's screen must hold no rules.** A 3D view that quietly decided game
   outcomes would be a second, divergent copy of the rules and would break
   re-simulation. The view must only show state and forward player intent.

We also want the rules covered by fast, deterministic tests with fixed seeds and
small hand-built maps, which is only possible if the rules have no I/O or framework
dependencies to stand up in a test.

## Decision

**Hexagonal architecture for the core.** The game rules live in a pure core module
with no dependency on any framework, I/O or networking. Everything the core needs
from the outside world (loading wave and mission content, storing a match, a source
of randomness, a clock) is expressed as an interface owned by the core. Concrete
adapters that talk to JSON files, HTTP, storage and the system clock live in an outer
module and depend inward on the core, never the other way around. Dependencies point
only inward.

Because the core is pure, the exact same rules are embedded in the client to play a
live match and in the match validator to re-simulate a finished one, so both reach
the same result.

**A passive view for the client.** The client separates the screen from the logic
that drives it. The view only renders the state it is given and reports raw player
input; it contains no game rules. A presenter sits between the view and the core: it
turns player input into calls on the core's use cases and feeds the resulting state
back to the view. The 3D scene therefore reads game state and never changes it.

## Consequences

- The rules are framework-free and can be tested in isolation with fixed seeds and
  small maps: fast, deterministic unit tests with no I/O to set up.
- The identical rules run in the client and in the validator, which is what makes
  server-side re-simulation of a match sound.
- Swapping an outside concern (for example, loading content from an HTTP API instead
  of local JSON files) is a new adapter behind an existing interface, with no change
  to the rules.
- The client view carries no logic, so it can be replaced or restyled (the 3D
  presentation arrives in a later step) without touching the rules, and a plain text
  debug view can drive the same presenters.
- The discipline has a cost: outside concerns must be defined as core-owned interfaces
  before an adapter is written, and the "dependencies point inward" rule must be held
  in code review and in the build.
