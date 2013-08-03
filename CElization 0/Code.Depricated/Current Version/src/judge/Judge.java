package judge;

import java.util.HashMap;
import java.util.Map;

import celization.Boat;
import celization.CElization;
import celization.Coordinates;
import celization.GameParameters;
import celization.NaturalResources;
import celization.buildings.Building;
import celization.buildings.MainBuilding;
import celization.buildings.Market;
import celization.buildings.Port;
import celization.buildings.Storage;
import celization.buildings.University;
import celization.buildings.extractables.Farm;
import celization.buildings.extractables.GoldMine;
import celization.buildings.extractables.StoneMine;
import celization.buildings.extractables.WoodCamp;
import celization.civilians.Civilian;
import celization.civilians.Scholar;
import celization.civilians.Soldier;
import celization.civilians.Worker;
import celization.civilians.WorkerState;
import celization.civilians.workeractions.Build;
import celization.civilians.workeractions.Move;
import celization.exceptions.BuildingBusyException;
import celization.exceptions.BuildingFullException;
import celization.exceptions.CannotWorkHereException;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;
import celization.exceptions.MaximumCapacityReachedException;
import celization.mapgeneration.BlockType;
import celization.mapgeneration.LandBlock;
import celization.research.CourseManager;

public class Judge {

	/** A little line of code, a big portion of information!! */
	private static CElization game;

	/** return strings */
	private static final String COMMANDSUCCESS = "command successful";
	private static final String INVALIDID = "invalid id";
	private static final String INVALIDCOMMAND = "invalid command";
	private static final String INVALIDBUILDING = "invalid building";
	private static final String INVALIDRESEARCH = "invalid research";
	private static final String INVALIDLOCATION = "invalid location";
	private static final String INSUFFICIENTRESOURCES = "insufficient resources";
	private static final String INSUFFICIENTRESEARCHES = "insufficient researches";
	private static final String BUILDINGBUSY = "building busy";
	private static final String UNIVERSITYFULL = "university full";
	private static final String BUILDINGFULL = "workplace full";

	/** Building types */
	public static Map<String, Building> buildingTypes = new HashMap<String, Building>();
	public static Map<String, Integer> researchIDSI = new HashMap<String, Integer>();
	public static Map<Integer, String> researchIDIS = new HashMap<Integer, String>();

	/** initialize Maps */
	public static void initializeJudge() {
		/** Building types */
		buildingTypes.put("1", new MainBuilding());
		buildingTypes.put("2", new University());
		buildingTypes.put("3", new GoldMine());
		buildingTypes.put("4", new StoneMine());
		buildingTypes.put("5", new Farm());
		buildingTypes.put("6", new WoodCamp());
		buildingTypes.put("7", new Storage());
		buildingTypes.put("8", new Market());
		buildingTypes.put("9", new Port());
		/** Researches */
		researchIDSI.put("Resources", 1);
		researchIDIS.put(1, "Resources");
		researchIDSI.put("Agriculture", 2);
		researchIDIS.put(2, "Agriculture");
		researchIDSI.put("Mining", 3);
		researchIDIS.put(3, "Mining");
		researchIDSI.put("Lumber Mill", 4);
		researchIDIS.put(4, "Lumber Mill");
		researchIDSI.put("Ranching", 5);
		researchIDIS.put(5, "Ranching");
		researchIDSI.put("Irrigation", 6);
		researchIDIS.put(6, "Irrigation");
		researchIDSI.put("Refinery", 7);
		researchIDIS.put(7, "Refinery");
		researchIDSI.put("Carpentry", 8);
		researchIDIS.put(8, "Carpentry");
		researchIDSI.put("Fishing", 9);
		researchIDIS.put(9, "Fishing");
		researchIDSI.put("Economy", 10);
		researchIDIS.put(10, "Economy");
		researchIDSI.put("Micro Economics", 11);
		researchIDIS.put(11, "Micro Economics");
		researchIDSI.put("Macro Economics", 12);
		researchIDIS.put(12, "Macro Economics");
		researchIDSI.put("Parsimony", 13);
		researchIDIS.put(13, "Parsimony");
		researchIDSI.put("Tax", 14);
		researchIDIS.put(14, "Tax");
		researchIDSI.put("Project Managment", 15);
		researchIDIS.put(15, "Project Managment");
		researchIDSI.put("Market", 16);
		researchIDIS.put(16, "Market");
		researchIDSI.put("Team Work", 17);
		researchIDIS.put(17, "Team Work");
		researchIDSI.put("Strategical Project Managment", 18);
		researchIDIS.put(18, "Strategical Project Managment");
		researchIDSI.put("Science", 19);
		researchIDIS.put(19, "Science");
		researchIDSI.put("Alphabet", 20);
		researchIDIS.put(20, "Alphabet");
		researchIDSI.put("Mathematics", 21);
		researchIDIS.put(21, "Mathematics");
		researchIDSI.put("Engineering", 22);
		researchIDIS.put(22, "Engineering");
		researchIDSI.put("School", 23);
		researchIDIS.put(23, "School");
		researchIDSI.put("University", 24);
		researchIDIS.put(24, "University");
		researchIDSI.put("CERN", 25);
		researchIDIS.put(25, "CERN");
	}

