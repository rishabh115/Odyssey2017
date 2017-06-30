package dev.rism.odyssey2016.Activity;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dev.rism.odyssey2016.Adapter.FeedAdapter;
import dev.rism.odyssey2016.Config;
import dev.rism.odyssey2016.ConnectionDetector;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.Models.FeedModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 29-09-2016.
 */
//Using this activity ,students can get the latest feeds
//Layout file for this activity is activity_feeds.xml

public class FeedActivity extends OdysseyActivity {
    ListView lvfeed;
    Toolbar toolbar;
    ProgressBar progressBar;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareLinkContent content;
    File httpCacheDir;
    long httpCacheSize = 15 * 1024 * 1024;
    HttpResponseCache cache;
   static final String FEED_URl="http://codelabs.16mb.com/feed.php";  //It is the php script for getting feeds in JSON form
    public static final String TAG=FeedActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_feeds);
        LogSaver.appendLog(TAG);
        cacher();

        progressBar=(ProgressBar) findViewById(R.id.feed_progress);
        toolbar= (Toolbar) findViewById(R.id.feed_toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        FacebookSdk.sdkInitialize(getApplicationContext());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvfeed= (ListView) findViewById(R.id.feed_list);
        ConnectionDetector ob=new ConnectionDetector(getApplicationContext());
      //  if (!ob.isConnecting())
      //  {
       //     Toast.makeText(getApplicationContext(),"Please connect to Internet",Toast.LENGTH_LONG).show();
      //  }
       // else{
        new Background().execute(FEED_URl);
    //}
    }
    class Background extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            String data="";
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                LogSaver.appendLog(TAG+" : "+e.toString());
            }
            HttpURLConnection urlConnection = null;
            try {
                ConnectionDetector ob=new ConnectionDetector(getApplicationContext());

                urlConnection = (HttpURLConnection) url.openConnection();
                if (ob.isConnecting()){
                urlConnection.addRequestProperty("Cache-Control", "max-age=0");}
                else{
                urlConnection.addRequestProperty("Cache-Control", "max-stale=" + 60*60*360);
                urlConnection.setUseCaches(true);}

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                data=readStream(in);

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return data;
        }
        private String readStream(InputStream in)
        {
            String s;
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            StringBuilder builder=new StringBuilder();
            try {
                while ((s=bufferedReader.readLine())!=null)
                {
                    builder.append(s);
                }}
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        LogSaver.appendLog(TAG+" : "+e.toString());
                    }
                }
            }
            return builder.toString();

        }


        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            ArrayList<FeedModel> feedModels=new ArrayList<>();
            try {
                int i;
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jArray = jsonObject.getJSONArray("data");
                for (i=0;i<jArray.length();i++)
                {
                    JSONObject feedObject=jArray.getJSONObject(i);
                    FeedModel ob=new FeedModel();
                    ob.setTitle(feedObject.getString("title"));
                    ob.setMatter(feedObject.getString("description"));
                    ob.setImage_url(feedObject.getString("image_url"));
                    ob.setDate(feedObject.getString("hosted_on"));
                    feedModels.add(ob);
                }
                if (jArray.length()>0){
                FeedAdapter adapter=new FeedAdapter(getApplicationContext(),feedModels);
                lvfeed.setAdapter(adapter);}
            }
            catch (Exception e){
                LogSaver.appendLog(e.toString());
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
       HttpResponseCache cache = HttpResponseCache.getInstalled();
        if(cache != null) {  //Clearing the cache
            cache.flush();
        }
    }

    public void cacher()
    {
        httpCacheDir = getExternalCacheDir();
        try {
            HttpResponseCache.install(httpCacheDir, httpCacheSize); //Setting the cache
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
