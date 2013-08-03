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
    private GameState gameInstance;

    public GameState getGame() {
        return gameInstance;
    }

    public void setGameInstance(GameState gameInstance) {
        this.gameInstance = gameInstance;
    }
    private String password;

    public UserInfo(String u, String p) {
        setUsernameAndPassword(u, p);
    }

    public UserInfo(String u, String p, GameState game) {
        this.gameInstance = game;
        setUsernameAndPassword(u, p);
    }

    private void setUsernameAndPassword(String u, String p) {
        username = u;
        // TODO: use hash
        password = p;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        // TODO: use hash
        return (this.password.equals(password));
    }
}
