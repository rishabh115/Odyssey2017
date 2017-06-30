package dev.rism.odyssey2016.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.rism.odyssey2016.R;
import dev.rism.odyssey2016.Models.VideoListModel;

/**
 * Created by risha on 17-09-l2016.
 */
public class VideoListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    ArrayList<VideoListModel> video;
    Typeface typeface;
    public VideoListAdapter(Context context, ArrayList<VideoListModel> video)
    {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.video=video;
        typeface=Typeface.createFromAsset(context.getAssets(),"Capture it.ttf");
    }
    @Override
    public int getCount() {
        return ( video.size());
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null) {
            holder=new ViewHolder();
            inflater=LayoutInflater.from(context);
            convertView= inflater.inflate(R.layout.videolist, parent, false);
            holder.tvtitle=(TextView) convertView.findViewById(R.id.videoTitletextview);
            holder.ivthumbnail=(ImageView) convertView.findViewById(R.id.videoPreviewThumb);
            convertView.setTag(holder);
        }
        else {
            holder=(ViewHolder)convertView.getTag();
        }
        holder.tvtitle.setText(video.get(position).getName());
        holder.tvtitle.setTypeface(typeface);
        Picasso.with(context).load("https://i.ytimg.com/vi/"+video.get(position).getVideoId().toString()+"/hqdefault.jpg").placeholder(R.drawable.logowhite).into(holder.ivthumbnail);

        return convertView;
    }
    static class ViewHolder {
        TextView tvtitle;
        ImageView ivthumbnail;
    }
}
