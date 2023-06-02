package example.com.two.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.two.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    private ListView listView;  //定义listview
    private ArrayList<News> data=new ArrayList<>();
    private NewsAdapter adapter;
    private boolean isLoading=true,isDown=false;
    //**************************************
    private static String URL = "http://v.juhe.cn/toutiao/index?type=top&key=a1a755458cc22f129942b34904feb820";
    private String citySubing;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public NewsFragment() {

    }


    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
        listView= (ListView) view.findViewById(R.id.listview);  //绑定
        //**********************添加带有底部视图**************************************
        /*View view1= LayoutInflater.from(getContext()).inflate(R.layout.footer_layout,null);
        listView.addFooterView(view1);*/

        loadData(URL);   //将列表项的数据加载到数据源（顺序表）中
        System.out.println("1111"+data);
        adapter=new NewsAdapter(getActivity(), R.layout.new_item_layout,data);
        System.out.println("2222");
        listView.setAdapter(adapter);

        //添加列表的滚动事件
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if(isDown==true&&scrollState==SCROLL_STATE_IDLE){
                    loadData(URL);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i+i1==i2){
                    isDown=true;
                }else{
                    isDown=false;
                }
            }
        });

        return view;
    }

    private void loadData(String URL) {
        if (isLoading) {
            isLoading = false;
            new NewsTask(new NewsTask.NewsCallBack() {
                @Override
                public void getResults(ArrayList<News> result) {//重写接接口方法
                    data.clear();
                    data.addAll(result);
                    adapter.notifyDataSetChanged();
                }
            }).execute(URL);
            isLoading = true;
        }

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}