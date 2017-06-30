package dev.rism.odyssey2016.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 30-09-2016.
 */
public class ProfileActivity extends OdysseyActivity {
    TextView tvName,tvCollege ,tvemail;
    LoginButton loginButton;
    CircleImageView imageView;
    TextView tvstatus;
    CallbackManager callbackManager;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        toolbar= (Toolbar) findViewById(R.id.Profile_toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView= (CircleImageView) findViewById(R.id.profile_image);
        tvName= (TextView) findViewById(R.id.tv_profile_name);
        tvCollege= (TextView) findViewById(R.id.tv_profile_college);
        tvemail= (TextView) findViewById(R.id.tv_profile_email);
        tvstatus=(TextView)findViewById(R.id.tv_status);
        loginButton= (LoginButton) findViewById(R.id.profile_login);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {
                    Profile profile = Profile.getCurrentProfile();
                    LogSaver.appendLog("Login Success:" + profile.getId());
                    tvstatus.setVisibility(View.GONE);
                    tvName.setTypeface(Typeface.createFromAsset(getAssets(), "gotham.otf"));
                    tvName.setText(profile.getName());
                    tvemail.setText(profile.getLinkUri().toString());
                    try {
                        Picasso.with(getApplicationContext()).load(profile.getProfilePictureUri(350, 350)).placeholder(R.drawable.profile).into(imageView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e){}}


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                LogSaver.appendLog("Facebook Login :"+error.toString());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Profile profile=Profile.getCurrentProfile();
        try
        {


            tvName.setTypeface(Typeface.createFromAsset(getAssets(),"gotham.otf"));
            tvName.setText(profile.getName());
            tvemail.setText(profile.getLinkUri().toString());
            try {
                Picasso.with(getApplicationContext()).load(profile.getProfilePictureUri(350,350)).placeholder(R.drawable.profile).into(imageView);
                tvstatus.setVisibility(View.GONE);
            }
            catch (Exception e){e.printStackTrace();}
        }
        catch (Exception e)
        {
            LogSaver.appendLog("Profile :"+e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
}
