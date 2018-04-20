package com.daixu.jandan.view.news;

import com.daixu.jandan.base.BasePresenter;
import com.daixu.jandan.base.BaseView;
import com.daixu.jandan.bean.NewsBean;

public class NewsContract {

    interface Presenter extends BasePresenter {
        void takeView(NewsContract.View view);

        void getNews(int pageNum);
    }

    interface View extends BaseView<Presenter> {

        void getNewsSuccess(NewsBean resp);

        void getNewsFailure();
    }
}
