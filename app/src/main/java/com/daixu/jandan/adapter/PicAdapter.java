package com.daixu.jandan.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.daixu.jandan.R;
import com.daixu.jandan.bean.OtherBean;
import com.daixu.jandan.utils.GlideUtil;
import com.daixu.jandan.utils.TimeUtil;
import com.daixu.jandan.view.img.ImageDetailActivity;
import com.daixu.jandan.widget.DividerGridItemDecoration;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class PicAdapter extends BaseQuickAdapter<OtherBean.CommentsBean, BaseViewHolder> {

    public PicAdapter(int layoutResId, @Nullable List<OtherBean.CommentsBean> data) {
        super(layoutResId, data);
    }

    private String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern pattern = compile("\\s*|\t|\r|\n");
            Matcher m = pattern.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    @Override
    protected void convert(BaseViewHolder helper, final OtherBean.CommentsBean item) {
        String content = item.text_content;
        content = replaceBlank(content);

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
        ImageView imageView = helper.getView(R.id.img_max_src);
        RecyclerView recyclerView = helper.getView(R.id.recycler_image);
        List<String> pics = item.pics;
        PicListAdapter adapter = null;

        if (null != pics) {
            if (pics.size() > 0) {
                if (pics.size() == 1) {
                    imageView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    String picUrl = pics.get(0);
                    GlideUtil.loadPicture(mContext, picUrl, imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    adapter = new PicListAdapter(R.layout.item_picture_list, pics);
                    GridLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 3);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.addItemDecoration(new DividerGridItemDecoration(10, ContextCompat.getColor(mContext, R.color.white)));
                    recyclerView.setAdapter(adapter);
                }
            } else {
                imageView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }

            helper.addOnClickListener(R.id.img_max_src);
        } else {
            imageView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        helper.setText(R.id.tv_author, item.comment_author);
        try {
            helper.setText(R.id.tv_time, TimeUtil.getTimeFormatText(TimeUtil.stringToDate(item.comment_date, "yyyy-MM-dd HH:mm:ss")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (null != adapter) {
            adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    OtherBean.CommentsBean bean = item;
                    switch (view.getId()) {
                        case R.id.img_src: {
                            Intent intent = new Intent(mContext, ImageDetailActivity.class);
                            if (bean.pics.size() > 0) {
                                intent.putStringArrayListExtra("url", (ArrayList<String>) bean.pics);
                                intent.putExtra("position", position);
                                mContext.startActivity(intent);
                            }
                        }
                        break;
                        default:
                            break;
                    }
                }
            });
        }
    }
}