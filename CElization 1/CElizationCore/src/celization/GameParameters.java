/**
 *
 */
package celization;

import celizationrequests.Coordinates;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public final class GameParameters implements Serializable {
    private static final long serialVersionUID = -2093932595623018453L;
    //
    public Boolean canGetTax = false;
    /// Science - Food - Wood - Gold - Stone
    public int foodCapacity = 200;
    public int woodCapacity = 200;
    public int goldCapacity = 200;
    public int stoneCapacity = 200;
    /// Buildings sizes
    public static final Coordinates headQuartersSize = new Coordinates(3, 3);
    public static final Coordinates universitySize = new Coordinates(3, 3);
    public static final Coordinates goldMineSize = new Coordinates(2, 2);
    public static final Coordinates stoneMineSize = new Coordinates(2, 2);
    public static final Coordinates farmSize = new Coordinates(2, 2);
    public static final Coordinates woodCampSize = new Coordinates(1, 1);
    public static final Coordinates stockpileSize = new Coordinates(2, 2);
    public static final Coordinates marketSize = new Coordinates(3, 3);
    public static final Coordinates portSize = new Coordinates(2, 2);
    public static final Coordinates barracksSize = new Coordinates(2, 2);
    public static final Coordinates stableSize = new Coordinates(2, 2);
    /// Buildings ETAs
    public static double headQuartersETA = 2;
    public static double universityETA = 2;
    public static double goldMineETA = 6;
    public static double stoneMineETA = 6;
    public static double farmETA = 6;
    public static double woodCampETA = 6;
    public static double stockpileETA = 15;
    public static double marketETA = 20;
    public static double portETA = 24;
    public static double barracksETA = 15;
    public static double stableETA = 20;
    /// People ETAs
    public static final int workerETA = 2;
    public static final int scholarETA = 6;
    public static final int sapperETA = 8;
    public static final int spearETA = 8;
    public static final int maceETA = 14;
    public static final int jockeyETA = 10;
    /// Equipment ETAs
    public static final int boatETA = 8;
    /// Building resources
    public static final NaturalResources headQuartersMaterial = new NaturalResources(0, 10, 20, 0, 0);
    public static final NaturalResources universityMaterial = new NaturalResources(0, 20, 20, 0, 0);
    public static final NaturalResources goldMineMaterial = new NaturalResources(50, 50, 50, 0, 0);
    public static final NaturalResources stoneMineMaterial = new NaturalResources(30, 20, 20, 0, 0);
    public static final NaturalResources farmMaterial = new NaturalResources(10, 10, 10, 0, 0);
    public static final NaturalResources woodCampMaterial = new NaturalResources(20, 0, 20, 0, 0);
    public static final NaturalResources stockpileMaterial = new NaturalResources(10, 100, 100, 0, 0);
    public static final NaturalResources marketMaterial = new NaturalResources(60, 120, 120, 0, 0);
    public static final NaturalResources portMaterial = new NaturalResources(20, 160, 140, 0, 0);
    public static final NaturalResources barracksMaterial = new NaturalResources(20, 50, 200, 0, 0);
    public static final NaturalResources stableMaterial = new NaturalResources(50, 50, 250, 0, 0);
    /// Civilians resources
    public static final NaturalResources workerMaterial = new NaturalResources(10, 0, 0, 0, 0);
    public static final NaturalResources scholarMaterial = new NaturalResources(40, 0, 0, 0, 0);
    public static final NaturalResources sapperMaterial = new NaturalResources(30, 0, 80, 0, 0);
    public static final NaturalResources spearMaterial = new NaturalResources(30, 0, 50, 0, 0);
    public static final NaturalResources maceMaterial = new NaturalResources(60, 0, 100, 0, 0);
    public static final NaturalResources jockeyMaterial = new NaturalResources(50, 0, 80, 0, 0);
    /// Equipment resources
    public static final NaturalResources boatMaterial = new NaturalResources(20, 0, 80, 0, 0);
    /// Food Consumptions
    public static final int scholarFoodConsumption = 1;
    public static final int workerFoodConsumption = 2;
    public static final int soldierFoodConsumption = 3;
    public static final NaturalResources StorageExtraCapacity = new NaturalResources(100, 100, 100, 100, 0);
    //// Soldiers parameters
    /// scores
    // against eachother
    public static final int spear_infantryScore = 30;
    public static final int spear_horsemanScore = 80;
    public static final int sapper_infantryScore = 60;
    public static final int sapper_horsemanScore = 10;
    public static final int mace_infantryScore = 50;
    public static final int mace_horsemanScore = 10;
    public static final int jockey_infantryScore = 30;
    public static final int jockey_horsemanScore = 50;
    // defensive/offensive
    public static float infantryOffensive = 20;
    public static float infantryDefensive = 60;
    public static float maceOffensive = 90;
    public static float maceDefensive = 10;
    public static float jockeyOffensive = 60;
    public static float jockeyDefensive = 10;
    /// health
    public static final float infantryHealth = 200;
    public static final float horsemanHealth = 300;
    /// Exchange rates
    public static final int priceOfGold = 1;
    public static final int priceOfWood = 6;
    public static final int priceOfStone = 3;
    public static final int priceOfFood = 10;
    /// maximum things
    public int maximumNumberOfStudents = 10;
    public int workerPocketCapacity = 30;
    public int maximumNumberOfWorkers = 1; // can be *2 with team work
    /// productions
    public static final int boatFoodProduction = 15;
    public double minesExtractionRatioSTONE = 1;
    public double minesExtractionRatioWOOD = 1;
    public double minesExtractionRatioGOLD = 1;
    public double minesExtractionRatioFOOD = 1;
    public double minesExtractionRatioSCIENCE = 1;
    /// Misc
    public static final Coordinates workerUnlockArea = new Coordinates(3, 3);
    /// Experiences
    public static final String ExpAgriculture = "Agriculture";
    public static final String ExpGoldMining = "GoldMining";
    public static final String ExpStoneMining = "StoneMining";
    public static final String ExpCarpentary = "Carpenting";
    public static final String ExpCivilEng = "CivilEng";
    //
    public static final String usernamePattern = "[A-Za-z0-9_]{5,25}";
}
