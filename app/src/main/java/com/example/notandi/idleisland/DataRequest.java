package com.example.notandi.idleisland;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by thorgeir on 5.3.2016.
 */
public class DataRequest {


    public String getData(String urlSpec) throws IOException {
        URL url= new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        return "this is message";
    }



}
