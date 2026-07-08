package com.kafka.MyApiConnector;


import com.google.gson.Gson;

import java.time.ZonedDateTime;

public class TestConnector {

    public static void main(String[] args) {

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