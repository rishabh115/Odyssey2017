package dev.rism.odyssey2016.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.rism.odyssey2016.R;

/**
 * Created by risha on 29-09-2016.
 */
public class Tab2Fragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab2,container,false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
