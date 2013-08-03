package celization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import celization.buildings.Building;
import celization.buildings.MainBuilding;
import celization.buildings.Market;
import celization.buildings.Port;
import celization.buildings.Storage;
import celization.buildings.University;
import celization.buildings.extractables.Farm;
import celization.buildings.extractables.GoldMine;
import celization.buildings.extractables.Mine;
import celization.buildings.extractables.StoneMine;
import celization.buildings.extractables.WoodCamp;
import celization.civilians.Civilian;
import celization.civilians.Scholar;
import celization.civilians.Worker;
import celization.civilians.WorkerState;
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
import celization.mapgeneration.astar.AStar;
import celization.research.CourseManager;
import java.io.Serializable;
import java.util.Collection;

public class GameState implements Serializable {

    private int turn;
    private NaturalResources storedResources;
    private NaturalResources storageCapacity;
    private Map<GameObjectID, Civilian> civilians;
    private Map<GameObjectID, Boat> boats;
    private Map<GameObjectID, Building> buildings;
    private ArrayList<GameObjectID> unfinishedBuildings;
    private int numberOfAliveOnes;
    private ArrayList<TurnEvent> events;
    private CourseManager courseManager;
    private CElization gameInstance;
    private GameParameters parameters;
    private String userName;
    private AStar pathFinder;
    private GameObjectID derpID;
    private GameObjectID HQID;
    private GameObjectID universityID;

    /**
     * Start a new game
     */

    /* TODO: recover game from file */
    public GameState(CElization gameInstance, String userName) {

        this.civilians = new HashMap<>();
        this.boats = new HashMap<>();
        this.buildings = new HashMap<>();
        this.unfinishedBuildings = new ArrayList<>();
        this.gameInstance = gameInstance;
        this.parameters = new GameParameters();
        this.userName = userName;
        this.pathFinder = new AStar();

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
        events.add(stepConsumeFood());
        events.addAll(stepBuildBoats());
        events.addAll(stepBuildBuildings());
        events.addAll(stepExtractFromMinesAndTrainPeople());
        stepMoveBoats();
        stepWorkerActions();
        stepGetTaxes();

        /**
         * Manage Storage OverHead
         */
        manageStorageOverHead();
        /**
         * clean up
         */
        while (events.contains(null)) {
            events.remove(null);
        }

        /**
         * increases turn number
         */
        turn++;
    }

    /**
     * Get taxes if available
     */
    private void stepGetTaxes() {
        if (parameters.canGetTax) {
            storedResources.add(new NaturalResources(0, (int) Math.floor(numberOfAliveOnes / 4), 0, 0, 0));
        }
    }

    /**
     * Move moving workers
     */
    private void stepWorkerActions() {
        for (Civilian worker : civilians.values()) {
            if (worker instanceof Worker) {
                ((Worker) worker).step();
            }
        }
    }

    /**
     * Make boats wander around water
     */
    private void stepMoveBoats() {
        for (Boat b : boats.values()) {
            if (!b.buildingFinished()) {
                continue;
            }
            b.wander();
        }
    }

    /**
     * Extract resources from mines and move boats in order to do fishings
     *
     * @return
     */
    private ArrayList<GameObjectID> stepExtractFromMinesAndTrainPeople() {
        ArrayList<GameObjectID> returnValue = new ArrayList<>();
        for (Building b : buildings.values()) {
            returnValue.add(b.step());
        }

        return returnValue;
    }

    /**
     * Go on one step in building of all unfinished boats
     *
     * @return UID of finished boats (in this turn)
     */
    private ArrayList<GameObjectID> stepBuildBoats() {
        ArrayList<GameObjectID> finishedboatsInThisTurn = new ArrayList<>();
        for (GameObjectID boatUID : boats.keySet()) {
            if (boats.get(boatUID).buildingFinished()) {
                continue;
            }

            boats.get(boatUID).stepBuilding();
            if (boats.get(boatUID).buildingFinished()) {
                finishedboatsInThisTurn.add(boatUID);
            }
        }
        return finishedboatsInThisTurn;
    }

    /**
     * Go on one step in building of all unfinished buildings
     *
     * @return UID of finished buildings (in this turn)
     */
    private ArrayList<GameObjectID> stepBuildBuildings() {
        ArrayList<GameObjectID> finishedBuildingsInThisTurn = new ArrayList<>();
        //
        GameObjectID buildingID;
        // TODO: do it with iterator
        for (int i = unfinishedBuildings.size() - 1; i >= 0; i--) {
            buildingID = unfinishedBuildings.get(i);

            buildings.get(buildingID).buildStep();
            if (buildings.get(buildingID).buildBuildingFinished()) {
                buildings.get(buildingID).buildBuildingFinishedCallBack();
                unfinishedBuildings.remove(i);
                finishedBuildingsInThisTurn.add(buildingID);
            }
        }

        return finishedBuildingsInThisTurn;
    }

