package com.daixu.jandan.view.news;

import com.daixu.jandan.bean.NewsBean;
import com.daixu.jandan.net.BaseSubscriber;
import com.daixu.jandan.net.ExceptionHandle;
import com.daixu.jandan.net.service.ApiServer;
import com.daixu.jandan.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.functions.Predicate;
import timber.log.Timber;

public class NewsPresenter implements NewsContract.Presenter {
    private NewsContract.View mView;
    private ApiServer mApiServer;

    @Inject
    NewsPresenter(ApiServer apiServer) {
        mApiServer = apiServer;
    }

    @Override
    public void takeView(NewsContract.View view) {
        mView = view;
    }

    @Override
    public void getNews(int pageNum) {
        mApiServer.news(pageNum)
                .compose(RxUtil.<NewsBean>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<NewsBean>bindToLife())
                .filter(new Predicate<NewsBean>() {
                    @Override
                    public boolean test(NewsBean newsBean) {
                        return "ok".equals(newsBean.status);
                    }
                })
                .subscribe(new BaseSubscriber<NewsBean>() {
                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable e) {
                        mView.getNewsFailure();
                    }

                    @Override
                    public void onNext(NewsBean resp) {
                        if (null != resp) {
                            mView.getNewsSuccess(resp);
                        } else {
                            mView.getNewsFailure();
                        }
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        Timber.tag("NewsPresenter").e("unSubscribe");
        mView = null;
    }
}
