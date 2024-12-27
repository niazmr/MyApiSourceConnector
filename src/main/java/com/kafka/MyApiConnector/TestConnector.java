package com.kafka.MyApiConnector;


import com.google.gson.Gson;

import java.time.ZonedDateTime;

public class TestConnector {

    public static void main(String[] args) {
//        String rawJson = "{\"weather\":{\"source_id\":303711,\"timestamp\":\"2024-11-28T20:00:00+01:00\",\"cloud_cover\":88,\"condition\":\"rain\",\"dew_point\":5.12,\"solar_10\":null,\"solar_30\":null,\"solar_60\":null,\"precipitation_10\":0.0,\"precipitation_30\":0.0,\"precipitation_60\":0.0,\"pressure_msl\":1019.8,\"relative_humidity\":92,\"visibility\":17612,\"wind_direction_10\":320,\"wind_direction_30\":317,\"wind_direction_60\":315,\"wind_speed_10\":31.0,\"wind_speed_30\":29.5,\"wind_speed_60\":29.5,\"wind_gust_direction_10\":310,\"wind_gust_direction_30\":310,\"wind_gust_direction_60\":320,\"wind_gust_speed_10\":40.0,\"wind_gust_speed_30\":43.9,\"wind_gust_speed_60\":44.6,\"sunshine_30\":null,\"sunshine_60\":null,\"temperature\":6.4,\"icon\":\"cloudy\"},\"sources\":[{\"id\":303711,\"dwd_station_id\":\"00433\",\"observation_type\":\"synop\",\"lat\":52.4676,\"lon\":13.402,\"height\":47.7,\"station_name\":\"Berlin-Tempelhof\",\"wmo_station_id\":\"10384\",\"first_record\":\"2024-11-27T12:30:00+00:00\",\"last_record\":\"2024-11-28T19:00:00+00:00\"}]}"; // Paste a sample API response here
//        Gson gson = new Gson();
//        try {
//            Response response = gson.fromJson(rawJson, Response.class);
//            System.out.println("Parsed response: " + response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

                String timestamp = "2024-11-28T23:00:00+01:00"; // Example timestamp
                try {
                    ZonedDateTime zdt = ZonedDateTime.parse(timestamp);
                    long epochTimestamp = zdt.toEpochSecond();
                    System.out.println("Epoch seconds: " + epochTimestamp);
                } catch (Exception e) {
                    System.err.println("Failed to parse timestamp: " + timestamp);
                    e.printStackTrace();
                }
            }
        }