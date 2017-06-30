package dev.rism.odyssey2016.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import java.util.HashMap;

import dev.rism.odyssey2016.ConnectionDetector;
import dev.rism.odyssey2016.Connector;
import dev.rism.odyssey2016.LogSaver;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 01-10-2016.
 */

//Layout file for this activity is activity_signin.xml
    //SnappyDB is used for session management
public class LoginActivity extends AppCompatActivity {
    private static final String BASE_URL ="http://codelabs.16mb.com/login.php" ; //It is the script for login authentication
    EditText etemail,etpassword;
    ProgressBar progressBar;
    Button login,bforgot;
    TextView tv;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectionDetector ob;
    DB snappyDB;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        try {
            snappyDB= DBFactory.open(getApplicationContext(),"users");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        ob=new ConnectionDetector(getApplicationContext());
        try{
        preferences=getApplicationContext().getSharedPreferences("User.io",MODE_PRIVATE);
        editor=preferences.edit();}
        catch (Exception e){e.printStackTrace();
            LogSaver.appendLog("MainActivity :"+e.toString());
        }
        etemail= (EditText) findViewById(R.id.lemail);
        tv= (TextView) findViewById(R.id.textView);
        etpassword= (EditText) findViewById(R.id.lpassword);
        login= (Button) findViewById(R.id.sign_in);
        bforgot= (Button) findViewById(R.id.forgot);
        progressBar= (ProgressBar) findViewById(R.id.signin_progress);
        etpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (event.getRawX()>=(etpassword.getRight()-etpassword.getCompoundDrawables()[2].getBounds().width()))
                    {
                        if (etpassword.getInputType()==InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        {
                            etpassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye_close,0);
                        etpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);}
                        else if (etpassword.getInputType()==(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                            etpassword.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.eye_open,0);
                            etpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);}
                    }
                }
                return false;
            }
        });
       String email,password;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ob.isConnecting()){
                String email=etemail.getText().toString().trim().toLowerCase();
                String password=etpassword.getText().toString().trim().toLowerCase();
                if (email.matches("")||!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches())
                {
                  etemail.setError("Enter your email");
                    return;
                }
                if (password.matches("")){etpassword.setError("Enter your password");return;}
                else{
                new Background().execute(new String[]{email,password});}}
                else {
                    Toast.makeText(getApplicationContext(),"Plz connect to net",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setEnabled(false);
                Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        bforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    class Background extends AsyncTask<String,Integer,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            if (s.equalsIgnoreCase("Success"))
            {
                try {
                    snappyDB.put("Email", etemail.getText().toString());
                    snappyDB.put("Password", etpassword.getText().toString());
                    snappyDB.close();
                }
                catch (SnappydbException e){e.printStackTrace();}
                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            HashMap<String,String> data=new HashMap<>();
            data.put("email",params[0]);
            data.put("password",params[1]);
           response= Connector.sendPostRequest(BASE_URL,data);
            return response;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv.setEnabled(true);
    }
}
