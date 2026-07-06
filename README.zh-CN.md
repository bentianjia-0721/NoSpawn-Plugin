# NoSpawn (Paper 插件)

语言：[English](README.md) | **简体中文**

NoSpawn 是一个 Paper 插件，可通过游戏内命令禁用指定生物类型的生成。适用于服务器管理、模组包平衡、自定义地图以及任何需要精确控制生物生成的场景。

这是 [Fabric NoSpawn 模组](https://github.com/bentianjia-0721/NoSpawn) 的 Paper 插件版本。

## 特性

- 禁用单个或多个生物类型的生成。
- 支持所有原版生物类型。
- 支持 `/ns` 和 `/nospawn` 两种命令形式。
- 适用于 Paper 1.21.4 服务器。
- 将禁用的生物类型保存到 `plugins/NoSpawn/config.yml`，重启后配置依然有效。
- 根据玩家游戏语言自动切换中英文提示。
- 支持 LuckPerms 权限组，未配置时使用 OP 权限。

## 命令

`<type>` 是生物名称，例如 `creeper`、`vex` 或 `phantom`。这些只是示例，你可以使用任何有效的生物类型名称。输入 `/ns add ` 或 `/nospawn add ` 后，Minecraft 会显示可用的生物提示。

```
/ns
/nospawn
/ns add <type>
/ns remove <type>
/nospawn add <type>
/nospawn remove <type>
```

示例：

```
/ns add creeper
/ns add vex
/nospawn add phantom
/ns remove vex
```

## 权限

拥有 OP 权限或匹配的 LuckPerms 权限节点均可使用。`nospawn.admin` 允许使用所有 NoSpawn 命令。

```
nospawn.admin
nospawn.use
nospawn.add
nospawn.remove
```

LuckPerms 示例：

```
/lp group admin permission set nospawn.admin true
/lp group helper permission set nospawn.use true
/lp group helper permission set nospawn.add true
/lp group helper permission set nospawn.remove true
```

## 兼容性

```
服务器：Paper 1.21.4
API：Bukkit/Paper API 1.21.x
Java：21+
```

## 构建

```bash
./gradlew build
```

构建产物位于 `build/libs/NoSpawn-1.0.0.jar`。

## 作者与贡献

- 开发者：bentianjia
- 贡献者：ZURAN_

特别感谢 ZURAN_ 提供的设计灵感和宝贵的测试机会。

## 许可证

MIT
