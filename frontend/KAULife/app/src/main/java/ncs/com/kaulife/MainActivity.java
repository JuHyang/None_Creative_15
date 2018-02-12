package ncs.com.kaulife;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SectionPagerAdapter mSectionsPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitView();
    }

    public void InitView() {
        viewPager = findViewById(R.id.main_pager);
        mSectionsPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
        viewPager.setCurrentItem(0);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }



    public class SectionPagerAdapter extends FragmentStatePagerAdapter {
        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = null;
            Bundle args = null;

            switch (position) {
                case 0:
                    fragment = new LmsFragment(MainActivity.this);
                    args = new Bundle();
                    break;
                case 1:
                    fragment = new GradeNowFragment(MainActivity.this);
                    args = new Bundle();
                    break;
                case 2:
                    fragment = new ScheduleTableFragment(MainActivity.this);
                    args = new Bundle();
                    break;
                case 3:
                    fragment = new ScheduleMajorFragment(MainActivity.this);
                    args = new Bundle();
                    break;
                case 4:
                    fragment = new SettingFragment(MainActivity.this);
                    args = new Bundle();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0 :
                    return "LMS";
                case 1:
                    return "성적";
                case 2:
                    return "시간표";
                case 3:
                    return "시간표 만들기";
                case 4:
                    return"설정";
                default :
                    return null;
            }
        }
    }




}
