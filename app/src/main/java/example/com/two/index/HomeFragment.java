package example.com.two.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.two.LoginActivity;
import example.com.two.R;
import example.com.two.RegisterActivity;
import example.com.two.chat.ChatRobotActivity;
import example.com.two.utils.JsonParse;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView tvCity, tvWeather, tvTemp, tvWind, tvPm;
    private ImageView ivIcon;
    private List<WeatherInfo> infoList;

    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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


         View view =  inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 在此处添加点击事件的处理逻辑
                System.out.println("1111");
                Intent intent = new Intent(getActivity(),ChatRobotActivity.class);
                startActivityForResult(intent,1);
            }
        });

        infoList = JsonParse.getInstance().getInfosFromJson(getContext());

        tvCity = (TextView) view.findViewById(R.id.tv_city);
        tvWeather = (TextView) view.findViewById(R.id.tv_weather);
        tvTemp = (TextView) view.findViewById(R.id.tv_temp);
        tvWind = (TextView)view. findViewById(R.id.tv_wind);
        tvPm = (TextView) view.findViewById(R.id.tv_pm);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        view.findViewById(R.id.btn_sh).setOnClickListener(this);
        view.findViewById(R.id.btn_bj).setOnClickListener(this);
        view.findViewById(R.id.btn_gz).setOnClickListener(this);

        getCityData("北京");

        return view;
    }

    private void setData(WeatherInfo info) {
        if (info == null) return;
        tvCity.setText(info.getCity());
        tvWeather.setText(info.getWeather());
        tvTemp.setText(info.getTemp());
        tvWind.setText("风力:" + info.getWind());
        tvPm.setText("PM:" + info.getPm());
        if (("晴转多云").equals(info.getWeather())) {
            ivIcon.setImageResource(R.drawable.colud_sun);
        } else if (("多云").equals(info.getWeather())){
            ivIcon.setImageResource (R.drawable.clouds);
        }else if (("晴").equals(info.getWeather())){
            ivIcon.setImageResource (R.drawable.sun);
        }
    }

    private void getCityData(String city) {
        for (WeatherInfo info : infoList) {
            if (info.getCity().equals(city)) {
                setData(info);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sh:
                getCityData("上海");
                break;
            case R.id.btn_bj:
                getCityData("北京");
                break;
            case R.id.btn_gz:
                getCityData("广州");
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}