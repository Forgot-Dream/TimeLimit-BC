package cn.forgotdream.timelimit;

import cn.forgotdream.timelimit.command.Commands;
import cn.forgotdream.timelimit.file.Configs;
import cn.forgotdream.timelimit.file.Data;
import cn.forgotdream.timelimit.lib.Player;
import cn.forgotdream.timelimit.lib.TimeCounter;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.logging.Level;

public final class TimeLimit extends Plugin implements Listener {
    public HashMap<String, Player> PLAYERS;
    public Configs configs;
    public Data data;
    private TimeCounter timeCounter ;

    @Override
    public void onEnable() {
        configs = new Configs(this);
        data = new Data(this);
        timeCounter = new TimeCounter(this,configs.LAST_BOOT_TIME);
        getProxy().getPluginManager().registerListener(this, this);
        if(configs.IS_ENABLED){
           timeCounter.start();
        }
        getProxy().getLogger().log(Level.INFO,"[TimeLimit]已加载");
        getProxy().getPluginManager().registerCommand(this, new Commands(this));
    }

    @Override
    public void onDisable() {
        data.save();
        timeCounter.exit();
        configs.save_config();
    }

    @EventHandler
    public void on_player_login(PostLoginEvent event){
        String name = event.getPlayer().getName();
        if (PLAYERS.containsKey(name)) {
            if(PLAYERS.get(name).BANNED){
                PLAYERS.get(name).check(this);
            }else {
                PLAYERS.get(name).login();
            }
        }
        else{
            PLAYERS.put(name, new Player(name,configs.TIME_MAX*1000, !configs.NO_LIMIT_PLAYERS.contains(name)));
        }
    }

    @EventHandler
    public void on_player_left(ServerDisconnectEvent event){
        String name = event.getPlayer().getName();
        PLAYERS.get(name).left(this);
    }
    public void reset_all() {
        for (String i : PLAYERS.keySet()) {
            if (PLAYERS.get(i).LIMITED) {
                PLAYERS.get(i).reset(configs.TIME_MAX * 1000);
            }
        }
    }
}
