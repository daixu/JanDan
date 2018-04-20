package com.daixu.jandan.view.pic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daixu.jandan.R;
import com.daixu.jandan.adapter.PicAdapter;
import com.daixu.jandan.base.BaseFragment;
import com.daixu.jandan.bean.OtherBean;
import com.daixu.jandan.view.img.ImageDetailActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class PicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PicContract.View {

    private SwipeRefreshLayout mRefreshLayout;
    private PicAdapter mAdapter;
    private List<OtherBean.CommentsBean> mList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private int mPage;
    private boolean isEnd;

    private ProgressBar mProgressBar;

    @Inject
    PicPresenter mPresenter;

    public PicFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mRefreshLayout = findViewById(R.id.swipe);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.aid_prompt_color, R.color.colorBackground);

        mProgressBar = findViewById(R.id.progressBar);
        mRecyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void initData() {
        mPage = 1;
        mPresenter.takeView(this);

        mAdapter = new PicAdapter(R.layout.item_pic_list, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                OtherBean.CommentsBean bean = mList.get(position);
                switch (view.getId()) {
                    case R.id.img_max_src: {
                        Intent intent = new Intent(PicFragment.this.getActivity(), ImageDetailActivity.class);
                        if (null != bean && bean.pics.size() > 0) {
                            intent.putStringArrayListExtra("url", (ArrayList<String>) bean.pics);
                            startActivity(intent);
                        }
                    }
                    break;
                    default:
                        break;
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRefreshLayout.setEnabled(false);
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setEnabled(false);
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                            mRefreshLayout.setEnabled(true);
                        } else {
                            //成功获取更多数据
                            mPresenter.getPic("jandan.get_pic_comments", mPage);
                        }
                    }
                }, 200);
            }
        }, mRecyclerView);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPage = 1;
                mPresenter.getPic("jandan.get_pic_comments", mPage);
            }
        }, 200);
    }

    @Override
    protected void lazyLoad() {
        mPage = 1;
        mPresenter.getPic("jandan.get_pic_comments", mPage);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void getPicSuccess(OtherBean resp) {
        Timber.tag("getPicSuccess").e("resp=" + resp);
        hideProgress();
        if (null != resp.comments && resp.comments.size() > 0) {
            if (mPage == 1) {
                mList.clear();
            }
            mList.addAll(resp.comments);
            if (mPage >= resp.comments.size()) {
                isEnd = true;
            } else {
                isEnd = false;
                mPage += 1;
            }
            mAdapter.setNewData(mList);
            mAdapter.loadMoreComplete();
            mRefreshLayout.setEnabled(true);
        }
    }

    @Override
    public void getPicFailure() {
        Timber.tag("getPicSuccess").e("getPicFailure");
        hideProgress();
    }

    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }
}
