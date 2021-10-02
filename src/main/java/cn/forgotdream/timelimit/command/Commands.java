package cn.forgotdream.timelimit.command;

import cn.forgotdream.timelimit.TimeLimit;
import cn.forgotdream.timelimit.lib.Player;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.LinkedList;
import java.util.List;

public class Commands extends Command implements TabExecutor {
    private TimeLimit main;
    public Commands(TimeLimit timeLimit){
        super("timelimit");
        this.main = timeLimit;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new LinkedList<>();
        final boolean commandAuth = sender.hasPermission("timelimit.command.all");
        if(args.length == 0){
            list.add("timelimit");
        }else if (commandAuth && args.length == 1){
            list.add("reset"); // 重置时间
            list.add("reset_all"); // 重置所有人时间
            list.add("add_whitelist"); // 增加白名单
            list.add("del_whitelist"); // 删除白名单
            list.add("query"); // 查询剩余时间
        }else if (args.length == 1){
            list.add("query");
        }
        return list;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        final boolean commandAuth = sender.hasPermission("timelimit.command.all");
        final BaseComponent[] error_args_warn_message = new ComponentBuilder("[TimeLimit]参数不完整或参数错误，请检查你输入的参数").color(ChatColor.YELLOW).create();
        final BaseComponent[] no_permission_warn_message = new ComponentBuilder("[TimeLimit]你没有权限执行这个指令").color(ChatColor.DARK_RED).create();
        final BaseComponent[] not_find_message = new ComponentBuilder("[TimeLimit]未找到玩家 请核实玩家ID").color(ChatColor.DARK_RED).create();
        final BaseComponent[] reset_all_message = new ComponentBuilder("[TimeLimit]已重置所有玩家的游戏时长").color(ChatColor.AQUA).create();
        if(args.length != 0) {
            switch (args[0]) {
                case "reset":
                    if (!commandAuth) {
                        sender.sendMessage(no_permission_warn_message);
                    } else if (commandAuth && args.length > 1) {
                        for (int i = 1; i < args.length; i++) {
                            if (main.PLAYERS.containsKey(args[i])) {
                                main.PLAYERS.get(args[i]).reset(main.configs.TIME_MAX * 1000);
                            } else {
                                sender.sendMessage(not_find_message);
                            }
                        }
                    } else {
                        sender.sendMessage(error_args_warn_message);
                    }
                    break;
                case "reset_all":
                    if (!commandAuth) {
                        sender.sendMessage(no_permission_warn_message);
                    } else if (commandAuth && args.length == 1) {
                        main.reset_all();
                        sender.sendMessage(reset_all_message);
                    } else {
                        sender.sendMessage(error_args_warn_message);
                    }
                    break;
                case "add_whitelist":
                    if (!commandAuth) {
                        sender.sendMessage(no_permission_warn_message);
                    } else if (commandAuth && args.length > 1) {
                        ComponentBuilder add_respond_message = new ComponentBuilder("[TimeLimit]已为以下玩家添加不限时: ").color(ChatColor.AQUA);
                        for (int i = 1; i < args.length; i++) {
                            main.configs.NO_LIMIT_PLAYERS.add(args[i]);
                            if (main.PLAYERS.containsKey(i)) {
                                Player player = main.PLAYERS.get(args[i]);
                                player.set_limited_status(true);
                                player.reset(0);
                                main.PLAYERS.replace(args[i], player);
                            }
                            add_respond_message.append(" " + args[i]).color(ChatColor.YELLOW);
                        }
                        sender.sendMessage(add_respond_message.create());
                    } else {
                        sender.sendMessage(error_args_warn_message);
                    }
                    break;
                case "del_whitelist":
                    if (!commandAuth) {
                        sender.sendMessage(no_permission_warn_message);
                    } else if (commandAuth && args.length > 1) {
                        ComponentBuilder del_respond_message = new ComponentBuilder("[TimeLimit]已为以下玩家删除不限时: ").color(ChatColor.AQUA);
                        for (int i = 1; i < args.length; i++) {
                            if (main.configs.NO_LIMIT_PLAYERS.contains(args[i])) {
                                main.configs.NO_LIMIT_PLAYERS.remove(args[i]);
                            }
                            if (main.PLAYERS.containsKey(args[i])) {
                                Player player = main.PLAYERS.get(args[i]);
                                player.set_limited_status(false);
                                player.reset(main.configs.TIME_MAX * 1000);
                                main.PLAYERS.replace(args[i], player);
                            }
                            del_respond_message.append(" " + args[i]).color(ChatColor.YELLOW);
                        }
                        sender.sendMessage(del_respond_message.create());
                    } else {
                        sender.sendMessage(error_args_warn_message);
                    }
                    break;
                case "query":
                    if (main.PLAYERS.containsKey(sender.getName())) {
                        if (main.configs.NO_LIMIT_PLAYERS.contains(sender.getName())) {
                            sender.sendMessage(new ComponentBuilder("[TimeLimit]您为不限时玩家").color(ChatColor.AQUA).bold(true).create());
                        } else if (!main.configs.IS_ENABLED) {
                            sender.sendMessage(new ComponentBuilder("[TimeLimit]本插件未启用").color(ChatColor.WHITE).bold(true).create());
                        } else {
                            sender.sendMessage(new ComponentBuilder("[TimeLimit]您剩余的游玩时间为: ").color(ChatColor.AQUA).append(String.valueOf(main.PLAYERS.get(sender.getName()).get_left_time())).color(ChatColor.YELLOW).append("s").color(ChatColor.AQUA).create());
                        }
                    } else {
                        sender.sendMessage(error_args_warn_message);
                    }
                    break;
                default:
                    sender.sendMessage(error_args_warn_message);
            }
        }else {
            sender.sendMessage(error_args_warn_message);
        }
    }
}
