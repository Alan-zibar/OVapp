package Service;

import model.Route;
import model.TransportType;
import java.util.ArrayList;
import java.util.List;

public class RouteService {
    private final List<Route> routes = new ArrayList<>();

    public RouteService() {
        initRoutes();
    }

    private void initRoutes() {
        List<String> allHours = new ArrayList<>();
        for (int i = 6; i <= 22; i++) {
            allHours.add(String.format("%02d:00", i));
        }

        // Trein
        routes.add(new Route("Amsterdam Centraal", "Utrecht Centraal", TransportType.TREIN, 35, 42, allHours));
        routes.add(new Route("Utrecht Centraal", "Den Haag Centraal", TransportType.TREIN, 40, 55, allHours));
        routes.add(new Route("Den Haag Centraal", "Rotterdam Centraal", TransportType.TREIN, 25, 25, allHours));
        routes.add(new Route("Rotterdam Centraal", "Amsterdam Centraal", TransportType.TREIN, 50, 70, allHours));
        routes.add(new Route("Amsterdam Centraal", "Den Haag Centraal", TransportType.TREIN, 45, 60, allHours));
        // Bus
        routes.add(new Route("Amsterdam Centraal", "Utrecht Centraal", TransportType.BUS, 50, 45, allHours));
        routes.add(new Route("Utrecht Centraal", "Den Haag Centraal", TransportType.BUS, 55, 50, allHours));
        routes.add(new Route("Den Haag Centraal", "Rotterdam Centraal", TransportType.BUS, 35, 30, allHours));
        routes.add(new Route("Rotterdam Centraal", "Amsterdam Centraal", TransportType.BUS, 65, 75, allHours));
        routes.add(new Route("Amsterdam Centraal", "Rotterdam Centraal", TransportType.BUS, 60, 70, allHours));
    }

    public Route findRoute(String from, String to, TransportType type) {
        for (Route r : routes) {
            if (r.getFrom().equals(from) && r.getTo().equals(to) && r.getTransport() == type) {
                return r;
            }
        }
        return null;
    }

    public List<String> getAllStations() {
        List<String> stations = new ArrayList<>();
        for (Route r : routes) {
            if (!stations.contains(r.getFrom())) stations.add(r.getFrom());
            if (!stations.contains(r.getTo())) stations.add(r.getTo());
        }
        return stations;
    }
}