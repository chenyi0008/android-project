package com.example.ggmusic;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MediaCursorAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater ;
    private Context mContext;

    public MediaCursorAdapter(Context context) {
        super(context, null, 0);
        mLayoutInflater  = LayoutInflater.from(context);
    }

    // 创建新的列表项视图
    @Override
    public View newView(Context context,
                        Cursor cursor, ViewGroup viewGroup) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item,
                viewGroup, false);

        if (itemView != null) {
            ViewHolder vh = new ViewHolder();
            vh.tvTitle = itemView.findViewById(R.id.tv_title);
            vh.tvArtist = itemView.findViewById(R.id.tv_artist);
            vh.tvOrder = itemView.findViewById(R.id.tv_order);
            vh.divider = itemView.findViewById(R.id.divider);
            itemView.setTag(vh);

            return itemView;
        }

        return null;
    }

    // 绑定数据到列表项视图
    @Override
    public void bindView(View view,
                         Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();

        int titleIndex = cursor.getColumnIndex(
                MediaStore.Audio.Media.TITLE);
        int artistIndex = cursor.getColumnIndex(
                MediaStore.Audio.Media.ARTIST);

        String title = cursor.getString(titleIndex);
        String artist = cursor.getString(artistIndex);

        int position = cursor.getPosition();

        if (vh != null) {
            vh.tvTitle.setText(title);
            vh.tvArtist.setText(artist);
            vh.tvOrder.setText(Integer.toString(position+1));
        }
    }

    // 通过专辑 ID 获取专辑封面图像
    private Bitmap getAlbumArt(Context context, String albumId) {
        // 在这里根据专辑 ID 查询并返回封面图像
        // 这里只是一个示例，您需要根据您的需求进行实现
        return null;
    }

    // 用于保存列表项视图中的视图组件
    public class ViewHolder {
        TextView tvTitle;
        TextView tvArtist;
        TextView tvOrder;
        View divider;
    }
}
