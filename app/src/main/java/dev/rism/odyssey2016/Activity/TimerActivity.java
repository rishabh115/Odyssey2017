package dev.rism.odyssey2016.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import dev.rism.odyssey2016.R;

/**
 * Created by risha on 17-09-l2016.
 */
public class TimerActivity extends AppCompatActivity {
    TextView tvtimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_item);
       /* tvtimer= (TextView) findViewById(R.id.tvtimer);
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                tvtimer.setText(millisUntilFinished/1000 +"seconds to go");
            }

            @Override
            public void onFinish() {
                tvtimer.setText("Event Started");
            }
        }.start();*/
    }
}
