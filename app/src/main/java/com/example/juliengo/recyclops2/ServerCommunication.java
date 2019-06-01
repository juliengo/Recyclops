package com.example.juliengo.recyclops2;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ServerCommunication  extends AsyncTask<String, Void, String>{

    @Override
    protected String doInBackground(String ... strings){
        String urlString = strings[0];
        String postData = strings[1];
        String response = "";

        try {
            URL url = new URL(urlString);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            try {
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type","application/json");
                conn.setRequestProperty("Content-Length",""+Integer.toString(postData.getBytes("UTF-8").length));
                conn.setRequestProperty("Accept","application/json");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                // SEND BODY
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(postData.getBytes("UTF-8"));
                outputStream.close();


                // READ RESONSE CODE VIA: conn.getResponseCode();

                // READ RESPONSE

                String responseString = "";
                InputStream in = new BufferedInputStream(conn.getInputStream());
                int data = in.read();

                while (data != -1) {
                    char current = (char) data;
                    data = in.read();
                    responseString += current;
                }

                response = responseString;

            } finally {
                conn.disconnect();
            }


        } catch (Exception e){

        }

        return response;
    }

}
