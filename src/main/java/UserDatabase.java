import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDatabase {

    //////////////////// "GREEN"  /////////////////

    private Map<String, List<Integer>> userFavourites = new HashMap<>();

    public void addFavourite(String user, int itemID) {
//        // If the user doesn't exist in the map, create a new list for their favourites
//        userFavourites.computeIfAbsent(user, k -> new ArrayList<>()).add(itemID);
    }

    public int[] getFavourites(String user) {
//        // Get the user's favourites or return an empty array if the user doesn't exist
//        List<Integer> favourites = userFavourites.getOrDefault(user, new ArrayList<>());
//        // Convert the list of integers to an array of primitives
//        return favourites.stream().mapToInt(i -> i).toArray();
        return new int[]{};
    }

    public void removeFavourite(String user, int itemID) {
//        // Remove the item from the user's favourites if the user exists
//        if (userFavourites.containsKey(user)) {
//            userFavourites.get(user).remove(Integer.valueOf(itemID));
//        }
    }

    //////////////////// "REFACTOR"  /////////////////

//    private Map<String, List<Integer>> userFavourites = new HashMap<>();
//
//    public void addFavourite(String user, int itemID) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        userFavourites.computeIfAbsent(user, k -> new ArrayList<>()).add(itemID);
//    }
//
//    public int[] getFavourites(String user) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        List<Integer> favourites = userFavourites.getOrDefault(user, new ArrayList<>());
//        return convertListToArray(favourites);
//    }

//    public void removeFavourite(String user, int itemID) {
//        if (user == null) {
//            throw new IllegalArgumentException("User cannot be null");
//        }
//        if (userFavourites.containsKey(user)) {
//            userFavourites.get(user).remove(Integer.valueOf(itemID));
//        }
//    }
////
//    private int[] convertListToArray(List<Integer> list) {
//        return list.stream().mapToInt(i -> i).toArray();
//    }
}
