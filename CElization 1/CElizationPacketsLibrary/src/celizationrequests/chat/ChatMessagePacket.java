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
public class ChatMessagePacket extends CELizationRequest {
    private String sender;
    private String reciever;
    private String message;

    /**
     * get message reciever username
     *
     * @return
     */
    public String getReciever() {
        return reciever;
    }

    /**
     * get message text
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *  constructor
     * @param to
     * @param message
     */
    public ChatMessagePacket(String from, String to, String message) {
        this.sender = from;
        this.reciever = to;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

}
