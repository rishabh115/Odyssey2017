package dev.rism.odyssey2016.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.batch.android.*;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.security.MessageDigest;
import java.util.Arrays;

import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;
import dev.rism.odyssey2016.RootChecker;
//It is first activity aka Splash Activity
//Layout file for this is activity_main.xml
public class MainActivity extends OdysseyActivity {
 ImageView logoiv;
    NotificationCompat.Builder mBuilder;
    int mNotificationId = 001;
    LoginButton loginButton;
    CallbackManager callbackManager;
    DB snappyDB;
    public static final String TAG=MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generator();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        LogSaver.appendLog(TAG);
        logoiv = (ImageView) findViewById(R.id.imageView);
        TextView tv2016= (TextView) findViewById(R.id.l2016);
        Typeface typeface1=Typeface.createFromAsset(getAssets(),"canaro_extra_bold.otf");
        tv2016.setTypeface(typeface1);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"canaro_extra_bold.otf");
        ShimmerTextView textView = (ShimmerTextView) findViewById(R.id.shimmertv);
        KenBurnsView kenBurnsView= (KenBurnsView) findViewById(R.id.kbview);
        kenBurnsView.animate().setDuration(500).start();
        if (textView != null) {
            textView.setTypeface(typeface);
        }
        Shimmer shimmer = new Shimmer();
        shimmer.start(textView);
        nextActivity();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      //  callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        Batch.onNewIntent(this, intent);

        super.onNewIntent(intent);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        LogSaver.appendLog(TAG+" : onStart");
        Batch.onStart(this);
    }

    @Override
    protected void onStop()
    {
        Batch.onStop(this);
       LogSaver.appendLog(TAG+" : onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Batch.onDestroy(this);
       LogSaver.appendLog(TAG+" : onDestroy");
        super.onDestroy();
    }
    public void generator()//Used to generate SHA key for adding Facebook SDK
    {
        try {

                    PackageInfo info=getPackageManager().getPackageInfo("dev.rism.odyssey2016", PackageManager.GET_SIGNATURES);
                for(Signature signature:info.signatures)
                {
                    MessageDigest md=MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));
                }
    }
    catch (Exception e){e.printStackTrace();}
    }
    public void nextActivity()
    {


        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
            try
            {
                snappyDB= DBFactory.open(getApplicationContext(),"users");
                Thread.sleep(2000);
            LogSaver.appendLog("Phone is rooted :"+RootChecker.isRooted()+" Device id : "+ Build.SERIAL);//To know whether phone is rooted
            }
            catch (Exception e)
            {}
                finally {
                try {
                    if (snappyDB.exists("Email") && snappyDB.exists("Password") && snappyDB.get("Email").length() > 0 && snappyDB.get("Password").length() > 0) {
                         runOnUiThread(new Runnable() {
                             @Override
                             public void run() {
                                 try {
                                     Toast.makeText(getApplicationContext(),"Welcome back "+snappyDB.get("Email"),Toast.LENGTH_SHORT).show();
                                 } catch (SnappydbException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        snappyDB.close();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        snappyDB.close();
                    }
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }


            }
            }
        });
        thread.start();
    }
}