	/**
	 * Starts the game
	 * 
	 * @param map
	 *            Array of characters (P M F W) defining the map block types
	 * @param goldMap
	 *            Array of integers (0-9) equal to the amount of gold in each
	 *            block
	 * @param stoneMap
	 *            Array of integers (0-9) equal to the amount of stone in each
	 *            block
	 * @param lumberMap
	 *            Array of integers (0-9) equal to the amount of lumber in each
	 *            block
	 * @param foodMap
	 *            Array of integers (0-9) equal to the amount of food in each
	 *            block
	 * 
	 */
	public static void start(char[][] map, int[][] goldMap, int[][] stoneMap,
			int[][] lumberMap, int[][] foodMap) {
		GameParameters.gameMapSize = new Coordinates(map[0].length, map.length);
		game = new CElization();

		BlockType type = BlockType.UNKNOWN;
		for (int i = 0; i < GameParameters.gameMapSize.row; i++) {
			for (int j = 0; j < GameParameters.gameMapSize.col; j++) {
				switch (map[i][j]) {
				case 'W':
					type = BlockType.WATER;
					break;
				case 'F':
					type = BlockType.JUNGLE;
					break;
				case 'P':
					type = BlockType.PLAIN;
					break;
				case 'M':
					type = BlockType.MOUNTAIN;
					break;
				}
				game.gameMap.map[j][i] = new LandBlock(j, i, type, 1);
				game.gameMap.map[j][i]
						.setResources(new NaturalResources(foodMap[i][j],
								goldMap[i][j], 0, stoneMap[i][j],
								lumberMap[i][j]));
			}
		}
	}

