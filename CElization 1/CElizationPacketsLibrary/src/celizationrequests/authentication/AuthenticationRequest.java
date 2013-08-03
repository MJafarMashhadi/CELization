package celizationrequests.authentication;

import celizationrequests.CELizationRequest;
import java.io.Serializable;

/**
 *
 * @author mjafar
 */
public class AuthenticationRequest extends CELizationRequest {
    private String username;
    private String password;

    public AuthenticationRequest(String u, String p) {
        setUserPass(u, p);
    }

    public void setUserPass(String u, String p) {
        username = u;
        password = p;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
