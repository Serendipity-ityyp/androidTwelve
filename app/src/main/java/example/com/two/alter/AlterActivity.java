package example.com.two.alter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.com.two.R;
import example.com.two.alter.adapter.NotepadAdapter;
import example.com.two.alter.bean.NotepadBean;


public class AlterActivity extends Activity {
    ListView listView;
    List<NotepadBean> list = new ArrayList<>();

    NotepadAdapter adapter;
    ContentResolver resolver ;
    String Rid;
    static Uri uri = Uri.parse("content://example.com.jishiben2/Note");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);
        ImageView add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterActivity.this,
                        RecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        initData();
    }
    protected void initData() {
//        mSQLiteHelper= new SQLiteHelper(this); //创建数据库
        showQueryData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(AlterActivity.this, RecordActivity.class);
                Rid = notepadBean.getId();
                System.out.println("::::"+Rid);

                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); //记录的内容
                AlterActivity.this.startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( AlterActivity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                Rid = notepadBean.getId();
                                System.out.println(Rid);
                                resolver.delete(uri,"id=?",new String[]{""+Rid});
                                list.remove(position);
                                adapter.notifyDataSetChanged();
                                Intent intent = new Intent();
                                intent.setAction("delete");
                                sendBroadcast(intent);
                                Toast.makeText(AlterActivity.this,"删除成功",
                                        Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog =  builder.create();
                dialog.show();
                return true;
            }
        });

    }
    private void showQueryData(){
        if (list!=null){
            list.clear();
        }
        resolver = getContentResolver();
        Cursor cursor = resolver.query(uri,new String[]{"id","content","notetime"},null,null,null);
        while (cursor.moveToNext()){
            NotepadBean notepadBean = new NotepadBean(cursor.getString(0)+"",cursor.getString(1)+"",cursor.getString(2)+"");
            list.add(notepadBean);
        }

        adapter = new NotepadAdapter(this, list);
        cursor.close();
        listView.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            showQueryData();
        }
    }
}
