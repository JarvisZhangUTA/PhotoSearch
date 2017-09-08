package com.jarviszhang.photosearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jarvis on 9/7/17.
 */

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    private Context context;//运行时的环境，上下文
    private List<GalleryItem> list;//图片信息

    public GalleryAdapter(Context context, List<GalleryItem> list){
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //xml装载到view中实例化
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gallery,parent,false);
        return new ViewHolder(v);
    }


    @Override
    public int getItemCount(){
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        GalleryItem item = list.get(position);

        Picasso.with(context)
                .load(item.getUrl())
                .into(holder.imageView);
//        Glide.with(context)
//                .load(item.getUrl())
//                //渐变下载，smooth
//                .thumbnail(0.5f)
//                .into(holder.imageView);
    }

    public void reset(){
        list.clear();
    }

    public void addAll(List<GalleryItem> list){
        this.list.addAll(list);
    }
}
