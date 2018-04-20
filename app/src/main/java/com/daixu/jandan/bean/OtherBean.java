package com.daixu.jandan.bean;

import java.util.List;

public class OtherBean {
    public String status;
    public int current_page;
    public int total_comments;
    public int page_count;
    public int count;
    public List<CommentsBean> comments;

    public static class CommentsBean {
        public String comment_ID;
        public String comment_post_ID;
        public String comment_author;
        public String comment_date;
        public String comment_date_gmt;
        public String comment_content;
        public String user_id;
        public String vote_positive;
        public String vote_negative;
        public String sub_comment_count;
        public String text_content;
        public List<String> pics;
    }
}
