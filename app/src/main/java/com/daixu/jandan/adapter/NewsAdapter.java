package com.daixu.jandan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daixu.jandan.R;
import com.daixu.jandan.bean.NewsBean;
import com.daixu.jandan.utils.GlideUtil;
import com.daixu.jandan.utils.TimeUtil;

import java.text.ParseException;
import java.util.List;

public class NewsAdapter extends BaseQuickAdapter<NewsBean.PostsBean, BaseViewHolder> {

    public NewsAdapter(int layoutResId, @Nullable List<NewsBean.PostsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NewsBean.PostsBean item) {
        helper.setText(R.id.tv_title, item.title);

        ImageView imageView = helper.getView(R.id.img_icon);
        GlideUtil.load(mContext, item.custom_fields.thumb_c.get(0), imageView);

        helper.setText(R.id.tv_author, item.author.nickname);
        try {
            helper.setText(R.id.tv_time, TimeUtil.getTimeFormatText(TimeUtil.stringToDate(item.date, "yyyy-MM-dd HH:mm:ss")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}