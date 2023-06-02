package example.com.two.mine;

import androidx.appcompat.app.AppCompatActivity;
import example.com.two.LoginActivity;
import example.com.two.MainActivity;
import example.com.two.R;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdatePersonActivity extends AppCompatActivity {
    private static final String BASE_URL = "http:10.10.10.1:8087/";
    private ImageView mAvatar;
    private TextView mAccount;
    private TextView mPassword;
    private TextView mUserName;
    private TextView mUserSex;
    static User user ;
    String account;
    String password;
    String userName;
    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_person);

        mAccount = findViewById(R.id.et_account);
        mPassword = findViewById(R.id.et_password);
        mUserName = findViewById(R.id.et_userName1);
        mUserSex = findViewById(R.id.et_userSex);

        Button button = findViewById(R.id.btn_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里实现按钮被点击时的响应
                // 可以在这里设置启动一个Activity，或者执行一些其他的操作
                 account = (String) mAccount.getText().toString();
                 password = (String) mPassword.getText().toString();
                 userName = (String) mUserName.getText().toString();
                 sex = (String) mUserSex.getText().toString();

                 user = new User(1, account, password, userName, sex);
                String jsonString = JSON.toJSONString(user);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String str = sendPostWithJson("http://10.10.10.1:8087/users", jsonString);
                            JSONObject jsonObject = JSON.parseObject(str);
                                        int code = jsonObject.getIntValue("code");


                            if (200 == code){
                                Intent intent = new Intent(UpdatePersonActivity.this,MainActivity.class);
                                startActivityForResult(intent,1);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                System.out.println("11111");


            }
        });

        //获取用户信息
//        getUserInfo();

    }

    /**
     * HTTP接口-POST方式，请求参数形式为body-json形式
     *
     * @param url
     * @param jsonString
     * @return String
     */
    public static String sendPostWithJson(String url, String jsonString) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
        Request request = new Request.Builder()
                .put(body)
                .url(url)
                .build();
        Call call = client.newCall(request);
        //返回请求结果
        try {
            Response response = call.execute();
            System.out.println("111"+response.body().toString());
            return response.body().string();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }


    public User getUserInfo() {

        new Thread(){
            @Override
            public void run() {

                HttpURLConnection connection=null;
                try {
                    URL url = new URL("http://10.10.10.1:8087/users");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);

                    //设置请求方式 GET / POST 一定要大小
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new IOException("HTTP error code" + responseCode);
                    }
                    String result = getStringByStream(connection.getInputStream());
                    if (result == null) {
                        Log.d("Fail", "失败了");
                    }else{
//                查询成功，数据展示
                        JSONObject json = JSON.parseObject(result);
                        user = JSON.toJavaObject(json.getJSONObject("data"), User.class);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 在主线程中进行UI操作，例如修改TextView的文本
                                mAccount.setText(user.getAccount());
                                mUserName.setText(user.getUsername());
                                mUserSex.setText(user.getSex());
                            }
                        });

                        System.out.println(user.getAccount());
                        System.out.println(user.getUsername());
                        System.out.println(user.getSex());
                        /*mAccount.setText(user.getAccount());
                        mUserName.setText(user.getUserName());
                        mUserSex.setText(user.getSex());*/

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return user;
    }

    //   发送post
    private void postRequest() {
        final OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("id", "1")
                .add("account", account)
                .add("password", password)
                .add("userName", userName)
                .add("sex", sex)
                .build();

        final Request request = new Request.Builder()
                .url("http://10.10.10.1:8087/users")
                .post(body)
                .build();

        Log.d("aa", "请求 URL: ${request.url()}");
        Log.d("aa", "请求方法: ${request.method()}");
        Log.d("aa", "请求头: ${request.headers()}");
        Log.d("aa", "请求体: ${request.body()?.contentType()}");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("WY","打印POST响应的数据：" + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    //    get请求
    private void getRequest() {
        final OkHttpClient client = new OkHttpClient();
        final Request request=new Request.Builder()
                .get()
                .tag(this)
                .url("http://192.168.0.200:8080/login")
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Log.i("WY","打印GET响应的数据：" + response.body().string());
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    private String getStringByStream(InputStream inputStream){
        Reader reader;
        try {
            reader=new InputStreamReader(inputStream,"UTF-8");
            char[] rawBuffer=new char[512];
            StringBuffer buffer=new StringBuffer();
            int length;
            while ((length=reader.read(rawBuffer))!=-1){
                buffer.append(rawBuffer,0,length);
            }
            return buffer.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
