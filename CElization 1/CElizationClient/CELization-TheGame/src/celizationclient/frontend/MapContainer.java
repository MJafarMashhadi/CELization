package celizationclient.frontend;

import celizationclient.frontend.gameicons.CivilianIcon;
import celizationclient.frontend.gameicons.BoatIcon;
import celizationclient.frontend.gameicons.BuildingIcon;
import celization.GameState;
import celization.UserInfo;
import celization.buildings.Building;
import celization.equipment.Boat;
import celizationclient.backend.net.CElizationClient;
import celization.mapgeneration.GameMap;
import celizationclient.frontend.gameicons.GameIcon;
import celizationrequests.Coordinates;
import celizationrequests.GameObjectID;
import java.io.IOException;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.util.Duration;

/**
 *
 * @author mjafar
 */
public class MapContainer extends Group {

    protected LandTile[][] map;
    protected GridPane mapGrid;
    protected GameMap realMap;
    protected GameMainFrameController mainFrameInstance;
    protected IconClickHandler iconClickHandler;
    protected HashMap<CombinedID, GameIcon> gameIcons = new HashMap<>();
    protected Duration animationDuration = Duration.millis(2300);

    public void setMainFrameInstance(GameMainFrameController mainFrameInstance) {
        this.mainFrameInstance = mainFrameInstance;
    }

    public MapContainer() {
        mapGrid = GridPaneBuilder.create()
                .vgap(0)
                .hgap(0).build();
        getChildren().add(mapGrid);
        iconClickHandler = new IconClickHandler();
    }

