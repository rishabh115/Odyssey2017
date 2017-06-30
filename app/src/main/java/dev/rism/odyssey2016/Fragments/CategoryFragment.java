package dev.rism.odyssey2016.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import dev.rism.odyssey2016.Activity.AboutActivity;
import dev.rism.odyssey2016.Activity.ContactsActivity;
import dev.rism.odyssey2016.Activity.EventActivity;
import dev.rism.odyssey2016.Activity.EventItemActivity;
import dev.rism.odyssey2016.Activity.YoutubeActivity;
import dev.rism.odyssey2016.Adapter.CategoryAdapter;
import dev.rism.odyssey2016.MyGridView;
import dev.rism.odyssey2016.R;
import dev.rism.odyssey2016.Activity.TimerActivity;

public class CategoryFragment extends Fragment {
    private CategoryAdapter mAdapter;
    private MyGridView mGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mGridView=(MyGridView) getView().findViewById(R.id.grid_view);
        if (this.mAdapter==null)
        {
            this.mAdapter=new CategoryAdapter(getActivity());

        }
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            String[] category=getActivity().getResources().getStringArray(R.array.fragment_category_names);
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==2)
                {
                    Intent intent=new Intent(getContext(),YoutubeActivity.class);
                    startActivity(intent);
                }
              //  if (position==1)
               // {
               //     Intent intent=new Intent(getContext(),TimerActivity.class);
               //     startActivity(intent);
              //  }
                if (position==1){
                    Intent intent=new Intent(getContext(),EventActivity.class);
                    startActivity(intent);
                }
                if (position==0){
                    Intent intent=new Intent(getContext(), AboutActivity.class);
                    startActivity(intent);
                }
                if (position==3){
                    Intent intent=new Intent(getContext(), ContactsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
