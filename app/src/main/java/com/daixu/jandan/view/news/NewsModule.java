package com.daixu.jandan.view.news;

import com.daixu.jandan.di.ActivityScoped;
import com.daixu.jandan.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NewsModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NewsFragment newsFragment();

    @ActivityScoped
    @Binds
    abstract NewsContract.Presenter newsPresenter(NewsPresenter presenter);
}
