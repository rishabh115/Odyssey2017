package dev.rism.odyssey2016.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.rism.odyssey2016.Models.FeedModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 29-09-2016.
 */
public class FeedAdapter extends BaseAdapter {
    Context context;
    ArrayList<FeedModel> feeds;
    LayoutInflater inflater;
    Typeface typeface;
    public FeedAdapter(Context context, ArrayList<FeedModel> feeds)
    {
    this.context=context;
        this.feeds=feeds;
        FacebookSdk.sdkInitialize(context);
        typeface=Typeface.createFromAsset(context.getAssets(),"Capture it.ttf");
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return feeds.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView==null)
        {
            holder=new ViewHolder();
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(feeds.get(position).getImage_url()))
                    .build();
            inflater=LayoutInflater.from(context);
            convertView= inflater.inflate(R.layout.activity_feeds_list, parent, false);
            holder.ivfeedimage= (ImageView) convertView.findViewById(R.id.iv_feed);
            holder.tvtitle= (TextView) convertView.findViewById(R.id.tv_feed_title);
            holder.tvmatter= (TextView) convertView.findViewById(R.id.tv_feed_matter);
            holder.tvdate= (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);

        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        try {
            holder.tvtitle.setText(feeds.get(position).getTitle());
            holder.tvtitle.setTypeface(typeface);
            holder.tvtitle.setTextColor(R.color.com_facebook_button_background_color);
            holder.tvmatter.setText(Html.fromHtml(feeds.get(position).getMatter()));
            holder.tvdate.setText("Posted at :"+feeds.get(position).getDate());

            Picasso.with(context).load(feeds.get(position).getImage_url()).fit().placeholder(R.drawable.placeholder).into( holder.ivfeedimage);

        }
        catch (Exception e){e.printStackTrace();}

        return convertView;
    }
    static class ViewHolder
    {
        TextView tvtitle,tvmatter,tvdate;
        ImageView ivfeedimage;
    }
}
