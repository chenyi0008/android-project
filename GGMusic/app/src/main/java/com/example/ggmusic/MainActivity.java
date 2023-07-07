package com.example.ggmusic;



import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import com.bumptech.glide.Glide;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView navigation;
    private TextView tvBottomTitle;
    private TextView tvBottomArtist;
    private ImageView ivAlbumThumbnail;

    private ContentResolver mContentResolver;
    private ImageView ivPlay;

    private MusicService mService;

    private boolean mBound = false;

    private Boolean mPlayStatus = false;

    public static final int UPDATE_PROGRESS = 1;

    private ProgressBar pbProgress;

    public static final String DATA_URI =
            "com.glriverside.xgqin.ggmusic.DATA_URI";
    public static final String TITLE =
            "com.glriverside.xgqin.ggmusic.TITLE";
    public static final String ARTIST =
            "com.glriverside.xgqin.ggmusic.ARTIST";

    private MediaPlayer mMediaPlayer = null;
    private final int REQUEST_EXTERNAL_STORAGE = 1;

    public static final String ACTION_MUSIC_START =
            "com.glriverside.xgqin.ggmusic.ACTION_MUSIC_START";

    public static final String ACTION_MUSIC_STOP =
            "com.glriverside.xgqin.ggmusic.ACTION_MUSIC_STOP";

    private MusicReceiver musicReceiver;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private ListView mPlaylist;
    //CursorAdapter 用于在列表视图（如 ListView）中显示数据库查询结果的数据
    private CursorAdapter mCursorAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化主活动布局
        mContentResolver = getContentResolver();
        mPlaylist = findViewById(R.id.lv_playlist);
        mCursorAdapter = new MediaCursorAdapter(MainActivity.this);
        mPlaylist.setAdapter(mCursorAdapter);

        musicReceiver = new MusicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MUSIC_START);
        intentFilter.addAction(ACTION_MUSIC_STOP);
        registerReceiver(musicReceiver, intentFilter);


        // 查找并设置使用自定义光标适配器的媒体播放列表 ListView

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 检查是否未授予 READ_EXTERNAL_STORAGE 权限

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 解释应用程序为何需要该权限（可选）
            } else {
                // 请求 READ_EXTERNAL_STORAGE 权限
                requestPermissions(PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } else {
            // 已授予 READ_EXTERNAL_STORAGE 权限，继续进行播放列表初始化
            initPlaylist();
        }

        navigation = findViewById(R.id.navigation);
        LayoutInflater.from(MainActivity.this).inflate(R.layout.bottom_media_toolbar, navigation, true);

        // 查找并填充底部媒体工具栏布局
        ivPlay = navigation.findViewById(R.id.iv_play);
        tvBottomTitle = navigation.findViewById(R.id.tv_bottom_title);
        tvBottomArtist = navigation.findViewById(R.id.tv_bottom_artist);
        ivAlbumThumbnail = navigation.findViewById(R.id.iv_thumbnail);

        // 在底部媒体工具栏中查找播放按钮、标题、艺术家和缩略图 ImageView

        if (ivPlay != null) {
            ivPlay.setOnClickListener(MainActivity.this);
        }

        // 初始状态下隐藏底部媒体工具栏

        navigation.setVisibility(View.GONE);

        // 为底部媒体工具栏中的播放按钮设置 OnClickListener

        mPlaylist.setOnItemClickListener(itemClickListener);


        pbProgress = findViewById(R.id.progress);
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(musicReceiver);
        super.onDestroy();
    }


    private void initPlaylist() {
        ContentResolver contentResolver = getContentResolver();

        // 获取内容解析器

        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media.IS_MUSIC + " = ? AND " +
                        MediaStore.Audio.Media.MIME_TYPE + " LIKE ?",
                new String[]{"1", "audio/mpeg"},
                null
        );

        // 查询媒体库中符合条件的音频文件

        if (cursor != null) {
            mCursorAdapter.changeCursor(cursor);
        }

        // 如果光标不为空，则将光标更新到 mCursorAdapter 中
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 处理权限请求的回调方法

        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                // 检查请求代码

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 检查权限授予结果是否为授予

                    initPlaylist();
                    // 初始化播放列表
                }
                break;
            default:
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(MainActivity.this,
                MusicService.class);
        bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(mConn);
        mBound = false;

        super.onStop();
    }


    private ListView.OnItemClickListener itemClickListener
            = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView,
                                View view, int i, long l) {
            // 当用户点击列表项时触发的回调方法

            // 获取适配器中的游标对象
            Cursor cursor = mCursorAdapter.getCursor();
            if (cursor != null && cursor.moveToPosition(i)) {
                // 如果游标不为空且移动到指定位置成功

                // 获取音频标题在游标中的索引
                int titleIndex = cursor.getColumnIndex(
                        MediaStore.Audio.Media.TITLE);
                // 获取音频艺术家在游标中的索引
                int artistIndex = cursor.getColumnIndex(
                        MediaStore.Audio.Media.ARTIST);
                // 获取音频专辑ID在游标中的索引
                int albumIdIndex = cursor.getColumnIndex(
                        MediaStore.Audio.Media.ALBUM_ID);
                // 获取音频数据路径在游标中的索引
                int dataIndex = cursor.getColumnIndex(
                        MediaStore.Audio.Media.DATA);

                // 通过索引获取当前点击项的音频标题、艺术家、专辑ID和数据路径
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                Long albumId = cursor.getLong(albumIdIndex);
                String data = cursor.getString(dataIndex);

                Toast.makeText(MainActivity.this, "开始播放：" + title, Toast.LENGTH_SHORT).show();
                Log.d("data", data);
                // 根据数据路径创建Uri对象
                Uri dataUri = Uri.parse(data);

                //加载新的音乐播放信息
                navigation.setVisibility(View.VISIBLE);

                if (tvBottomTitle != null) {
                    tvBottomTitle.setText(title);
                }
                if (tvBottomArtist != null) {
                    tvBottomArtist.setText(artist);
                }

                Uri albumUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        albumId);

                Cursor albumCursor = mContentResolver.query(
                        albumUri,
                        null,
                        null,
                        null,
                        null);

                if (albumCursor != null && albumCursor.getCount() > 0) {
                    albumCursor.moveToFirst();
                    int albumArtIndex = albumCursor.getColumnIndex(
                            MediaStore.Audio.Albums.ALBUM_ART);
                    String albumArt = albumCursor.getString(
                            albumArtIndex);
                    Glide.with(MainActivity.this)
                            .load(albumArt)
                            .into(ivAlbumThumbnail);
                    albumCursor.close();
                }




                Intent serviceIntent =
                        new Intent(MainActivity.this, MusicService.class);
                serviceIntent.putExtra(MainActivity.DATA_URI, data);
                serviceIntent.putExtra(MainActivity.TITLE, title);
                serviceIntent.putExtra(MainActivity.ARTIST, artist);
                startService(serviceIntent);


            }
        }

    };


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_play) {
            mPlayStatus = !mPlayStatus;
            if (mPlayStatus == true) {
                mService.play();
                ivPlay.setImageResource(
                        R.drawable.baseline_pause_circle_outline_24);
            } else {
                mService.pause();
                ivPlay.setImageResource(
                        R.drawable.baseline_play_circle_outline_24);
            }
        }
    }


    private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(
                ComponentName componentName, IBinder iBinder) {
            MusicService.MusicServiceBinder binder =
                    (MusicService.MusicServiceBinder) iBinder;

            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(
                ComponentName componentName) {
            mService = null;
            mBound = false;
        }
    };

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    int position = msg.arg1;
                    pbProgress.setProgress(position);
                    break;
                default:
                    break;
            }
        }
    };

    private class MusicProgressRunnable implements Runnable {
        public MusicProgressRunnable() {
        }

        @Override
        public void run() {
            boolean mThreadWorking = true;
            while (mThreadWorking) {
                try {
                    if (mService != null) {
                        int position =
                                mService.getCurrentPosition();
                        Message message = new Message();
                        message.what = UPDATE_PROGRESS;
                        message.arg1 = position;
                        mHandler.sendMessage(message);
                    }
                    mThreadWorking = mService.isPlaying();
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (mService != null) {
                pbProgress.setMax(mService.getDuration());

                new Thread(new MusicProgressRunnable()).start();
            }
        }
    }


}