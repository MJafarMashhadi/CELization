package celization;

import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import celization.equipment.Boat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import celization.buildings.Building;
import celization.buildings.HeadQuarters;
import celization.buildings.Market;
import celization.buildings.Barracks;
import celization.buildings.Port;
import celization.buildings.Stable;
import celization.buildings.Storage;
import celization.buildings.University;
import celization.buildings.extractables.Farm;
import celization.buildings.extractables.GoldMine;
import celization.buildings.extractables.Mine;
import celization.buildings.extractables.StoneMine;
import celization.buildings.extractables.WoodCamp;
import celization.civilians.ActiveCivilians;
import celization.civilians.Civilian;
import celization.civilians.Scholar;
import celization.civilians.soldiers.Soldier;
import celization.civilians.Worker;
import celization.civilians.CivilianState;
import celization.civilians.soldiers.JockeyHorseman;
import celization.civilians.soldiers.MaceHorseman;
import celization.civilians.soldiers.SapperInfantry;
import celization.civilians.soldiers.SpearInfantry;
import celization.civilians.workeractions.Move;
import celization.exceptions.BuildingBusyException;
import celization.exceptions.BuildingFullException;
import celization.exceptions.CannotWorkHereException;
import celization.exceptions.IllegalDuplicateException;
import celization.exceptions.InsufficientResearchesException;
import celization.exceptions.InsufficientResourcesException;
import celization.exceptions.MaximumCapacityReachedException;
import celization.mapgeneration.BlockType;
import celization.mapgeneration.GameMap;
import celization.mapgeneration.LandBlock;
import celization.mapgeneration.astar.AStar;
import celization.research.CourseManager;
import java.io.Serializable;

public class GameState implements Serializable {

    private int turn;
    private GameParameters parameters;
    private AStar pathFinder;
    private NaturalResources storedResources;
    private NaturalResources storageCapacity;
    private Map<GameObjectID, Civilian> civilians;
    private Map<GameObjectID, Boat> boats;
    private Map<GameObjectID, Building> buildings;
    private ArrayList<GameObjectID> unfinishedBuildings;
    private GameObjectID universityID;
    private int numberOfAliveOnes;
    private ArrayList<TurnEvent> events;
    private CourseManager courseManager;
    private CElization gameInstance;
    private String username;
    private GameObjectID derpID;
    private GameObjectID HQID;
    private GameObjectID lastBuildingAffectedByUnderBuildingMethod;

    /**
     * Start a new game
     */
    public GameState(CElization gameInstance, String userName) {
        this.username = userName;
        this.gameInstance = gameInstance;
    }

    /**
     * Returns Game Session. now we can get information of other users too
     *
     * @return
     */
    public CElization getGameInstance() {
        return gameInstance;
    }

    /**
     * Makes a new empty game
     */
    public void newGame() {

        this.civilians = new HashMap<>();
        this.boats = new HashMap<>();
        this.buildings = new HashMap<>();
        this.unfinishedBuildings = new ArrayList<>();
        this.parameters = new GameParameters();
        this.pathFinder = new AStar(gameInstance.getGameMap().getGameMapSize());

        createDerp();
        initiatializeStorageCapacities();
        events = new ArrayList<>();
        courseManager = new CourseManager();
        turn = 0;
    }

    /**
     * set stored resources, used for initializing
     *
     * @param resources
     */
    public void setResources(NaturalResources resources) {
        storedResources = resources;
    }

    /**
     * Do actions of this turns
     */
    public void step() {
        clearEvents();
        // game objects steps
        stepConsumeFood();
        stepBuildBoats();
        stepBuildBuildings();
        stepActiveCiviliansActions();
        stepGetTaxes();
        stepCallStepForAllBuildings();
        // Manage Storage OverHead
        manageStorageOverhead();
        // increases turn number
        turn++;
    }

    /**
     * Get taxes if available
     */
    private void stepGetTaxes() {
        if (parameters.canGetTax) {
            storedResources.add(new NaturalResources((int) Math.floor(numberOfAliveOnes / 4), 0, 0, 0, 0));
        }
    }

    /**
     * Move moving workers
     */
    private void stepActiveCiviliansActions() {
        for (Civilian activeCivilian : civilians.values()) {
            if (activeCivilian instanceof ActiveCivilians) {
                ((ActiveCivilians) activeCivilian).step();
            }
        }
    }

