package model;

import java.util.List;

public class Route {
    private final String from;
    private final String to;
    private final TransportType transport;
    private final int durationMinutes;
    private final int distanceKm;
    private final List<String> departureTimes;

    public Route(String from, String to, TransportType transport, int durationMinutes, int distanceKm, List<String> departureTimes) {
        this.from = from;
        this.to = to;
        this.transport = transport;
        this.durationMinutes = durationMinutes;
        this.distanceKm = distanceKm;
        this.departureTimes = departureTimes;
    }

    public String getFrom() { return from; }
    public String getTo() { return to; }
    public TransportType getTransport() { return transport; }
    public int getDurationMinutes() { return durationMinutes; }
    public int getDistanceKm() { return distanceKm; }
    public List<String> getDepartureTimes() { return departureTimes; }
}