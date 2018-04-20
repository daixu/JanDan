package com.daixu.jandan.view.duanzi;

import com.daixu.jandan.bean.OtherBean;
import com.daixu.jandan.net.BaseSubscriber;
import com.daixu.jandan.net.ExceptionHandle;
import com.daixu.jandan.net.service.ApiServer;
import com.daixu.jandan.utils.RxUtil;
import com.daixu.jandan.view.meizi.MeiziContract;

import javax.inject.Inject;

import io.reactivex.functions.Predicate;
import timber.log.Timber;

public class DuanziPresenter implements DuanziContract.Presenter {
    private DuanziContract.View mView;
    private ApiServer mApiServer;

    @Inject
    DuanziPresenter(ApiServer apiServer) {
        mApiServer = apiServer;
    }

    @Override
    public void takeView(DuanziContract.View view) {
        mView = view;
    }

    @Override
    public void getPic(String api, int pageNum) {
        mApiServer.other(api, pageNum)
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
                        mView.getPicFailure();
                    }

                    @Override
                    public void onNext(OtherBean resp) {
                        if (null != resp) {
                            mView.getPicSuccess(resp);
                        } else {
                            mView.getPicFailure();
                        }
                    }
                });
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        Timber.tag("DuanziPresenter").e("unSubscribe");
        mView = null;
    }
}
