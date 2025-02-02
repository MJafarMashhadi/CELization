/**
 *
 */
package celization;

import celization.mapgeneration.BlockType;
import java.io.Serializable;

/**
 * @author mjafar
 *
 */
public class NaturalResources implements Serializable {
	public int numberOfGolds;
	public int numberOfStones;
	public int numberOfWoods;
	public int numberOfFood;
	public int numberOfScience;

	public NaturalResources() {
		clear();
	}

	public NaturalResources(BlockType blockType) {
		this.clear();

		switch (blockType) {
		case JUNGLE:
			numberOfWoods = 1 + CElization.rndMaker.nextInt(10);
			break;
		case WATER:
		case PLAIN:
			numberOfFood = 1 + CElization.rndMaker.nextInt(10);
			break;
		case MOUNTAIN:
			numberOfStones = 1 + CElization.rndMaker.nextInt(10);
			numberOfGolds = 1 + CElization.rndMaker.nextInt(10);
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}
	}

	public NaturalResources(int food, int gold, int sc, int stone, int wood) {
		numberOfFood = food;
		numberOfGolds = gold;
		numberOfScience = sc;
		numberOfStones = stone;
		numberOfWoods = wood;
	}

	public int sum() {
		return numberOfFood + numberOfGolds + numberOfStones + numberOfWoods;
	}

	public boolean doWeHave(NaturalResources compareValue) {
		return (numberOfFood >= compareValue.numberOfFood)
				&& (numberOfGolds >= compareValue.numberOfGolds)
				&& (numberOfScience >= compareValue.numberOfScience)
				&& (numberOfStones >= compareValue.numberOfStones)
				&& (numberOfWoods >= compareValue.numberOfWoods);
	}

	public void subtract(NaturalResources material) {
		this.numberOfFood -= material.numberOfFood;
		this.numberOfGolds -= material.numberOfGolds;
		this.numberOfScience -= material.numberOfScience;
		this.numberOfStones -= material.numberOfStones;
		this.numberOfWoods -= material.numberOfWoods;
	}

	public void add(NaturalResources material) {
		this.numberOfFood += material.numberOfFood;
		this.numberOfGolds += material.numberOfGolds;
		this.numberOfScience += material.numberOfScience;
		this.numberOfStones += material.numberOfStones;
		this.numberOfWoods += material.numberOfWoods;
	}

	public int get(String from) {
		if (from.equals("stone")) {
			return numberOfStones;
		} else if (from.equals("gold")) {
			return numberOfGolds;
		} else if (from.equals("lumber")) {
			return numberOfWoods;
		} else if (from.equals("food")) {
			return numberOfFood;
		} else {
			return -2;
		}
	}

	@Override
	public String toString() {
		return String.format("%s:%d\n%s:%d\n%s:%d\n%s:%d\n%s:%d\n", "food",
				numberOfFood, "gold", numberOfGolds, "stone", numberOfStones,
				"lumber", numberOfWoods, "knowledge", numberOfScience);
	}

	public void clear() {
		numberOfFood = 0;
		numberOfGolds = 0;
		numberOfScience = 0;
		numberOfStones = 0;
		numberOfWoods = 0;
	}
}
