package example.com.two.mine;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.two.LoginActivity;
import example.com.two.MainActivity;
import example.com.two.R;
import example.com.two.alter.AlterActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MineFragment extends Fragment{

    private static final String BASE_URL = "http:10.10.10.1:8087/";
    private ImageView mAvatar;
    private TextView mAccount;
    private TextView mPassword;
    private TextView mUserName;
    private TextView mUserSex;
    static User user ;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public MineFragment() {

    }


    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        //绑定控件
//        mAccount = view.findViewById(R.id.iv_avator);
        mAccount = view.findViewById(R.id.et_account);
        mUserName = view.findViewById(R.id.et_userName1);
        mUserSex = view.findViewById(R.id.et_userSex);

        Button button = view.findViewById(R.id.btn_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里实现按钮被点击时的响应
                // 可以在这里设置启动一个Activity，或者执行一些其他的操作
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });
        Button btn_update = view.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里实现按钮被点击时的响应
                // 可以在这里设置启动一个Activity，或者执行一些其他的操作
                Intent intent = new Intent(getActivity(), UpdatePersonActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        Button btn_alter = view.findViewById(R.id.btn_alter);
        btn_alter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在这里实现按钮被点击时的响应
                // 可以在这里设置启动一个Activity，或者执行一些其他的操作
                Intent intent = new Intent(getActivity(), AlterActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //获取用户信息
        getUserInfo();

        return view;
    }

    public User getUserInfo() {

        new Thread(){
            @Override
            public void run() {

                HttpURLConnection connection=null;
                try {
                    URL url = new URL("http://10.10.10.1:8087/users/"+LoginActivity.id);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}