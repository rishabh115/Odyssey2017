package dev.rism.odyssey2016.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dev.rism.odyssey2016.ConnectionDetector;
import dev.rism.odyssey2016.Manifest;
import dev.rism.odyssey2016.R;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity
{
    public static final String BASE_URL="http://codelabs.16mb.com/register.php";
    EditText etusername,etpassword,etcollege;
    Spinner emailspinner;
    Button register;
    ProgressBar progressBar;
    TextView tv;
   static int REQUEST_CODE=20;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ConnectionDetector ob;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ob=new ConnectionDetector(getApplicationContext());
       preferences=this.getSharedPreferences("permission",MODE_PRIVATE);
        editor=preferences.edit();
        etcollege= (EditText) findViewById(R.id.college);
        progressBar=(ProgressBar) findViewById(R.id.login_progress);
       emailspinner= (Spinner) findViewById(R.id.email);
        etpassword=(EditText) findViewById(R.id.password);
        etusername=(EditText) findViewById(R.id.username);
        register= (Button) findViewById(R.id.email_sign_in_button);
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        Account[] accounts1=manager.getAccountsByType("com.android.email");
        List<String> possibleEmails = new ArrayList<String>();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
//            int hasReadPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
//            int hasWritePermission=ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            if (hasReadPermission == PackageManager.PERMISSION_GRANTED && hasWritePermission==PackageManager.PERMISSION_GRANTED) {
//                //Do smthng
//            }
//            else{
            try{
            if (preferences.getString("Permission","").equalsIgnoreCase("Granted")){}
            else{
              Toast.makeText(getApplicationContext(),"Give permission for contacts and storage",Toast.LENGTH_LONG).show();
            startInstalledAppDetailsActivity(SignUpActivity.this);}}
            catch (Exception e){e.printStackTrace();}
        }

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }
        for (Account account : accounts1) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,possibleEmails);
        emailspinner.setAdapter(adapter);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if (ob.isConnecting()){
                String username,email,password,college;
                email="";
                username=etusername.getText().toString().trim().toLowerCase();
                password=etpassword.getText().toString().trim().toLowerCase();
                college=etcollege.getText().toString();
                if (emailspinner.getCount()==0){
                    startInstalledAppDetailsActivity(SignUpActivity.this);
                    Toast.makeText(getApplicationContext(),"Please grant the permission for Contacts",Toast.LENGTH_LONG).show();
                    return;
                }
                if(username.trim().matches("")){
                    etusername.setError("Enter the name");
                    return;
                }
               if(etpassword.length()<6|| TextUtils.isEmpty(password)){etpassword.setError("Password should be of atleast 6 characters ");
               return;}
               if (college.trim().matches("")){etcollege.setError("Enter your college");}
                else{
                   editor.putString("Permission","Granted");
                   editor.commit();
                   email=emailspinner.getSelectedItem().toString().trim().toLowerCase();
            new Background().execute(new String[]{username, email, password, college});
               }
            }
                else {
            Toast.makeText(getApplicationContext(),"Plz connect to net",Toast.LENGTH_LONG).show();
            }
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
        protected String doInBackground(String... params) {
            String response;
          String urlSuffix="?name="+params[0]+"&email="+params[1]+"&password="+params[2]+"&college="+params[3];
            try
            {
                URL url=new URL(BASE_URL+urlSuffix);

                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String result=reader.readLine();

                if(urlConnection!=null)
                    urlConnection.disconnect();

                return result;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            finish();
        }
    }
    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivityForResult(i,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    populate();
                }
            });



        }
    }
    public void populate()
    {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        Account[] accounts1=manager.getAccountsByType("com.android.email");
        List<String> possibleEmails = new ArrayList<String>();
        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }
        for (Account account : accounts1) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,possibleEmails);
        emailspinner.setAdapter(adapter);
    }
}
