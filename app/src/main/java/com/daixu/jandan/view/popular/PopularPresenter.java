package com.daixu.jandan.view.popular;

import com.daixu.jandan.bean.OtherBean;
import com.daixu.jandan.net.BaseSubscriber;
import com.daixu.jandan.net.ExceptionHandle;
import com.daixu.jandan.net.moyu.service.MoyuServer;
import com.daixu.jandan.utils.RxUtil;

import javax.inject.Inject;

import io.reactivex.functions.Predicate;
import timber.log.Timber;

public class PopularPresenter implements PopularContract.Presenter {
    private PopularContract.View mView;
    private MoyuServer moyuServer;

    @Inject
    PopularPresenter(MoyuServer moyuServer) {
        this.moyuServer = moyuServer;
    }

    @Override
    public void takeView(PopularContract.View view) {
        mView = view;
    }

    @Override
    public void getRecent() {
        moyuServer.recent()
                .compose(RxUtil.<OtherBean>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<OtherBean>bindToLife())
                .filter(new Predicate<OtherBean>() {
                    @Override
                    public boolean test(OtherBean newsBean) {
                        return "ok".equals(newsBean.status);
                    }
                })
                .subscribe(new BaseSubscriber<OtherBean>() {
                    @Override
                    protected void hideDialog() {

                    }

                    @Override
                    protected void showDialog() {

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable e) {
                        mView.getRecentFailure();
                    }

                    @Override
                    public void onNext(OtherBean resp) {
                        if (null != resp) {
                            mView.getRecentSuccess(resp);
                        } else {
                            mView.getRecentFailure();
                        }
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        Timber.tag("PicPresenter").e("unSubscribe");
        mView = null;
    }
}