    public void updateMap(GameState currentGameState, String username, CElizationClient client) {
        TimelineBuilder mapUpdaterTimeline = TimelineBuilder.create();
        HashMap<String, UserInfo> usersList;
        usersList = currentGameState.getGameInstance().getUsersList();

        realMap = currentGameState.getGameMap();

        System.out.println("  Refreshing land tiles");
        if (map == null) {
            map = new LandTile[realMap.getGameMapSize().col][realMap.getGameMapSize().row];
            for (int col = 0; col < map.length; col++) {
                for (int row = 0; row < map[col].length; row++) {
                    map[col][row] = new LandTile(row, col);
                    map[col][row].setOnMouseClicked(iconClickHandler);
                    mapGrid.add(map[col][row], col, row);
                    map[col][row].setType(realMap.get(col, row).getType(username));
                }
            }
        } else {
            for (int col = 0; col < map.length; col++) {
                for (int row = 0; row < map[col].length; row++) {
                    // If is for debug
                    if (map[col][row].getType() != realMap.get(col, row).getType(username)) {
                        map[col][row].setType(realMap.get(col, row).getType(username));
                        System.err.printf("Unlocked [%d %d]\n", col, row);
                    }
                }
            }
        }

        boolean animate = false;
        double newX, newY;

        Coordinates currentCivilianLocation;
        CivilianIcon civilianIcon;
        CombinedID combinedID;
        GameState currentUserGameState;
        usersLoop:
        for (String currentUsername : usersList.keySet()) {
            currentUserGameState = usersList.get(currentUsername).getGame();
            // Civilians:
            civiliansLoop:
            for (celization.civilians.Civilian currentCivilian : currentUserGameState.getCivilians().values()) {
                if (currentCivilian instanceof celization.civilians.Scholar || !currentCivilian.stillAlive() || !currentCivilian.isMature()) {
                    continue civiliansLoop;
                }
                animate = false;
                currentCivilianLocation = currentCivilian.getLocation();

                combinedID = combine(currentCivilian.getID(), currentUsername);
                if (gameIcons.containsKey(combinedID)) {
                    civilianIcon = (CivilianIcon) gameIcons.get(combinedID);
                    animate = true;
                } else {
                    civilianIcon = new CivilianIcon(currentCivilian.getID(), client, currentCivilian.getName(), currentUsername);
                    civilianIcon.setOnMouseClicked(iconClickHandler);
                    gameIcons.put(combinedID, civilianIcon);
                    MapContainer.this.getChildren().add(civilianIcon);
                }
                newX = (currentCivilianLocation.col) * GameIcon.BLOCK_WIDTH;
                newY = (currentCivilianLocation.row) * GameIcon.BLOCK_HEIGHT;
                if (!animate) {
                    civilianIcon.setLayoutX(newX);
                    civilianIcon.setLayoutY(newY);
                } else {
                    mapUpdaterTimeline.keyFrames(
                            new KeyFrame(Duration.ZERO, new KeyValue(civilianIcon.layoutXProperty(), civilianIcon.getLayoutX())),
                            new KeyFrame(Duration.ZERO, new KeyValue(civilianIcon.layoutYProperty(), civilianIcon.getLayoutY())),
                            new KeyFrame(animationDuration, new KeyValue(civilianIcon.layoutXProperty(), newX)),
                            new KeyFrame(animationDuration, new KeyValue(civilianIcon.layoutYProperty(), newY)));
                }
            }
            // Buildigns
            Building currentBuilding;
            BuildingIcon buildingIcon;
            Coordinates currentBuildingLocation;
            int size;
            for (GameObjectID currentBuildingID : currentUserGameState.getBuildings().keySet()) {
                currentBuilding = currentUserGameState.getBuildingByUID(currentBuildingID);
                currentBuildingLocation = currentBuilding.getLocation();
                Image icon = null;
                animate = false;
                if (!currentBuilding.buildBuildingFinished()) {
                    size = currentBuilding.getSize().row;
                    icon = GameIcon.INCOMPLETE_BUILDINGS[size];
                } else {
                    if (currentBuilding instanceof celization.buildings.HeadQuarters) {
                        icon = GameIcon.HQ;
                    } else if (currentBuilding instanceof celization.buildings.University) {
                        icon = GameIcon.UNIVERSITY;
                    } else if (currentBuilding instanceof celization.buildings.extractables.GoldMine) {
                        icon = GameIcon.GOLD_MINE;
                    } else if (currentBuilding instanceof celization.buildings.extractables.StoneMine) {
                        icon = GameIcon.STONE_MINE;
                    } else if (currentBuilding instanceof celization.buildings.extractables.Farm) {
                        icon = GameIcon.FARM;
                    } else if (currentBuilding instanceof celization.buildings.extractables.WoodCamp) {
                        icon = GameIcon.WOOD_CAMP;
                    } else if (currentBuilding instanceof celization.buildings.Stable) {
                        icon = GameIcon.STABLE;
                    } else if (currentBuilding instanceof celization.buildings.Barracks) {
                        icon = GameIcon.BARRACKS;
                    } else if (currentBuilding instanceof celization.buildings.Port) {
                        icon = GameIcon.PORT;
                    } else if (currentBuilding instanceof celization.buildings.Market) {
                        icon = GameIcon.MARKET;
                    } else if (currentBuilding instanceof celization.buildings.Storage) {
                        icon = GameIcon.STORAGE;
                    }
                }
                combinedID = combine(currentBuildingID, currentUsername);
                if (gameIcons.containsKey(combinedID)) {
                    buildingIcon = (BuildingIcon) gameIcons.get(combinedID);
                    buildingIcon.setIcon(icon);
                    animate = true;
                } else {
                    buildingIcon = new BuildingIcon(icon, currentBuildingID, currentUsername);
                    buildingIcon.setOnMouseClicked(iconClickHandler);
                    gameIcons.put(combinedID, buildingIcon);
                    MapContainer.this.getChildren().add(buildingIcon);
                }
                newX = (currentBuildingLocation.col) * GameIcon.BLOCK_WIDTH;
                newY = (currentBuildingLocation.row) * GameIcon.BLOCK_HEIGHT;
                if (!animate) {
                    buildingIcon.setLayoutX(newX);
                    buildingIcon.setLayoutY(newY);
                } else {
                    mapUpdaterTimeline.keyFrames(
                            new KeyFrame(Duration.ZERO, new KeyValue(buildingIcon.layoutXProperty(), buildingIcon.getLayoutX())),
                            new KeyFrame(Duration.ZERO, new KeyValue(buildingIcon.layoutYProperty(), buildingIcon.getLayoutY())),
                            new KeyFrame(animationDuration, new KeyValue(buildingIcon.layoutXProperty(), newX)),
                            new KeyFrame(animationDuration, new KeyValue(buildingIcon.layoutYProperty(), newY)));
                }
            }
            // Boats
            Boat currentBoat;
            BoatIcon boatIcon;
            Coordinates currentBoatLocation;
            for (GameObjectID currentBoatID : currentUserGameState.getBoats().keySet()) {
                currentBoat = currentUserGameState.getBoatByUID(currentBoatID);
                currentBoatLocation = currentBoat.getLocation();
                animate = false;
                if (currentBoat.buildingFinished()) {
                    combinedID = combine(currentBoatID, currentUsername);
                    if (gameIcons.containsKey(combinedID)) {
                        boatIcon = (BoatIcon) gameIcons.get(combinedID);
                        animate = true;
                    } else {
                        boatIcon = new BoatIcon(currentBoatID, currentUsername);
                        boatIcon.setOnMouseClicked(iconClickHandler);
                        MapContainer.this.getChildren().add(boatIcon);
                        gameIcons.put(combinedID, boatIcon);
                    }
                    newX = (currentBoatLocation.col) * GameIcon.BLOCK_WIDTH;
                    newY = (currentBoatLocation.row) * GameIcon.BLOCK_HEIGHT;
                    if (!animate) {
                        boatIcon.setLayoutX(newX);
                        boatIcon.setLayoutY(newY);
                    } else {
                        mapUpdaterTimeline.keyFrames(
                                new KeyFrame(Duration.ZERO, new KeyValue(boatIcon.layoutXProperty(), boatIcon.getLayoutX())),
                                new KeyFrame(Duration.ZERO, new KeyValue(boatIcon.layoutYProperty(), boatIcon.getLayoutY())),
                                new KeyFrame(animationDuration, new KeyValue(boatIcon.layoutXProperty(), newX)),
                                new KeyFrame(animationDuration, new KeyValue(boatIcon.layoutYProperty(), newY)));
                    }
                }
            }
        }
        System.out.println("timeline keyframes count = " + mapUpdaterTimeline.build().getKeyFrames().size());
        mapUpdaterTimeline.delay(Duration.ONE).build().play();
        // Garbage collection
        System.gc();
    }

