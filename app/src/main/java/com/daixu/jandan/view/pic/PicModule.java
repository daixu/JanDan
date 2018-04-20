package com.daixu.jandan.view.pic;

import com.daixu.jandan.di.ActivityScoped;
import com.daixu.jandan.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PicModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PicFragment picFragment();

    @ActivityScoped
    @Binds
    abstract PicContract.Presenter picPresenter(PicPresenter presenter);
}
