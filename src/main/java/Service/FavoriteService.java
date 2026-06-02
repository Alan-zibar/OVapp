package Service;

import java.util.ArrayList;
import java.util.List;

public class FavoriteService {

    private static List<model.Trip> favorites = new ArrayList<>();

    public static void addFavorite(model.Trip trip){
        favorites.add(trip);
    }

    public static List<model.Trip> getFavorites() {


        return favorites;
    }

    public static void removeFavorite(model.Trip trip) {

        favorites.remove(trip); }

    public static boolean isFavorite(model.Trip trip) {
        return favorites.contains(trip);

    }    

    }






