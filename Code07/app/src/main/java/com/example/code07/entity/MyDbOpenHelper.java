package com.example.code07.entity;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.code07.R;

import java.util.Arrays;

public class MyDbOpenHelper extends SQLiteOpenHelper {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " (" +
                    NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY," +
                    NewsContract.NewsEntry.COLUMN_NAME_TITLE + " VARCHAR(200)," +
                    NewsContract.NewsEntry.COLUMN_NAME_AUTHOR + " VARCHAR(100)," +
                    NewsContract.NewsEntry.COLUMN_NAME_CONTENT + " TEXT," +
                    NewsContract.NewsEntry.COLUMN_NAME_IMAGE + " VARCHAR(100)" +
                    ")";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "news.db";

    private Context mContext;

    public MyDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        initDb(sqLiteDatabase);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase,
                          int oldVersion, int newVersion) {
        // 在数据库升级时的操作，可根据需求进行相应的处理
        // 如果不需要处理升级操作，可以空实现或直接删除该方法
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    private void initDb(SQLiteDatabase sqLiteDatabase) {
        Resources resources = mContext.getResources();
        String[] titles = resources.getStringArray(R.array.titles);
        String[] authors = resources.getStringArray(R.array.authors);
        String[] contents = resources.getStringArray(R.array.contents);
        TypedArray images = resources.obtainTypedArray(R.array.images);

        int length = Arrays.stream(new int[]{titles.length, authors.length, contents.length, images.length()})
                .min().getAsInt();

        for (int i = 0; i < length; i++) {
            ContentValues values = new ContentValues();
            values.put(NewsContract.NewsEntry.COLUMN_NAME_TITLE,
                    titles[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_AUTHOR,
                    authors[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_CONTENT,
                    contents[i]);
            values.put(NewsContract.NewsEntry.COLUMN_NAME_IMAGE,
                    images.getString(i));

            // 将ContentValues对象中的值插入到数据库表中
            long r = sqLiteDatabase.insert(
                    NewsContract.NewsEntry.TABLE_NAME, // 表名
                    null, // 插入时不指定列的默认值
                    values // 存储待插入数据的ContentValues对象
            );
        }
    }
}
