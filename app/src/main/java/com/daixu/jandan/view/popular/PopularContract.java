package com.daixu.jandan.view.popular;

import com.daixu.jandan.base.BasePresenter;
import com.daixu.jandan.base.BaseView;
import com.daixu.jandan.bean.OtherBean;

public class PopularContract {

    interface Presenter extends BasePresenter {
        void takeView(PopularContract.View view);

        void getRecent();
    }

    interface View extends BaseView<Presenter> {

        void getRecentSuccess(OtherBean resp);

        void getRecentFailure();
    }
}
