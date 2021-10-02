package cn.forgotdream.timelimit.lib;

import cn.forgotdream.timelimit.TimeLimit;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Calendar;
import java.util.Date;

public class TimeCounter extends Thread{
    private boolean enabled = true;
    private int check_time_second = 5;
    private TimeLimit main;
    private int day_of_week;
    public TimeCounter(TimeLimit timeLimit,float last_boot_time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date((long) last_boot_time));
        day_of_week = calendar.get(Calendar.DATE);
        main = timeLimit;
    }
    public void exit(){
        this.enabled = false;
    }

    private void check_date_change(){
        Calendar calendar = Calendar.getInstance();
        if(day_of_week != calendar.get(Calendar.DATE)-1){
            day_of_week = calendar.get(Calendar.DATE)-1;
            main.reset_all();
        }
    }

    @Override
    public void run() {
        while (enabled){
            for (ProxiedPlayer i:
                 main.getProxy().getPlayers()) {
                main.PLAYERS.get(i.getName()).check(main);
            }
            check_date_change();
            try {
                sleep(check_time_second* 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