	/**
	 * Retrieves info for an object in the game
	 * 
	 * @param id
	 *            The id of the object in the game to retrieve info for. If null
	 *            then general info about the game will be returned
	 * @return A Map containing the request information
	 * 
	 */
	public static Map<String, String> info(String id) {
		Map<String, String> map = new HashMap<String, String>();

		ObjectType typeofID = ObjectType.NULL;
		if (id != null) {
			typeofID = getTypeOf(id.split(" ")[0]);
		}

		if (id == null) {
			/**
			 * 
			 * No ID, means general information about game state and environment
			 * 
			 */

			map.put("gold", String.format("%d/%d",
					game.gameState.storedResources.numberOfGolds,
					game.gameState.storageCapacity.numberOfGolds));
			map.put("food", String.format("%d/%d",
					game.gameState.storedResources.numberOfFood,
					game.gameState.storageCapacity.numberOfFood));
			map.put("stone", String.format("%d/%d",
					game.gameState.storedResources.numberOfStones,
					game.gameState.storageCapacity.numberOfStones));
			map.put("knowledge", String.format("%d/%d",
					game.gameState.storedResources.numberOfScience,
					game.gameState.storageCapacity.numberOfScience));
			map.put("lumber", String.format("%d/%d",
					game.gameState.storedResources.numberOfWoods,
					game.gameState.storageCapacity.numberOfWoods));

			StringBuilder buildingsIDs = new StringBuilder();
			StringBuilder workersNames = new StringBuilder();
			StringBuilder studentsNames = new StringBuilder();
			StringBuilder boatsIDs = new StringBuilder();

			for (String b : game.gameState.buildings.keySet()) {
				if (game.getBuildingByUID(b).buildBuildingFinished()) {
					buildingsIDs.append(b + " ");
				}
			}
			for (Civilian c : game.gameState.civilians.values()) {
				if (!c.isMature() || !c.stillAlive()) {
					continue;
				}
				if (c instanceof Scholar) {
					studentsNames.append(String.format("student:%s ",
							c.getName()));
				} else if (c instanceof Worker) {
					if (!c.getName().equals("derp")) {
						workersNames.append(String.format("worker:%s ",
								c.getName()));
					} else {
						workersNames.append("derp ");
					}
				} else if (c instanceof Soldier) {
					// TODO: for soldiers
				}
			}

			for (String b : game.gameState.boats.keySet()) {
				if (game.gameState.getBoatByUID(game, b).buildingFinished()) {
					boatsIDs.append(String.format("%s ", b));
				}
			}

			map.put("buildings", buildingsIDs.toString());
			map.put("workers", workersNames.toString());
			map.put("scholars", studentsNames.toString());
			map.put("boats", boatsIDs.toString());

			return map;
		} else if (typeofID == ObjectType.WORKER) {
			/**
			 * 
			 * ID of a worker
			 * 
			 */
			map.put("type", "worker");

			/** find him */
			Worker currentCivilian = game.getWorkerByUID(id.split(" ")[0]);

			/** put location in map */
			map.put("location", String.format("%d %d",
					currentCivilian.getPos().row + 1,
					currentCivilian.getPos().col + 1));

			/** put inventory in `map' */
			NaturalResources inventory = currentCivilian.getInventory();
			int pocketSize = GameParameters.workerPocketCapacity;
			if (inventory.numberOfGolds > 0) {
				map.put("inventory", String.format("%d/%d Gold",
						inventory.numberOfGolds, pocketSize));
			}
			if (inventory.numberOfFood > 0) {
				map.put("inventory", String.format("%d/%d Food",
						inventory.numberOfFood, pocketSize));
			}
			if (inventory.numberOfWoods > 0) {
				map.put("inventory", String.format("%d/%d Lumber",
						inventory.numberOfWoods, pocketSize));
			}
			if (inventory.numberOfStones > 0) {
				map.put("inventory", String.format("%d/%d Stone",
						inventory.numberOfStones, pocketSize));
			}

			/** put occupation in map */
			map.put("occupation",
					currentCivilian.getOccupationForJudge(id.split(" ")[0]));

			/** buildings which can be built */
			StringBuilder buildings = new StringBuilder();
			buildings.append("1 2 6 7 "); // Head Quarters - University -
											// Woodcamp - Stockpile
			if (game.gameState.courseManager.hasBeenDone("Agriculture")) { // Farm
				buildings.append("5 ");
			}
			if (game.gameState.courseManager.hasBeenDone("Mining")) { // Stone
																		// and
																		// gold
																		// mine
				buildings.append("3 4 ");
			}
			if (game.gameState.courseManager.hasBeenDone("Fishing")) { // Port
																		// and
																		// boat
				buildings.append("9 ");
			}
			if (game.gameState.courseManager.hasBeenDone("Market")) { // Market
				buildings.append("8 ");
			}

			map.put("buildings", buildings.toString());

			return map;
		} else if (typeofID == ObjectType.BOAT) {
			/**
			 * 
			 * ID of a boat
			 * 
			 */
			map.put("type", "boat");
			/** find it */
			Boat currentBoat = (Boat) game.gameState.getBoatByUID(game, id.split(" ")[0]);

			/** position */
			map.put("location", String.format("%d %d",
					currentBoat.getPosition().row + 1,
					currentBoat.getPosition().col + 1));

			return map;
		} else if (typeofID == ObjectType.LANDBLOCK) {
			/**
			 * 
			 * ID of a land block
			 * 
			 */
			LandBlock thisBlock = null;
			try {
				thisBlock = game.gameMap.get(new Coordinates(Integer
						.parseInt((id.split(" "))[2]) - 1, Integer.parseInt((id
						.split(" "))[1]) - 1));
			} catch (ArrayIndexOutOfBoundsException e) {
				return new HashMap<String, String>();
			}
			switch (thisBlock.getType()) {
			case PLAIN:
				map.put("type", "P");
				break;
			case JUNGLE:
				map.put("type", "F");
				break;
			case WATER:
				map.put("type", "W");
				break;
			case MOUNTAIN:
				map.put("type", "M");
				break;
			case UNKNOWN:
			default:
				map.put("type", "?");
				break;
			}

			if (thisBlock.locked
					|| !game.gameState.courseManager.hasBeenDone("Resources")) {
				map.put("gold", "?");
				map.put("stone", "?");
				map.put("lumber", "?");
				map.put("food", "?");
			} else {
				map.put("gold",
						String.valueOf(thisBlock.getResources().numberOfGolds));
				map.put("stone",
						String.valueOf(thisBlock.getResources().numberOfStones));
				map.put("lumber",
						String.valueOf(thisBlock.getResources().numberOfWoods));
				map.put("food",
						String.valueOf(thisBlock.getResources().numberOfFood));
			}

			return map;
		} else if (typeofID == ObjectType.NULL) {
			return new HashMap<String, String>();
		} else {
			/***
			 * 
			 * ID of a building
			 * 
			 */
			/** put building type in map */
			if (typeofID == ObjectType.HEADQUARTERS) {
				map.put("building type", "1");
			} else if (typeofID == ObjectType.MARKET) {
				map.put("building type", "8");
			} else if (typeofID == ObjectType.PORT) {
				map.put("building type", "9");
			} else if (typeofID == ObjectType.STORAGE) {
				map.put("building type", "7");
			} else if (typeofID == ObjectType.UNIVERSITY) {
				map.put("building type", "2");
			} else if (typeofID == ObjectType.GOLDMINE) {
				map.put("building type", "3");
			} else if (typeofID == ObjectType.STONEMINE) {
				map.put("building type", "4");
			} else if (typeofID == ObjectType.FARM) {
				map.put("building type", "5");
			} else if (typeofID == ObjectType.WOODCAMP) {
				map.put("building type", "6");
			}

			map.put("type", "building");
			Building b = game.getBuildingByUID(id);
			if (b == null) {
				/** there's no such building */
				throw new IllegalArgumentException(
						"There's no such building named : " + id);
			}

			/** put blocks in map */
			StringBuilder blocksString = new StringBuilder();
			for (int r = 0; r < b.size.row; r++) {
				for (int c = 0; c < b.size.col; c++) {
					blocksString.append(String.format("%d %d,", b.position.row
							+ r + 1, b.position.col + c + 1));
				}
			}

			map.put("blocks",
					blocksString.toString().substring(0,
							blocksString.length() - 1));

			/** Further information about university */
			if (typeofID == ObjectType.UNIVERSITY) {
				University u = ((University) game.getBuildingByUID(id));
				/** Scholars */
				if (GameParameters.maximumNumberOfStudents == -1) {
					map.put("scholars",
							String.format("%d/inf",
									game.gameState.getNumberOfScholars(true)));
				} else {
					map.put("scholars", String.format("%d/%d",
							game.gameState.getNumberOfScholars(true),
							GameParameters.maximumNumberOfStudents));
				}

				/** Researches */
				/*** Being done */
				if (u.currentResearch() == null) {
					map.put("researching", "no researches in process");
				} else {
					map.put("researching", String.valueOf(researchIDSI
							.get(game.gameState.courseManager.inProgress)));
				}

				/*** done before */
				StringBuilder finishedCourses = new StringBuilder();
				for (String courseName : CourseManager.courseList.keySet()) {
					if (game.gameState.courseManager.get(courseName)
							.hasBeenDone()) {
						finishedCourses.append(String.valueOf(researchIDSI
								.get(courseName)));
						finishedCourses.append(' ');
					}
				}

				map.put("finished", finishedCourses.toString().trim());

				/*** available now */
				StringBuilder canBeDone = new StringBuilder();
				for (String courseName : CourseManager.courseList.keySet()) {
					if (!game.gameState.courseManager.get(courseName)
							.hasBeenDone()
							&& game.gameState.courseManager
									.dependencyMet(courseName)) {
						canBeDone.append(String.valueOf(researchIDSI
								.get(courseName)));
						canBeDone.append(' ');
					}
				}

				map.put("available", canBeDone.toString().trim());
			}

			return map;
		}

	}

