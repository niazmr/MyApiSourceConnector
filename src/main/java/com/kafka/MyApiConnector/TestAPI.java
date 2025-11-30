package com.kafka.MyApiConnector;
import java.net.URL;
import java.net.HttpURLConnection;

public class TestAPI {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://api.brightsky.dev/current_weather?lat=52.52&lon=13.4");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int code = con.getResponseCode();
        System.out.println("API Response Code: " + code);
    }
}