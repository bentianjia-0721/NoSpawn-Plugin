# NoSpawn (Paper Plugin)

Language: **English** | [简体中文](README.zh-CN.md)

NoSpawn is a Paper plugin that disables spawning for selected mob types using in-game commands. It is useful for server management, modpack balancing, custom maps, and any setup that needs precise control over mob spawning.

This is a Paper plugin port of the [Fabric NoSpawn mod](https://github.com/bentianjia/NoSpawn).

## Features

- Disable any single mob type or multiple mob types.
- Supports all vanilla mob types.
- Supports both `/ns` and `/nospawn` commands.
- Works on Paper 1.21.4 servers.
- Saves disabled mob types to `plugins/NoSpawn/config.yml`, so changes persist after restarts.
- Automatically switches between Chinese and English messages based on the player's game language.
- Supports LuckPerms permission groups with OP fallback.

## Commands

`<type>` is a mob name, such as `creeper`, `vex`, or `phantom`. These are examples only; you can use any valid mob type name. After typing `/ns add ` or `/nospawn add `, Minecraft will show available mob suggestions.

```
/ns
/nospawn
/ns add <type>
/ns remove <type>
/nospawn add <type>
/nospawn remove <type>
```

Examples:

```
/ns add creeper
/ns add vex
/nospawn add phantom
/ns remove vex
```

## Permissions

Either OP permission or a matching LuckPerms node is enough. `nospawn.admin` allows every NoSpawn command.

```
nospawn.admin
nospawn.use
nospawn.add
nospawn.remove
```

LuckPerms examples:

```
/lp group admin permission set nospawn.admin true
/lp group helper permission set nospawn.use true
/lp group helper permission set nospawn.add true
/lp group helper permission set nospawn.remove true
```

## Compatibility

```
Server: Paper 1.21.4
API: Bukkit/Paper API 1.21.x
Java: 21+
```

## Building

```bash
./gradlew build
```

The output jar will be in `build/libs/NoSpawn-1.0.0.jar`.

## Author And Credits

- Developer: bentianjia
- Contributor: ZURAN_

Special thanks to ZURAN_ for the design inspiration and valuable testing opportunities.

## License

MIT
