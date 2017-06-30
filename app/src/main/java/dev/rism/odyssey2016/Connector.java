package dev.rism.odyssey2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by risha on 30-09-2016.
 */
public class Connector {
    public static String sendPostRequest(String requestURL, HashMap<String,String> postDataParams)
    {
        URL url;
        String response="";

        try
        {
            url=new URL(requestURL);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            //for writing the data to the webserver
            OutputStream outputStream=urlConnection.getOutputStream();
            BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            outputStream.close();

            //reading the response code and checking if the data
            //has been pushed correctly
            int responseCode=urlConnection.getResponseCode();

            if(responseCode== HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                response=reader.readLine();
            }
            else
            {
                response="Error registering";
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    public static String getPostDataString(HashMap<String,String> params)throws UnsupportedEncodingException
    {
        StringBuilder result=new StringBuilder();
        boolean firstAlpha=true;
        for(Map.Entry<String,String> entry: params.entrySet())
        {
            if(firstAlpha)
                firstAlpha=false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        return result.toString();
    }
}
