/**
 *
 */
package celization.buildings.extractables;

import celization.Coordinates;
import celization.GameObjectID;
import celization.GameParameters;
import celization.buildings.Building;
import celization.civilians.Worker;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public abstract class Mine extends Building implements Extractable, Serializable {

    /**
     *
     */
    public Mine() {
        super();
        requiredBuildingTime = gameInstance.getParams().portETA;
        requiredResources = GameParameters.portMaterial;
        size = GameParameters.portSize;
    }

    @Override
    public boolean busy() {
        return false;
    }

    @Override
    public GameObjectID step() {
        if (!this.isActive()) {
            return null;
        }
        Worker w = null;
        double amount = 0;
        /**
         * add to the worker's experience
         */
        for (GameObjectID name : this.occupiedWorkers) {
            w = gameInstance.getWorkerByUID(name);
            if (this instanceof Farm) {
                for (int i = 0; i < size.col; i++) {
                    for (int j = 0; j < size.row; j++) {
                        amount += gameInstance.getMapBlock(new Coordinates(i + getLocation().col, j + getLocation().row)).
                                getResources().numberOfFood;
                    }
                }
                amount /= size.row * size.col;

                amount *= gameInstance.getParams().minesExtractionRatioFOOD
                        * (1 + w.getExperience(GameParameters.ExpAgriculture));
                w.extract(GameParameters.ExpAgriculture, amount);
            } else if (this instanceof StoneMine) {
                for (int i = 0; i < size.col; i++) {
                    for (int j = 0; j < size.row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfStones;
                    }
                }
                amount /= size.row * size.col;

                amount *= gameInstance.getParams().minesExtractionRatioSTONE
                        * (1 + w.getExperience(GameParameters.ExpStoneMining));
                w.extract(GameParameters.ExpStoneMining, amount);
            } else if (this instanceof GoldMine) {
                for (int i = 0; i < size.col; i++) {
                    for (int j = 0; j < size.row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfGolds;
                    }
                }
                amount /= size.row * size.col;

                amount *= gameInstance.getParams().minesExtractionRatioGOLD
                        * (1 + w.getExperience(GameParameters.ExpGoldMining));
                w.extract(GameParameters.ExpGoldMining, amount);
            } else if (this instanceof WoodCamp) {
                for (int i = 0; i < size.col; i++) {
                    for (int j = 0; j < size.row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfWoods;
                    }
                }
                amount /= size.row * size.col;

                amount *= gameInstance.getParams().minesExtractionRatioWOOD
                        * (1 + w.getExperience(GameParameters.ExpCarpentary));
                w.extract(GameParameters.ExpCarpentary, amount);
            }
        }

        return null;
    }
}
