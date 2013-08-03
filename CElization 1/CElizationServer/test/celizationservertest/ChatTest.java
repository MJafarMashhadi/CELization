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
import celizationrequests.authentication.AuthenticationRequest;
import celizationrequests.authentication.AuthenticationReportPacket;
import celizationrequests.chat.ChatErrorPacket;
import celizationrequests.chat.ChatMessagePacket;
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
public class ChatTest {

    private CElizationServer serverInstance;

    private Coordinates mapSize = new Coordinates(10, 10);
    private PerlinNoiseParameters perlinParameters = new PerlinNoiseParameters(0.5, 0.3, 20, 1, 1);
    private GameSession game;

    private UserInfo[] users = new UserInfo[3];

    private Socket[] userConnections = new Socket[3];
    private ObjectInputStream[] userInputStreams = new ObjectInputStream[3];
    private ObjectOutputStream[] userOutputStreams = new ObjectOutputStream[3];

    @Before
    public void setUp() {
        serverInstance = new CElizationServer();
        game = new GameSession("ChatGame", new CElization(perlinParameters, mapSize));

        try {
            serverInstance.addGame(0, game);
        } catch (IOException ex) {
            Logger.getLogger(ChatTest.class.getName()).log(Level.WARNING, null, ex);
        }

        for (int number = 0; number < 3; number++) {
            users[number] = new UserInfo("User" + (number + 1), "Pass" + (number + 1));
            try {
                serverInstance.addUser(users[number], game.getPort());
            } catch (UserExistsException ex) {
                // should not happen
            }
        }

        Integer port = game.getPort();
        for (int i = 0; i < 3; i++) {
            try {
                userConnections[i] = new Socket("127.0.0.1", port.intValue());
                userInputStreams[i] = new ObjectInputStream(userConnections[i].getInputStream());
                userOutputStreams[i] = new ObjectOutputStream(userConnections[i].getOutputStream());
                login(i);
            } catch (Exception ex) {
                fail(ex.getMessage());
            }
        }
    }

    @After
    public void tearDown() {
        try {
            for (int i = 0; i < 3; i++) {
                userInputStreams[i].close();
                userOutputStreams[i].close();
                userConnections[i].close();
            }
            serverInstance.shutDown();
        } catch (IOException ex) {
        }
    }

    @Test
    public void publicMessageTest() {
        try {
            game.sendMessage("User1", null, "PUBLIC MESSAGE");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        Object[] userIncomes = new Object[3];
        for (int i = 0; i < 3; i++) {
            userIncomes[i] = recieve(i);
            assertTrue(userIncomes[i] instanceof ChatMessagePacket);
            assertEquals(((ChatMessagePacket) userIncomes[i]).getMessage(), "PUBLIC MESSAGE");
            assertEquals(((ChatMessagePacket) userIncomes[i]).getReciever(), "User1");
        }

    }

    @Test
    public void userToUserMessageTest() {
        try {
            game.sendMessage("User1", "User2", "from u1 to u2");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        Object user2income = recieve(1);

        assertTrue(user2income instanceof ChatMessagePacket);
        assertEquals(((ChatMessagePacket) user2income).getMessage(), "from u1 to u2");
        assertEquals(((ChatMessagePacket) user2income).getReciever(), "User1");
    }


    @Test
    public void offlineChatTest() {
        try {
            game.sendMessage("User1", "User100", "from u1 to u100");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        Object user1income = recieve(0);

        assertTrue(user1income instanceof ChatErrorPacket);
    }

    private Object recieve(int i) {
        Object response;
        try {
            response = userInputStreams[i].readObject();
            assertTrue(response instanceof celizationrequests.CELizationRequest);
            return response;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }
        return null;
    }

    private void login(int i) {
        try {
            AuthenticationRequest loginPacket = new AuthenticationRequest("User" + (i + 1), "Pass" + (i + 1));
            userOutputStreams[i].writeObject(loginPacket);

            Object response;
            response = recieve(i);

            assertTrue(response instanceof AuthenticationReportPacket);
            assertTrue(((AuthenticationReportPacket) response).isSuccessful());
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }
}