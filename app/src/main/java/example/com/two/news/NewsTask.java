package example.com.two.news;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class NewsTask  extends AsyncTask<String,Void,ArrayList<News>> {
    private NewsCallBack  newsCallBack;
    public NewsTask(NewsCallBack newsCallBack) {
        this.newsCallBack = newsCallBack;
    }
    @Override
    protected ArrayList<News> doInBackground(String... strings) {
        //************************访问网络获取数据，得到列表项的数据*****************
        ArrayList<News> result=null;  //创建arraylist ,接收数据

        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(strings[0]).build();
        try {
            Response response=client.newCall(request).execute();
            String jsonData=response.body().string();
            result=  JSONUtils.parseJson(jsonData);   //调用,返回Arraylist

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<News> result) {
        //在主线程中执行
        if(newsCallBack!=null)
            newsCallBack.getResults(result);
        super.onPostExecute(result);
    }


    @Override
    protected void onPreExecute() {
        //在主线程执行
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        //做进度显示的操作
        super.onProgressUpdate(values);
    }
    //定义接口
    public interface NewsCallBack{
        void getResults(ArrayList<News> result);
    }
}
