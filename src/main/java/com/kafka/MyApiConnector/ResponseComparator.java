package com.kafka.MyApiConnector;

import java.util.Comparator;

public class ResponseComparator implements Comparator<Response> {

    @Override
    public int compare(Response r1, Response r2) {
        int cmp = Long.compare(r1.getEpochTimestamp(), r2.getEpochTimestamp());
        if (cmp != 0) {
            return cmp;
        }

        // Fallback comparison by station ID (if available)
        String s1 = r1.getStationIdSafe();
        String s2 = r2.getStationIdSafe();
        return s1.compareTo(s2);
    }
}