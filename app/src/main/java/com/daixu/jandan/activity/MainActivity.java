package com.daixu.jandan.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.daixu.jandan.R;
import com.daixu.jandan.adapter.TabMainAdapter;
import com.daixu.jandan.base.BaseActivity;
import com.daixu.jandan.view.duanzi.DuanziFragment;
import com.daixu.jandan.view.meizi.MeiziFragment;
import com.daixu.jandan.view.news.NewsFragment;
import com.daixu.jandan.view.pic.PicFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] tabNames;
    private Fragment[] fragments;
    private TabMainAdapter mTabMainAdapter;

    private Fragment mNewsFragment;
    private Fragment mPicFragment;
    private Fragment meiziFragment;
    private Fragment mDuanziFragment4;

    private int nowCurrentItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToolbar("煎蛋", false);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.viewpager);
        mNewsFragment = new NewsFragment();
        mPicFragment = new PicFragment();
        meiziFragment = new MeiziFragment();
        mDuanziFragment4 = new DuanziFragment();
        tabNames = new String[]{"新鲜事", "无聊图", "妹子图", "段子"};
        fragments = new Fragment[]{mNewsFragment, mPicFragment, meiziFragment, mDuanziFragment4};
        mTabMainAdapter = new TabMainAdapter(getSupportFragmentManager(), tabNames, fragments);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mTabMainAdapter);
        mViewPager.setOffscreenPageLimit(tabNames.length);
        mViewPager.setCurrentItem(nowCurrentItem);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:

                break;
            case R.id.action_about_me:
                new AlertDialog.Builder(this)
                        .setTitle("daixu")
                        .setMessage("https://github.com/daixu")
                        .setPositiveButton("ok", null)
                        .show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
