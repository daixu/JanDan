package com.daixu.jandan.view.popular;

import com.daixu.jandan.di.ActivityScoped;
import com.daixu.jandan.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class PopularModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PopularFragment popularFragment();

    @ActivityScoped
    @Binds
    abstract PopularContract.Presenter popularPresenter(PopularPresenter presenter);
}
