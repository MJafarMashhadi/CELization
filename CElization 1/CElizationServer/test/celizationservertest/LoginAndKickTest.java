package celizationservertest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import celization.CElization;
import celizationrequests.Coordinates;
import celization.UserInfo;
import celization.exceptions.UserExistsException;
import celization.mapgeneration.perlinnoise.PerlinNoiseParameters;
import celizationserver.core.CElizationServer;
import celizationserver.core.GameSession;
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.AuthenticationReportPacket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mjafar
 */
public class LoginAndKickTest {

    private CElizationServer serverInstance;
    private Coordinates mapSize = new Coordinates(40, 40);
    private PerlinNoiseParameters perlinParameters = new PerlinNoiseParameters(0.5, 0.3, 20, 1, 1);
    private GameSession game;
    private UserInfo user;
    private Socket userConnection;

    @Before
    public void setUp() {
        serverInstance = new CElizationServer();

        game = new GameSession("LoginTest", new CElization(perlinParameters, mapSize));
        try {
            serverInstance.addGame(0, game);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        user = new UserInfo("UserName", "CorrectPassword");
        try {
            serverInstance.addUser(user, game.getPort());
        } catch (UserExistsException ex) {
            // should not happen
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
    public void successfulLoginTest() {
        Object response;
        try {
            response = sendAuthenticationPacket("UserName", "CorrectPassword");
        } catch (IOException ex) {
            fail();
            return;
        }

        assertTrue(response instanceof AuthenticationReportPacket);
        assertTrue(((AuthenticationReportPacket) response).isSuccessful());

    }

    @Test
    public void unSuccessfulLoginTest() {
        Object response;
        try {
            response = sendAuthenticationPacket("UserName", "WrongPassword");
        } catch (IOException ex) {
            fail();
            return;
        }

        assertTrue(response instanceof AuthenticationReportPacket);
        assertFalse(((AuthenticationReportPacket) response).isSuccessful());
    }

    @Test
    public void unSuccessfulLogin2Test() {
        Object response;
        try {
            response = sendAuthenticationPacket("Not-Exist-Username", "WrongPassword");
        } catch (IOException ex) {
            fail();
            return;
        }

        assertTrue(response instanceof AuthenticationReportPacket);
        assertFalse(((AuthenticationReportPacket) response).isSuccessful());
    }

    @Test
    public void kickUserTest() {
        successfulLoginTest();
        assertEquals(game.getOnlineCount(), 1);
        game.kickUser("UserName");
        assertEquals(game.getOnlineCount(), 0);
    }

    private Object sendAuthenticationPacket(String username, String password) throws IOException {
        ObjectOutputStream oos;
        ObjectInputStream ois;
        try {
            Integer port = game.getPort();
            try {
                userConnection = new Socket("127.0.0.1", port.intValue());
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
            AuthenticationRequest loginPacket = new AuthenticationRequest(username, password);

            oos = new ObjectOutputStream(userConnection.getOutputStream());
            ois = new ObjectInputStream(userConnection.getInputStream());
            oos.writeObject(loginPacket);

            return ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            fail(ex.getMessage());
            return null;
        } finally {
            userConnection.close();
        }
    }
}