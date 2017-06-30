package dev.rism.odyssey2016.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;

import java.util.Random;

import dev.rism.odyssey2016.R;

/**
 * Created by risha on 15-09-l2016.
 */
public class CategoryAdapter extends BaseAdapter {
   private static final SparseArray<Double> sPositionHeightRatios=new SparseArray<>();
    private String [] categoryColors={"#292c33","#26A69A","#34495e","#097054","#004D40"};
    private TypedArray categoryIcons;
    private String[] categoryNames;
    Context context;
    private Typeface font;
    private final Random mRandom=new Random();
    static class ViewHolder
    {
        DynamicHeightImageView imageView;
        LayerDrawable layers;
        GradientDrawable shape;
        TextView txtLineOne;
        ViewHolder(){}
    }
    public CategoryAdapter(Context context)
    {
        this.categoryIcons=context.getResources().obtainTypedArray(R.array.fragment_category_icons);
        this.categoryNames=context.getResources().getStringArray(R.array.fragment_category_names);
        this.context=context;
        this.font=Typeface.createFromAsset(context.getAssets(),"Roboto.ttf");
    }
    @Override
    public int getCount() {
        return this.categoryNames.length;
    }

    @Override
    public Object getItem(int position) {
        return this.categoryNames[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null)
        {
            convertView=LayoutInflater.from(context).inflate(R.layout.fragment_category_list_item,parent,false);
            vh=new ViewHolder();
            vh.imageView= (DynamicHeightImageView) convertView.findViewById(R.id.imageViewCategory);
            vh.txtLineOne=(TextView) convertView.findViewById(R.id.textViewCategory);
            vh.layers= (LayerDrawable) this.context.getResources().getDrawable(R.drawable.white_layer_list);
            vh.shape=(GradientDrawable)vh.layers.findDrawableByLayerId(R.id.layerListItem);
            convertView.setTag(vh);
        }
        else {
            vh=(ViewHolder) convertView.getTag();
        }
        double positionHeight=getPositionRatio(position);
        vh.imageView.setHeightRatio(positionHeight);
        vh.imageView.setImageResource(this.categoryIcons.getResourceId(position,0));
        vh.shape.mutate();try {
            vh.shape.setColor(Color.parseColor(this.categoryColors[position]));
        }
        catch (Exception e){e.printStackTrace();}
        if(Build.VERSION.SDK_INT<16)
        {
            convertView.setBackgroundDrawable(vh.shape);
        }
        else
        {
            convertView.setBackground(vh.shape);
        }
        vh.txtLineOne.setTypeface(this.font);
        vh.txtLineOne.setText(this.categoryNames[position]);

        return convertView;
    }

    private double getPositionRatio(int position) {
        double ratio=(Double)sPositionHeightRatios.get(position,Double.valueOf(0.0d)).doubleValue();
        if (ratio!=0.0d)
        {
            return  ratio;
        }
        ratio=getRandomHeightRatio();
        sPositionHeightRatios.append(position,Double.valueOf(ratio));
        return ratio;
    }
    private double getRandomHeightRatio()
    {
      return (this.mRandom.nextDouble()/2.0d)+0.5d;
    }
}
