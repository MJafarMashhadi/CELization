/**
 *
 */
package celization.civilians;

import celization.GameParameters;

/**
 * @author mjafar
 *
 */
public final class Scholar extends Civilian {

    public Scholar(String name) {
        super(name);

        foodConsumption = GameParameters.scholarFoodConsumption;
        this.name = name;

        creationTime = GameParameters.scholarETA;
    }

    @Override
    public String getTypeString() {
        return "student";
    }

    public void finishInstantly() {
        while (!isMature()) {
            growUp();
        }
    }
}
