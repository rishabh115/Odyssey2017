package dev.rism.odyssey2016.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import dev.rism.odyssey2016.Adapter.EventAdapter;
import dev.rism.odyssey2016.Models.EventModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 04-10-2016.
 */
//This activity displays the list of events in grid based recycler view .
//Replace images and sample data used here with your own data
//Layout file for this event is activity_event_content.xml
public class EventActivity extends OdysseyActivity {
    RecyclerView recyclerView;
    List<EventModel> list;
    EventAdapter adapter;
    String [] url=new String[]{"https://hnd2thomasknapp.files.wordpress.com/2013/02/linkin_parkminutes_to_midnightfrontal.jpeg","http://i0.wp.com/www.ucreative.com/wp-content/uploads/2012/09/Linkin-Park.jpg?resize=600%2C600",
            "http://img15.deviantart.net/6ff8/i/2011/146/8/3/linkin_park___tf3_album_cover_by_vankata97-d3h94do.jpg",
            "https://cdn.cloudpix.co/images/linkin-park/linkin-park-album-art-by-shawks-hlcoy-album-cover-dfd830a5bf24c3f36e296e9b1a6db2e9-large-1279578.jpg"
             ,"https://upload.wikimedia.org/wikipedia/en/2/22/Coldplay_-_Paradise.JPG","http://orig12.deviantart.net/d4f6/f/2014/179/8/0/coldplay___magic__alternate_album_cover__by_rrpjdisc-d7oe2qr.jpg"
            ,"https://s-media-cache-ak0.pinimg.com/564x/86/57/65/865765aeea21f06b6010142ff19dea73.jpg"

    };
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_content);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        list=new ArrayList<>();
        adapter=new EventAdapter(this,list);
        toolbar= (Toolbar) findViewById(R.id.Event_toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this,2);  // 2 is the number of columns in grid view
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2,dpToPx(10),true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        initialize();

    }

    private int dpToPx(int i) {  //To convert dp to px
        Resources r=getResources();

        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics()));
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration { //Do not change anything in this class ,It is the itemdecorator for recycler view used
        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        public GridSpacingItemDecoration(int i, int dpToPx, boolean b) {
            spanCount=i;
            spacing=dpToPx;
            includeEdge=b;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position=parent.getChildAdapterPosition(view);
            int column=position%spanCount;
            if (includeEdge)
            {
                outRect.left=spacing-column*spacing/spanCount;
                outRect.right=(column+1)*spacing/spanCount;
                if (position<spanCount)
                {
                    outRect.top=spacing;
                }
                outRect.bottom=spacing;
            }
            else {
                outRect.left=column*spacing/spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
    public void initialize()  //Change the parameters with required data
    {
    EventModel ob=new EventModel("Midnight",url[0]);
        list.add(ob);
        ob=new EventModel("Linkin Park",url[1]);
        list.add(ob);
        ob=new EventModel("Transformer",url[2]);
        list.add(ob);
        ob=new EventModel("Iridescent",url[3]);
        list.add(ob);
       ob= new EventModel("Paradise",url[4]);
        list.add(ob);
        ob= new EventModel("Coldplay",url[5]);
        list.add(ob);
        ob= new EventModel("Ghost",url[6]);
        list.add(ob);
        adapter.notifyDataSetChanged();
    }
}
