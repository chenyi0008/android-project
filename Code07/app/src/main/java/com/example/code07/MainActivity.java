package com.example.code07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import com.example.code07.entity.MyDbOpenHelper;
import com.example.code07.entity.News;
import com.example.code07.entity.NewsAdapter;
import com.example.code07.entity.NewsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String[] titles = null;
    private String[] authors = null;

    public static final String NEWS_ID = "news_id";
    private List<News> newsList = new ArrayList<>();

    private NewsAdapter newsAdapter = null;
    private RecyclerView recyclerView;

    private MyDbOpenHelper myDbHelper;

    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = findViewById(R.id.lv_news_list);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(llm); // 设置布局管理器为线性布局管理器
//        recyclerView.setAdapter(newsAdapter); // 设置适配器给RecyclerView
//        initData();

        myDbHelper = new MyDbOpenHelper(MainActivity.this);
        db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                NewsContract.NewsEntry.TABLE_NAME,
                null, null, null, null, null, null);

        newsAdapter = new NewsAdapter(MainActivity.this, R.layout.list_item, newsList); // 创建新闻适配器




        List<News> newsList = new ArrayList<>();

        int titleIndex = cursor.getColumnIndex(
                NewsContract.NewsEntry.COLUMN_NAME_TITLE);
        int authorIndex = cursor.getColumnIndex(
                NewsContract.NewsEntry.COLUMN_NAME_AUTHOR);
        int imageIndex = cursor.getColumnIndex(
                NewsContract.NewsEntry.COLUMN_NAME_IMAGE);

        while (cursor.moveToNext()) {
            News news = new News();

            String title = cursor.getString(titleIndex);
            String author = cursor.getString(authorIndex);
            String image = cursor.getString(imageIndex);

            Bitmap bitmap = BitmapFactory.decodeStream(
                    getClass().getResourceAsStream("/" + image));

            news.setTitle(title);
            news.setAuthor(author);
            news.setImage(bitmap);
            newsList.add(news);
        }

        NewsAdapter newsAdapter = new NewsAdapter(
                MainActivity.this,
                R.layout.list_item,
                newsList);
        ListView lvNewsList = findViewById(R.id.lv_news_list);
        lvNewsList.setAdapter(newsAdapter);
    }

    private static final String NEWS_TITLE = "news_title";
    private static final String NEWS_AUTHOR = "news_author";

    private List<Map<String,String>> dataList = new ArrayList<>();
    private void initData() {
        int length;
        titles = getResources().getStringArray(R.array.titles);
        authors = getResources().getStringArray(R.array.authors);
        TypedArray images = getResources().obtainTypedArray(R.array.images);

        if (titles.length > authors.length) {
            length = authors.length;
        } else {
            length = titles.length;
        }

        for (int i = 0; i < length; i++) {
            News news = new News();
            news.setTitle(titles[i]);
            news.setAuthor(authors[i]);
            news.setImageId(images.getResourceId(i, 0));
            newsList.add(news);
        }




    }


}