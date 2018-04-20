package com.daixu.jandan.net.service;

import com.daixu.jandan.bean.NewsBean;
import com.daixu.jandan.bean.OtherBean;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServer {

    @GET("?oxwlxojflwblxbsapi=get_recent_posts&include=url,date,tags,author,title,excerpt,comment_count,comment_status,custom_fields&custom_fields=thumb_c,views&dev=1")
    Flowable<NewsBean> news(@Query("page") int page);

    @GET("/")
    Flowable<OtherBean> other(@Query("oxwlxojflwblxbsapi") String api, @Query("page") int page);
}
