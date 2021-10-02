package cn.forgotdream.timelimit.lib;

import cn.forgotdream.timelimit.TimeLimit;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.math.BigDecimal;

public class Player {
    public String NAME;//玩家名称
    public BigDecimal LOGIN_TIME;//登录时间
    public float LEFT_TIME;//剩余时间 ms
    public boolean LIMITED;//是否被限制 false -> 无限制
    public boolean BANNED = false;//是否被禁止登录游戏 false -> 否
    public Player(String name, float left_time, boolean limited){
        this.NAME = name;
        this.LOGIN_TIME = BigDecimal.valueOf(System.currentTimeMillis());
        this.LEFT_TIME = left_time;
        this.LIMITED = limited;
    }
    public void login(){
        this.LOGIN_TIME = BigDecimal.valueOf(System.currentTimeMillis());
    }
    public void left(TimeLimit main){
        if(LIMITED && main.configs.IS_ENABLED) {
            this.LEFT_TIME = LEFT_TIME - BigDecimal.valueOf(System.currentTimeMillis()).subtract(LOGIN_TIME).floatValue();
        }
    }
    public void reset(float left_time){
        this.BANNED = false;
        this.LEFT_TIME = left_time;
        login();
    }
    public void set_limited_status(boolean status){
        this.LIMITED = status;
    }
    public int get_left_time(){
        return -BigDecimal.valueOf(System.currentTimeMillis()).subtract(LOGIN_TIME.add(BigDecimal.valueOf(LEFT_TIME))).intValue()/1000;
    }
    public void check(TimeLimit main){
        if(LIMITED && BigDecimal.valueOf(System.currentTimeMillis()).compareTo(BigDecimal.valueOf(LEFT_TIME).add(LOGIN_TIME)) > 0){
            TextComponent message = new TextComponent("[TimeLimit]\n您已经超时游戏\n如果您是成年人 请联系管理员添加白名单");
            message.setColor(ChatColor.DARK_RED);
            message.setBold(true);
            main.getProxy().getPlayer(NAME).disconnect(message);
            BANNED = true;
        }else if(LIMITED && get_left_time() < 60){
            TextComponent message = new TextComponent("[TimeLimit]您还有" + get_left_time() + "秒游玩时间");
            message.setColor(ChatColor.AQUA);
            main.getProxy().getPlayer(NAME).sendMessage(message);
        }
    }
}
