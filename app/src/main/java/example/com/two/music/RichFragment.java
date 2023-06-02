package example.com.two.music;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import example.com.two.R;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.*;

public class RichFragment extends Fragment implements View.OnClickListener{


    private static SeekBar sb;
    private static TextView tv_progress,tv_total;
    private ObjectAnimator animator;
    private MusicService.MusicControl musicControl;
    MyServiceConn conn;
    Intent intent;
    private boolean isUnbind = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public RichFragment() {

    }


    public static RichFragment newInstance(String param1, String param2) {
        RichFragment fragment = new RichFragment();
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
        View view =  inflater.inflate(R.layout.fragment_rich, container, false);

        tv_progress = (TextView) view.findViewById(R.id.tv_progress);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        sb = (SeekBar) view.findViewById(R.id.sb);
        view.findViewById(R.id.btn_play).setOnClickListener(this);
        view.findViewById(R.id.btn_pause).setOnClickListener(this);
        view.findViewById(R.id.btn_continue_play).setOnClickListener(this);
        view.findViewById(R.id.btn_exit).setOnClickListener(this);
        intent = new Intent(getActivity(),MusicService.class);
        conn = new MyServiceConn();
        getActivity().bindService(intent,conn,BIND_AUTO_CREATE);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == seekBar.getMax()){
                    animator.pause();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                musicControl.seekTo(progress);
            }
        });
        ImageView iv_music = view.findViewById(R.id.iv_music);
        animator = ObjectAnimator.ofFloat(iv_music,"rotation", 0f,360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void unbind(boolean isUnbind){
        if (!isUnbind ){
            musicControl.pausePlay();
            getActivity().unbindService(conn);
            getActivity().stopService(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                musicControl.play();
                animator.start();
                break;
            case R.id.btn_pause:
                musicControl.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue_play:
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:
                unbind(isUnbind);
                isUnbind = true;
                getActivity().finish();
                break;
        }
    }
//        @Override
    protected void onDestory(){
        super.onDestroy();
        unbind(isUnbind);
    }



    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPosition);
            int minute = duration/1000/60;
            int second = duration/1000%60;
            String strMinute = null;
            String strSecond = null;
            if(minute<10){
                strMinute = "0"+minute;
            }else{
                strMinute = minute+"";
            }
            if (second<10){
                strSecond = "" +second;
            }else {
                strSecond = second+"";
            }
            tv_total.setText(strMinute+":"+strSecond);
            minute = currentPosition/1000/60;
            second = currentPosition/1000%60;
            if(minute<10){
                strMinute = "0"+minute;
            }else{
                strMinute = minute+"";
            }
            if (second<10){
                strSecond = "" +second;
            }else {
                strSecond = second+"";
            }
            tv_progress.setText(strMinute+":"+strSecond);
        }
    };


    class MyServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println(service);
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }


}