    /**
     * Extract resources from mines and move boats in order to do fishings
     */
    private void stepCallStepForAllBuildings() {
        for (Building b : buildings.values()) {
            b.step();
        }
    }

    /**
     * Go on one step in building of all unfinished boats
     */
    private void stepBuildBoats() {
        for (GameObjectID boatUID : boats.keySet()) {
            if (boats.get(boatUID).buildingFinished()) {
                continue;
            }

            boats.get(boatUID).stepBuilding();
            if (boats.get(boatUID).buildingFinished()) {
                addEvent(new TurnEvent(TurnEvent.TRAINING_FINISHED, "Boat"));
            }
        }
    }

    /**
     * Go on one step in building of all unfinished buildings
     *
     * @return UID of finished buildings (in this turn)
     */
    private void stepBuildBuildings() {
        GameObjectID buildingID;
        for (int i = unfinishedBuildings.size() - 1; i >= 0; i--) {
            buildingID = unfinishedBuildings.get(i);

            buildings.get(buildingID).buildStep();
            if (buildings.get(buildingID).buildBuildingFinished()) {
                buildings.get(buildingID).buildBuildingFinishedCallBack();
                unfinishedBuildings.remove(i);
                addEvent(new TurnEvent(TurnEvent.CONSTRUCTION_FINISHED, "Building: " + buildingID.toString()));
            }
        }
    }

    /**
     * Consume food and kill hungry ones
     *
     * @return
     */
    private void stepConsumeFood() {
        /**
         * Feed people
         */
        for (Civilian currentCivilian : civilians.values()) {
            if (currentCivilian.stillAlive() && currentCivilian.isMature()) {
                storedResources.numberOfFood -= currentCivilian.getFoodConsumption();
            }
        }
        /**
         * kill hungry ones
         */
        if (storedResources.numberOfFood <= 0) {
            storedResources.numberOfFood = 0;
            if (numberOfAliveOnes > 0) {
                GameObjectID lesMiserable;
                int miserableNumber;
                do {
                    miserableNumber = CElization.rndMaker.nextInt(civilians.size());
                    lesMiserable = (GameObjectID) civilians.keySet().toArray()[miserableNumber];
                } while (!civilians.get(lesMiserable).stillAlive());

                civilians.get(lesMiserable).die();
                numberOfAliveOnes--;

                /**
                 * notify game
                 */
                addEvent(new TurnEvent(TurnEvent.HUNGER_DEATH, civilians.get(lesMiserable).getName()));
            }
        }
    }

    /**
     * Add an event to the events list
     *
     * @param event
     */
    public void addEvent(TurnEvent event) {
        events.add(event);
    }

    /**
     * Delete events occurred in previous turn
     */
    public void clearEvents() {
        events.clear();
    }

    /**
     * Returns list of events occurred in one turn
     *
     * @return
     */
    public ArrayList getEvents() {
        return events;
    }

    /**
     * Returns game map container instance
     *
     * @return
     */
    public GameMap getGameMap() {
        return gameInstance.getGameMap();
    }

    /**
     * Getter for civilians
     *
     * @return
     */
    public Map<GameObjectID, Civilian> getCivilians() {
        return civilians;
    }

    /**
     * Getter for boats
     *
     * @return
     */
    public Map<GameObjectID, Boat> getBoats() {
        return boats;
    }

    /**
     * Getter for Buildings
     *
     * @return
     */
    public Map<GameObjectID, Building> getBuildings() {
        return buildings;
    }

    /**
     * apply researches effects in the game
     *
     * @param name
     */
    public void takeEffectOfResearches(String name) {
        switch (name) {
            case "Ranching":
                parameters.minesExtractionRatioFOOD *= 1.20;
                break;
            case "Irrigation":
                parameters.minesExtractionRatioFOOD *= 1.30;
                break;
            case "Refinery":
                parameters.minesExtractionRatioSTONE *= 1.20;
                break;
            case "Carpentry":
                parameters.minesExtractionRatioWOOD *= 1.20;
                break;
            case "Economy":
                parameters.minesExtractionRatioGOLD *= 1.05;
                break;
            case "Micro Economy":
                parameters.workerPocketCapacity *= 2;
                break;
            case "Macro Economy":
                parameters.minesExtractionRatioGOLD *= 1.20;
                break;
            case "Parsimony":
                break;
            case "Tax":
                parameters.canGetTax = true;
                break;
            case "Team Work":
                parameters.maximumNumberOfWorkers *= 2;
                break;
            case "Science":
                storedResources.numberOfScience += 50;
                break;
            case "Alphabet":
                break;
            case "Mathematics":
                break;
            case "School":
                parameters.maximumNumberOfStudents += 3;
                break;
            case "University":
                parameters.maximumNumberOfStudents += 6;
                break;
            case "CERN":
                parameters.maximumNumberOfStudents = -1;
                break;
            case "Animal Husbandry":
                GameParameters.jockeyDefensive *= 1.2;
                GameParameters.maceDefensive *= 1.2;
                GameParameters.jockeyOffensive *= 1.2;
                GameParameters.maceOffensive *= 1.2;
                break;
            case "Advanced Armors":
                GameParameters.infantryDefensive *= 1.2;
                GameParameters.infantryOffensive *= 1.2;
                break;
            default:
                break;
        }
    }

