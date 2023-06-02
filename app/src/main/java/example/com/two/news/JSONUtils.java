package example.com.two.news;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class JSONUtils {
    public static ArrayList<News> parseJson(String jsonData) {
        ArrayList<News> result = new ArrayList<>();
        JSONObject jo = null;
        News news;
        try {
            jo = new JSONObject(jsonData);
            if (jo.getString("reason").equals("success!")) {
                JSONObject jo1 = jo.getJSONObject("result");
                JSONArray ja = jo1.getJSONArray("data");
                for (int i = 0; i < ja.length(); i++) {
                    news = new News();
                    JSONObject obj = ja.getJSONObject(i);
                    news.setTitle(obj.getString("title"));
                    news.setDate(obj.getString("date"));
                    news.setAuthor_name(obj.getString("author_name"));
                    news.setThumbnail_pic_s(obj.getString("thumbnail_pic_s"));
                    news.setUrl(obj.getString("url"));
                    result.add(news);
                }
            }
            return  result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
