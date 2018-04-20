package com.daixu.jandan.bean;

import java.util.List;

public class NewsBean {

    public String status;
    public int count;
    public int count_total;
    public int pages;
    public List<PostsBean> posts;

    public static class PostsBean {
        public int id;
        public String url;
        public String title;
        public String excerpt;
        public String date;
        public AuthorBean author;
        public int comment_count;
        public String comment_status;
        public CustomFieldsBean custom_fields;
        public List<TagsBean> tags;

        public static class AuthorBean {
            public int id;
            public String slug;
            public String name;
            public String first_name;
            public String last_name;
            public String nickname;
            public String url;
            public String description;
        }

        public static class CustomFieldsBean {
            public List<String> thumb_c;
        }

        public static class TagsBean {
            public int id;
            public String slug;
            public String title;
            public String description;
            public int post_count;
        }
    }
}
