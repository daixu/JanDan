package com.daixu.jandan.view.duanzi;

import com.daixu.jandan.di.ActivityScoped;
import com.daixu.jandan.di.FragmentScoped;
import com.daixu.jandan.view.meizi.MeiziContract;
import com.daixu.jandan.view.meizi.MeiziFragment;
import com.daixu.jandan.view.meizi.MeiziPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DuanziModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract DuanziFragment duanziFragment();

    @ActivityScoped
    @Binds
    abstract DuanziContract.Presenter duanziPresenter(DuanziPresenter presenter);
}
