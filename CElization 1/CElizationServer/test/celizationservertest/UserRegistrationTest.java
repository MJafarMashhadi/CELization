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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author mjafar
 */
public class UserRegistrationTest {

    private CElizationServer serverInstance;
    private Coordinates mapSize = new Coordinates(10, 10);
    private PerlinNoiseParameters perlinParameters = new PerlinNoiseParameters(0.5, 0.3, 20, 1, 1);
    private GameSession game;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        serverInstance = new CElizationServer();
        game = new GameSession("Game 1", new CElization(perlinParameters, mapSize));
        try {
            serverInstance.addGame(0, game);
        } catch (IOException ex) {
        }
    }

    @After
    public void tearDown() {
        try {
            serverInstance.shutDown();
        } catch (IOException ex) {
            Logger.getLogger(LoginAndKickTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void registerUserTest() {
        for (int i = 0; i < 3; i++) {
            System.out.println("made user" + (i+1));
            try {
                serverInstance.getGame("Game 1").getGame().userRegister(new UserInfo("Game1User" + (i + 1), "Pass" + (i + 1)));
                System.out.println("registered user" + (i+1));
            } catch (UserExistsException ex) {
                // should not happen
            }
        }
        System.out.println("registerUserFinished");
    }

    @Test
    public void duplicateUserTest() throws UserExistsException {
        System.out.println("double chek");
        serverInstance.getGame("Game 1").getGame().userRegister(new UserInfo("Game1User1", "Pass1"));
        exception.expect(UserExistsException.class);
        serverInstance.getGame("Game 1").getGame().userRegister(new UserInfo("Game1User1", "Pass1"));
    }
}
