/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.backend.net;

import celizationclient.frontend.GameMainFrameController;
import celizationrequests.chat.ChatErrorPacket;
import celizationrequests.chat.ChatMessagePacket;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mjafar
 */
public class FetchChatMessages extends TimerTask {

    CElizationClient client;
    GameMainFrameController formController;

    public FetchChatMessages(CElizationClient client, GameMainFrameController formController) {
        this.client = client;
        this.formController = formController;
    }

    @Override
    public void run() {
        /// Upadate messages
        final Class messagePacketClass = ChatMessagePacket.class;
        final Class errorPacketClass = ChatErrorPacket.class;

        while (client.getListenerThread().hasResponse(messagePacketClass)) {
            ChatMessagePacket messagePacket = (ChatMessagePacket) client.getListenerThread().getResponse(messagePacketClass);
            formController.newMessage(messagePacket.getSender(), messagePacket.getMessage());
        }
        while (client.getListenerThread().hasResponse(errorPacketClass)) {
            ChatErrorPacket error;
            error = (ChatErrorPacket) client.getListenerThread().getResponse(ChatErrorPacket.class);
            formController.userWentOffline(error.getUsername());
        }
        formController.refreshMessagesCounter();
        /// update online users
        ArrayList<String> usersList;
        try {
            formController.setOnlineList(client.getOnlineUsers());
        }catch (SocketException e){
            this.cancel();
        } catch (IOException ex) {
            Logger.getLogger(FetchChatMessages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
