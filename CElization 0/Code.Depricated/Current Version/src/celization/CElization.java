/**
 *
 */
package celization;

import java.util.HashMap;
import java.util.Random;

import celization.buildings.Building;
import celization.civilians.Civilian;
import celization.civilians.Scholar;
import celization.civilians.Worker;
import celization.exceptions.UserExistsException;
import celization.mapgeneration.GameMap;
import celization.mapgeneration.PerlinNoiseParameters;
import celization.mapgeneration.astar.AStar;
import celizationserver.core.packets.authentication.AuthenticationPacket;
import java.io.Serializable;
import java.util.Iterator;

/**
 * @author mjafar
 *
 */
public class CElization implements Serializable {

    /**
     * random maker that used in the game wherever we need a random-generated
     * number
     */
    public static Random rndMaker = new Random();
    /**
     * Game States like number of people and equipment and...
     */
    // usage:
    // users.put("administrator", new GameState(this, "administrator"));
    //public HashMap<String, GameState> users = new HashMap<>();
    public HashMap<String, UserInfo> users = new HashMap<>();
    /**
     * Game terrain
     */
    public GameMap gameMap;

    /**
     * New Game constructor
     */
    public CElization(PerlinNoiseParameters mapParameters, Coordinates mapSize) {
        gameMap = new GameMap(this, mapParameters, mapSize);

    }

    public void userRegister(UserInfo userInfo) throws UserExistsException {
        if (users.containsKey(userInfo.getUsername())) {
            throw new UserExistsException();
        }
        userInfo.setGameInstance(new GameState(this, userInfo.getUsername()));
        users.put(userInfo.getUsername(), userInfo);
    }

    public boolean userAuthenticate(AuthenticationPacket authenticatenInfo) {
        String username = authenticatenInfo.getUserName();
        if (!users.containsKey(username)) {
            return false;
        }

        return users.get(username).authenticate(authenticatenInfo.getPassword());
    }

    public AStar getPathFinder(String username) {
        return users.get(username).getGame().getPathFinder();
    }

    public int getTurn() {
        Iterator<String> usersIterator = users.keySet().iterator();
        String userName;
        int maxTurnNumber = 0;
        while (usersIterator.hasNext()) {
            userName = usersIterator.next();
            if (users.get(userName).getGame().getTurnNumber() > maxTurnNumber) {
                maxTurnNumber = users.get(userName).getGame().getTurnNumber();
            }
        }
        return maxTurnNumber;
    }

    public int getUsersCount() {
        return users.size();
    }

    /**
     * Get a Building
     *
     * @param UID
     * @return Building instance
     */
    public Building getBuildingByUID(GameObjectID UID, String userID) {
        return users.get(userID).getGame().getBuildingByUID(UID);
    }

    /**
     * get Civilian
     *
     * @param UID
     * @return civilian instance
     */
    public Civilian getCivilianByUID(GameObjectID UID, String userID) {
        return users.get(userID).getGame().getCivilianByUID(UID);
    }

    /**
     * Get a worker
     *
     * @param UID
     * @return civilian instance casted to "Worker"
     */
    public Worker getWorkerByUID(GameObjectID UID, String userID) {
        return users.get(userID).getGame().getWorkerByUID(UID);
    }

    /**
     * Get a scholar
     *
     * @param UID
     * @return civilian instance casted to "Scholar"
     */
    public Scholar getScholarByUID(GameObjectID UID, String userID) {
        return users.get(userID).getGame().getScholarByUID(UID);
    }
}
