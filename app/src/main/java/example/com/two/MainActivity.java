package example.com.two;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import example.com.two.index.HomeFragment;
import example.com.two.mine.MineFragment;
import example.com.two.music.RichFragment;
import example.com.two.news.NewsFragment;
import example.com.two.notepad.MyReceiver;
import example.com.two.notepad.ServiceFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Fragment> fragmentList = new ArrayList<>();
    private MyReceiver receiver = new MyReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("add");
        intentFilter.addAction("delete");
        intentFilter.addAction("update");
        registerReceiver(receiver,intentFilter);

        final ViewPager viewPager = findViewById(R.id.main_vp);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.main_bnv);

        initData();

        MainActivityAdapter adapter = new MainActivityAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
//        页面更改监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.menu_news);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.menu_home);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.menu_service);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.menu_rich);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.menu_mine);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        图标选择监听
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_news:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_home:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_service:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_rich:
                        viewPager.setCurrentItem(3);
                        break;

                    case R.id.menu_mine:
                        viewPager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
    }

    private void initData() {
        NewsFragment newsFragment = NewsFragment.newInstance("新闻", "");
        fragmentList.add(newsFragment);

        HomeFragment homeFragment = HomeFragment.newInstance("天气", "");
        fragmentList.add(homeFragment);


        ServiceFragment serviceFragment = ServiceFragment.newInstance("记事本", "");
        fragmentList.add(serviceFragment);

        RichFragment richFragment = RichFragment.newInstance("音乐", "");
        fragmentList.add(richFragment);

        MineFragment mineFragment = MineFragment.newInstance("个人中心", "");
        fragmentList.add(mineFragment);
    }


}
