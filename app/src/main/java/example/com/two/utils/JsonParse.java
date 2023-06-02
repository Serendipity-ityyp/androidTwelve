package example.com.two.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import example.com.two.index.WeatherInfo;

public class JsonParse {
    private  static  JsonParse instance;
    private JsonParse() {
    }
        public static  JsonParse getInstance() {
            if(instance == null) {
                instance = new JsonParse();
            }
           return instance;
        }
        private String read(InputStream in) {
            BufferedReader reader = null;
            StringBuilder sb = null;
            String line = null;
            try{
                sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(in));
                while((line = reader.readLine()) != null ) {
            sb.append(line);
            sb.append("\n");
                }
            }catch(IOException e) {
                e.printStackTrace();
                return "";
            }finally{
                try{
                    if(in !=null) in.close();
                    if(reader !=null) reader.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }

        return sb.toString();
    }
    public List<WeatherInfo> getInfosFromJson(Context context) {
        List<WeatherInfo> weatherInfos = new ArrayList<>();
        InputStream is = null;
        try{
            is = context.getResources().getAssets().open("weather.json");
            String json = read(is);
            Gson gson = new Gson();
                Type listType = new TypeToken<List<WeatherInfo>>() {
                }.getType();
        List<WeatherInfo> infoList = gson.fromJson(json, listType);
        return infoList;
    } catch  (Exception e) {
    }
    return weatherInfos;
}
}
