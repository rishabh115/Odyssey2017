package dev.rism.odyssey2016.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import dev.rism.odyssey2016.Adapter.VideoListAdapter;
import dev.rism.odyssey2016.Config;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;
import dev.rism.odyssey2016.Models.VideoListModel;

/**
 * Created by risha on 17-09-l2016.
 */
public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    ListView listView;
    ProgressBar progressBar;
    YouTubePlayer player;
    public static final String TAG=YoutubeActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        LogSaver.appendLog(TAG);
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        listView=(ListView) findViewById(R.id.listView);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);
        new Background().execute(Config.ODYSSEY_PLAYLIST);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            youTubePlayer.cueVideo(Config.YOUTUBE_VIDEO_CODE);
            player=youTubePlayer;
            // Hiding player controls
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            LogSaver.appendLog(TAG+" :"+youTubeInitializationResult.toString());
            youTubeInitializationResult.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
    class Background extends AsyncTask<String,Boolean,String>
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
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
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
                    }
                }
            }
            return builder.toString();

        }

        @Override
        protected void onPostExecute(String s) {
          String st="";

            final ArrayList<VideoListModel> models=new ArrayList<>();
            try
            {
                int i;
                JSONObject jsonObject= new JSONObject(s);
                JSONArray jArray = jsonObject.getJSONArray("items");
                for(i=0;i<jArray.length();i++)
                {
                    JSONObject videoInfo = jArray.getJSONObject(i).getJSONObject("snippet");
                    VideoListModel ob=new VideoListModel();
                    String title;
                    ob.setName(videoInfo.getString("title"));
                    ob.setVideoId(videoInfo.getJSONObject("resourceId").getString("videoId"));

                    models.add(i,ob);
                }
                LogSaver.appendLog("Youtube :Video list added !");
                VideoListAdapter adapter=new VideoListAdapter(getApplicationContext(),models);
                listView.setAdapter(adapter);

            }
            catch (Exception e){e.printStackTrace();}
            finally {
                progressBar.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (player!=null){
                     player.loadVideo(models.get(position).getVideoId());
                        player.play();}
                    }
                });
            }
        }

    }
}
