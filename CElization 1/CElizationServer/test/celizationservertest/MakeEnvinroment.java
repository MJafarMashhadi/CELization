/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationservertest;

import celization.CElization;
import celizationrequests.Coordinates;
import celization.UserInfo;
import celization.exceptions.UserExistsException;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import celizationserver.core.CElizationServer;
import celizationserver.core.GameSession;
import celizationserver.swing.ManagerForm;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author mjafar
 */
public class MakeEnvinroment {

    private CElizationServer serverInstance;
    private Coordinates mapSize = new Coordinates(10, 10);
    private PerlinNoiseParameters perlinParameters = new PerlinNoiseParameters(0.5, 0.3, 20, 1, 1);
    private GameSession[] games = new GameSession[5];
    private UserInfo[][] users = new UserInfo[5][10];

    public MakeEnvinroment() {
        serverInstance = new CElizationServer();
        for (int gameNumber = 0; gameNumber < games.length; gameNumber++) {
            games[gameNumber] = new GameSession("Game " + gameNumber, new CElization(perlinParameters, mapSize));
            try {
                serverInstance.addGame(new Integer(0), games[gameNumber]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            users[gameNumber] = new UserInfo[10];
            for (int userNumber = 0; userNumber < users[gameNumber].length; userNumber++) {
                users[gameNumber][userNumber] = new UserInfo("User(" + gameNumber + "," + userNumber + ")", "pass");
                try {
                    serverInstance.addUser(users[gameNumber][userNumber], games[gameNumber].getPort());
                } catch (UserExistsException ex) {
                }
            }
        }
    }

    public CElizationServer getServerInstance() {
        return serverInstance;
    }

    public static void main(String[] args) {
        MakeEnvinroment s = new MakeEnvinroment();
        Scanner in = new Scanner(System.in);
        String input;
        input = in.next();
        if ("show".equals(input)) {
            ManagerForm frmMain = new ManagerForm(s.getServerInstance());
            frmMain.setVisible(true);
            frmMain.refresh();
        }
    }
}
