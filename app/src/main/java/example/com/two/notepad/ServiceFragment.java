package example.com.two.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import example.com.two.R;
import example.com.two.utils.SQLiteHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ServiceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public static ListView listView;
    public static List<NotepadBean> list;
    public static SQLiteHelper mSQLiteHelper;
    public static NotepadAdapter adapter;

    public ServiceFragment() {

    }


    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
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
        View view =  inflater.inflate(R.layout.fragment_service, container, false);


        //用于显示便签的列表
        listView = (ListView) view.findViewById(R.id.listview);
        ImageView add = (ImageView) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        mSQLiteHelper= new SQLiteHelper(getContext()); //创建数据库

//        showQueryData();
        if (list!=null){
            list.clear();
        }
        //从数据库中查询数据(保存的标签)
        list = mSQLiteHelper.query();
        adapter = new NotepadAdapter(getContext(), list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); //记录的内容
                startActivityForResult(intent, 1);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder( getContext())
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NotepadBean notepadBean = list.get(position);
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){
                                    list.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getContext(),"删除成功",
                                            Toast.LENGTH_SHORT).show();
                                }
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



        return view;
    }

    @Override
    public  void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            if (list!=null){
                list.clear();
            }
            //从数据库中查询数据(保存的标签)
            list = mSQLiteHelper.query();
            adapter = new NotepadAdapter(getContext(), list);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}