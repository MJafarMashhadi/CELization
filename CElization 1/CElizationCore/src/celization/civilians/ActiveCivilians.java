/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celization.civilians;

import celizationrequests.Coordinates;
import celization.GameParameters;
import celization.civilians.workeractions.Move;
import celization.civilians.workeractions.CivilianActions;
import celization.mapgeneration.astar.Node;
import java.util.LinkedList;

/**
 *
 * @author mjafar
 */
public abstract class ActiveCivilians extends Civilian {
    protected LinkedList<CivilianActions> actionsQueue;
    protected Coordinates nextPos;
    public CivilianState workState;

    public ActiveCivilians(String name) {
        super(name);
        this.actionsQueue = new LinkedList<>();
    }

    public void addAction(CivilianActions a) {
        actionsQueue.add(a);
        if (a instanceof Move) {
            getNextMove();
        }
    }

    public void cancelAllJobs() {
        actionsQueue.clear();
        nextPos = location;
        workState = CivilianState.Free;
    }

    /**
     * removes action from list when doing an action is completed
     */
    protected void firstActionDone() {
        if (actionsQueue.size() > 0) {
            actionsQueue.removeFirst();
        }
    }

    /**
     * get next position according to A* path finding algorithm
     */
    protected void getNextMove() {
        boolean haveMoveInQueue = false;
        Node whereToGo = null;
        for (CivilianActions firstAction : actionsQueue) {
            if (firstAction instanceof Move) {
                int index = ((Move) firstAction).getPathFinderIndex();
                if (index == -1) {
                    actionsQueue.removeFirst();
                    actionsQueue.addFirst(new Move(gameInstance.getPathFinder().newPathFinder(location, ((Move) firstAction).getDestination())));
                    index = ((Move) actionsQueue.getFirst()).getPathFinderIndex();
                }
                whereToGo = gameInstance.getPathFinder().getNextMove(index);
                haveMoveInQueue = true;
                if (whereToGo == null) {
                    haveMoveInQueue = false;
                    actionsQueue.removeFirst();
                }
            }
        }
        if (!haveMoveInQueue) {
            return;
        }
        nextPos = whereToGo.toCoordinates();
    }

    /**
     * whether worker is in an moving action
     *
     * @return
     */
    protected boolean isWalking() {
        if (actionsQueue.isEmpty()) {
            return false;
        }
        return actionsQueue.getFirst() instanceof Move;
    }

    /**
     * set new destination
     *
     * @param destination
     * @throws IllegalArgumentException
     */
    public void move(Coordinates destination) throws IllegalArgumentException {
        if (destination.row < 0 || destination.row >= gameInstance.getGameMap().getGameMapSize().row || destination.col < 0 || destination.col >= gameInstance.getGameMap().getGameMapSize().col) {
            throw new IllegalArgumentException("Out of map");
        }
        if (isWalking()) {
            // update destination
            int pathfinderIndex = ((Move) actionsQueue.getFirst()).getPathFinderIndex();
            gameInstance.getPathFinder().remove(pathfinderIndex);
            actionsQueue.removeFirst();
            actionsQueue.addFirst(new Move(gameInstance.getPathFinder().newPathFinder(location, destination)));
        } else {
            actionsQueue.add(new Move(destination));
        }
        getNextMove();
    }

    public void step() {
        if (actionsQueue.size() == 0) {
            return;
        }
        if (isWalking()) {
            stepMove();
        }
    }

    /**
     * if worker should walk do the moves
     */
    protected void stepMove() {
        /** go on one step */
        setLocation(nextPos);
        /** unlock visited cells */
        int diff = (GameParameters.workerUnlockArea.row - 1) / 2;
        gameInstance.unlockCells(new Coordinates(location.col - diff, location.row - diff), GameParameters.workerUnlockArea);
        getNextMove();
    }

}
