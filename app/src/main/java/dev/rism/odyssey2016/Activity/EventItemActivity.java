package dev.rism.odyssey2016.Activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.rism.odyssey2016.Config;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;
import dev.rism.odyssey2016.Fragments.Tab1Fragment;
import dev.rism.odyssey2016.Fragments.Tab2Fragment;
import dev.rism.odyssey2016.Fragments.Tab3Fragment;

/**
 * Created by risha on 29-09-2016.
 */

//This activity is opened after clicking on any grid item in EventActivity
//Layout file for this activity is  activity_event_item.xml

public class EventItemActivity extends OdysseyActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView iv_bg;
    Typeface typeface;
    public static final String TAG=EventItemActivity.class.getSimpleName();
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface=Typeface.createFromAsset(getAssets(),"canaro_extra_bold.otf");
        setContentView(R.layout.activity_event_item);
        LogSaver.appendLog(TAG);
        collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FFFFFF"));
        iv_bg=(ImageView) findViewById(R.id.event_bg);
        Picasso.with(this).load(Config.EVENT_BG).placeholder(R.drawable.backsplash).into(iv_bg);
        toolbar=(Toolbar) findViewById(R.id.toolbar_event);
       setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tabLayout= (TabLayout) findViewById(R.id.tabs);
        viewPager= (ViewPager) findViewById(R.id.viewpager_event);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {                    //Here you can define the tabs to be displayed
        EventAdapter adapter = new EventAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "About");  //Intially I have defined three tabs and their fragments ,you can add or remove easily
        adapter.addFragment(new Tab2Fragment(), "Schedule");
        adapter.addFragment(new Tab3Fragment(), "Register");
        viewPager.setAdapter(adapter);
        LogSaver.appendLog(TAG+" adapter added!");
    }
    class EventAdapter extends FragmentPagerAdapter
    {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public EventAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
