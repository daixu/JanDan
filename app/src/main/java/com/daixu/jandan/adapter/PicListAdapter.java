package com.daixu.jandan.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daixu.jandan.R;
import com.daixu.jandan.utils.GlideUtil;

import java.util.List;

public class PicListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PicListAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.img_src);
        GlideUtil.load(mContext, item, imageView);
        helper.addOnClickListener(R.id.img_src);
    }
}
