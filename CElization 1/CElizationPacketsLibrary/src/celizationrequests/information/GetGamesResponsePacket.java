/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.information;

import celizationrequests.CELizationRequest;
import java.util.HashMap;

/**
 *
 * @author mjafar
 */
public class GetGamesResponsePacket extends CELizationRequest {
    private HashMap<String, Integer> gamesList;

    public GetGamesResponsePacket() {
        gamesList = new HashMap<>();
    }

    public void put(String name, Integer port) {
        gamesList.put(name, port);
    }
    
    public HashMap<String, Integer> getGamesList() {
        return gamesList;
    }
}
