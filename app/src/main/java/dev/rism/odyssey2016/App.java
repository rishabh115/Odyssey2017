package dev.rism.odyssey2016;

import android.app.Application;
import android.graphics.Typeface;

import com.batch.android.*;

/**
 * Created by Dmytro Denysenko on 5/6/15.
 */
public class App extends Application {
    private static final String CANARO_EXTRA_BOLD_PATH = "canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;

    @Override
    public void onCreate() {
        super.onCreate();
        Batch.Push.setGCMSenderId("1089320446828");

        Batch.setConfig(new com.batch.android.Config("DEV57DE801E64DEBB62752A08A4CCA"));
        initTypeface();
    }

    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);

    }
}