    public double getWidth() {
        if (map == null) {
            System.err.println("Map is null in MapContainer.java :| Shit!");
        }
        return map.length * GameIcon.BLOCK_WIDTH;
    }

    public double getHeight() {
        return map[0].length * GameIcon.BLOCK_HEIGHT;
    }

    private CombinedID combine(GameObjectID id, String currentUsername) {
        return new CombinedID(id, currentUsername);
    }

    protected class IconClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() == MouseButton.SECONDARY) {
                mainFrameInstance.currentState = GameMainFrameController.State.SELECT_UNIT_TO_COMMAND;
                mainFrameInstance.stateReason = GameMainFrameController.StateReason.NONE;
                return;
            } else if (t.getButton() == MouseButton.PRIMARY) {
                if (mainFrameInstance.currentState == GameMainFrameController.State.SELECT_UNIT_TO_COMMAND) {
                    GameObjectID id;
                    if (t.getSource() instanceof GameIcon) {
                        GameIcon source = (GameIcon) t.getSource();
                        id = source.getBoundID();
                    } else {
                        System.out.println("Source = " + t.getSource());
                        id = null;
                    }
                    mainFrameInstance.activeObjectID = id;
                    mainFrameInstance.showActionTab();
                } else {
                    if (mainFrameInstance.currentState == GameMainFrameController.State.SELECT_ONE_CELL
                            || mainFrameInstance.currentState == GameMainFrameController.State.SELECT_TWO_CELLS
                            || mainFrameInstance.currentState == GameMainFrameController.State.SELECT_THREE_CELLS) {
                        if (t.getSource() instanceof LandTile) {
                            LandTile s = (LandTile) t.getSource();
                            mainFrameInstance.selectedTile(s.getCol(), s.getRow());
                        }
                    } else if (mainFrameInstance.currentState == GameMainFrameController.State.SELECT_UNIT_FOR_PARAMETER) {
                        if (!(t.getSource() instanceof LandTile)) {
                            GameIcon s = (GameIcon) t.getSource();

                        }
                    }
                }
            }
        }
    }
}
