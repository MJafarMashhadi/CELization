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
public class UniversityResearchTurnAction extends TurnAction {
    private String researchName;

    public String getResearchName() {
        return researchName;
    }
    
    public UniversityResearchTurnAction(GameObjectID id, String name) {
        super(id);
        researchName = name;
    }

}
