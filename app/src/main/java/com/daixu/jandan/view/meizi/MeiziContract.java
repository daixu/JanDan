package com.daixu.jandan.view.meizi;

import com.daixu.jandan.base.BasePresenter;
import com.daixu.jandan.base.BaseView;
import com.daixu.jandan.bean.OtherBean;

public class MeiziContract {

    interface Presenter extends BasePresenter {
        void takeView(MeiziContract.View view);

        void getPic(String api, int pageNum);
    }

    interface View extends BaseView<Presenter> {

        void getPicSuccess(OtherBean resp);

        void getPicFailure();
    }
}
