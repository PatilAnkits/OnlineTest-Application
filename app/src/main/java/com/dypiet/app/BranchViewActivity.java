package com.dypiet.app;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.dypiet.app.fragment.SESem1Fragment;
import com.dypiet.app.fragment.SESem2Fragment;

public class BranchViewActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);
        String branch = getIntent ().getStringExtra ("Branch");
        String year = getIntent ().getStringExtra ("Year");

        final SESem1Fragment se1 = new SESem1Fragment ();
        final SESem2Fragment se2 = new SESem2Fragment ();

        se1.setData (year,branch);
        se2.setData (year,branch);
        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    se1,
                    se2,
            };
            private final String[] mFragmentNames = new String[] {
                    "SEM-I",
                    "SEM-II"
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager(mViewPager);


    }

}
