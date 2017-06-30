package dev.rism.odyssey2016.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import dev.rism.odyssey2016.Activity.EventActivity;
import dev.rism.odyssey2016.Activity.EventItemActivity;
import dev.rism.odyssey2016.Models.EventModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 03-10-2016.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>
{
    Context context;
    List<EventModel> list;
    public EventAdapter(Context context,List<EventModel> list){this.context=context;
    this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_event_grid, parent, false);
        itemView.setOnClickListener(new MyOnClickListener());
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
     EventModel eventModel=list.get(position);
        holder.title.setText(eventModel.getName());
       holder.title.setTypeface(Typeface.createFromAsset(context.getAssets(),"gotham.otf"));
        Picasso.with(context).load(eventModel.getUrl()).into(holder.ivgrid);
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View overflow) {
        PopupMenu menu=new PopupMenu(context,overflow);
        MenuInflater inflater=menu.getMenuInflater();
        inflater.inflate(R.menu.events_menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        menu.show();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
TextView title;
    ImageView ivgrid,overflow;
    public ViewHolder(View view)
    {
        super(view);
        title=(TextView) view.findViewById(R.id.title_grid_view);

        ivgrid= (ImageView) view.findViewById(R.id.iv_grid_view);
        overflow= (ImageView) view.findViewById(R.id.iv_three_dots);


    }
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        MyMenuItemClickListener(){}
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context,EventItemActivity.class);

            context.startActivity(intent);
        }
    }
}