/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package celizationrequests.turnaction;

import celizationrequests.CELizationRequest;
import java.util.ArrayList;

/**
 *
 * @author MohammmadJafar MashhadiEbrahim
 */
public class TurnEvents extends CELizationRequest {
    private ArrayList events;

    public ArrayList getEvents() {
        return events;
    }

    public TurnEvents(ArrayList events) {
        this.events = events;
    }
}
