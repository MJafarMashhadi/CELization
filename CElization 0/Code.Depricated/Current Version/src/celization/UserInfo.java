/**
 *
 */
package celization;

import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class UserInfo implements Serializable {

    private String username;
    private String password;
    private GameState gameInstance;

    public void setUsernameAndPassword(String u, String p) {
        username = u;
        // TODO: use hash
        password = p;
    }

    public UserInfo(String u, String p) {
        setUsernameAndPassword(u, p);
    }

    public UserInfo(String u, String p, GameState g) {
        setUsernameAndPassword(u, p);
        setGameInstance(g);
    }

    public void setGameInstance(GameState g) {
        gameInstance = g;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        // TODO: use hash
        return (password == this.password);
    }

    public GameState getGame() {
        return gameInstance;
    }
}
