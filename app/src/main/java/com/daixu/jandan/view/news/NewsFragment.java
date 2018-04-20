package com.daixu.jandan.view.news;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daixu.jandan.R;
import com.daixu.jandan.adapter.NewsAdapter;
import com.daixu.jandan.base.BaseFragment;
import com.daixu.jandan.bean.NewsBean;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsContract.View {

    private SwipeRefreshLayout mRefreshLayout;
    private NewsAdapter mAdapter;
    private List<NewsBean.PostsBean> mList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private int mPage;
    private boolean isEnd;

    private ProgressBar mProgressBar;

    @Inject
    NewsPresenter mPresenter;

    public NewsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
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

        mAdapter = new NewsAdapter(R.layout.item_news_list, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        if (null != this.getActivity()) {
            Paint paint = new Paint();
            paint.setStrokeWidth(1);
            paint.setColor(ContextCompat.getColor(this.getActivity(), R.color.line_color));
            paint.setAntiAlias(true);
            mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this.getActivity()).paint(paint).build());
        }

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
                            mPresenter.getNews(mPage);
                        }
                    }
                }, 200);
            }
        }, mRecyclerView);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void lazyLoad() {
        mPresenter.getNews(mPage);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPage = 1;
                mPresenter.getNews(mPage);
            }
        }, 200);
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
    public void getNewsSuccess(NewsBean resp) {
        Timber.tag("getNewsSuccess").e("resp=" + resp);
        hideProgress();
        if (null != resp.posts && resp.posts.size() > 0) {
            if (mPage == 1) {
                mList.clear();
            }
            mList.addAll(resp.posts);
            if (mPage >= resp.posts.size()) {
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
    public void getNewsFailure() {
        hideProgress();
        Timber.tag("NewsFragment").e("getNewsFailure");
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
