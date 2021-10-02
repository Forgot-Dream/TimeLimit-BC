package cn.forgotdream.timelimit.file;

import cn.forgotdream.timelimit.TimeLimit;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.logging.Level;

public class Configs {
    private final File Config_FILE = new File("./plugins/TimeLimit/config.yml");
    private final File Config_DIR = new File("./plugins/TimeLimit");
    private TimeLimit main;
    public boolean IS_ENABLED = false;
    public float TIME_MAX = 0;
    public List<String> NO_LIMIT_PLAYERS;
    public Float LAST_BOOT_TIME;

    public Configs(TimeLimit timeLimit){
        this.main = timeLimit;
        load_file();
    }

    private void load_file() {
        if (Config_DIR.isDirectory() && Config_FILE.isFile()) {
            load_config();
            main.getProxy().getLogger().log(Level.INFO,"[TimeLimit]配置文件已加载");
        } else {
            try {
                Config_DIR.mkdirs();
                Config_FILE.createNewFile();
                InputStream src = Configs.class.getClassLoader().getResourceAsStream("config.yml");
                RandomAccessFile randomAccessFile = new RandomAccessFile(Config_FILE.getPath(), "rw");
                byte[] data = new byte[1024 * 10];
                int len = 0;
                while (true) {
                    assert src != null;
                    if ((len = src.read(data)) == -1) break;
                    randomAccessFile.write(data, 0, len);
                }
                src.close();
                load_config();
                main.getProxy().getLogger().log(Level.INFO,"[TimeLimit]未找到配置文件，已生成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void load_config(){
        Configuration configuration;
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(Config_FILE);
            this.IS_ENABLED = configuration.getBoolean("enabled");
            this.TIME_MAX = configuration.getFloat("max_time");
            this.NO_LIMIT_PLAYERS = configuration.getStringList("no_limited_players");
            this.LAST_BOOT_TIME = configuration.getFloat("last_boot_time");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void save_config(){
        Configuration configuration = new Configuration();
        configuration.set("last_boot_time",System.currentTimeMillis());
        configuration.set("enabled",this.IS_ENABLED);
        configuration.set("max_time",(int)this.TIME_MAX);
        configuration.set("no_limited_players",this.NO_LIMIT_PLAYERS);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration,Config_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
