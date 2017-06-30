package dev.rism.odyssey2016.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

import dev.rism.odyssey2016.Adapter.ContactUsAdapter;
import dev.rism.odyssey2016.Models.ContactModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 15-10-2016.
 */

//Layout file for this activity is activity_contacts_container.xml
public class ContactsActivity extends OdysseyActivity {
    GridView gridView;
    ArrayList<ContactModel> models=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_container);
        for (int i=0;i<10;i++){              //Replace the details of contacts with your data
        ContactModel ob=new ContactModel();
        ob.setName("Name");
        ob.setEmail("xyz@gmail.com");
        ob.setPhone("917607926420");
        ob.setImg_id(R.drawable.logow);
        ob.setProfile("Profile");
        models.add(ob);}

        gridView=(GridView) findViewById(R.id.grid_view_contacts);
        ContactUsAdapter adapter=new ContactUsAdapter(this,models);
        gridView.setAdapter(adapter);

    }
}
