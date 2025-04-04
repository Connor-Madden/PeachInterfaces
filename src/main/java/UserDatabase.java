import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a database for storing a users favourite item id's in a JSON
 *
 * provides functionality for:
 *  add favourite items for a user
 *  get a users favourite items
 *  remove favourite items from a user
 *  clear all favourites
 *
 * all operations work with the json: "favourites.json"
 */
public class UserDatabase {

    private final String jsonFileName = "favourites.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * reads the json database and returns its data as a hashmap
     *
     * the structure of the return is: {username : {list of favourite item id's}}
     *
     * @return a hashmap containing user favourites, or an empty hashmap if the file doesn't exist
     */
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

    /**
     * takes a hashmap and write the data to a json file
     *
     * @param userFavourites a map containing favourites in the format: {username : {list of favourite item id's}}
     */
    public void hashmapToJson(Map<String, List<Integer>> userFavourites){
        try{
            // write the json file
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonFileName), userFavourites);

        }catch (Exception error){
            error.printStackTrace();
            System.out.println("Error while writing json (hashmapToJson)");
        }
    }

    /**
     * Adds a favourite item to the database for a specified user
     *
     * @param user the username to add the favourite item for (cannot be null)
     * @param itemID the id of the item to add as favourite
     *
     * @throws IllegalArgumentException if the user parameter is null
     */
    public void addFavourite(String user, int itemID) {
//        // If the user doesn't exist in the map, create a new list for their favourites
//        userFavourites.computeIfAbsent(user, k -> new ArrayList<>()).add(itemID);
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        try {
            // first fetch from the json
            Map<String, List<Integer>> userFavourites = jsonToHashmap();

            // add if the item to the array
            if (!userFavourites.containsKey(user)) {
                userFavourites.put(user, new ArrayList<>());
            }
            userFavourites.get(user).add(itemID);

            // write back to the json
            hashmapToJson(userFavourites);

        }catch(Exception error) {
            error.printStackTrace();
            System.out.println("Error while adding favourite item (addFavourite)");
        }
    }

    /**
     * gets a specified username's favourite items
     *
     * @param user the username to retrieve the favourites of
     * @return returns an array of favourite item id's, or an empty array if the user has no favourites
     *
     * @throws IllegalArgumentException if the user parameter is null
     */
    public int[] getFavourites(String user) {
//        // Get the user's favourites or return an empty array if the user doesn't exist
//        List<Integer> favourites = userFavourites.getOrDefault(user, new ArrayList<>());
//        // Convert the list of integers to an array of primitives
//        return favourites.stream().mapToInt(i -> i).toArray();

        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
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

    /**
     * removes a specified item from a specified username
     *
     * @param user the username to remove the item from
     * @param itemID the id of the item to remove from the user
     *
     * @throws IllegalArgumentException if the user's parameter is null
     */
    public void removeFavourite(String user, int itemID) {
//        // Remove the item from the user's favourites if the user exists
//        if (userFavourites.containsKey(user)) {
//            userFavourites.get(user).remove(Integer.valueOf(itemID));
//        }
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        try {
            // fetch the favourites from the json
            Map<String, List<Integer>> userFavourites = jsonToHashmap();

            // remove the user if it exists
            if (userFavourites.containsKey(user)) {
                userFavourites.get(user).remove(Integer.valueOf(itemID));
            }

            // write back to the json
            hashmapToJson(userFavourites);

        }catch (Exception error) {
            error.printStackTrace();
            System.out.println("Error while removing favourite item (removeFavourite)");
        }
    }

    /**
     * clears all users favourites from the database (for testing)
     *
     * @NOTE: this method is intended for testing and should not be used in production
     */
    public void clearDatabase() {
        try {
            hashmapToJson(new HashMap<>());
        } catch (Exception error) {
            error.printStackTrace();
            System.out.println("Error while clearing database (clearDatabase)");
        }
    }
}