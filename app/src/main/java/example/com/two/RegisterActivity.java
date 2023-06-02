package example.com.two;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;
import example.com.two.utils.SPSave;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioGroup radioGroup;
    private TextView textView;
    private EditText et_account;
    private EditText et_password;
    private EditText et_userName;
    private EditText et_userSex;
    private Button btn_login;
    private Button btn_register;
    String account;
    String password;
    String username;
    String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.boy){
                    sex = "男";
                }else {
                    sex = "女";
                }
            }
        });
    }

    private void initView() {
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        et_userName = findViewById(R.id.et_userName);
        radioGroup = findViewById(R.id.rdg);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                account = et_account.getText().toString().trim();
                password = et_password.getText().toString();
                username = et_userName.getText().toString();
                if (TextUtils.isEmpty(account)){
                    Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this,"请输入昵称",Toast.LENGTH_SHORT).show();
                    return;
                }

//                发送请求
                new Thread(){
                    @Override
                    public void run() {
                        networdRequest();
                    }
                }.start();
                break;
        }
    }

    public void networdRequest(){

        HttpURLConnection connection=null;
        try {
            URL url = new URL("http://10.10.10.1:8087/users/register");
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            //设置请求方式 GET / POST 一定要大写
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.connect();

            JSONObject body = new JSONObject();
            body.put("account", account);
            body.put("password", password);
            body.put("username", username);
            body.put("sex", sex);
            String json = String.valueOf(body);


            OutputStream dos=connection.getOutputStream();
            dos.write(json.getBytes());

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code " + responseCode);
            }
            String result = getStringByStream(connection.getInputStream());
            if (result == null) {
                Log.d("Fail", "失败了");
            }else{
                boolean isSaveSuccess = SPSave.saveUserInfo(this,account,password);

//                登录成功，跳转页面
//                Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivityForResult(intent,1);
                Log.d("succuss", "成功注册 ");
            }
        } catch (Exception e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "注册有误，请检查", Toast.LENGTH_SHORT).show();
                }
            });
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

