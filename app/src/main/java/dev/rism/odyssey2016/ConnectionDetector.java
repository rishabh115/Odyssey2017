package dev.rism.odyssey2016;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by risha on 30-09-2016.
 */
public class ConnectionDetector {
    public  Context context;
    public ConnectionDetector(Context context)
    {
        this.context=context;
    }
    public boolean isConnecting() {
        ConnectivityManager check=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (check!=null){
            NetworkInfo[] infos=check.getAllNetworkInfo();
            for (int i=0;i<infos.length;i++)
            {
                if (infos[i].getState()== NetworkInfo.State.CONNECTED)
                    return true;
            }}

        return false;

    }
}
