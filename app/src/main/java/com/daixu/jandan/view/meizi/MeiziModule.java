package com.daixu.jandan.view.meizi;

import com.daixu.jandan.di.ActivityScoped;
import com.daixu.jandan.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MeiziModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MeiziFragment meiziFragment();

    @ActivityScoped
    @Binds
    abstract MeiziContract.Presenter meiziPresenter(MeiziPresenter presenter);
}
