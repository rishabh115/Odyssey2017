package dev.rism.odyssey2016.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;

import dev.rism.odyssey2016.Adapter.ViewPagerAdapter;
import dev.rism.odyssey2016.Fragments.CategoryFragment;
import dev.rism.odyssey2016.Config;
import dev.rism.odyssey2016.Guillotine.GuillotineAnimation;
import dev.rism.odyssey2016.Guillotine.GuillotineListener;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 15-09-l2016.
 */
//This activity opens after login activity
public class HomeActivity extends OdysseyActivity implements View.OnClickListener {
ViewPager viewPager;
    FragmentManager fragmentManager;
    //String urls contains image you want to display in ViewPager
    String [] urls=new String[]{"http://plusquotes.com/images/quotes-img/8-Motivational-Quotes.png",
   "https://quotefancy.com/download/354044/original/wallpaper.jpg",
    "http://www.thefreshquotes.com/wp-content/uploads/2015/01/Opportunities-are-like-sunrises.-If-you-wait-too-long-you-miss-them..jpg"};
    private DatabaseReference mDatabase;
     public static final String TAG=HomeActivity.class.getSimpleName();
    private static final long RIPPLE_DURATION = 250;
    Toolbar toolbar;
    FrameLayout root;
    View contentHamburger;
    LinearLayout container;
    LinearLayout profile,feed,myevents,logout;
    View guillotineMenu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LogSaver.appendLog(TAG);
        viewPager = (ViewPager)findViewById(R.id.slider);
        mDatabase=FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_URL);

       initialize();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        final CategoryFragment fragment=new CategoryFragment();
        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,fragment).commit(); //Adding category fragment in layout below viewpager

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true).setGuillotineListener(new GuillotineListener() {
            @Override
            public void onGuillotineOpened() {
            }

            @Override
            public void onGuillotineClosed() {

            }
        })
                .build();
        profile= (LinearLayout) findViewById(R.id.profile_group);
        feed= (LinearLayout) findViewById(R.id.feed_group);
        myevents= (LinearLayout) findViewById(R.id.activity_group);
        logout= (LinearLayout) findViewById(R.id.settings_group);
        profile.setOnClickListener(this);
        feed.setOnClickListener(this);
        myevents.setOnClickListener(this);
        logout.setOnClickListener(this);

        Thread thread=new Thread(){  // To animate the viewpager
            @Override
            public void run() {
            int i=0;
                while (i<3)
                {
                    final int j=i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (viewPager.isActivated()||viewPager.isEnabled())
                            viewPager.setCurrentItem(j);
                        }
                    });

                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finally {
                        i++;
                        if (i==3){i=0;}
                    }

                }

            }
        };
        thread.start();
      }
     public void initialize(){
         root= (FrameLayout) findViewById(R.id.root);
         toolbar= (Toolbar) findViewById(R.id.toolbar);
         container= (LinearLayout) findViewById(R.id.content);
         contentHamburger=findViewById(R.id.content_hamburger);
     }
      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        switch (v.getId())
        {
            case R.id.activity_group:
                Toast.makeText(this,"My Events",Toast.LENGTH_SHORT).show();
                break;
            case R.id.feed_group:
                Intent intent2=new Intent(HomeActivity.this,FeedActivity.class);
                startActivity(intent2);
                break;
            case R.id.profile_group:
                Intent intent1=new Intent(HomeActivity.this,ProfileActivity.class);
                startActivity(intent1);
                break;
            case R.id.settings_group:
                LogSaver.appendLog("Facebook Logged Out :"+LoginManager.getInstance());
                LoginManager.getInstance().logOut();
                try {
                    DB db = DBFactory.open(getApplicationContext(), "users");
                    db.destroy();
                    db.close();
                }
                catch (SnappydbException e){e.printStackTrace();}
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                LogSaver.appendLog("Facebook Logged Out");
               break;
        }
    }
    public void initializeViewPager()
    {
        ArrayList<String> IMAGES= new ArrayList<String>();
        for (int i=0;i<3;i++)
        {
         IMAGES.add(urls[i]);
        }
        ViewPagerAdapter viewPagerAdapter= new ViewPagerAdapter(HomeActivity.this,IMAGES);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initializeViewPager();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
