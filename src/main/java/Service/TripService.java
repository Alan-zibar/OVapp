package Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Station;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TripService {

    private final List<Station> stations = loadStations();

    private List<Station> loadStations() {
        InputStream inputStream = getClass().getResourceAsStream("/com/example/ovapp/data/stations.json");

        if (inputStream == null) {
            System.out.println("stations.json niet gevonden");
            return new ArrayList<>();
        }

        InputStreamReader reader = new InputStreamReader(inputStream);
        Type stationListType = new TypeToken<List<Station>>() {}.getType();

        return new Gson().fromJson(reader, stationListType);
    }

    public List<String> getStations() {
        List<String> stationNames = new ArrayList<>();

        for (Station station : stations) {
            stationNames.add(station.getName());
        }

        return stationNames;
    }

    public boolean isValidTrip(String from, String to) {
        return from != null
                && to != null
                && !from.isBlank()
                && !to.isBlank()
                && !from.equals(to)
                && getStations().contains(from)
                && getStations().contains(to);
    }
}
