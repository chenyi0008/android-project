package com.example.code07.entity;

import android.content.Context;
import android.provider.BaseColumns;

public class NewsContract {
    private NewsContract() {
        // 私有构造函数，防止实例化
    }

    public static class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "tbl_news"; // 表名
        public static final String COLUMN_NAME_TITLE = "title"; // 标题列名
        public static final String COLUMN_NAME_AUTHOR = "author"; // 作者列名
        public static final String COLUMN_NAME_CONTENT = "content"; // 内容列名
        public static final String COLUMN_NAME_IMAGE = "image"; // 图片列名
    }
}
