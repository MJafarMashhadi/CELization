/**
 *
 */
package celization.civilians;

import celization.Coordinates;
import celization.GameObjectID;
import celization.GameState;
import celization.NeedsGameInstance;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public abstract class Civilian implements NeedsGameInstance, Serializable {

    protected String name;
    protected boolean alive;
    protected int foodConsumption;
    protected Coordinates position;
    protected GameState gameInstance;
    protected int creationTime;
    protected int creationPhase;
    protected GameObjectID UID;

    public Civilian(String name) {
        alive = true;
        position = new Coordinates(0, 0);
        this.name = name;
        creationPhase = 0;
    }

    public void die() {
        alive = false;
    }

    public boolean stillAlive() {
        return alive;
    }

    public Coordinates getPos() {
        return position;
    }

    public void setPosition(Coordinates newPosition) {
        position.row = newPosition.row;
        position.col = newPosition.col;
    }

    public String getName() {
        return name;
    }

    public int getFoodConsumption() {
        return foodConsumption;
    }

    public abstract String getTypeString();

    public void injectGameInstance(GameState state) {
        gameInstance = state;
    }

    public void growUp() {
        if (creationPhase < creationTime) {
            creationPhase++;
        }
    }

    public boolean isMature() {
        return creationPhase >= creationTime;
    }

    public int getAge() {
        return creationPhase;
    }

    public int getETA() {
        return creationTime;
    }

    public void setID(GameObjectID UID) {
        this.UID = UID;
    }

    public GameObjectID getID() {
        return UID;
    }
}
