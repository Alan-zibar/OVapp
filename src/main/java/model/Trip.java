package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trip {

    private final String departureTime;
    private final String scheduledArrivalTime;
    private final String expectedArrivalTime;
    private final String fromStation;
    private final String toStation;
    private final String transportMode;
    private final String transportType;
    private final String departureLocation;
    private final String arrivalLocation;
    private final int delayMinutes;
    private final int durationMinutes;
    private final int distanceKm;
    private final int transfers;
    private final List<TripStop> stops;

    public Trip(String departureTime,
                String scheduledArrivalTime,
                String expectedArrivalTime,
                String fromStation,
                String toStation,
                String transportMode,
                String transportType,
                String departureLocation,
                String arrivalLocation,
                int delayMinutes,
                int durationMinutes,
                int distanceKm,
                int transfers,
                List<TripStop> stops) {
        this.departureTime = departureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.expectedArrivalTime = expectedArrivalTime;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.transportMode = transportMode;
        this.transportType = transportType;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.delayMinutes = delayMinutes;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.transfers = transfers;
        this.stops = new ArrayList<>(stops);
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public String getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public String getTransportMode() {
        return transportMode;
    }

    public String getTransportType() {
        return transportType;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public int getDelayMinutes() {
        return delayMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getDistanceKm() {
        return distanceKm;
    }

    public int getTransfers() {
        return transfers;
    }

    public List<TripStop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    public static class TripStop {
        private final String time;
        private final String stationName;
        private final String stopType;
        private final String location;

        public TripStop(String time, String stationName, String stopType, String location) {
            this.time = time;
            this.stationName = stationName;
            this.stopType = stopType;
            this.location = location;
        }

        public String getTime() {
            return time;
        }

        public String getStationName() {
            return stationName;
        }

        public String getStopType() {
            return stopType;
        }

        public String getLocation() {
            return location;
        }
    }
}
