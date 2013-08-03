/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationrequests.turnaction;

import celizationrequests.GameObjectID;

/**
 *
 * @author mjafar
 */
public class MarketExchangeTurnAction extends TurnAction {
    private String sourceType;
    private String destinationType;
    private int amount;

    public String getSourceType() {
        return sourceType;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public MarketExchangeTurnAction(GameObjectID id, String type1, String type2, int amount) {
        super(id);
        sourceType = type1;
        destinationType = type2;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

}
