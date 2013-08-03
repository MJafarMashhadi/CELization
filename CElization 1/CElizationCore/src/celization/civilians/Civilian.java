/**
 *
 */
package celization.civilians;

import celization.CElization;
import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
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
    protected Coordinates location;
    protected GameState gameInstance;
    protected int creationTime;
    protected int creationPhase;
    protected GameObjectID UID;
    protected int outfitNumber;

    public final int getOutfitNumber() {
        return outfitNumber;
    }

    public Civilian(String name) {
        alive = true;
        location = Coordinates.ZERO;
        this.name = name;
        creationPhase = 0;
        outfitNumber = CElization.rndMaker.nextInt(16);
    }

    public final void die() {
        alive = false;
    }

    public final boolean stillAlive() {
        return alive;
    }

    public final Coordinates getLocation() {
        return location;
    }

    public final void setLocation(Coordinates newPosition) {
        location.row = newPosition.row;
        location.col = newPosition.col;
    }

    public final String getName() {
        return name;
    }

    public final int getFoodConsumption() {
        return foodConsumption;
    }

    @Override
    public final void injectGameInstance(GameState state) {
        gameInstance = state;
    }

    public final void growUp() {
        if (creationPhase < creationTime) {
            creationPhase++;
        }
    }

    public final boolean isMature() {
        return creationPhase >= creationTime;
    }

    public final int getAge() {
        return creationPhase;
    }

    public final int getETA() {
        return creationTime;
    }

    public final void setID(GameObjectID UID) {
        this.UID = UID;
    }

    public final GameObjectID getID() {
        return UID;
    }
}
