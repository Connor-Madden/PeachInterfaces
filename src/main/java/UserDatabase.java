import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDatabase {

    //////////////////// "GREEN"  /////////////////

    private final String jsonFileName = "favourites.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // reads a hashmap from the json file
    public Map<String, List<Integer>> jsonToHashmap(){
        try{
            // make sure the file exists
            File file = new File(jsonFileName);
            if (!file.exists()){
                return new HashMap<>();
            }

            // read and parse the json
            return objectMapper.readValue(
                    file,
                    new TypeReference<Map<String, List<Integer>>>() {}
            );

        }catch (Exception error){
            error.printStackTrace();
            System.out.println("Error while reading json (jsonToHashmap)");
        }
        return null;
    }

    // writes the hashmap to the json file
    public void hashmapToJson(Map<String, List<Integer>> userFavourites){
        try{
            // write the json file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFileName), userFavourites);

        }catch (Exception error){
            error.printStackTrace();
            System.out.println("Error while writing json (hashmapToJson)");
        }
    }

    public void addFavourite(String user, int itemID) {
//        // If the user doesn't exist in the map, create a new list for their favourites
//        userFavourites.computeIfAbsent(user, k -> new ArrayList<>()).add(itemID);

        try {
            // first fetch from the json
            Map<String, List<Integer>> userFavourites = jsonToHashmap();

            // add if the item to the array
            if (!userFavourites.containsKey(user)) {
                userFavourites.put(user, new ArrayList<>());
            }else{
                userFavourites.get(user).add(itemID);
            }

            // write back to the json
            hashmapToJson(userFavourites);

        }catch(Exception error) {
            error.printStackTrace();
            System.out.println("Error while adding favourite item (addFavourite)");
        }
    }

    public int[] getFavourites(String user) {
//        // Get the user's favourites or return an empty array if the user doesn't exist
//        List<Integer> favourites = userFavourites.getOrDefault(user, new ArrayList<>());
//        // Convert the list of integers to an array of primitives
//        return favourites.stream().mapToInt(i -> i).toArray();

        try {
            // fetch the hashmap from json file
            Map<String, List<Integer>> userFavourites = jsonToHashmap();

            // get the favourites and return them as an int[]
            List<Integer> favourites = userFavourites.getOrDefault(user, new ArrayList<>());
            return favourites.stream().mapToInt(i -> i).toArray();

        }catch(Exception error) {
            error.printStackTrace();
            System.out.println("Error while getting favourite items (getFavourites)");
        }
        return null;
    }

    public void removeFavourite(String user, int itemID) {
//        // Remove the item from the user's favourites if the user exists
//        if (userFavourites.containsKey(user)) {
//            userFavourites.get(user).remove(Integer.valueOf(itemID));
//        }
        try {
            // fetch the favourites from the json
            Map<String, List<Integer>> userFavourites = jsonToHashmap();

            // remove the user if it exists
            if (userFavourites.containsKey(user)) {
                userFavourites.get(user).remove(itemID);
            }

            // write back to the json
            hashmapToJson(userFavourites);

        }catch (Exception error) {
            error.printStackTrace();
            System.out.println("Error while removing favourite item (removeFavourite)");
        }
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
