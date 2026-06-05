package com.bentianjia.nospawn;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NoSpawnCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION_USE = "nospawn.use";
    private static final String PERMISSION_ADD = "nospawn.add";
    private static final String PERMISSION_REMOVE = "nospawn.remove";
    private static final String PERMISSION_ADMIN = "nospawn.admin";
    private static final String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "NoSpawn" + ChatColor.GRAY + "] ";

    private final NoSpawnConfig config;

    public NoSpawnCommand(NoSpawnConfig config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!hasAnyPermission(sender, PERMISSION_USE, PERMISSION_ADMIN)) {
            sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                    "你没有使用此命令的权限。",
                    "You do not have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            return listDisabled(sender);
        }

        String sub = args[0].toLowerCase(Locale.ROOT);

        if (sub.equals("add")) {
            if (!hasAnyPermission(sender, PERMISSION_ADD, PERMISSION_ADMIN)) {
                sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                        "你没有添加生物类型的权限。",
                        "You do not have permission to add mob types."));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                        "请指定生物类型。",
                        "Please specify a mob type."));
                return true;
            }
            return addType(sender, args[1]);
        }

        if (sub.equals("remove")) {
            if (!hasAnyPermission(sender, PERMISSION_REMOVE, PERMISSION_ADMIN)) {
                sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                        "你没有移除生物类型的权限。",
                        "You do not have permission to remove mob types."));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                        "请指定生物类型。",
                        "Please specify a mob type."));
                return true;
            }
            return removeType(sender, args[1]);
        }

        sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                "未知子命令。使用 /" + label + " [add|remove]",
                "Unknown subcommand. Use /" + label + " [add|remove]"));
        return true;
    }

    private boolean listDisabled(CommandSender sender) {
        List<String> disabled = config.disabledTypeNames();

        if (disabled.isEmpty()) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + msg(sender,
                    "当前没有禁用任何生物生成。",
                    "No mob types are currently disabled."));
            return true;
        }

        String joined = String.join(", ", disabled);
        sender.sendMessage(PREFIX + ChatColor.YELLOW + msg(sender,
                "已禁用生成的生物：" + joined,
                "Disabled mob types: " + joined));
        return true;
    }

    private boolean addType(CommandSender sender, String typeName) {
        String name = NoSpawnConfig.normalizeEntityName(typeName);
        if (name == null) {
            sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                    "未知实体类型：" + typeName,
                    "Unknown entity type: " + typeName));
            return true;
        }

        EntityType type = getEntityType(name);
        if (type == null || !isMobType(type)) {
            sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                    typeName + " 不是生物类型。",
                    typeName + " is not a mob type."));
            return true;
        }

        if (!config.add(name)) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + msg(sender,
                    typeName + " 已经被禁用。",
                    typeName + " is already disabled."));
            return true;
        }

        sender.sendMessage(PREFIX + ChatColor.GREEN + msg(sender,
                "已禁用 " + name + " 的生成。",
                "Disabled spawning for " + name + "."));
        return true;
    }

    private boolean removeType(CommandSender sender, String typeName) {
        String name = NoSpawnConfig.normalizeEntityName(typeName);
        if (name == null) {
            sender.sendMessage(PREFIX + ChatColor.RED + msg(sender,
                    "未知实体类型：" + typeName,
                    "Unknown entity type: " + typeName));
            return true;
        }

        if (!config.remove(name)) {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + msg(sender,
                    typeName + " 没有被禁用。",
                    typeName + " is not disabled."));
            return true;
        }

        sender.sendMessage(PREFIX + ChatColor.GREEN + msg(sender,
                "已允许 " + name + " 生成。",
                "Enabled spawning for " + name + "."));
        return true;
    }

    private EntityType getEntityType(String name) {
        try {
            return EntityType.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private boolean isMobType(EntityType type) {
        Class<? extends org.bukkit.entity.Entity> entityClass = type.getEntityClass();
        if (entityClass == null) {
            return false;
        }
        return org.bukkit.entity.Mob.class.isAssignableFrom(entityClass);
    }

    private boolean hasAnyPermission(CommandSender sender, String... permissions) {
        for (String perm : permissions) {
            if (sender.hasPermission(perm)) {
                return true;
            }
        }
        return false;
    }

    private String msg(CommandSender sender, String zhCn, String enUs) {
        return prefersChinese(sender) ? zhCn : enUs;
    }

    private boolean prefersChinese(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String locale = player.getLocale();
            return locale != null && locale.toLowerCase(Locale.ROOT).startsWith("zh");
        }
        return Locale.getDefault().toLanguageTag().toLowerCase(Locale.ROOT).startsWith("zh");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String partial = args[0].toLowerCase(Locale.ROOT);
            List<String> completions = new ArrayList<>();
            if (hasAnyPermission(sender, PERMISSION_ADD, PERMISSION_ADMIN) && "add".startsWith(partial)) {
                completions.add("add");
            }
            if (hasAnyPermission(sender, PERMISSION_REMOVE, PERMISSION_ADMIN) && "remove".startsWith(partial)) {
                completions.add("remove");
            }
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("add")) {
            if (!hasAnyPermission(sender, PERMISSION_ADD, PERMISSION_ADMIN)) {
                return List.of();
            }
            String partial = args[1].toLowerCase(Locale.ROOT);
            return Arrays.stream(EntityType.values())
                    .filter(this::isMobType)
                    .map(t -> t.name().toLowerCase(Locale.ROOT))
                    .filter(name -> name.startsWith(partial))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            if (!hasAnyPermission(sender, PERMISSION_REMOVE, PERMISSION_ADMIN)) {
                return List.of();
            }
            String partial = args[1].toLowerCase(Locale.ROOT);
            return config.disabledTypeNames().stream()
                    .filter(name -> name.startsWith(partial))
                    .collect(Collectors.toList());
        }

        return List.of();
    }
}
