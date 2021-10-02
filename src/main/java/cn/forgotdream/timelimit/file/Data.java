package cn.forgotdream.timelimit.file;

import cn.forgotdream.timelimit.TimeLimit;
import cn.forgotdream.timelimit.lib.Player;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.*;
import java.util.HashMap;

public class Data {
    private final File DATA_FILE = new File("./plugins/TimeLimit/data.json");
    private TimeLimit main;
    public Data(TimeLimit timeLimit){
        this.main = timeLimit;
        if(DATA_FILE.exists()){
            read();
        }else {
            try {
                DATA_FILE.createNewFile();
                read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void read() {
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DATA_FILE)));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line=br.readLine()) != null){
                stringBuilder.append(line);
            }
            String json_str = stringBuilder.toString();
            main.PLAYERS = gson.fromJson(json_str,new TypeToken<HashMap<String, Player>>(){}.getType());
            if(main.PLAYERS == null){
                main.PLAYERS = new HashMap<>();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void save(){
        Gson gson = new Gson();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(DATA_FILE);
            String json = gson.toJson(main.PLAYERS);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
