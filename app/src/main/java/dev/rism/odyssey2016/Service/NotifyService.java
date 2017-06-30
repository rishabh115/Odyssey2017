package dev.rism.odyssey2016.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

import dev.rism.odyssey2016.Activity.MainActivity;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 01-10-2016.
 */
public class NotifyService extends Service {


    private PowerManager.WakeLock mWakeLock;

    /**
     * Simply return null, since our Service will not be communicating with
     * any other components. It just does its work silently.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings("deprecation")
    private void handleIntent() {
        // obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Partial");
        mWakeLock.acquire();

        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }

        // do the actual work, in a separate thread
        new PollTask().execute();
    }

    private class PollTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... args) {
            // do stuff!
            String title = null;
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            int year = c.get(Calendar.YEAR);
            String Month = (String.valueOf(month));
            String Day = (String.valueOf(day));
            String Year = (String.valueOf(year));
            String todaysDate = (Month + "-" + Day + "-" + Year);

            try {
                // Create a URL for the desired page
                URL updateURL = new URL("URL to your notification directory" + todaysDate + ".txt");

                // Read all the text returned by the server
                BufferedReader in = new BufferedReader(new InputStreamReader(updateURL.openStream()));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    total.append(line).append("\n");
                }
                title = total.toString();

                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return title;

        }

        /**
         * In here you should interpret whatever you fetched in doInBackground
         * and push any notifications you need to the status bar, using the
         * NotificationManager. I will not cover this here, go check the docs on
         * NotificationManager.
         *
         * What you HAVE to do is call stopSelf() after you've pushed your
         * notification(s). This will:
         * 1) Kill the service so it doesn't waste precious resources
         * 2) Call onDestroy() which will release the wake lock, so the device
         *    can go to sleep again and save precious battery.
         */
        @Override
        protected void onPostExecute(String title) {
            int mId = 420;
            if (title == null) {
                stopSelf();
            }else{

                Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(NotifyService.this, 0, notificationIntent, 0);

                //Set up the notification

                Notification noti = new NotificationCompat.Builder(getApplicationContext()).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                        .setSmallIcon(R.drawable.noti_small)
                        .setTicker("New Notification ...")
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("Your app name")
                        .setContentText(title)
                        .setContentIntent(contentIntent)
                        //At most three action buttons can be added
                        //.addAction(android.R.drawable.ic_menu_camera, "Action 1", contentIntent)
                        //.addAction(android.R.drawable.ic_menu_compass, "Action 2", contentIntent)
                        //.addAction(android.R.drawable.ic_menu_info_details, "Action 3", contentIntent)
                        .setAutoCancel(true).build();

                //Show the notification
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                mNotificationManager.notify(mId, noti);
                // handle your data
                stopSelf();
            }
        }
    }

    /**
     * This is deprecated, but you have to implement it if you're planning on
     * supporting devices with an API level lower than 5 (Android 2.0).
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        handleIntent();
    }

    /**
     * This is called on 2.0+ (API level 5 or higher). Returning
     * START_NOT_STICKY tells the system to not restart the service if it is
     * killed because of poor resource (memory/cpu) conditions.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent();
        return START_NOT_STICKY;
    }

    /**
     * In onDestroy() we release our wake lock. This ensures that whenever the
     * Service stops (killed for resources, stopSelf() called, etc.), the wake
     * lock will be released.
     */
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
}