    /**
     * Consume food and kill hungry ones
     *
     * @return
     */
    private String stepConsumeFood() {
        /**
         * Feed people
         */
        for (Civilian currentCivilian : civilians.values()) {
            if (currentCivilian.stillAlive() && currentCivilian.isMature()) {
                storedResources.numberOfFood -= currentCivilian
                        .getFoodConsumption();
            }
        }
        /**
         * kill hungry ones
         */
        if (storedResources.numberOfFood <= 0) {
            storedResources.numberOfFood = 0;
            if (numberOfAliveOnes > 0) {
                Civilian lesMiserable;
                int miserableNumber;
                do {
                    miserableNumber = CElization.rndMaker.nextInt(civilians.size());
                    lesMiserable = civilians.get(civilians.keySet().toArray()[miserableNumber]);
                } while (!lesMiserable.stillAlive());

                lesMiserable.die();
                numberOfAliveOnes--;

                /**
                 * notify game
                 */
                if (lesMiserable.getName().equals("derp")) {
                    return "worker died derp";
                } else {
                    return String.format("%s died %s:%s",
                            lesMiserable.getTypeString(),
                            lesMiserable.getTypeString(),
                            lesMiserable.getName());
                }
            }
        }

        return null;
    }

    /**
     * Add an event to the events list
     *
     * @param event
     */
    public void addEvent(String event) {
        events.add(event);
    }

    /**
     * Delete events occurred in previous turn
     */
    public void clearEvents() {
        events.clear();
    }

    /**
     * Returns array of events occurred in one turn
     *
     * @return
     */
    public String[] getEvents() {
        return events.toArray(new String[events.size()]);
    }

    /**
     * apply researches' effects in the game
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
            case "Engineering":
                parameters.mainBuildingETA *= 3 / 4;
                parameters.universityETA *= 3 / 4;
                parameters.goldMineETA *= 3 / 4;

                parameters.stoneMineETA *= 3 / 4;
                parameters.woodCampETA *= 3 / 4;
                parameters.farmETA *= 3 / 4;
                parameters.portETA *= 3 / 4;
                parameters.marketETA *= 3 / 4;
                parameters.stockpileETA *= 3 / 4;
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
     * remove from stored resources
     *
     * @param naturalResources
     */
    public void removeFromResources(NaturalResources naturalResources) {
        storedResources.subtract(naturalResources);
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
        storageCapacity.subtract(naturalResources);
    }

