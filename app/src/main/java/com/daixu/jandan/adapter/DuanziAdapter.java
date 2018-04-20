package com.daixu.jandan.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daixu.jandan.R;
import com.daixu.jandan.bean.OtherBean;
import com.daixu.jandan.utils.TimeUtil;

import java.text.ParseException;
import java.util.List;

public class DuanziAdapter extends BaseQuickAdapter<OtherBean.CommentsBean, BaseViewHolder> {

    public DuanziAdapter(int layoutResId, @Nullable List<OtherBean.CommentsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final OtherBean.CommentsBean item) {
        String content = item.text_content;

        TextView tvContent = helper.getView(R.id.tv_content);
        if (TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_content, content);
        }

        helper.setText(R.id.tv_oo, "OO " + item.vote_positive);
        helper.setText(R.id.tv_xx, "XX " + item.vote_negative);
        helper.setText(R.id.tv_comment, "吐槽 " + item.sub_comment_count);
        PicListAdapter adapter = null;

        helper.setText(R.id.tv_author, item.comment_author);
        try {
            helper.setText(R.id.tv_time, TimeUtil.getTimeFormatText(TimeUtil.stringToDate(item.comment_date, "yyyy-MM-dd HH:mm:ss")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}