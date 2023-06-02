package example.com.two.alter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import example.com.two.R;
import example.com.two.alter.utils.DBUtils;


public class RecordActivity extends Activity implements View.OnClickListener {
    ImageView note_back;
    TextView note_time;
    EditText content;
    ImageView delete;
    ImageView note_save;
    TextView noteName;
    String id;
    ContentResolver resolver ;
    static Uri uri = Uri.parse("content://example.com.jishiben2/Note");
    ContentValues values = new ContentValues();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        note_back = findViewById(R.id.note_back);
        note_time = findViewById(R.id.tv_time);
        content = findViewById(R.id.note_content);
        delete = findViewById(R.id.delete);
        note_save = findViewById(R.id.note_save);
        noteName = findViewById(R.id.note_name);
        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);
        initData();
    }
    protected void initData() {
        noteName.setText("添加记录");
        Intent intent = getIntent();
        if(intent!= null){
            id = intent.getStringExtra("id");
            if (id != null){
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onClick(View v) {
        resolver = getContentResolver();
        switch (v.getId()) {
            case R.id.note_back:
                finish();
                break;
            case R.id.delete:
                System.out.println("22222");
                content.setText("");
                resolver.delete(uri,"id=?",new String[]{""+id});
                break;
            case R.id.note_save:
                String noteContent=content.getText().toString().trim();
                if (id != null){//修改操作
                    if (noteContent.length()>0){
                        values.put("content",noteContent);
                        values.put("notetime",DBUtils.getTime());
                        resolver.update(uri,values,"id=?",new String[]{""+id});
                        Intent intent = new Intent();
                        intent.setAction("update");
                        sendBroadcast(intent);

                        showToast("修改成功");
                        setResult(2);
                        finish();
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }else {
                    //向数据库中添加数据
                    if (noteContent.length()>0){
                        values.put("content",noteContent);
                        values.put("notetime",DBUtils.getTime());
                        Uri newuri = resolver.insert(uri,values);
                        //发送广播
                        Intent intent = new Intent();
                        intent.setAction("add");
                        sendBroadcast(intent);

                        showToast("保存成功");
                        setResult(2);
                        finish();
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }
                break;
        }
    }
    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