    public boolean doWeHave(NaturalResources material) {
        return storedResources.doWeHave(material);
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
        return userName;
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
     * get unique buildings references
     *
     * @throws InsufficientResourcesException
     */
    public MainBuilding getHeadQuarters() throws InsufficientResourcesException {
        if (!buildings.containsKey(HQID)) {
            throw new InsufficientResourcesException(
                    "Do not have main building");
        }
        return (MainBuilding) buildings.get(HQID);
    }

    /**
     * Get university UID if there is a university
     *
     * @return
     * @throws InsufficientResourcesException
     */
    public University getUniversity() throws InsufficientResourcesException {
        if (!buildings.containsKey(universityID)) {
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
        //return (Worker) getCivilianByUID(UID.replaceFirst("worker:", ""));
        return (Worker) getCivilianByUID(UID);
    }

    /**
     * Get a scholar
     *
     * @param UID
     * @return civilian instance casted to "Scholar"
     */
    public Scholar getScholarByUID(GameObjectID UID) {
        //return (Scholar) getCivilianByUID(UID.replaceFirst("student:", ""));
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
        return gameInstance.gameMap.get(col, row);
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
     * Get Buildings in a collection for pathfinding reasons
     *
     * @return
     */
    public Collection<Building> getBuildingsArray() {
        return buildings.values();
    }

    /**
     * get civilians in a collection for game information frame
     *
     * @return
     */
    public Collection<Civilian> getCiviliansArray() {
        return civilians.values();
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
    public void manageStorageOverHead() {
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
     * @throws BuildingBusyException
     * @param gameInstance
     * @throws InsufficientResourcesException
     */
    public GameObjectID makeNewWorker() throws BuildingBusyException,
            InsufficientResourcesException {
        MainBuilding hq = getHeadQuarters();

        if (hq.busy()) {
            throw new BuildingBusyException();
        }

        if (!storedResources.doWeHave(GameParameters.workerMaterial)) {
            throw new InsufficientResourcesException();
        }

        Worker w = new Worker(NameChooser.getFreeName());
        GameObjectID wid = new GameObjectID(w.getClass(), civilians.size());
        w.injectGameInstance(this);
        w.setID(wid);
        civilians.put(wid, w);
        storedResources.subtract(GameParameters.workerMaterial);
        hq.startTrainingWorker(w.getID());

        /**
         * find free land around headquarters
         */
        boolean found = false;

        for (int r = -1; !found && r < GameParameters.mainBuildingSize.row + 1; r++) {
            for (int c = -1; !found
                    && c < GameParameters.mainBuildingSize.col + 1; c++) {
                try {
                    if (gameInstance.gameMap.isWalkable(c + hq.getLocation().col, r
                            + hq.getLocation().row, userName)
                            && !underBuilding(new Coordinates(c
                            + hq.getLocation().col, r + hq.getLocation().row))) {
                        found = true;
                        w.setPosition(new Coordinates(c + hq.getLocation().col, r
                                + hq.getLocation().row));
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
        try {
            getUniversity();
        } catch (Exception e) {
            throw new InsufficientResourcesException();
        }
        if (getUniversity().busy()) {
            throw new BuildingBusyException();
        }

        if (!haveRoomForNewStudent()) {
            throw new MaximumCapacityReachedException();
        }

        if (!storedResources.doWeHave(GameParameters.scholarMaterial)) {
            throw new InsufficientResourcesException();
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
        getUniversity().startTrainingStudent(sid);

        return sid;
    }

    /**
     * command "workerName" to start building a "buildingType" in given position
     *
     * @param workerName
     * @param position
     * @param buildingType
     * @return building unique id
     * @throws InsufficientResourcesException
     * @throws InsufficientResearchesException
     * @throws IllegalArgumentException
     * @throws IllegalDuplicateException
     */
    public GameObjectID makeNewBuilding(GameObjectID workerName, Coordinates position,
            Class buildingType) throws InsufficientResourcesException,
            InsufficientResearchesException, IllegalArgumentException,
            IllegalDuplicateException {

        GameObjectID buildingID = newBuildingID(buildingType);
        Building buildingInstance = newBuildingInstance(buildingType);

        if (buildingInstance == null) {
            throw new IllegalArgumentException();
        }

        checkAvailabilityOfBuildBuilding(buildingInstance.getClass());

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
        if (buildingType == MainBuilding.class) {
            return new MainBuilding();
        } else
        if (buildingType == University.class) {
            return new University();
        } else
        if (buildingType == GoldMine.class) {
            return new GoldMine();
        } else
        if (buildingType == StoneMine.class) {
            return new StoneMine();
        } else
        if (buildingType == Farm.class) {
            return new Farm();
        } else
        if (buildingType == WoodCamp.class) {
            return new WoodCamp();
        } else
        if (buildingType == Storage.class) {
            return new Storage();
        } else
        if (buildingType == Market.class) {
            return new Market();
        } else
        if (buildingType == Port.class) {
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
        if (buildingType == MainBuilding.class) {
            if (getBuildingByUID(HQID) != null) {
                throw new IllegalDuplicateException();
            }
            HQID = new GameObjectID(MainBuilding.class, 0);
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
     * Check whether player can make a building
     *
     * @param instance of building (? Extends Building)
     * @throws InsufficientResourcesException
     * @throws InsufficientResearchesException
     * @throws IllegalArgumentException
     * @throws IllegalDuplicateException
     */
    public void checkAvailabilityOfBuildBuilding(Class buildingTypeClass) throws InsufficientResourcesException, InsufficientResearchesException, IllegalArgumentException, IllegalDuplicateException {
        try {
            /**
             * check resources
             */
            if (!storedResources.doWeHave((NaturalResources) buildingTypeClass.getField("requiredResources").get(null))) {
                throw new InsufficientResourcesException();
            }
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException ex) {
            // never happens
        }
        checkResearches(buildingTypeClass);
    }

    /**
     * check effect of researches in ability to make a new building of type "b"
     *
     * @param buildingTypeClass (? extends Building)
     * @throws InsufficientResearchesException
     * @throws IllegalArgumentException
     * @throws IllegalDuplicateException
     */
    private void checkResearches(Class buildingTypeClass) throws InsufficientResearchesException, IllegalArgumentException, IllegalDuplicateException {
        if (buildingTypeClass == Farm.class && !courseManager.hasBeenDone("Agriculture")) {
            throw new InsufficientResearchesException();
        }
        if (buildingTypeClass == Mine.class && !courseManager.hasBeenDone("Mining")) {
            throw new InsufficientResearchesException();
        }
        if (buildingTypeClass == WoodCamp.class && !courseManager.hasBeenDone("Lumber Mill")) {
            throw new InsufficientResearchesException();
        }
        if (buildingTypeClass == Port.class && !courseManager.hasBeenDone("Fishing")) {
            throw new InsufficientResearchesException();
        }
        if (buildingTypeClass == Market.class && !courseManager.hasBeenDone("Market")) {
            throw new InsufficientResearchesException();
        }

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
                worker.workState = WorkerState.MiningGold;
            } else if (workplace instanceof StoneMine) {
                worker.workState = WorkerState.MiningStone;
            } else if (workplace instanceof Farm) {
                worker.workState = WorkerState.Farming;
            } else if (workplace instanceof WoodCamp) {
                worker.workState = WorkerState.WoodCamp;
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
        // FIXME
        GameObjectID boatID = new GameObjectID((Boat.class), boats.size());
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

        double soorat = 1, makhraj = 1;
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

        manageStorageOverHead();
    }

    /**
     * Check wether given point is around building
     *
     * @param point
     * @param buildingLocation
     * @param buildingSize
     * @return
     */
    public boolean aroundBuilding(Coordinates point, Coordinates buildingLocation, Coordinates buildingSize) {

        // Distance from one of building blocks is 1
        for (int row = buildingLocation.row; row < buildingLocation.row
                + buildingSize.row; row++) {
            for (int col = 0; col < buildingLocation.col + buildingSize.col; col++) {
                if (Math.abs(point.row - row) <= 1
                        || Math.abs(point.col - col) <= 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check land type and building type to get a match and check wether we can
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
        if (targetBuildingInstance == MainBuilding.class ||
            targetBuildingInstance == University.class ||
            targetBuildingInstance == Farm.class ||
            targetBuildingInstance == Storage.class ||
            targetBuildingInstance == Market.class) {
            requiredType = BlockType.PLAIN;
        } else
        if (targetBuildingInstance == GoldMine.class ||
            targetBuildingInstance == StoneMine.class) {
            requiredType = BlockType.MOUNTAIN;
        } else
        if (targetBuildingInstance == WoodCamp.class) {
            requiredType = BlockType.JUNGLE;
        } else
        if (targetBuildingInstance == Port.class) {
            requiredType = BlockType.WATER;
        } else {
            throw new IllegalArgumentException();
        }

        Coordinates currentProcessedBlock = new Coordinates(0, 0);
        try {
            for (int i = 0; i < ((Coordinates) targetBuildingInstance.getField("size").get(null)).row; i++) {
                for (int j = 0; j < ((Coordinates) targetBuildingInstance.getField("size").get(null)).col; j++) {
                    currentProcessedBlock.row = location.row + j;
                    currentProcessedBlock.col = location.col + i;

                    if (gameInstance.gameMap.get(currentProcessedBlock).getType(userName) != requiredType) {
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
        for (Building b : buildings.values()) {
            if (underBuilding(coordinates, b.getLocation(), b.size)) {
                return true;
            }
        }
        return false;
    }

    /**
     * check whether a point is under specified building
     *
     * @param coordinates
     * @param buildingLocation
     * @param size
     * @return true/false
     */
    public boolean underBuilding(Coordinates coordinates,
            Coordinates buildingLocation, Coordinates size) {
        Coordinates currentProcessedBlock = new Coordinates(0, 0);
        for (int i = 0; i < size.row; i++) {
            for (int j = 0; j < size.col; j++) {
                currentProcessedBlock.row = buildingLocation.row + j;
                currentProcessedBlock.col = buildingLocation.col + i;

                if (coordinates.equals(currentProcessedBlock)) {
                    return false;
                }
            }
        }

        return true;
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
                    && underBuilding(w.getPos(), location, size)) {
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
        storageCapacity = new NaturalResources(parameters.foodCapacity,
                parameters.goldCapacity, 0, parameters.stoneCapacity,
                parameters.woodCapacity);
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
                    CElization.rndMaker.nextInt(GameParameters.gameMapSize.col),
                    CElization.rndMaker.nextInt(GameParameters.gameMapSize.row));

            landUnderDerpFeet = gameInstance.gameMap.get(derpPosition).getType(false, userName);

        } while (landUnderDerpFeet != BlockType.PLAIN);

        derpInstance.setPosition(derpPosition);
        derpInstance.injectGameInstance(this);
        while (!derpInstance.isMature()) {
            derpInstance.growUp();
        }
        int diff = (GameParameters.workerUnlockArea.row - 1) / 2;
        gameInstance.gameMap.unlockCells(new Coordinates(derpPosition.col
                - diff, derpPosition.row - diff),
                GameParameters.workerUnlockArea, userName);
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
        gameInstance.gameMap.unlockCells(topLeft, unlockArea, userName);
    }
}