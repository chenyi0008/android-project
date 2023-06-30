package com.example.code06.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.code06.R;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    private List<News>mNewsData;
    private Context mContext;
    private int resourceId;

    public NewsAdapter(Context context,int resourceId,List<News>data) {
        super(context, resourceId, data);
        this.mContext = context;
        this.mNewsData = data;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        News news = getItem(position);
        View view = null;

        ViewHolder viewHolder = null;
        if(convertView == null){
            view = LayoutInflater.from(getContext ()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tv_title);
            viewHolder.tvAuthor = view.findViewById(R.id.tv_subtitle);
            viewHolder.ivImage = view.findViewById(R.id.iv_image);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        
        viewHolder.tvTitle.setText (news.getTitle());
        viewHolder.tvAuthor.setText (news.getAuthor());
        viewHolder.ivImage.setImageResource(news.getImageId());
        return view;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivImage;
    }

    public NewsAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<News> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
