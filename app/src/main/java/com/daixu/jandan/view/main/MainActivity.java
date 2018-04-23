package com.daixu.jandan.view.main;

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
import com.daixu.jandan.view.popular.PopularFragment;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> supportFragmentInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToolbar("煎蛋", false);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.viewpager);
        Fragment newsFragment = new NewsFragment();
        Fragment popularFragment = new PopularFragment();
        Fragment picFragment = new PicFragment();
        Fragment meiziFragment = new MeiziFragment();
        Fragment duanziFragment4 = new DuanziFragment();
        String[] tabNames = new String[]{"新鲜事", "流行", "无聊图", "妹子图", "段子"};
        Fragment[] fragments = new Fragment[]{newsFragment, popularFragment, picFragment, meiziFragment, duanziFragment4};
        TabMainAdapter tabMainAdapter = new TabMainAdapter(getSupportFragmentManager(), tabNames, fragments);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(tabMainAdapter);
        viewPager.setOffscreenPageLimit(tabNames.length);
        viewPager.setCurrentItem(0);
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