    /**
     * Add something to resources
     *
     * @param naturalResources
     */
    public void addToResources(NaturalResources naturalResources) {
        storedResources.add(naturalResources);
    }

    /**
     * Add something to storage capacities
     *
     * @param naturalResources
     */
    public void addToStorageCapacity(NaturalResources naturalResources) {
        storageCapacity.add(naturalResources);
    }

    /**
     * Remove from storage capacity
     *
     * @param naturalResources
     */
    public void removeFromStorageCapacity(NaturalResources naturalResources) {
        try {
            storageCapacity.subtract(naturalResources);
        } catch (InsufficientResourcesException ex) {
            // never happens
        }
    }

    /**
     * remove a building from buildings in order to sell it
     *
     * @param buildingInstance
     */
    public void removeBuilding(Building buildingInstance) {
        Coordinates position = buildingInstance.getLocation();
        for (int i = 0; i < buildingInstance.size.col; i++) {
            for (int j = 0; j < buildingInstance.size.row; j++) {
                pathFinder.unlockCell(i + position.col, j + position.row);
            }
        }

        buildings.remove(buildingInstance);
    }

    /**
     * Increase number of alive people when a worker/student/soldier is trained
     */
    public void increaseAliveOnes() {
        numberOfAliveOnes++;
    }

    /**
     * get user specific game parameters
     *
     * @return
     */
    public GameParameters getParams() {
        return parameters;
    }

    /**
     * get path finder
     *
     * @return
     */
    public AStar getPathFinder() {
        return pathFinder;
    }

    /**
     * Username associated with this game
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get Game objects by their unique IDs
     */
    /**
     * Get a Builiding
     *
     * @param UID
     * @return Building instance
     */
    public Building getBuildingByUID(GameObjectID UID) {
        return buildings.get(UID);
    }

    /**
     * Checks if there is a HQ in map
     *
     * @return
     */
    public boolean haveHeadQuarters() {
        return buildings.containsKey(HQID);
    }

    /**
     * Checks if there is a university in map
     *
     * @return
     */
    public boolean haveUniversity() {
        return buildings.containsKey(universityID);
    }

    /**
     * get unique buildings references
     *
     * @throws InsufficientResourcesException
     */
    public HeadQuarters getHeadQuarters() throws InsufficientResourcesException {
        if (!haveHeadQuarters()) {
            throw new InsufficientResourcesException("Do not have HQ");
        }
        return (HeadQuarters) buildings.get(HQID);
    }

    /**
     * Get university UID if there is a university
     *
     * @return
     * @throws InsufficientResourcesException
     */
    public University getUniversity() throws InsufficientResourcesException {
        if (!haveUniversity()) {
            throw new InsufficientResourcesException("Do not have university");
        }
        return (University) buildings.get(universityID);
    }

    /**
     * get Civilian
     *
     * @param UID
     * @return civilian instance
     */
    public Civilian getCivilianByUID(GameObjectID UID) {
        return civilians.get(UID);
    }

    /**
     * Get a worker
     *
     * @param UID
     * @return civilian instance casted to "Worker"
     */
    public Worker getWorkerByUID(GameObjectID UID) {
        return (Worker) getCivilianByUID(UID);
    }

    /**
     * Get a soldier
     *
     * @param UID
     * @return civilian instance casted to "Worker"
     */
    public Soldier getSoldierByUID(GameObjectID UID) {
        return (Soldier) getCivilianByUID(UID);
    }

