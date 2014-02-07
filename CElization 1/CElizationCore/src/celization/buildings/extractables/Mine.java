package celization.buildings.extractables;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
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
        requiredBuildingTime = GameParameters.portETA;
        requiredResources = GameParameters.portMaterial;
    }

    @Override
    public boolean busy() {
        return false;
    }

    @Override
    public void step() {
        Worker w = null;
        double amount = 0;
        /**
         * add to the worker's experience
         */
        for (GameObjectID name : this.occupiedWorkers) {
            w = gameInstance.getWorkerByUID(name);
            if (this instanceof Farm) {
                for (int i = 0; i < getSize().col; i++) {
                    for (int j = 0; j < getSize().row; j++) {
                        amount += gameInstance.getMapBlock(new Coordinates(i + getLocation().col, j + getLocation().row)).
                                getResources().numberOfFood;
                    }
                }
                amount /= getSize().row * getSize().col;

                amount *= gameInstance.getParams().minesExtractionRatioFOOD
                        * (1 + w.getExperience(GameParameters.ExpAgriculture));
                w.extract(GameParameters.ExpAgriculture, amount);
            } else if (this instanceof StoneMine) {
                for (int i = 0; i < getSize().col; i++) {
                    for (int j = 0; j < getSize().row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfStones;
                    }
                }
                amount /= getSize().row * getSize().col;

                amount *= gameInstance.getParams().minesExtractionRatioSTONE
                        * (1 + w.getExperience(GameParameters.ExpStoneMining));
                w.extract(GameParameters.ExpStoneMining, amount);
            } else if (this instanceof GoldMine) {
                for (int i = 0; i < getSize().col; i++) {
                    for (int j = 0; j < getSize().row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfGolds;
                    }
                }
                amount /= getSize().row * getSize().col;

                amount *= gameInstance.getParams().minesExtractionRatioGOLD
                        * (1 + w.getExperience(GameParameters.ExpGoldMining));
                w.extract(GameParameters.ExpGoldMining, amount);
            } else if (this instanceof WoodCamp) {
                for (int i = 0; i < getSize().col; i++) {
                    for (int j = 0; j < getSize().row; j++) {
                        amount += gameInstance.getMapBlock(
                                new Coordinates(i + getLocation().col, j
                                + getLocation().row)).getResources().numberOfWoods;
                    }
                }
                amount /= getSize().row * getSize().col;

                amount *= gameInstance.getParams().minesExtractionRatioWOOD
                        * (1 + w.getExperience(GameParameters.ExpCarpentary));
                w.extract(GameParameters.ExpCarpentary, amount);
            }
        }
    }
}