	/**
	 * Gives a command to a game object
	 * 
	 * @param id
	 *            The id of the object in the game to give a command to
	 * @param command
	 *            The command to be given to the object
	 * @return A String holding the result of the given command
	 * 
	 */
	public static String action(String id, String command) {
		String[] args = command.split(" ");

		if (id.equals("judge")) { // judge
			/** We're doing something about judge */

			if (args[0].equals("set")) { // judge -> set
				return handleJudgeSet(args[1], Integer.parseInt(args[2]));
			} else if (args[0].equals("worker")) { // judge -> worker
				return handleJudgeWorker(new Coordinates(
						Integer.parseInt(args[2]) - 1,
						Integer.parseInt(args[1]) - 1));
			} else if (args[0].equals("move")) { // judge -> move
				return handleJudgeMove(
						args[1],
						new Coordinates(Integer.parseInt(args[3]) - 1, Integer
								.parseInt(args[2]) - 1));
			} else if (args[0].equals("building")) { // judge -> building
				return handleJudgeMakeBuilding(
						new Coordinates(Integer.parseInt(args[3]) - 1,
								Integer.parseInt(args[2]) - 1), args[1]);
			} else if (args[0].equals("scholar")) { // judge -> scholar
				try {
					return game.makeNewScholar(true);
				} catch (MaximumCapacityReachedException e) {
					return null;
				}
			} else if (args[0].equals("research")) { // judge -> research
				try {
					CourseManager.courseList.get(
							researchIDIS.get(Integer.parseInt(args[1])))
							.forceToDo();
					game.gameState.takeEffectOfResearches(researchIDIS
							.get(Integer.parseInt(args[1])));
				} catch (IllegalArgumentException e) {
					return INVALIDCOMMAND;
				}
			} else if (args[0].equals("explore")) {
				// judge -> explore/explore-all
				if (command.equals("explore all")) { // judge -> explore all
					for (int c = 0; c < GameParameters.gameMapSize.col; c++) {
						for (int r = 0; r < GameParameters.gameMapSize.row; r++) {
							game.gameMap.map[c][r].unLock();
						}
					}
				} else { // judge -> explore
					Coordinates blockToExplore = new Coordinates(
							Integer.parseInt(args[2]) - 1,
							Integer.parseInt(args[1]) - 1);
					game.gameMap.get(blockToExplore).unLock();
				}

				return COMMANDSUCCESS;
			}
		} else { // not judge
			ObjectType type = getTypeOf(id);

			switch (type) {
			case UNIVERSITY:
				return handleUniversityActions(args);
			case WORKER:
				if (game.getWorkerByUID(id) == null) {
					return INVALIDID;
				}
				return handleWorkerActions(id, args[0], args);
			case HEADQUARTERS:
				if (args[0].equals("train")) {
					return handleHeadQuartersMakeNewWorker();
				} else if (args[0].equals("sell")) {
					game.getBuildingByUID(id).sell();
					return COMMANDSUCCESS;
				} else {
					return INVALIDCOMMAND;
				}
			case PORT:
				if (args[0].equals("train")) {
					return handleMakeNewBoat(id);
				} else if (args[0].equals("sell")) {
					game.getBuildingByUID(id).sell();
					return COMMANDSUCCESS;
				} else {
					return INVALIDCOMMAND;
				}
			case MARKET:
				if (args[0].equals("exchange")) {
					try {
						game.exchangeResources(Integer.parseInt(args[1]),
								args[2], args[3]);
						return COMMANDSUCCESS;
					} catch (InsufficientResourcesException e) {
						return INSUFFICIENTRESOURCES;
					}
				} else if (args[0].equals("sell")) {
					game.getBuildingByUID(id).sell();
					return COMMANDSUCCESS;
				} else {
					return INVALIDCOMMAND;
				}
			case STORAGE:
			case FARM:
			case GOLDMINE:
			case STONEMINE:
			case WOODCAMP:
				if (args[0].equals("sell")) {
					game.getBuildingByUID(id).sell();
					return COMMANDSUCCESS;
				} else {
					return INVALIDCOMMAND;
				}
			default:
				return INVALIDID;
			}
		}
		return null;
	}

