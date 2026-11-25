# Product Roadmap

Hedgehog Firewall is a 3D isometric tower defense. E.R.I.Z.O. agents defend the
Central Node from waves of friendly malware by placing and upgrading security
towers, with short spy missions between waves that teach security concepts.

This roadmap holds the product backlog (epics and user stories) and the story
map (backbone, releases and estimates).

## Story Map

### Backbone (the main user journey, left to right)

Open headquarters -> Pick map -> Build towers -> Survive waves -> Play a mission
-> See report -> Check ranking

### Releases

- **Release 1** — core single-map experience: build and manage towers, survive 8
  waves, one spy mission, daily challenge, leaderboards and profile.
- **Release 2** — more content and depth: extra maps, waves and towers, a second
  spy mission, richer reports.
- **Release 3** — social and long-term play: groups, seasons, cosmetics and
  accessibility options.

Release 1 stories carry Fibonacci story points. Release 2 and Release 3 stories
are listed as scope only and are estimated when they are pulled into a release.

---

## Epics

### Epic: Defense

The map, path, tiles, towers, upgrades, targeting, enemies, waves and the match
itself.

#### Release 1

**Build towers (5 pts)**
As an agent, I want to place towers on free tiles, so that I can stop the malware.
- Given I have 50 bits and a free tile, When I place a Spike Wall costing 50, Then the tower appears and my bits drop to 0.
- Given I have 10 bits, When I try to place a Spike Wall, Then the placement is rejected and nothing changes.

**Survive waves (8 pts)**
As an agent, I want enemies to arrive in waves along the path, so that the defense has a goal.
- Given a wave is running, When an enemy reaches the Central Node, Then node integrity decreases.
- Given all 8 waves are cleared and integrity is above 0, When the last enemy is disinfected, Then the match ends in victory.

**Upgrade and sell towers (3 pts)**
As an agent, I want to upgrade or sell a tower, so that I can adapt my strategy.
- Given I have enough bits, When I upgrade a tower, Then its stats improve and bits are spent.
- Given a placed tower, When I sell it, Then I get part of its cost back and the tile is free.

#### Release 2

- As an agent, I want additional maps and paths, so that each match feels different.
- As an agent, I want new towers and enemy types, so that I face fresh tactical choices.

#### Release 3

- As an agent, I want seasonal defense modifiers, so that the meta changes over time.

### Epic: Missions

Spy missions between waves, their steps and results.

#### Release 1

**Play a spy mission (5 pts)**
As an agent, I want a short mission between waves, so that I learn a security concept.
- Given a wave just ended, When the mission starts, Then I see the Intercept the Message challenge.
- Given I answer correctly, When the mission ends, Then I earn a bit reward.

#### Release 2

- As an agent, I want the Is This Seal Real? certificate-chain mission, so that I learn to verify trust.

#### Release 3

- As an agent, I want a mission archive, so that I can replay past missions to practice.

### Epic: Content

Wave and mission definitions served by the API, so content grows without code changes.

#### Release 1

**Load content from the API (2 pts)**
As an agent, I want waves and missions delivered as data, so that new content appears without updating the client.
- Given the API serves the Release 1 wave set, When I start a match, Then the 8 waves load from that data.
- Given the API serves a mission set, When a wave ends, Then the next mission loads from that data.

#### Release 2

- As a content author, I want to add waves and missions as data files, so that I can expand the game without code changes.

#### Release 3

- As a content author, I want to version content sets, so that older clients keep working.

### Epic: Daily Challenge

A daily challenge with the same seed and wave modifier for everyone.

#### Release 1

**Daily challenge (5 pts)**
As an agent, I want a daily challenge with a fixed seed, so that I can compare results with others.
- Given two agents play the same day, When both start the challenge, Then they get the same seed and modifier.
- Given I finish today's challenge, When I open it again, Then my best score is shown.

#### Release 2

- As an agent, I want a history of past daily challenges, so that I can see how I improved.

#### Release 3

- As an agent, I want weekly themed challenges, so that there is variety beyond the daily run.

### Epic: Leaderboards

Weekly and group rankings.

#### Release 1

**Leaderboards (5 pts)**
As an agent, I want to see weekly and group rankings, so that I can compete.
- Given scores exist, When I open the weekly ranking, Then I see the top agents ordered by score.
- Given I belong to a group, When I open its ranking, Then I only see members of that group.

#### Release 2

- As an agent, I want all-time rankings, so that I can see the best scores ever.

#### Release 3

- As an agent, I want seasonal leaderboards that reset, so that newcomers can still compete.

### Epic: Profile

Agent profile with active playtime and achievements.

#### Release 1

**Profile and achievements (3 pts)**
As an agent, I want a profile with active playtime and achievements, so that I can see my progress.
- Given I played 30 active minutes, When I open my profile, Then it shows 30 minutes.
- Given I meet an achievement condition, When the match ends, Then the achievement is unlocked.

#### Release 2

- As an agent, I want achievement progress bars, so that I can see how close I am to the next one.

#### Release 3

- As an agent, I want profile cosmetics I can unlock, so that I can personalize my agent.
