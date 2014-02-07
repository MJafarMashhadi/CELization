package celizationrequests.turnaction;

import celizationrequests.CELizationRequest;

/**
 *
 * @author mjafar
 */
public class ClearToSendNewTurnsAction extends CELizationRequest {
    // Just it!
    private int thisTurnNumber;

    public ClearToSendNewTurnsAction(int thisTurnNumber) {
        this.thisTurnNumber = thisTurnNumber;
    }

    public int getThisTurnNumber() {
        return thisTurnNumber;
    }
    
}
