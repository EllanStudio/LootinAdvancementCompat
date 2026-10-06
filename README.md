# LootinAdvancementCompat

A tiny, removable compatibility bridge that restores Minecraft's **War Pigs**
advancement for per-player containers managed by Lootin or Lootin Remastered.

## Why it exists

Lootin stores a container's vanilla loot table and fills a per-player inventory
through plugin code. This preserves personal loot, but skips Minecraft's native
`player_generates_container_loot` trigger used by `minecraft:nether/loot_bastion`.

This plugin listens only to Lootin's own `LootinInventoryOpenEvent`, reads the
stored loot table, and awards the matching vanilla criterion for these tables:

- `minecraft:chests/bastion_bridge`
- `minecraft:chests/bastion_hoglin_stable`
- `minecraft:chests/bastion_other`
- `minecraft:chests/bastion_treasure`

## Performance

There are no timers, database queries, packet listeners, world scans, chunk
scans, or player caches. A handler runs only when Lootin opens one of its own
inventories and performs a PDC read plus a constant-size map lookup.

## Requirements

- Java 25 or newer
- Paper 26.3-compatible server (compile target `26.3.build.157-beta`) with the modern advancement API
- Lootin or Lootin Remastered exposing `LootinInventoryOpenEvent`

## Build

```shell
./gradlew clean check build
```

The output is written to `build/libs/`. The declaration under
`src/lootinApi/` is compile-only and is not packaged into the final JAR.

## Installation

1. Place the JAR in the backend server's `plugins/` directory.
2. Keep Lootin installed.
3. Restart the server normally.

Remove this plugin when Lootin restores the native advancement trigger itself.
