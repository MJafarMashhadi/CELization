/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.chat;

import celizationrequests.CELizationRequest;

/**
 *
 * @author mjafar
 */
public class ChatErrorPacket extends CELizationRequest {
    
    String username;

    public String getUsername() {
        return username;
    }

    public ChatErrorPacket(String username) {
        this.username = username;
    }
}