	private static String handleMakeNewBoat(String id) {
		game.makeNewBoat(id);
		return COMMANDSUCCESS;
	}

	private static String handleHeadQuartersMakeNewWorker() {
		try {
			game.gameState.makeNewWorker(game);
			return COMMANDSUCCESS;
		} catch (BuildingBusyException e) {
			return BUILDINGBUSY;
		} catch (InsufficientResourcesException e) {
			return INSUFFICIENTRESOURCES;
		}

	}

	private static String handleWorkerActions(String id, String action,
			String[] args) {
		Worker w = game.getWorkerByUID(id);
		if (action.equals("move")) {

			try {
				return handleWorkerMove(
						w,
						new Coordinates(Integer.parseInt(args[2]) - 1, Integer
								.parseInt(args[1]) - 1));
			} catch (BuildingBusyException e) {
				return INVALIDCOMMAND;
			}

		} else if (action.equals("work")) {
			try {
				// FIXME: WORDKERID is args[0] ? :D
				game.goAndWork(id, args[1]);
				return COMMANDSUCCESS;
			} catch (BuildingFullException e) {
				return BUILDINGFULL;
			} catch (CannotWorkHereException e) {
				return INVALIDID;
			}
		} else if (action.equals("build")) {
			Coordinates location = new Coordinates(
					Integer.parseInt(args[3]) - 1,
					Integer.parseInt(args[2]) - 1);

			if (!game.canBuildThere(location, args[1])) {
				return INVALIDLOCATION;
			}

			try {
				game.checkAvailabilityOfBuildBuilding(args[1]);
			} catch (InsufficientResearchesException e) {
				return INSUFFICIENTRESEARCHES;
			} catch (IllegalArgumentException e) {
				return INVALIDBUILDING;
			} catch (IllegalDuplicateException e) {
				// FIXME: not sure
				return INVALIDCOMMAND;
			}

			if (!game.aroundBuilding(w.getPos(), location,
					buildingTypes.get(args[1]).size)) {
				Coordinates d = new Coordinates(0, 0);
				boolean foundProperPlace = false;
				for (int r = -1; r <= buildingTypes.get(args[1]).size.row; r++) {
					for (int c = -1; r <= buildingTypes.get(args[1]).size.col; c++) {
						d.row = location.row + r;
						d.col = location.col + c;
						try {
							if (game.aroundBuilding(d, location,
									buildingTypes.get(args[1]).size)
									&& game.workerCanGoThere(d)
									&& !game.underBuilding(d, location,
											buildingTypes.get(args[1]).size)) {
								foundProperPlace = true;
								break;
							}
						} catch (Exception e) {

						}
					}
				}
				if (foundProperPlace) {
					w.addAction(new Move(d));
					w.addAction(new Build(String.format("worker:%s",
							w.getName()), location, args[1]));
					return COMMANDSUCCESS;
				} else {
					return INVALIDLOCATION;
				}
			}

			if (game.workerUnderBuilding(location,
					buildingTypes.get(args[1]).size)) {
				return INVALIDLOCATION;
			}

			try {
				game.gameState.events.add(String.format(
						"construction started %s", game.makeNewBuilding(
								String.format("worker:%s", w.getName()),
								location, args[1])));
			} catch (InsufficientResourcesException e) {
				return INSUFFICIENTRESOURCES;
			} catch (InsufficientResearchesException e) {
				return INSUFFICIENTRESEARCHES;
			} catch (IllegalArgumentException e) {
				return INVALIDBUILDING;
			} catch (IllegalDuplicateException e) {
				// FIXME: not sure
				return INVALIDCOMMAND;
			}
			return COMMANDSUCCESS;
		} else {
			return INVALIDCOMMAND;
		}
	}

