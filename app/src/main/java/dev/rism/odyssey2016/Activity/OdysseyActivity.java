package dev.rism.odyssey2016.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;

/**
 * Created by risha on 28-09-2016.
 */
public class OdysseyActivity extends AppCompatActivity { // It is the base activity of all the activities
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }
}
