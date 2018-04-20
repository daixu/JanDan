package com.daixu.jandan.di;

import com.daixu.jandan.activity.MainActivity;
import com.daixu.jandan.view.duanzi.DuanziModule;
import com.daixu.jandan.view.meizi.MeiziModule;
import com.daixu.jandan.view.news.NewsModule;
import com.daixu.jandan.view.pic.PicModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = {NewsModule.class, PicModule.class, MeiziModule.class, DuanziModule.class})
    abstract MainActivity mainActivity();

}