	private static String handleWorkerMove(Worker w, Coordinates location)
			throws BuildingBusyException {
		try {
			if (w.workState == WorkerState.Free) {
				w.move(location);
				return COMMANDSUCCESS;
			} else {
				throw new BuildingBusyException();
			}
		} catch (IllegalArgumentException e) {
			return INVALIDLOCATION;
		}
	}

	private static String handleUniversityActions(String[] args) {

		University u;
		try {
			u = game.gameState.getUniversity();
		} catch (InsufficientResourcesException e) {
			return null;
		}
		if (args[0].equals("research")) { // university -> research
			Integer courseID = Integer.parseInt(args[1]);
			String courseName = null;
			if (!researchIDIS.containsKey(courseID)) {
				return INVALIDRESEARCH;
			}
			courseName = researchIDIS.get(courseID);
			try {
				u.startResearch(courseName);
				return COMMANDSUCCESS;
			} catch (InsufficientResourcesException e) {
				return INSUFFICIENTRESOURCES;
			} catch (InsufficientResearchesException e) {
				return INSUFFICIENTRESEARCHES;
			} catch (BuildingBusyException e) {
				return BUILDINGBUSY;
			}
		} else if (args[0].equals("train")) { // university -> train
			try {
				game.makeNewScholar();
				return COMMANDSUCCESS;
			} catch (InsufficientResourcesException e) {
				return INSUFFICIENTRESOURCES;
			} catch (BuildingBusyException e) {
				return BUILDINGBUSY;
			} catch (MaximumCapacityReachedException e) {
				return UNIVERSITYFULL;
			}
		} else {
			return INVALIDCOMMAND;
		}
	}

