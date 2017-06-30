package dev.rism.odyssey2016.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.rism.odyssey2016.R;

/**
 * Created by risha on 16-09-l2016.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private  Context context;
    private ArrayList<String> IMAGES = new ArrayList<>();


    public ViewPagerAdapter(Context context, ArrayList<String> IMAGES) {
        this.IMAGES = IMAGES;
        this.context = context;
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.imagepager_layout,null);
        ((ViewPager) collection).addView(view);
        final ImageView img = (ImageView) view.findViewById(R.id.img);
        Picasso.with(context)
                .load(IMAGES.get(position))
                .placeholder(R.drawable.placeholder).fit()
                .into(img);
        return view;
    }
}