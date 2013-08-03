/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;

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

    public void updateMap(CElizationClient client) {
        GameState currentGameState;
        //
        map = null;
        realMap = null;
        mapGrid.getChildren().clear();
        //
        try {
            currentGameState = client.getGameState();
        } catch (IOException ex) {
            System.err.println("Couldn't get game state");
            return;
        }

        String username = client.getUsername();
        realMap = currentGameState.getGameMap();

        map = new LandTile[realMap.getGameMapSize().col][realMap.getGameMapSize().row];
        for (int col = 0; col < map.length; col++) {
            for (int row = 0; row < map[col].length; row++) {
                map[col][row] = new LandTile();
                map[col][row].setOnMouseClicked(iconClickHandler);
                mapGrid.add(map[col][row], row, col);
//                map[col][row].setType(realMap.get(col, row).getType(username));
                map[col][row].setType(realMap.get(col, row).getType(false, username));
            }
        }

        // Civilians:
        HashMap<String, UserInfo> usersList;
        usersList = currentGameState.getGameInstance().getUsersList();

        GameState currentUserGameState;
        Coordinates currentCivilianLocation;
        CivilianIcon civilianIcon;
        usersLoop:
        for (String currentUsername : usersList.keySet()) {
            currentUserGameState = usersList.get(currentUsername).getGame();
            civiliansLoop:
            for (celization.civilians.Civilian currentCivilian : currentUserGameState.getCivilians().values()) {
                if (currentCivilian instanceof celization.civilians.Scholar || !currentCivilian.stillAlive() || !currentCivilian.isMature()) {
                    continue civiliansLoop;
                }
                currentCivilianLocation = currentCivilian.getLocation();

                civilianIcon = new CivilianIcon(currentCivilian.getID(), client, currentCivilian.getName(), currentUsername);
                civilianIcon.setOnMouseClicked(iconClickHandler);
                MapContainer.this.getChildren().add(civilianIcon);
                civilianIcon.setLayoutX((currentCivilianLocation.col) * GameIcon.BLOCK_WIDTH);
                civilianIcon.setLayoutY((currentCivilianLocation.row) * GameIcon.BLOCK_HEIGHT);
            }
            // Buildigns
            Building currentBuilding;
            BuildingIcon buildingIcon;
            Coordinates currentBuildingLocation;
            int size;
            for (GameObjectID currentBuildingID : currentUserGameState.getBuildings().keySet()) {
                currentBuilding = currentUserGameState.getBuildingByUID(currentBuildingID);
                currentBuildingLocation = currentBuilding.getLocation();
                if (!currentBuilding.buildBuildingFinished()) {
                    size = currentBuilding.size.row;
                    buildingIcon = new BuildingIcon(GameIcon.INCOMPLETE_BUILDINGS[size], currentBuildingID, currentUsername);
                } else {
                    Image icon = null;
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
                    buildingIcon = new BuildingIcon(icon, currentBuildingID, currentUsername);
                    buildingIcon.setOnMouseClicked(iconClickHandler);
                }

                buildingIcon.setLayoutX((currentBuildingLocation.col) * GameIcon.BLOCK_WIDTH);
                buildingIcon.setLayoutY((currentBuildingLocation.row) * GameIcon.BLOCK_HEIGHT);


                MapContainer.this.getChildren().add(buildingIcon);
//                }
            }
            // Boats
            Boat currentBoat;
            BoatIcon boatIcon;
            Coordinates currentBoatLocation;
            for (GameObjectID currentBoatID : currentUserGameState.getBoats().keySet()) {
                currentBoat = currentUserGameState.getBoatByUID(currentBoatID);
                currentBoatLocation = currentBoat.getLocation();
                if (currentBoat.buildingFinished()) {
                    boatIcon = new BoatIcon(currentBoatID, currentUsername);
                    boatIcon.setLayoutX((currentBoat.getLocation().col) * GameIcon.BLOCK_WIDTH);
                    boatIcon.setLayoutY((currentBoat.getLocation().row) * GameIcon.BLOCK_HEIGHT);
                    boatIcon.setOnMouseClicked(iconClickHandler);
                    MapContainer.this.getChildren().add(boatIcon);
                }
//                }
            }
        }

        // Garbage collection
        System.gc();
    }

    protected class IconClickHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() != MouseButton.PRIMARY) {
                return;
            }
            GameObjectID id;
            if (t.getSource() instanceof GameIcon) {
                GameIcon source = (GameIcon) t.getSource();
                id = source.getBoundID();
            } else {
                id = null;
            }
            System.out.println("Active id changed to: " + id);
            mainFrameInstance.activeObjectID = id;
            mainFrameInstance.showActionTab();
        }
    }
}
