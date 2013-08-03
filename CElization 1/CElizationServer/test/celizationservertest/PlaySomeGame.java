package celizationservertest;

import celization.GameParameters;
import celization.GameState;
import celization.TurnEvent;
import celization.buildings.HeadQuarters;
import celization.buildings.University;
import celizationrequests.CELizationRequest;
import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import celizationrequests.authentication.AuthenticationReportPacket;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.LogoutPacket;
import celizationrequests.information.GetGamesRequestPacket;
import celizationrequests.information.GetGamesResponsePacket;
import celizationrequests.information.GetInformationPacket;
import celizationrequests.turnaction.BuildingTrainTurnAction;
import celizationrequests.turnaction.ClearToSendNewTurnsAction;
import celizationrequests.turnaction.TurnActionsRequest;
import celizationrequests.turnaction.TurnEvents;
import celizationrequests.turnaction.UniversityResearchTurnAction;
import celizationrequests.turnaction.WorkerBuildTurnAction;
import celizationrequests.turnaction.WorkerMoveTurnAction;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author mjafar
 */
public class PlaySomeGame {

    Socket connection;
    ObjectOutputStream os;
    ObjectInputStream is;
    GameState gameState;
    GetInformationPacket getInfo = new GetInformationPacket();
    TurnActionsRequest actionsRequest = new TurnActionsRequest();

    public PlaySomeGame login() throws IOException, ClassNotFoundException {
        connection = new Socket("127.0.0.1", CELizationRequest.gamesListListeningPort.intValue());
        os = new ObjectOutputStream(connection.getOutputStream());
        is = new ObjectInputStream(connection.getInputStream());
        os.writeObject(new GetGamesRequestPacket());
        GetGamesResponsePacket games = (GetGamesResponsePacket) is.readObject();
        Integer port = games.getGamesList().get(games.getGamesList().keySet().toArray()[0]);
        os.close();
        is.close();
        connection.close();
        connection = new Socket("127.0.0.1", port.intValue());
        os = new ObjectOutputStream(connection.getOutputStream());
        is = new ObjectInputStream(connection.getInputStream());
        os.writeObject(new AuthenticationRequest("user1", "password"));
        System.out.println(((AuthenticationReportPacket)is.readObject()).isSuccessful());
        return this;
    }

    public PlaySomeGame play() throws IOException, ClassNotFoundException {
        getGameState();
        System.out.println(getDerpLocation());
//        moveWorker(gameState.getDerpID(), 10, 10);
//        System.out.println(getDerpLocation());
        Set<GameObjectID> civiliansSet = gameState.getCivilians().keySet();
        actionsRequest = new TurnActionsRequest();
        int i = 1;
        for (GameObjectID c : civiliansSet) {
            actionsRequest.addRequest(moveWorker(c, -i, -i, false));
            i++;
        }
        os.writeObject(actionsRequest);
        nextTurn(i);
//        System.out.println(getDerpLocation());
//        make(gameState.getDerpID(), HeadQuarters.class);
//        nextTurn((int)GameParameters.headQuartersETA);
//        newWorker();
//        newWorker();
//        moveWorker(gameState.getDerpID(), 4, 0);
//        make(gameState.getDerpID(), University.class);
//        nextTurn((int)GameParameters.universityETA);
//        newScholar();
//        newScholar();
//        research("Resources");
//        research("Economy");
//        research("Military");
//        research("Science");
//        research("Basic Combat");
//        research("Mining");
//        research("Lumber Mill");
//        research("Agriculture");
//        research("Fishing");
        research("Alphabet");
        research("School");
        return this;
    }

    public PlaySomeGame logout() throws IOException {
        os.writeObject(new LogoutPacket());
        os.flush();
        os.close();
        is.close();
        connection.close();
        return this;
    }

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        new PlaySomeGame().login().play().logout();
    }

    public void nextTurn(int numberOfTurns) {
        for (int turnNumber = 0; turnNumber < numberOfTurns; turnNumber++) {
            try {
                os.writeObject(new TurnActionsRequest());
                os.flush();
                Object response;
                do {
                    response = is.readObject();
                    if (response.getClass() == TurnEvents.class) {
                        TurnEvents t = (TurnEvents) response;
                        for (Iterator<TurnEvent> it = t.getEvents().iterator(); it.hasNext();) {
                            TurnEvent event = it.next();
                            System.out.println(event.getEventType() + "---" + event.getDescription());
                        }
                    }
                } while (!(response instanceof ClearToSendNewTurnsAction));
            } catch (IOException | ClassNotFoundException c) {
                c.printStackTrace();
            }
        }
    }

    public void getGameState() throws IOException, ClassNotFoundException {
        os.writeObject(getInfo);
        os.flush();

        Object response;
        do {
            response = is.readObject();
            System.err.println(response.getClass().getSimpleName());
        } while (!(response instanceof GameState));
        gameState = (GameState) response;
    }

    public Coordinates getDerpLocation() {
        return gameState.getCivilianByUID(gameState.getDerpID()).getLocation();
    }

    public void research(String name) throws IOException {
        UniversityResearchTurnAction a = new UniversityResearchTurnAction(new GameObjectID(University.class, 1), name);
        actionsRequest = new TurnActionsRequest();
        actionsRequest.addRequest(a);
        os.writeObject(actionsRequest);
        os.flush();
        nextTurn(gameState.getCourseManager().get(name).getETA());
    }

    public void moveWorker(GameObjectID id, int col, int row) throws IOException {
        Coordinates location;
        location = gameState.getCivilianByUID(id).getLocation();
        location.col += col;
        location.row += row;
        WorkerMoveTurnAction moveAction = new WorkerMoveTurnAction(id, location);
        actionsRequest = new TurnActionsRequest();
        actionsRequest.addRequest(moveAction);
        os.writeObject(actionsRequest);
        os.flush();
        nextTurn((int) (Math.abs(row) + Math.abs(col)));
    }
    public WorkerMoveTurnAction moveWorker(GameObjectID id, int col, int row, boolean a) throws IOException {
        Coordinates location;
        location = gameState.getCivilianByUID(id).getLocation();
        location.col += col;
        location.row += row;
        WorkerMoveTurnAction moveAction = new WorkerMoveTurnAction(id, location);
        return moveAction;
    }

    public void newWorker() throws IOException {
        BuildingTrainTurnAction trainWorker = new BuildingTrainTurnAction(new GameObjectID(HeadQuarters.class, 0));
        actionsRequest = new TurnActionsRequest();
        actionsRequest.addRequest(trainWorker);
        os.writeObject(actionsRequest);
        os.flush();
        nextTurn(GameParameters.workerETA);
    }

    public void newScholar() throws IOException {
        BuildingTrainTurnAction trainWorker = new BuildingTrainTurnAction(new GameObjectID(University.class, 1));
        actionsRequest = new TurnActionsRequest();
        actionsRequest.addRequest(trainWorker);
        os.writeObject(actionsRequest);
        os.flush();
        nextTurn(GameParameters.scholarETA);
    }

    public void make(GameObjectID w, Class b) throws IOException {
        Coordinates loc = gameState.getCivilianByUID(w).getLocation().clone();
        loc.col++;
        loc.row++;
        WorkerBuildTurnAction build = new WorkerBuildTurnAction(w, loc, b);
        actionsRequest = new TurnActionsRequest();
        actionsRequest.addRequest(build);
        os.writeObject(actionsRequest);
        os.flush();
    }
}