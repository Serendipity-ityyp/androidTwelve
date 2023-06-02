package example.com.two;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import example.com.two.utils.SPSave;


public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText et_account;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;
    public static String account;
    public static int id;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Map<String,String> userInfo = SPSave.getUserInfo(this);
        if (userInfo != null){
            et_account.setText(userInfo.get("account"));
            et_password.setText(userInfo.get("password"));
        }

    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                account = et_account.getText().toString().trim();
                password = et_password.getText().toString();
                if (TextUtils.isEmpty(account)){
                    Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
                boolean isSaveSuccess = SPSave.saveUserInfo(this,account,password);
                if (isSaveSuccess){
                    Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this,"保存失败",Toast.LENGTH_SHORT).show();
                }

//                发送请求
                new Thread(){
                    @Override
                    public void run() {

                        networdRequest();
                    }
                }.start();
                break;
            case R.id.btn_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
                break;

        }
    }

    public void networdRequest(){

        HttpURLConnection connection=null;
        try {
            URL url = new URL("http://10.10.10.1:8087/users/login?account="+account+"&password="+password);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            //设置请求方式 GET / POST 一定要大小
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code" + responseCode);
            }
            String result = getStringByStream(connection.getInputStream());
//            System.out.println(result);
            JSONObject jsonString = new JSONObject(result);
            JSONObject data = jsonString.getJSONObject("data");
            int code = jsonString.getInt("code");
             id = data.getInt("id");

            if (code != 200) {
                Log.d("Fail", "失败了");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "账号密码有误，请检查", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
//                登录成功，跳转页面
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivityForResult(intent,1);
                Log.d("succuss", "成功登录 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

