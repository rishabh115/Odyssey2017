package dev.rism.odyssey2016.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import dev.rism.odyssey2016.Fragments.CategoryFragment;
import dev.rism.odyssey2016.Guillotine.GuillotineAnimation;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 16-09-l2016.
 */
public class TestActivity extends AppCompatActivity{

    private static final long RIPPLE_DURATION = 250;
    Toolbar toolbar;
    FrameLayout root;
    View contentHamburger;
LinearLayout container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
       root=(FrameLayout) findViewById(R.id.root);
       toolbar= (Toolbar) findViewById(R.id.toolbar);
        container= (LinearLayout) findViewById(R.id.gcontainer);
        contentHamburger=findViewById(R.id.content_hamburger);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        CategoryFragment fragment=new CategoryFragment();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gcontainer,fragment).commit();

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
    }
}

