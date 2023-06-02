package example.com.two.news;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class ImageTask extends AsyncTask<String,Void,Bitmap> {
    private CallBack back;
    public ImageTask(CallBack back) {
        this.back = back;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap result=null;
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(strings[0]).build();
        try {
            Response response=client.newCall(request).execute();
            byte[]imageData= response.body().bytes() ;
            result= Thumbnail.getThumbnail(imageData,400,500);  //调用工具类

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(back!=null){
            back.getResults(bitmap);
        }
    }
    public interface  CallBack{
        void getResults(Bitmap result);
    }
}

