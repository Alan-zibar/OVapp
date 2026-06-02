package Service;

import model.Trip;

public class TripSearchState {

    private static String fromStation;
    private static String toStation;
    private static String selectedDate;
    private static String selectedTime;
    private static String selectedTransport = "TRAIN";
    private static int visibleResults = 2;
    private static Trip selectedTrip;

    public static void saveSearch(String from, String to, String date, String time) {
        fromStation = from;
        toStation = to;
        selectedDate = date;
        selectedTime = time;
        selectedTransport = "TRAIN";
        visibleResults = 2;
        selectedTrip = null;
    }

    public static String getFromStation() {
        return fromStation;
    }

    public static String getToStation() {
        return toStation;
    }

    public static String getSelectedDate() {
        return selectedDate;
    }

    public static String getSelectedTime() {
        return selectedTime;
    }

    public static void saveResultsViewState(String transport, int resultsCount) {
        selectedTransport = transport;
        visibleResults = resultsCount;
    }

    public static String getSelectedTransport() {
        return selectedTransport;
    }

    public static int getVisibleResults() {
        return visibleResults;
    }

    public static void saveSelectedTrip(Trip trip) {
        selectedTrip = trip;
    }

    public static Trip getSelectedTrip() {
        return selectedTrip;
    }
}
