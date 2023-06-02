package example.com.two.mine;

import android.icu.text.IDNA;

import example.com.two.utils.Data;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    //get请求
    @GET("api/rand.music")
    Call<Data<IDNA.Info>> getJsonData(@Query("sort") String sort, @Query("format") String format);
}
