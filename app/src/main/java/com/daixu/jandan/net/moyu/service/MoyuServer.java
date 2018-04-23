package com.daixu.jandan.net.moyu.service;

import com.daixu.jandan.bean.OtherBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MoyuServer {

    @GET("jandan/hot?category=recent")
    Flowable<OtherBean> recent();
}
