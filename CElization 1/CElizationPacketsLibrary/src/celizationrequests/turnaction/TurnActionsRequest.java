/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.turnaction;

import celizationrequests.CELizationRequest;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author mjafar
 */
public class TurnActionsRequest extends CELizationRequest {

    private ArrayList<TurnAction> requests;

    public ArrayList<TurnAction> getRequests() {
        return requests;
    }

    public TurnActionsRequest() {
        requests = new ArrayList<>();
    }

    public void addRequest(TurnAction action) {
        requests.add(action);
    }
 
    public void clear() {
        requests.clear();
    }
}
