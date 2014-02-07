/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend;

import celization.TurnEvent;
import celizationclient.backend.net.CElizationClient;
import java.util.ArrayList;

/**
 *
 * @author mjafar
 */
public abstract class FormsParent {

    protected CElizationClient client;
    protected CELizationClientRunner application;

    public FormsParent() {
        client = CElizationClient.getInstance();
    }

    protected void loginSuccess() {
        application.showGame();
    }

    public void setApplicationInstance(CELizationClientRunner app) {
        application = app;
    }

    public void clearToSend() {
    }

    public void setTurnEvents(ArrayList<TurnEvent> events) {
    }

    public void refreshInfo() {
    }
}
