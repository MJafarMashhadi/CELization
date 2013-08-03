package judge;

import java.util.Map;
import java.util.HashMap;

public class Judge
{

    /**
     *  Starts the game
     *  @param map       Array of characters (P M F W) defining the map block types 
     *  @param goldMap   Array of integers (0-9) equal to the amount of gold in each block            
     *  @param stoneMap  Array of integers (0-9) equal to the amount of stone in each block
     *  @param lumberMap Array of integers (0-9) equal to the amount of lumber in each block
     *  @param foodMap   Array of integers (0-9) equal to the amount of food in each block
     *              
     */
    public static void start(char[][] map, int[][] goldMap, int[][] stoneMap, 
                             int[][] lumberMap, int[][] foodMap){
        // TODO
    }



    /**
     *  Retrieves info for an object in the game
     *
     *  @param id     The id of the object in the game to retrieve info for. If null then
     *                general info about the game will be returned
     *  @return A Map containing the request information
     *              
     */
    public static Map<String, String> info(String id){
        Map<String, String> map = new HashMap<String, String>();

        /* TODO
            
            for example:

            if(isWorker(id)){
                Worker w = getWorker(id);

                map.put("location", w.getRow() + " " + w.getCol());
                map.put("inventory", w.getInventory() + "/" + w.getMaxInventory() + " " + w.getInventoryType());
                map.put("occupation", w.getOccupation());
                map.put("buildings", w.getAvailableBuildings());
            }

        */

        return map;
    }



    /**
     *  Gives a command to a game object
     *
     *  @param id      The id of the object in the game to give a command to
     *  @param command The command to be given to the object
     *  @return A String holding the result of the given command
     *              
     */
    public static String action(String id, String command){

        // TODO

        return null;
    }


    /**
     *  Ends the current turn and moves to the next
     *
     *  @return An array of Strings holding messages for events that have occured
     *              
     */
    public static String[] nextTurn(){

        // TODO

        return null;
    }
}