/**
 *
 */
package celization;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import java.util.HashMap;
import java.util.Random;

import celization.buildings.Building;
import celization.civilians.Civilian;
import celization.civilians.CivilianState;
import celization.civilians.Scholar;
import celization.civilians.Worker;
import celization.civilians.soldiers.JockeyHorseman;
import celization.civilians.soldiers.MaceHorseman;
import celization.civilians.soldiers.SapperInfantry;
import celization.civilians.soldiers.Soldier;
import celization.civilians.soldiers.SpearInfantry;
import celization.civilians.workeractions.Build;
import celization.civilians.workeractions.Move;
import celization.exceptions.BuildingBusyException;
import celization.exceptions.BuildingFullException;
import celization.exceptions.CannotWorkHereException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;
import celization.exceptions.MaximumCapacityReachedException;
import celization.exceptions.UserExistsException;
import celization.mapgeneration.GameMap;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import celization.mapgeneration.astar.AStar;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.turnaction.BuildingSellTurnAction;
import celizationrequests.turnaction.BuildingTrainTurnAction;
import celizationrequests.turnaction.MarketExchangeTurnAction;
import celizationrequests.turnaction.SoldierFightTurnAction;
import celizationrequests.turnaction.TurnAction;
import celizationrequests.turnaction.TurnActionsRequest;
import celizationrequests.turnaction.UniversityResearchTurnAction;
import celizationrequests.turnaction.WorkerBuildTurnAction;
import celizationrequests.turnaction.WorkerMoveTurnAction;
import celizationrequests.turnaction.WorkerWorkTurnAction;
import java.io.Serializable;
import java.util.ArrayList;
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
    private HashMap<String, UserInfo> users = new HashMap<>();
    /**
     * Game terrain
     */
    private GameMap gameMap;

    /**
     * Makes a new empty game with no users
     *
     */
    public CElization(PerlinNoiseParameters mapParameters, Coordinates mapSize) {
        gameMap = new GameMap(this, mapParameters, mapSize);

    }

    /**
     * checks a username for existance in this game session
     *
     * @param username
     * @return
     */
    public boolean isUserInGame(String username) {
        return users.containsKey(username);
    }

    /**
     * get users list for map generation algorithm
     *
     * @return
     */
    public HashMap<String, UserInfo> getUsersList() {
        return users;
    }

    /**
     * getter for game map
     *
     * @return
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Events occured in last turn of a user
     *
     * @param username
     * @return
     */
    public ArrayList getEvents(String username) {
        return users.get(username).getGame().getEvents();
    }

    /**
     * Add a new user to game
     *
     * @param userInfo
     * @throws UserExistsException
     */
    public void userRegister(UserInfo userInfo) throws UserExistsException {
        String username = userInfo.getUsername();

        if (users.containsKey(username)) {
            throw new UserExistsException();
        }
        users.put(username, userInfo);
        userInfo.setGameInstance(new GameState(this, username));
        userInfo.getGame().newGame();
    }

    /**
     * checks username and password to match for a user to log in
     *
     * @param authenticationInfo
     * @return
     */
    public boolean userAuthenticate(AuthenticationRequest authenticationInfo) {
        String username = authenticationInfo.getUserName();

        if (!users.containsKey(username)) {
            return false;
        }

        return users.get(username).authenticate(authenticationInfo.getPassword());
    }

    /**
     * Get A* path finder instance. usually used to change map
     *
     * @param username
     * @return
     */
    public AStar getPathFinder(String username) {
        return users.get(username).getGame().getPathFinder();
    }

    /**
     * What turn game is in
     *
     * @return
     */
    public int getTurn() {
        if (users.size() == 0) {
            return 0;
        }
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

    /**
     * Get number of registered users in the game
     *
     * @return
     */
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

    /**
     * Process a bunch of requests that is sent to game
     *
     * @param request
     */
    public void processRequests(String username, TurnActionsRequest request) {
        for (TurnAction nextRequest : request.getRequests()) {
            processRequest(nextRequest, username);
            System.out.println("** Processed action. (" + nextRequest.getClass().getSimpleName() + ")");
        }
        //
        users.get(username).getGame().step();
    }

    /**
     * Process one request at a time
     *
     * @param action
     * @param username
     */
    private void processRequest(TurnAction action, String username) {
        GameObjectID target = action.getTarget();
        Class targetType = target.getType();
        GameState game = users.get(username).getGame();

        if (targetType == celization.buildings.HeadQuarters.class) {
            if (action instanceof BuildingTrainTurnAction) {
                try {
                    game.makeNewWorker();
                } catch (BuildingBusyException | InsufficientResourcesException ex) {
                }
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.buildings.University.class) {
            if (action instanceof BuildingTrainTurnAction) {
                try {
                    users.get(username).getGame().makeNewScholar();
                } catch (InsufficientResourcesException | BuildingBusyException | MaximumCapacityReachedException e) {
                }
            } else if (action instanceof UniversityResearchTurnAction) {
                try {
                    users.get(username).getGame().getUniversity().startResearch(((UniversityResearchTurnAction) action).getResearchName());
                } catch (InsufficientResourcesException | InsufficientResearchesException | BuildingBusyException ex) {
                    // never happens
                }
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.buildings.Port.class) {
            if (action instanceof BuildingSellTurnAction) {
                getBuildingByUID(target, username).sell();
            } else if (action instanceof BuildingTrainTurnAction) {
                users.get(username).getGame().makeNewBoat(target);
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.buildings.Market.class) {
            if (action instanceof BuildingSellTurnAction) {
                getBuildingByUID(target, username).sell();
            } else if (action instanceof MarketExchangeTurnAction) {
                MarketExchangeTurnAction exchangeAction = (MarketExchangeTurnAction) action;
                try {
                    users.get(username).getGame().exchangeResources(exchangeAction.getAmount(), exchangeAction.getSourceType(), exchangeAction.getDestinationType());
                } catch (InsufficientResourcesException ex) {
                    // Won't happen
                }
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.buildings.extractables.Mine.class
                || targetType == celization.buildings.extractables.Farm.class
                || targetType == celization.buildings.extractables.GoldMine.class
                || targetType == celization.buildings.extractables.StoneMine.class
                || targetType == celization.buildings.extractables.WoodCamp.class) {
            if (action instanceof BuildingSellTurnAction) {
                getBuildingByUID(target, username).sell();
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.civilians.soldiers.JockeyHorseman.class
                || targetType == celization.civilians.soldiers.MaceHorseman.class
                || targetType == celization.civilians.soldiers.SapperInfantry.class
                || targetType == celization.civilians.soldiers.SpearInfantry.class) {
            if (action instanceof SoldierFightTurnAction) {
                fight(username, target, ((SoldierFightTurnAction) action).getTargetLocation());
            } else {
                return; // Wrong action :-? How come?
            }
        } else if (targetType == celization.civilians.Worker.class) {
            Worker w = getWorkerByUID(target, username);
            if (action instanceof WorkerBuildTurnAction) {
                WorkerBuildTurnAction buildAction = (WorkerBuildTurnAction) action;

                if (!game.aroundBuilding(w.getLocation(), buildAction.getBuildingLocation(), buildAction.getBuildingType())) {
                    Coordinates d = Coordinates.ZERO;
                    boolean foundProperPlace = false;
                    Coordinates size;
                    try {
                        size = (Coordinates) buildAction.getBuildingType().getField("size").get(null);
                    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
                        // never happens
                        return;
                    }
                    for (int r = -1; r <= size.row; r++) {
                        for (int c = -1; c <= size.col; c++) {
                            d.row = buildAction.getBuildingLocation().row + r;
                            d.col = buildAction.getBuildingLocation().col + c;
                            try {
                                if (game.aroundBuilding(d, buildAction.getBuildingLocation(), buildAction.getBuildingType())
                                        && gameMap.isWalkable(d, username)
                                        && !game.underBuilding(d, buildAction.getBuildingLocation(), size)) {
                                    foundProperPlace = true;
                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                    if (foundProperPlace) {
                        w.addAction(new Move(d));
                    } else {
                        // Invalid locaiton
                    }
                }
                w.addAction(new Build(buildAction.getTarget(), buildAction.getBuildingLocation(), buildAction.getBuildingType()));
            } else if (action instanceof WorkerMoveTurnAction) {
                if (w.workState == CivilianState.Free) {
                    w.move(((WorkerMoveTurnAction) action).getDestination());
                } else {
                    // Client can't send such turn action
                    //throw new BuildingBusyException();
                    return;
                }
            } else if (action instanceof WorkerWorkTurnAction) {
                try {
                    users.get(username).getGame().goAndWork(target, ((WorkerWorkTurnAction) action).getWorkPlace());
                } catch (BuildingFullException | CannotWorkHereException e) {
                    // Client can't send such turn action
                }

            } else {
                return; // Wrong action :-? How come?
            }
        } else {
            // Wrong ID dude!
        }
    }

    /**
     * Attack another user's building or soldier
     *
     * @param fighterUser
     * @param fighterSoldier
     * @param targetLocation
     * @throws IllegalArgumentException
     */
    public void fight(String fighterUser, GameObjectID fighterSoldier, Coordinates targetLocation)
            throws IllegalArgumentException {
        // find out what and whose target is
        GameObjectID target = null;
        String targetOwner = null;
        for (String t : users.keySet()) {
            target = users.get(t).getGame().isASoldierThere(targetLocation);
            if (target != null) {
                targetOwner = t;
                break;
            }
            if (users.get(t).getGame().underBuilding(targetLocation)) {
                target = users.get(t).getGame().getLastBuildingAffectedByUnderBuildingMethod();
            }
            if (target != null) {
                targetOwner = t;
                break;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("There's no soldier nor buildings there");
        }

        // check target type
        if (target.getType() == JockeyHorseman.class
                || target.getType() == MaceHorseman.class
                || target.getType() == SpearInfantry.class
                || target.getType() == SapperInfantry.class) {
            // fight with the soldier
            Soldier offenderSoldier, defenderSoldier;
            offenderSoldier = (Soldier) getCivilianByUID(fighterSoldier, fighterUser);
            defenderSoldier = (Soldier) getCivilianByUID(target, targetOwner);
            // apply health and kill rules
            float offenderSxH = offenderSoldier.calculateScore(defenderSoldier, true) * offenderSoldier.getHealth();
            float defenderSxH = defenderSoldier.calculateScore(offenderSoldier, false) * defenderSoldier.getHealth();
            if (offenderSxH > defenderSxH) {
                defenderSoldier.die();
                offenderSoldier.decreseHealth(defenderSxH / offenderSoldier.calculateScore(defenderSoldier, true));
                offenderSoldier.setLocation(targetLocation);
                users.get(targetOwner).getGame().addEvent(new TurnEvent(TurnEvent.WAR_DEATH, defenderSoldier.getName()));
            } else {
                offenderSoldier.die();
                offenderSoldier.decreseHealth(offenderSxH / defenderSoldier.calculateScore(defenderSoldier, true));
                users.get(fighterUser).getGame().addEvent(new TurnEvent(TurnEvent.WAR_DEATH, offenderSoldier.getName()));
            }
        } else if (target.getType() == Worker.class) {
            Soldier offender = (Soldier) getCivilianByUID(target, fighterUser);
            getCivilianByUID(target, targetOwner).die();
            offender.setLocation(getCivilianByUID(target, targetOwner).getLocation());
        } else {
            // destroy that building
            Soldier offenderSoldier;
            offenderSoldier = users.get(fighterUser).getGame().getSoldierByUID(fighterSoldier);
            offenderSoldier.setLocation(targetLocation);
            getBuildingByUID(target, targetOwner).destroy();
        }
    }
}