	private static String handleJudgeMakeBuilding(Coordinates location,
			String buildingType) {
		try {
			if (!game.canBuildThere(location, buildingType)) {
				return INVALIDLOCATION;
			}
			return game.makeNewBuilding(location, buildingType);
		} catch (IllegalDuplicateException e) {
			return null;
		}
	}

	/**
	 * @param args
	 * @param workerPos
	 */
	private static String handleJudgeMove(String id, Coordinates workerPos) {
		if (!game.workerCanGoThere(workerPos)) {
			return INVALIDCOMMAND;
		}
		if (game.getWorkerByUID(id) == null
				|| !(game.getWorkerByUID(id) instanceof Worker)) {
			return INVALIDID;
		}

		game.getWorkerByUID(id).setPosition(workerPos);
		int diff = (GameParameters.workerUnlockArea.row - 1) / 2;
		game.gameMap.unlockCells(new Coordinates(
				workerPos.col - diff, workerPos.row - diff),
				GameParameters.workerUnlockArea);
		return id;
	}

	/**
	 * @param args
	 * @return
	 */
	private static String handleJudgeWorker(Coordinates workerPos) {
		/** We're doing something about a miserable worker */
		String workerName = new String();
		if (!game.workerCanGoThere(workerPos)) {
			return null;
		}

		workerName = game.gameState.makeNewWorker(game, true, workerPos);
		return String.format("worker:%s", workerName);
	}

	private static String handleJudgeSet(String resource, int amount) {
		if (resource.equals("gold")) { // judge -> set -> gold
			game.gameState.storedResources.numberOfGolds = amount;
		} else if (resource.equals("stone")) { // judge -> set -> stone
			game.gameState.storedResources.numberOfStones = amount;
		} else if (resource.equals("lumber")) { // judge -> set -> lumber
			game.gameState.storedResources.numberOfWoods = amount;
		} else if (resource.equals("food")) { // judge -> set -> food
			game.gameState.storedResources.numberOfFood = amount;
		} else if (resource.equals("knowledge")) { // judge -> set -> knowledge
			game.gameState.storedResources.numberOfScience = amount;
		}
		game.gameState.manageStorageOverHead();

		return COMMANDSUCCESS;
	}

	/**
	 * Ends the current turn and moves to the next
	 * 
	 * @return An array of Strings holding messages for events that have occured
	 * 
	 */
	public static String[] nextTurn() {
		game.gameState.step();
		String[] e = game.gameState.getEvents();
		game.gameState.clearEvents();
		return e;
	}

	private static final ObjectType getTypeOf(String id) {
		if (id.contains("worker:") || id.equals("derp")) {
			if (game.getCivilianByUID(id.replaceFirst("worker:", "")) == null) {
				return ObjectType.NULL;
			}
			return ObjectType.WORKER;
		} else if (id.contains("student:")) {
			if (game.getCivilianByUID(id.replaceFirst("student:", "")) == null) {
				return ObjectType.NULL;
			}
			return ObjectType.STUDENT;
		} else if (id.equals("block")) {
			return ObjectType.LANDBLOCK;
		} else if (id.contains("boat:")) {
			return ObjectType.BOAT;
		} else if (id.contains(GameParameters.headquartersID)) {
			return ObjectType.HEADQUARTERS;
		} else if (id.contains(GameParameters.universityID)) {
			return ObjectType.UNIVERSITY;
		} else if (id.contains(GameParameters.goldmineID)) {
			return ObjectType.GOLDMINE;
		} else if (id.contains(GameParameters.stonemineID)) {
			return ObjectType.STONEMINE;
		} else if (id.contains(GameParameters.farmID)) {
			return ObjectType.FARM;
		} else if (id.contains(GameParameters.woodcampID)) {
			return ObjectType.WOODCAMP;
		} else if (id.contains(GameParameters.storageID)) {
			return ObjectType.STORAGE;
		} else if (id.contains(GameParameters.marketID)) {
			return ObjectType.MARKET;
		} else if (id.contains(GameParameters.portID)) {
			return ObjectType.PORT;
		} else {
			return ObjectType.NULL;
		}
	}

	public static void injectGameInstance(CElization g) {
		game = g;
	}
}
