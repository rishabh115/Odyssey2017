package dev.rism.odyssey2016.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import dev.rism.odyssey2016.R;


//Layout for this in activity_about_us.xml
//To Change the title and description of College fest ,replace text in tvabout and tvfestdesc with your fest name and description  in xml file respectively
public class AboutActivity extends OdysseyActivity {
    ImageView iv;
    String link="http://img.collegepravesh.com/2016/01/HBTI-Kanpur.jpg";  //Replace the link with your college logo
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        iv= (ImageView) findViewById(R.id.ivabout);
        Picasso.with(this).load(link).placeholder(R.drawable.placeholder).into(iv);
    }
}