    /**
     * Get a scholar
     *
     * @param UID
     * @return civilian instance casted to "Scholar"
     */
    public Scholar getScholarByUID(GameObjectID UID) {
        return (Scholar) getCivilianByUID(UID);
    }

    /**
     * train new student checks
     *
     * @return number of alive students
     *
     */
    public int getNumberOfScholars() {
        int numberOfScholars = 0;
        for (Civilian c : civilians.values()) {
            if (c instanceof Scholar && c.stillAlive()) {
                numberOfScholars++;
            }
        }

        return numberOfScholars;
    }

    /**
     * Get number of alive and mature students
     *
     * @param shouldBeMature
     * @return
     */
    public int getNumberOfScholars(boolean shouldBeMature) {
        if (shouldBeMature == false) {
            return getNumberOfScholars();
        } else {
            int numberOfScholars = 0;
            for (Civilian c : civilians.values()) {
                if (c instanceof Scholar && c.isMature() && c.stillAlive()) {
                    numberOfScholars++;
                }
            }

            return numberOfScholars;
        }
    }

    /**
     * Get a boat
     *
     * @param UID
     * @return civilian instance casted to "Boat"
     */
    public Boat getBoatByUID(GameObjectID UID) {
        return boats.get(UID);
    }

    /**
     * Get map block
     *
     * @param pos
     * @return
     */
    public LandBlock getMapBlock(Coordinates pos) {
        return getMapBlock(pos.col, pos.row);
    }

    /**
     * Get map block
     *
     * @param col
     * @param row
     * @return
     */
    public LandBlock getMapBlock(int col, int row) {
        return gameInstance.getGameMap().get(col, row);
    }

    /**
     * Get turn number
     *
     * @return
     */
    public int getTurnNumber() {
        return turn;
    }

    /**
     * Get course manager
     *
     * @return
     */
    public CourseManager getCourseManager() {
        return courseManager;
    }

    /**
     * Get natural resources
     */
    public NaturalResources getNaturalResources() {
        return storedResources;
    }

    public GameObjectID getDerpID() {
        return derpID;
    }

    /**
     * Checks university capacity and number of students to determine wether
     * there is capacity for a new student to be trained
     *
     * @return
     */
    public boolean haveRoomForNewStudent() {
        return (parameters.maximumNumberOfStudents == -1 || getNumberOfScholars() < parameters.maximumNumberOfStudents);
    }

    /**
     * If there was more resources than storages can hold, we just throw them
     * away
     */
    public void manageStorageOverhead() {
        if (storedResources.numberOfFood > storageCapacity.numberOfFood) {
            storedResources.numberOfFood = storageCapacity.numberOfFood;
        }
        if (storedResources.numberOfGolds > storageCapacity.numberOfGolds) {
            storedResources.numberOfGolds = storageCapacity.numberOfGolds;
        }
        if (storedResources.numberOfStones > storageCapacity.numberOfStones) {
            storedResources.numberOfStones = storageCapacity.numberOfStones;
        }
        if (storedResources.numberOfWoods > storageCapacity.numberOfWoods) {
            storedResources.numberOfWoods = storageCapacity.numberOfWoods;
        }
    }

