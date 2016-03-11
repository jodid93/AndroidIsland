package com.example.notandi.idleisland;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by thorgeir on 11.3.2016.
 */
public class ServerDatabaseAccess {

    private String mResult;

    private String httpConnectString;
    AsyncTask<String,Void,String> mTask;

    public ServerDatabaseAccess(){
        httpConnectString = "http://10.0.3.2:8094/";
        mTask = new ServerConnectTask();
    }


    enum Action {
        AUTH("auth/"), REGISTER("register/");
        private final String pos;
        Action( String position ){
            pos = position;
        }
        public String toStr(){
            return pos;
        }
    }

    public void authorization(String userName, String passWord){
        String action = Action.AUTH.toStr();
        String httpURI = httpConnectString + action + userName + "/" + passWord;
        Log.i("VALUE", action );
        Log.i("VALUE", httpURI);
        AsyncTask<String, Void, String> a = mTask.execute(httpURI);
        Log.i("VALUE","Server result " + mResult);
    }



    //
    //  CONNECTION TO SERVER USING TASK
    //

    public class ServerConnectTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        // LOCAL EMULATOR NETWROK INFORMATION ->
        // http://developer.android.com/tools/devices/emulator.html#networkaddresses

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... param) {
            String res = null;
            String url = param[0];

            HttpURLConnection conn = getConnection(url);

            try {
                showStatus(conn);

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                InputStream in = conn.getInputStream();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(conn.getResponseMessage() + ": with " + url);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();

                byte[] message = out.toByteArray();
                res = new String(message, StandardCharsets.UTF_8);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            mResult = result;
            // TODO: check this.exception
            // TODO: do something with the feed
        }


        private void showStatus( HttpURLConnection conn ) throws IOException{
            Log.d("CONNECTION", String.valueOf(conn));
            Log.d("CONNECTION", "Content Encoding -> "+conn.getContentEncoding());
            Log.d("CONNECTION","Response Message -> " + conn.getResponseMessage() );
            Log.d("CONNECTION","Permission -> " + conn.getPermission());
        }

        private HttpURLConnection getConnection(String urlString){
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = null;

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return conn;
        }
    }
}
