package dev.rism.odyssey2016.Adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import dev.rism.odyssey2016.CanaroTextView;
import dev.rism.odyssey2016.Models.ContactModel;
import dev.rism.odyssey2016.R;

/**
 * Created by risha on 27-10-2016.
 */
public class ContactUsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ContactModel> contactModels;

    public ContactUsAdapter(Context context,ArrayList<ContactModel> contactModels) {
        this.contactModels = contactModels;
        this.context=context;
    }

    @Override
    public int getCount() {
        return contactModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        final ViewHolder1 holder;
        if(convertView==null)
        {
          holder=new ViewHolder1();
            inflater= LayoutInflater.from(context);
         convertView=inflater.inflate(R.layout.activity_contacts,null);
            holder.ivcontact= (ImageView) convertView.findViewById(R.id.iv_contact);
            holder.tvname=(CanaroTextView)convertView.findViewById(R.id.tv_contact_name);
            holder.tvemail= (TextView) convertView.findViewById(R.id.tv_contact_email);
            holder.tvphone=(TextView) convertView.findViewById(R.id.tv_contact_phone);
            holder.tvprofile=(TextView)convertView.findViewById(R.id.tv_contact_profile);
            convertView.setTag(holder);
        }
        else
        {
       holder=(ViewHolder1) convertView.getTag();
        }
        try {
            holder.tvname.setText(contactModels.get(position).getName());
            holder.tvprofile.setText(contactModels.get(position).getProfile());
            holder.tvphone.setText(contactModels.get(position).getPhone());
            holder.tvemail.setText(contactModels.get(position).getEmail());
            holder.ivcontact.setImageResource(contactModels.get(position).getImg_id());
            holder.tvphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Clicked text",Toast.LENGTH_SHORT).show();
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(holder.tvphone.getText().toString().trim())+"@s.whatsapp.net");
                    context.startActivity(sendIntent);

                }
            });
        }
        catch (Exception e){}
        return convertView;
    }
    static class ViewHolder1
    {
        TextView tvprofile,tvphone,tvemail;
        CanaroTextView tvname;
        ImageView ivcontact;
    }
}