    /**
     * train a new worker
     *
     * @return worker unique id
     * @param gameInstance
     * @throws BuildingBusyException
     * @throws InsufficientResourcesException
     */
    public GameObjectID makeNewWorker() throws BuildingBusyException,
            InsufficientResourcesException {
        HeadQuarters hq = getHeadQuarters();

        if (hq.busy()) {
            throw new BuildingBusyException();
        }

        Worker w = new Worker(NameChooser.getFreeName());
        GameObjectID wid = new GameObjectID(w.getClass(), civilians.size());
        w.injectGameInstance(this);
        w.setID(wid);
        storedResources.subtract(GameParameters.workerMaterial);
        civilians.put(wid, w);
        hq.startTraining(wid);

        /**
         * find free land around headquarters
         */
        boolean found = false;

        outerFor:
        for (int r = -1; r <= GameParameters.headQuartersSize.row + 1; r++) {
            for (int c = -1; c <= GameParameters.headQuartersSize.col + 1; c++) {
                try {
                    if (gameInstance.getGameMap().isWalkable(c + hq.getLocation().col, r + hq.getLocation().row, username) 
                        && !underBuilding(new Coordinates(c + hq.getLocation().col, r + hq.getLocation().row),hq.getLocation(),GameParameters.headQuartersSize)) {
                        found = true;
                        w.setLocation(new Coordinates(c + hq.getLocation().col, r + hq.getLocation().row));
                        Coordinates tl = w.getLocation().clone();
                        tl.col -= (GameParameters.workerUnlockArea.col - 1) / 2;
                        tl.row -= (GameParameters.workerUnlockArea.row - 1) / 2;
                        unlockCells(tl, GameParameters.workerUnlockArea);
                        break outerFor;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // he he he he
                }
            }
        }

        return wid;
    }

    /**
     * Make a new scholar in University
     *
     * @return scholar unique id
     * @throws InsufficientResourcesException
     * @throws BuildingBusyException
     * @throws MaximumCapacityReachedException
     * @throws InsufficientResourcesException
     */
    public GameObjectID makeNewScholar() throws InsufficientResourcesException,
            BuildingBusyException, MaximumCapacityReachedException,
            InsufficientResourcesException {

        // For insufficient resources exception
        getUniversity();

        if (getUniversity().busy()) {
            throw new BuildingBusyException();
        }

        if (!haveRoomForNewStudent()) {
            throw new MaximumCapacityReachedException();
        }

        /**
         * take material from resources
         */
        storedResources.subtract(GameParameters.scholarMaterial);

        Scholar s = new Scholar(NameChooser.getFreeName());
        GameObjectID sid = new GameObjectID(s.getClass(), civilians.size());
        s.injectGameInstance(this);
        s.setID(sid);

        civilians.put(sid, s);
        getUniversity().startTraining(sid);

        return sid;
    }

    /**
     * Train a new soldier
     *
     * @param building
     * @param soldierType
     * @return
     * @throws InsufficientResourcesException
     * @throws BuildingBusyException
     */
    public GameObjectID makeNewSoldier(GameObjectID building, Class soldierType) throws InsufficientResourcesException, BuildingBusyException {
        if (!buildings.containsKey(building)) {
            throw new InsufficientResourcesException();
        }
        if (buildings.get(building).busy()) {
            throw new BuildingBusyException();
        }

        GameObjectID soldierUID;
        soldierUID = new GameObjectID(soldierType, civilians.size());

        String name;
        name = NameChooser.getFreeName();

        Soldier soldier = null;
        if (soldierType == SpearInfantry.class) {
            storedResources.subtract(GameParameters.spearMaterial);
            soldier = new SpearInfantry(name);
        } else if (soldierType == SapperInfantry.class) {
            storedResources.subtract(GameParameters.sapperMaterial);
            soldier = new SapperInfantry(name);
        } else if (soldierType == MaceHorseman.class) {
            storedResources.subtract(GameParameters.maceMaterial);
            soldier = new MaceHorseman(name);
        } else if (soldierType == JockeyHorseman.class) {
            storedResources.subtract(GameParameters.jockeyMaterial);
            soldier = new JockeyHorseman(name);
        }

        soldier.setID(soldierUID);
        soldier.injectGameInstance(this);
        Building fort;
        fort = getBuildingByUID(building);
        soldier.setLocation(fort.getLocation());
        if (building.getType() == Stable.class) {
            ((Stable) fort).startTraining(soldierUID);
        } else {
            ((Barracks) fort).startTraining(soldierUID);
        }

        return soldierUID;
    }

    /**
     * Checks if a soldier is at the given location
     *
     * @param location
     * @return
     */
    public GameObjectID isASoldierThere(Coordinates location) {
        for (GameObjectID soldier : civilians.keySet()) {
            if (getCivilianByUID(soldier) instanceof Soldier && getCivilianByUID(soldier).stillAlive()) {
                if (getSoldierByUID(soldier).getLocation().equals(location)) {
                    return soldier;
                }
            }
        }
        return null;
    }

    /**
     * command "workerName" to start building a "buildingType" in given location
     *
     * @param workerName
     * @param location
     * @param buildingType
     * @return building unique id
     * @throws InsufficientResourcesException
     * @throws InsufficientResearchesException
     * @throws IllegalDuplicateException
     * @throws IllegalArgumentException
     */
    public GameObjectID makeNewBuilding(GameObjectID workerName, Coordinates position, Class buildingType)
            throws InsufficientResourcesException,
            InsufficientResearchesException,
            IllegalDuplicateException,
            IllegalArgumentException {

        GameObjectID buildingID = newBuildingID(buildingType);
        Building buildingInstance = newBuildingInstance(buildingType);

        if (buildingInstance == null) {
            throw new IllegalArgumentException();
        }

        if (!checkResearches(buildingInstance.getClass())) {
            throw new InsufficientResearchesException();
        }

        /**
         * Subtract from resources and start building
         */
        storedResources.subtract(buildingInstance.requiredResources);
        buildingInstance.injectGameInstance(this);
        buildingInstance.buildAddBuilderWorker(workerName);
        buildingInstance.buildStartBuilding(position);
        buildings.put(buildingID, buildingInstance);
        unfinishedBuildings.add(buildingID);
        /**
         * mark blocks under this building as unavailable
         */
        for (int i = 0; i < buildingInstance.size.col; i++) {
            for (int j = 0; j < buildingInstance.size.row; j++) {
                pathFinder.lockCell(i + position.col, j + position.row);
            }
        }
        return buildingID;
    }

    /**
     * Get an instance of a building type with type name of "buildingType"
     *
     * @param buildingType
     * @return
     */
    public Building newBuildingInstance(Class buildingType) {
        if (buildingType == HeadQuarters.class) {
            return new HeadQuarters();
        } else if (buildingType == University.class) {
            return new University();
        } else if (buildingType == GoldMine.class) {
            return new GoldMine();
        } else if (buildingType == StoneMine.class) {
            return new StoneMine();
        } else if (buildingType == Farm.class) {
            return new Farm();
        } else if (buildingType == WoodCamp.class) {
            return new WoodCamp();
        } else if (buildingType == Storage.class) {
            return new Storage();
        } else if (buildingType == Market.class) {
            return new Market();
        } else if (buildingType == Port.class) {
            return new Port();
        } else {
            return null;
        }
    }

    /**
     * Get a free ID for making a new building
     *
     * @param buildingType
     * @return
     * @throws IllegalDuplicateException
     */
    public GameObjectID newBuildingID(Class buildingType) throws IllegalDuplicateException {
        if (buildingType == HeadQuarters.class) {
            if (getBuildingByUID(HQID) != null) {
                throw new IllegalDuplicateException();
            }
            HQID = new GameObjectID(HeadQuarters.class, 0);
            return HQID;
        } else if (buildingType == University.class) {
            if (getBuildingByUID(universityID) != null) {
                throw new IllegalDuplicateException();
            }
            universityID = new GameObjectID(University.class, 0);
            return universityID;
        } else {
            return new GameObjectID(buildingType, buildings.size());
        }
    }

    /**
     * Sell a building and return half of used resources used in its building to
     * the warehouses
     *
     * @param UID
     */
    public void sellBuilding(GameObjectID UID) {
        getBuildingByUID(UID).sell();
    }

    /**
     * check effect of researches in ability to make a new building.
     *
     * @param buildingTypeClass (? extends Building).class
     * @throws InsufficientResearchesException
     */
    public boolean checkResearches(Class buildingTypeClass) {
        if ((buildingTypeClass == Farm.class && !courseManager.hasBeenDone("Agriculture"))
                || (buildingTypeClass == Mine.class && !courseManager.hasBeenDone("Mining"))
                || (buildingTypeClass == WoodCamp.class && !courseManager.hasBeenDone("Lumber Mill"))
                || (buildingTypeClass == Port.class && !courseManager.hasBeenDone("Fishing"))
                || (buildingTypeClass == Market.class && !courseManager.hasBeenDone("Market"))
                || (buildingTypeClass == Barracks.class && !courseManager.hasBeenDone("Basic Combat"))
                || (buildingTypeClass == Stable.class && !courseManager.hasBeenDone("Horseback Riding"))) {
            return false;
        }
        return true;
    }

    /**
     * Make a worker go to a place and work there
     *
     * @param workerID
     * @param workplaceID
     * @throws CannotWorkHereException
     * @throws BuildingFullException
     */
    public void goAndWork(GameObjectID workerID, GameObjectID workplaceID)
            throws CannotWorkHereException, BuildingFullException {
        Building workplace = getBuildingByUID(workplaceID);
        Worker worker = getWorkerByUID(workerID);

        Coordinates workplaceCoordinates = workplace.getLocation();
        // it may throw BuildingFullException, we don't try...catch it
        // because we want it to be re-throw-ed !
        workplace.addOccupiedWorker(workerID);
        // if building was busy worker won't go there
        try {
            worker.addAction(new Move(workplaceCoordinates));
            if (workplace instanceof GoldMine) {
                worker.workState = CivilianState.MiningGold;
            } else if (workplace instanceof StoneMine) {
                worker.workState = CivilianState.MiningStone;
            } else if (workplace instanceof Farm) {
                worker.workState = CivilianState.Farming;
            } else if (workplace instanceof WoodCamp) {
                worker.workState = CivilianState.WoodCamp;
            }
        } catch (IllegalArgumentException e) {
            // this will not happen :D
        }
    }

    /**
     * Start building a new boat in given port
     *
     * @param portID
     * @return
     */
    public GameObjectID makeNewBoat(GameObjectID portID) {
        Boat b = new Boat();
        GameObjectID boatID = new GameObjectID(Boat.class, boats.size());
        b.injectGameInstance(this);
        b.addToPort(portID);
        boats.put(boatID, b);

        return boatID;
    }

    /**
     * Exchange resources in market
     *
     * @param amount
     * @param from
     * @param to
     * @throws InsufficientResourcesException
     */
    public void exchangeResources(int amount, String from, String to) throws InsufficientResourcesException {
        if (storedResources.get(from) < amount) {
            throw new InsufficientResourcesException();
        }

        double soorat = 1;
        double makhraj = 1;
        switch (from) {
            case "gold":
                makhraj = GameParameters.priceOfGold;
                storedResources.numberOfGolds -= amount;
                break;
            case "stone":
                makhraj = GameParameters.priceOfStone;
                storedResources.numberOfStones -= amount;
                break;
            case "lumber":
                makhraj = GameParameters.priceOfWood;
                storedResources.numberOfWoods -= amount;
                break;
            case "food":
                makhraj = GameParameters.priceOfFood;
                storedResources.numberOfFood -= amount;
                break;
        }
        switch (to) {
            case "gold":
                soorat = GameParameters.priceOfGold;
                storedResources.numberOfGolds += amount * soorat / makhraj;
                break;
            case "stone":
                soorat = GameParameters.priceOfStone;
                storedResources.numberOfStones += amount * soorat / makhraj;
                break;
            case "lumber":
                soorat = GameParameters.priceOfWood;
                storedResources.numberOfWoods += amount * soorat / makhraj;
                break;
            case "food":
                soorat = GameParameters.priceOfFood;
                storedResources.numberOfFood += amount * soorat / makhraj;
                break;
        }

        manageStorageOverhead();
    }

    /**
     * Check whether given point is around building
     *
     * @param point
     * @param buildingLocation
     * @param buildingSize
     * @return
     */
    public boolean aroundBuilding(Coordinates point, Coordinates buildingLocation, Class buildingType) {
        Coordinates buildingSize;
        try {
            buildingSize = (Coordinates) buildingType.getField("size").get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            return false;
        }
        // Distance from one of building blocks is 1
        for (int row = buildingLocation.row - 1; row <= buildingLocation.row + buildingSize.row; row++) {
            for (int col = buildingLocation.col - 1; col <= buildingLocation.col + buildingSize.col; col++) {
                if (Math.abs(point.row - row) <= 1 || Math.abs(point.col - col) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check land type and building type to get a match and check whether we can
     * build that kind of building there
     *
     * @param location
     * @param buildingType
     * @return
     */
    public boolean canBuildThere(Coordinates location, Class targetBuildingInstance) {

        BlockType requiredType = null;
        /**
         * check whether a building can be built there or not
         */
        if (targetBuildingInstance == HeadQuarters.class
                || targetBuildingInstance == University.class
                || targetBuildingInstance == Farm.class
                || targetBuildingInstance == Storage.class
                || targetBuildingInstance == Market.class) {
            requiredType = BlockType.PLAIN;
        } else if (targetBuildingInstance == GoldMine.class
                || targetBuildingInstance == StoneMine.class) {
            requiredType = BlockType.MOUNTAIN;
        } else if (targetBuildingInstance == WoodCamp.class) {
            requiredType = BlockType.JUNGLE;
        } else if (targetBuildingInstance == Port.class) {
            requiredType = BlockType.WATER;
        } else {
            throw new IllegalArgumentException();
        }

        Coordinates currentProcessedBlock = Coordinates.ZERO;
        try {
            for (int i = 0; i < ((Coordinates) targetBuildingInstance.getField("size").get(null)).row; i++) {
                for (int j = 0; j < ((Coordinates) targetBuildingInstance.getField("size").get(null)).col; j++) {
                    currentProcessedBlock.row = location.row + j;
                    currentProcessedBlock.col = location.col + i;

                    if (gameInstance.getGameMap().get(currentProcessedBlock).getType(username) != requiredType) {
                        return false;
                    }
                }
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            // never happens
        }
        return true;
    }

    /**
     * Checks whether a point is under any building
     *
     * @param coordinates
     * @return true/false
     */
    boolean underBuilding(Coordinates coordinates) {
        for (GameObjectID buildingID : buildings.keySet()) {
            if (underBuilding(coordinates, buildings.get(buildingID).getLocation(), buildings.get(buildingID).size)) {
                lastBuildingAffectedByUnderBuildingMethod = buildingID;
                return true;
            }
        }
        lastBuildingAffectedByUnderBuildingMethod = null;
        return false;
    }

    public boolean underBuilding(Coordinates coordinats, Coordinates buildingLocation, Class buildingType) {
        try {
            return underBuilding(buildingLocation, buildingLocation, (Coordinates) buildingType.getField("size").get(null));
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            // never happens
            return false;
        }
    }

    /**
     * check whether a point is under specified building
     *
     * @param coordinates
     * @param buildingLocation
     * @param size
     * @return true/false
     */
    public boolean underBuilding(Coordinates coordinates, Coordinates buildingLocation, Coordinates size) {
//        Coordinates currentProcessedBlock = Coordinates.ZERO;
//        for (int i = 0; i < size.row; i++) {
//            for (int j = 0; j < size.col; j++) {
//                currentProcessedBlock.row = buildingLocation.row + j;
//                currentProcessedBlock.col = buildingLocation.col + i;
//
//                if (coordinates.equals(currentProcessedBlock)) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
        
        return (buildingLocation.col <= coordinates.col && coordinates.col <= buildingLocation.col + size.col) && (buildingLocation.row <= coordinates.row && coordinates.row <= buildingLocation.row + size.row);
    }

    /**
     * Check all workers to find out if any of them is under given building
     *
     * @param location
     * @param size
     * @return
     */
    public boolean workerUnderBuilding(Coordinates location, Coordinates size) {

        for (Civilian w : civilians.values()) {
            if (w instanceof Worker
                    && underBuilding(w.getLocation(), location, size)) {
                return false;
            }
        }

        return true;
    }

    /**
     * New game initialization functions
     *
     */
    /**
     * Set storage capacities by default capacities and fill them with some
     * resources
     */
    private void initiatializeStorageCapacities() {
        storageCapacity = new NaturalResources(parameters.goldCapacity, parameters.stoneCapacity,
                parameters.woodCapacity, parameters.foodCapacity, 0);
//		storedResources = new NaturalResources(50, 50, 0, 50, 50);
    }

    /**
     * Create first worker
     */
    private void createDerp() {
        Worker derpInstance;
        derpInstance = new Worker("derp");
        derpID = new GameObjectID(Worker.class, 0);
        derpInstance.setID(derpID);
        Coordinates derpPosition;
        BlockType landUnderDerpFeet;

        do {
            derpPosition = new Coordinates(
                    CElization.rndMaker.nextInt(gameInstance.getGameMap().getGameMapSize().col),
                    CElization.rndMaker.nextInt(gameInstance.getGameMap().getGameMapSize().row));

            landUnderDerpFeet = gameInstance.getGameMap().get(derpPosition).getType(false, username);

        } while (landUnderDerpFeet != BlockType.PLAIN);

        derpInstance.setLocation(derpPosition);
        derpInstance.injectGameInstance(this);
        while (!derpInstance.isMature()) {
            derpInstance.growUp();
        }
        int diff = (GameParameters.workerUnlockArea.row - 1) / 2;
        gameInstance.getGameMap().unlockCells(new Coordinates(derpPosition.col
                - diff, derpPosition.row - diff),
                GameParameters.workerUnlockArea, username);
        numberOfAliveOnes = 1;
        civilians.put(derpID, derpInstance);
    }

    /**
     * Unlock cells by moving people
     *
     * @param topLeft
     * @param unlockArea
     */
    public void unlockCells(Coordinates topLeft, Coordinates unlockArea) {
        gameInstance.getGameMap().unlockCells(topLeft, unlockArea, username);
    }

    public GameObjectID getLastBuildingAffectedByUnderBuildingMethod() {
        return lastBuildingAffectedByUnderBuildingMethod;
    }
}