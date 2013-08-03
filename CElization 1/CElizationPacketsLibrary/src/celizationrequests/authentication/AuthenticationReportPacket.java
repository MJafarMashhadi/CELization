package celizationrequests.authentication;

import celizationrequests.CELizationRequest;

/**
 *
 * @author mjafar
 */
public class AuthenticationReportPacket extends CELizationRequest {

    private boolean status;

    public AuthenticationReportPacket(boolean status) {
        this.status = status;
    }

    public boolean isSuccessful() {
        return status;
    }
}
