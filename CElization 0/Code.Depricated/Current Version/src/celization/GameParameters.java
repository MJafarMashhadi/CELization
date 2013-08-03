/**
 *
 */
package celization;

import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public final class GameParameters implements Serializable {
	//
	public static Coordinates gameMapSize;
	public Boolean canGetTax = false;
	/// Science - Food - Wood - Gold - Stone
	public static final int numberOfResources = 5;
	public int foodCapacity = 200;
	public int woodCapacity = 200;
	public int goldCapacity = 200;
	public int stoneCapacity = 200;
	///
	public static final Coordinates unlockSize = new Coordinates(3, 3);
	/// Buildings sizes
	public static final Coordinates mainBuildingSize = new Coordinates(3, 3);
	public static final Coordinates universitySize = new Coordinates(3, 3);
	public static final Coordinates goldMineSize = new Coordinates(2, 2);
	public static final Coordinates stoneMineSize = new Coordinates(2, 2);
	public static final Coordinates farmSize = new Coordinates(2, 2);
	public static final Coordinates woodCampSize = new Coordinates(1, 1);
	public static final Coordinates stockpileSize = new Coordinates(2, 2);
	public static final Coordinates marketSize = new Coordinates(3, 3);
	public static final Coordinates portSize = new Coordinates(2, 2);
	/// Buildings ETAs
	public double mainBuildingETA = 2;
	public double universityETA = 2;
	public double goldMineETA = 6;
	public double stoneMineETA = 6;
	public double farmETA = 6;
	public double woodCampETA = 6;
	public double stockpileETA = 15;
	public double marketETA = 20;
	public double portETA = 24;
	/// People ETAs
	public static final int workerETA = 2;
	public static final int scholarETA = 6;
	/// Equipment ETAs
	public static final int boatETA = 8;
	/// Building resources
	public static final NaturalResources mainBuildingMaterial = new NaturalResources(0, 0, 0, 10, 20);
	public static final NaturalResources universityMaterial = new NaturalResources(0, 0, 0, 20, 20);
	public static final NaturalResources goldMineMaterial = new NaturalResources(0, 50, 0, 50, 50);
	public static final NaturalResources stoneMineMaterial = new NaturalResources(0, 30, 0, 20, 20);
	public static final NaturalResources farmMaterial = new NaturalResources(0, 10, 0, 10, 10);
	public static final NaturalResources woodCampMaterial = new NaturalResources(0, 20, 0, 0, 20);
	public static final NaturalResources stockpileMaterial = new NaturalResources(0, 10, 0, 100, 100);
	public static final NaturalResources marketMaterial = new NaturalResources(0, 60, 0, 120, 120);
	public static final NaturalResources portMaterial = new NaturalResources(0, 20, 0, 160, 140);
	/// Civilians resources
	public static final NaturalResources workerMaterial = new NaturalResources(0, 10, 0, 0, 0);
	public static final NaturalResources scholarMaterial = new NaturalResources(0, 40, 0, 0, 0);
	/// Equipment resources
	public static final NaturalResources boatMaterial = new NaturalResources(0, 20, 0, 0, 80);
	/// Food Consumptions
	public static final int scholarFoodConsumption = 1;
	public static final int workerFoodConsumption = 2;
	// public static final int soldierFoodConsumption = ;
	public static final NaturalResources StorageExtraCapacity = new NaturalResources(100, 100, 0, 100, 100);
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
}
