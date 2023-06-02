package example.com.two.chat;

import androidx.appcompat.app.AppCompatActivity;
import example.com.two.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatRobotActivity extends AppCompatActivity {
    private ChatAdapter adapter;
    //存放所有的聊天数据
    private List<ChatBean> chatBeanList;
    private EditText et_send_msg;
    //接口地址
    private static final String WEB_SITE = "http://api.qingyunke.com/api.php";
    private static final String KEY = "free";
    private String sendMsg;   //发送的信息
    private String[] welcome; //存储欢迎信息
    private ChatRobotActivity.MHandler mHandler;
    public static final int MSG_OK = 1;//获取数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_robot);

        chatBeanList = new ArrayList<>();
        mHandler = new ChatRobotActivity.MHandler();
        //获取内置欢迎信息
        welcome = getResources().getStringArray(R.array.welcome);
        initView();//初始化界面控件
    }

    private void initView() {
        ListView listView = findViewById(R.id.list);
        et_send_msg = findViewById(R.id.et_send_msg);
        Button btn_send = findViewById(R.id.btn_send);
        adapter = new ChatAdapter(chatBeanList, this);
        listView.setAdapter(adapter);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(); //点击发送按钮，发送信息
            }
        });
        et_send_msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    sendData();//点击Enter键也可以发送信息
                }
                return false;
            }
        });
        int position = (int) (Math.random() * welcome.length - 1); //获取一个随机数
        showData(welcome[position]); //用随机数获取机器人的首次聊天信息
    }

    /**
     * 获取你输入的信息
     */
    private void sendData() {
        sendMsg = et_send_msg.getText().toString(); //获取你输入的信息
        if (TextUtils.isEmpty(sendMsg)) {
            Toast.makeText(this, "您还未输入任何信息哦", Toast.LENGTH_SHORT).show();
            return;
        }
        et_send_msg.setText("");
        //替换空格和换行
        sendMsg = sendMsg.replaceAll(" ", "").replaceAll("\n", "").trim();
        ChatBean chatBean = new ChatBean();
        chatBean.setMessage(sendMsg);
        chatBean.setState(chatBean.SEND);//SEND表示自己发送的信息
        chatBeanList.add(chatBean);      //将发送的信息添加到chatBeanList中
        adapter.notifyDataSetChanged();  //更新ListView列表
        getDataFromServer();             //从服务器获取机器人发送的信息
    }

    private void getDataFromServer() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(WEB_SITE + "?key=" + KEY + "&appid=0&msg="
                + sendMsg).build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException e) {
            }
        });
    }

    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (msg.what == MSG_OK) {
                if (msg.obj != null) {
                    String vlResult = (String) msg.obj;
                    paresData(vlResult);
                }
            }
        }
    }

    /**
     * Json解析
     *
     * @param vlResult
     */
    private void paresData(String vlResult) {
        try {
            JSONObject jsonObject = new JSONObject(vlResult);
            System.out.println(jsonObject);
            //获取机器人的信息
//            int code = jsonObject.getInt("result");
            int code = 40006;
            String content = jsonObject.getString("content");
            showData(content);
//            updateView(code, content);
        } catch (JSONException e) {
            e.printStackTrace();
            showData("主人，你的网络不好哦");
        }
    }

    private void showData(String s) {
        ChatBean chatBean = new ChatBean();
        chatBean.setMessage(s);
        chatBean.setState(ChatBean.RECEIVE);
        chatBeanList.add(chatBean);
        adapter.notifyDataSetChanged();
    }

    private void updateView(int code, String content) {
        //code有很多种状，在此只例举几种，如果想了解更多，请参考官网http://www.tuling123.com
        switch (code) {
            case 4004:
                showData("主人，今天我累了，我要休息了，明天再来找我耍吧");
                break;
            case 40005:
                showData("主人，你说的是外星语吗？");
                break;
            case 40006:
                showData("主人，我今天要去约会哦，暂不接客啦");
                break;
            case 40007:
                showData("主人，明天再和你耍啦，我生病了，呜呜......");
                break;
            default:
                showData(content);
                break;
        }
    }

    protected long exitTime; // 记录第一次点击时的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(ChatRobotActivity.this, "再按一次退出智能聊天程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ChatRobotActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
