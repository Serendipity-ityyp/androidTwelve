package example.com.two.notepad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import example.com.two.utils.SQLiteHelper;

import static example.com.two.notepad.ServiceFragment.list;
import static example.com.two.notepad.ServiceFragment.mSQLiteHelper;

public class MyReceiver extends BroadcastReceiver {


//    List<NotepadBean> list;
//    SQLiteHelper mSQLiteHelper;
//    NotepadAdapter adapter;
//    ListView listView;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i("MyReceiver","自定义广播事件接受者，接收到了广播事件");
        Log.i("MyReceiver",intent.getAction());
//        Context context = context.getApplicationContext();
        System.out.println("自定义广播事件接受者，接收到了广播事件");
        Toast.makeText(context, "广播接收到："+intent.getAction(), Toast.LENGTH_SHORT).show();

        /*list = mSQLiteHelper.query();
        adapter = new NotepadAdapter(new ServiceFragment().getContext(), list);
        listView.setAdapter(adapter);*/
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
