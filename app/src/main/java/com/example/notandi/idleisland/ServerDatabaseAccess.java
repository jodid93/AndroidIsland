package com.example.notandi.idleisland;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;


//
// INFORMATION
//

// IN GENERAL ...
//  this is a connector to the server database
//  via http protocol using  AsyncTask technique.
//

//
// DRAWBACK =
//

// = security
//  - The http url request to the server is not secure.
//  - The connection string to database shouldn't be inside the class
//
// = implementation
//  -   Connection to database goes through MVC spring framework
//      It is more efficient to go straight through the database
//      connection itself instead of using the MVC. One thing that
//      the current connection model has advance over the straight
//      connection to database is that in the future, when the server
//      is more complex, the MVC controls all the request to database
//      and other stuff so then it is good to have organized Server(MVC).



// HOW TO DO =

// = Asynchronous call
//
//
// onlineDB.authorizationAsync("a","b");
// ....later on, get the result ->
// String res = onlineDB.getResult();
// ....this will return null if task has not finish
// otherwise res will be the result.

//
// = Synchronous call
//
//
// String res = onlineDB.authorizationSync("a","b");
//  NOTE. this will stop main UI thread and complete
//  the server request.

//
//
//
//[method] =
//
//
//




/**
 * Created by thorgeir on 11.3.2016.
 */
public class ServerDatabaseAccess {

    private String mResult;
    private String httpConnectString;
    private static ServerDatabaseAccess sInstance;
    private String SP = "/"; //URL splitter

    //TODO: add the connection string to ENV file or as a parameter
    public ServerDatabaseAccess(){
        httpConnectString = "http://10.0.3.2:8094/";
    }

    public static synchronized ServerDatabaseAccess getInstance() {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.w w w
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ServerDatabaseAccess();
        }
        return sInstance;
    }

    public enum Action {
        AUTH("auth/"), REGISTER("register/"), USERDATA("userdata/"),
        USEREXIST("userexist/"), ADD_PENDING("addpending/"),
        GET("GET"), POST("POST"),ASYNC("async"), SYNC("sync");
        private final String pos;
        Action( String position ){
            pos = position;
        }
        public String toStr(){
            return pos;
        }
    }


    // (synchronous call)
    // Returns the String "true" if userName and passWord
    // exists in server Database otherwise String "false"
    public String authorizationSync(String userName, String passWord){
        return authCheck(userName, passWord, Action.SYNC);
    }

    // (Asynchronous call)
    // Post: Local variable mResult has the value "true" if userName and passWord
    // exists in server Database, if not, it has the value "false".
    // mResult has the value null if the request to the server hasn't finish yet.
    public String authorizationAsync(String userName, String passWord){
        return authCheck(userName, passWord, Action.ASYNC);
    }

    private String authCheck(String un, String pw, Action runAction ){
        String restURI=Action.AUTH.toStr()+un+SP+pw;
        return exe(runAction, Action.GET, restURI);
    }

    // returns true if username exist in
    // the database server otherwise false
    public Boolean userNameExistsSync(String userName){
        return userNameExists(Action.SYNC, userName);
    }

    private Boolean userNameExists( Action runAction, String username) {
        String restURI=Action.USEREXIST.toStr()+ username;
        return exe(runAction, Action.GET, restURI).equals("true");
    }

    public void registerAsync(String userName, String passWord, String userData){
        String restURI = null;
        String register = Action.REGISTER.toStr();
        String changedUserData = userData.replace(".","%2E");
        try {
            String encodedUserData=URLEncoder.encode(changedUserData,"UTF-8");
            restURI=register+ userName+SP+ passWord+SP+encodedUserData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String res = exe(Action.ASYNC, Action.GET, restURI);
    }

    public void addPendingAsync(String requester, String receiver){
        addPending(Action.ASYNC, requester, receiver);
    }

    private void addPending(Action runMethod, String requester, String receiver){
        String restURI = Action.ADD_PENDING.toStr()+requester+SP+receiver;
        String res = exe(runMethod,Action.POST, restURI);
    }

    public void createNewUserSync(String userData){
        createNewUser(Action.SYNC, userData);
    }

    public void createNewUserAsync(String userData){
        createNewUser(Action.ASYNC, userData);
    }

    private void createNewUser(Action runMethod, String data){
        String restURI = runMethod.toStr() + data;
        String res = exe(runMethod, Action.GET, restURI);
    }


    public String getUserDataSync(String username){
        return getUserData(Action.SYNC, username);
    }

    public String getUserDataAsync(String username){
        return getUserData(Action.ASYNC, username);
    }

    public void setUserDataAsync( String data ){
        postUserData(Action.ASYNC, data);
    }

    private String getUserData(Action runAction, String username){
        Action uriAction = Action.USERDATA;
        String httpURI=httpConnectString+uriAction.toStr()+username;
        Log.i("VALUE", uriAction.toStr());
        Log.i("VALUE", httpURI);
        return execute(httpURI, runAction, Action.GET);
    }

    private void postUserData(Action runAction, String data){
        String restURI = Action.USERDATA.toStr()+data;
        String res = exe(runAction, Action.POST, restURI);
    }


    private String exe(Action runAction, Action method, String restURI){
        String httpURI=httpConnectString+restURI;
        Log.i("VALUE", runAction.toStr());
        Log.i("VALUE", httpURI);
        return execute(httpURI, runAction, method);
    }



    //Executes a request to server using new AsyncTask
    private String execute(String httpURI, Action runAction, Action method){
        AsyncTask<String,Void,String> task = new ServerConnectTask();
        String res = null;
        String met = method.toStr();

        switch (runAction) {
            case ASYNC:
                task.execute(httpURI, met);
                res = mResult;
                break;

            case SYNC:
                try {
                    res = task.execute(httpURI, met).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                mResult = res;
                break;
        }
        return res;
    }


    // Return the result value from the latest ServerConnectTask call.
    // Return null if the request to server hasn't finish or
    // this method have been called more than one times in a row.
    public String getResult(){
        String returnValue = mResult;
        mResult = null;
        return returnValue;
    }

    //
    //  CONNECTION TO THE SERVER USING ASYNC TASK
    //
    private class ServerConnectTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        // LOCAL EMULATOR NETWROK INFORMATION ->
        // http://developer.android.com/tools/devices/emulator.html#networkaddresses

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... param) {
            String res = null;
            String url = param[0];
            String method = param[1];

            Log.i("DOINBACKGROUND"," method is -> " + method);

            HttpURLConnection conn = getConnection(url, method);

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

        //TODO: notify the user if result is null
        @Override
        protected void onPostExecute(String result) {
            Log.d("SERVER REQUEST","Request to server has finish and result is " + result);
            mResult = result;
            Log.d("SERVER REQUEST","Local variable should have same result -> " + mResult);
            // TODO: check this.exception
        }


        private void showStatus( HttpURLConnection conn ) throws IOException{
            Log.d("CONNECTION", String.valueOf(conn));
            Log.d("CONNECTION", "Content Encoding -> "+conn.getContentEncoding());
            Log.d("CONNECTION","Response Message -> " + conn.getResponseMessage() );
            Log.d("CONNECTION","Permission -> " + conn.getPermission());
        }

        private HttpURLConnection getConnection(String urlString, String method){
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = null;

            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(method);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return conn;
        }
    }
}
