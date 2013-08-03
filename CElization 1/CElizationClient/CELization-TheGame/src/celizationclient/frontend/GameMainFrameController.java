/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package celizationclient.frontend;

import celization.TurnEvent;
import celization.buildings.*;
import celization.buildings.extractables.*;
import celization.civilians.*;
import celizationclient.backend.net.FetchChatMessages;
import celizationclient.backend.net.NotReadyForNextTurnException;
import celizationrequests.GameObjectID;
import celizationrequests.turnaction.BuildingSellTurnAction;
import celizationrequests.turnaction.BuildingTrainTurnAction;
import celizationrequests.turnaction.MarketExchangeTurnAction;
import celizationrequests.turnaction.UniversityResearchTurnAction;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.SplitPaneBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author mjafar
 */
public class GameMainFrameController extends FormsParent implements Initializable {

    boolean clear = true;
    private InfoRefreshThread infoRefreshThread;
    @FXML
    private TabPane tabs;
    @FXML
    private ListView lstOnlineUsers;
    @FXML
    private TabPane chatTabs;
    @FXML
    private Text CELizationLogo;
    @FXML
    private MapContainer mapPreviewPane;
    @FXML
    private AnchorPane mapPane;
    @FXML
    private Label lblChatMessages;
    private HashMap<String, AnchorPane> chatBoxes;
    private HashMap<String, Integer> usersNewMessagesCount;
    // Resources page
    @FXML
    private ProgressBar prgGold;
    @FXML
    private ProgressBar prgStone;
    @FXML
    private ProgressBar prgFood;
    @FXML
    private ProgressBar prgLumber;
    @FXML
    private Label lblKnowledge;
    //
    @FXML
    private Label lblGoldsExtraction;
    @FXML
    private Label lblStoneExtraction;
    @FXML
    private Label lblLumberExtraction;
    @FXML
    private Label lblFoodExtraction;
    @FXML
    private Label lblFoodConsumption;
    @FXML
    private Label lblKnowledgeGeneration;
    //
    @FXML
    private ListView lstResearches;
    //
    @FXML
    private ListView lstTurnActions;
    //
    @FXML
    private TabPane actionsTabs;
    @FXML
    private Button btnSoldierMove;
    @FXML
    private Button btnSoldierFight;
    @FXML
    private Button btnWorkerMove;
    @FXML
    private Button btnWorkerWork;
    @FXML
    private Button btnWorkerBuild;
    @FXML
    private Button btnUniversityTrain;
    @FXML
    private Button btnUniversityResearch;
    @FXML
    private Button btnStableTrainJockey;
    @FXML
    private Button btnStableTrainMace;
    @FXML
    private Button btnBarracksSpear;
    @FXML
    private Button btnBarracksSapper;
    @FXML
    private Label lblSelectedUnitInfo;
    //
    @FXML
    private TextField txtTransferAmount;
    @FXML
    private ChoiceBox choiceFrom;
    @FXML
    private ChoiceBox choiceTo;
    @FXML
    private Button btnConvert;
    //
    @FXML
    private Button btnBuildBoat;
    @FXML
    private Button btnHQTrain;
    // Sell buttons
    @FXML
    private Button btnHQSell;
    @FXML
    private Button btnUniversitySell;
    @FXML
    private Button btnStableSell;
    @FXML
    private Button btnBarracksSell;
    @FXML
    private Button btnMarketSell;
    @FXML
    private Button btnStorageSell;
    @FXML
    private Button btnPortSell;
    //
    @FXML
    private ListView lstTurnEvents;
    @FXML
    private Button btnNextTurn;
    //
    private Timeline chatShow;
    private Timeline chatHide;
    private KeyValue chatHidden;
    private KeyValue chatShown;
    private KeyValue lblChatMessagesShown;
    private KeyValue lblChatMessagesHidden;
    public java.util.Timer chatMessagesFetcherTimer;
    private ChatMessagesTabsSelectionChangedHandler chatMessagesTabsSelectionChangedHandler;
    //
    private celization.GameState currentGameState;
    // changes when clicking on objects
    protected GameObjectID activeObjectID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chatHidden = new KeyValue(chatTabs.prefWidthProperty(), 0);
        chatShown = new KeyValue(chatTabs.prefWidthProperty(), 200);
        chatShow = TimelineBuilder.create()
                .keyFrames(new KeyFrame(Duration.ZERO, chatHidden))
                .keyFrames(new KeyFrame(Duration.millis(100), chatShown))
                .build();
        chatHide = TimelineBuilder.create()
                .keyFrames(new KeyFrame(Duration.ZERO, chatShown))
                .keyFrames(new KeyFrame(Duration.millis(100), chatHidden))
                .build();
        lblChatMessagesShown = new KeyValue(lblChatMessages.opacityProperty(), 1);
        lblChatMessagesHidden = new KeyValue(lblChatMessages.opacityProperty(), 0);
        usersNewMessagesCount = new HashMap<>();
        chatBoxes = new HashMap<>();
        chatMessagesTabsSelectionChangedHandler = new ChatMessagesTabsSelectionChangedHandler();
        chatTabs.getSelectionModel().selectedItemProperty().addListener(chatMessagesTabsSelectionChangedHandler);

//        PerspectiveCamera camera = PerspectiveCameraBuilder.create().fieldOfView(60).build();
//        mapPreviewPane.getScene().setCamera(camera);
//        mapPreviewPane.mapGrid.getScene().setCamera(camera);
        mapPreviewPane.setMainFrameInstance(this);
        mapPreviewPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
//                for (Node child : mapPane.getChildren()) {
//                    child.setLayoutX(t.getSceneX() + a.x);
//                    child.setLayoutY(t.getSceneY() + a.y);
//                }
            }
        });

        chatMessagesFetcherTimer = new java.util.Timer("Chat messages fetcher", true);
        chatMessagesFetcherTimer.schedule(new FetchChatMessages(super.client, this), 0, 1000);

        infoRefreshThread = new InfoRefreshThread();
        refreshInfo();
    }

    @FXML
    protected void toggleChatPane() {
        if (chatTabs.prefWidthProperty().get() == 0) {
            chatShow.play();
        } else {
            chatHide.play();
        }
    }

    @FXML
    protected void openNewChatTab(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() >= 2) {
            String targetUsername;
            targetUsername = (String) lstOnlineUsers.getSelectionModel().getSelectedItem();
            getChatTab(targetUsername);
        }
    }

    @FXML
    protected void nextTurn() {
        try {
            client.sendTurnActions();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lstTurnActions.getItems().clear();
                    btnNextTurn.setDisable(true);
                    clear = false;
                    // TODO : reset pagination buttons
                }
            });
        } catch (NotReadyForNextTurnException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "It's not your turn to send actions yet", "Unable to send actions", javax.swing.JOptionPane.WARNING_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(GameMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cancelAction() {
        // TODO
        lstTurnActions.getSelectionModel().getSelectedItem();
    }

    private void sell(Button btn) {
        btn.setDisable(true);
        client.addTurnAction(new BuildingSellTurnAction(activeObjectID));
        lstTurnActions.getItems().add("Sell " + activeObjectID);
    }

    @FXML
    private void btnHQSellClicked(ActionEvent event) {
        sell(btnHQSell);
    }

    @FXML
    private void btnUniversitySellClicked(ActionEvent event) {
        sell(btnUniversitySell);
    }

    @FXML
    private void btnStableSellClicked(ActionEvent event) {
        sell(btnStableSell);
    }

    @FXML
    private void btnBarracksSellClicked(ActionEvent event) {
        sell(btnBarracksSell);
    }

    @FXML
    private void btnMarketSellClicked(ActionEvent event) {
        sell(btnMarketSell);
    }

    @FXML
    private void btnStorageSellClicked(ActionEvent event) {
        sell(btnStorageSell);
    }

    @FXML
    private void btnPortSellClicked(ActionEvent event) {
        sell(btnPortSell);
    }

    @FXML
    private void btnConvertClicked(ActionEvent event) {
        btnConvert.setDisable(true);
        String from, to;
        int amount;
        from = choiceFrom.getValue().toString();
        to = choiceTo.getValue().toString();
        amount = Integer.valueOf(txtTransferAmount.getText());
// TODO: for test
        System.err.println(from);
        MarketExchangeTurnAction exchangeAction = new MarketExchangeTurnAction(activeObjectID, from, to, amount);
        client.addTurnAction(exchangeAction);
        lstTurnActions.getItems().add("Exchange resources");
    }

    @FXML
    private void btnBuildBoatClicked(ActionEvent event) {
        btnBuildBoat.setDisable(true);
        BuildingTrainTurnAction boatTraining = new BuildingTrainTurnAction(activeObjectID);
        client.addTurnAction(boatTraining);
        lstTurnActions.getItems().add("Make new boat");
    }

    @FXML
    private void btnHQTrainClicked(ActionEvent event) {
        btnHQTrain.setDisable(true);
        BuildingTrainTurnAction workerTraining = new BuildingTrainTurnAction(activeObjectID);
        client.addTurnAction(workerTraining);
        lstTurnActions.getItems().add("Train new worker");
    }

    @Override
    public void clearToSend() {
        refreshInfo();
        clear = true;
        btnNextTurn.setDisable(false);
    }

    private void refreshInfo() {
        Platform.runLater(infoRefreshThread);
    }

    private void addToNewMessagesCount(String username) {
        int newMessagesNewValue;
        if (usersNewMessagesCount.containsKey(username)) {
            newMessagesNewValue = usersNewMessagesCount.get(username).intValue();
            newMessagesNewValue++;
        } else {
            newMessagesNewValue = 1;
        }
        usersNewMessagesCount.put(username, newMessagesNewValue);
    }

    private int newMessagesCount() {
        int count = 0;
        if (usersNewMessagesCount.isEmpty()) {
            return 0;
        }

        for (Integer c : usersNewMessagesCount.values()) {
            count += c.intValue();
        }
        return count;
    }

    public void refreshMessagesCounter() {
        if (newMessagesCount() > 0) {
            if (lblChatMessages.getText().equals(String.valueOf(newMessagesCount()))) {
                return;
            }
            TimelineBuilder.create()
                    .keyFrames(new KeyFrame(Duration.ZERO, lblChatMessagesShown))
                    .keyFrames(new KeyFrame(Duration.millis(400), lblChatMessagesHidden))
                    .onFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    lblChatMessages.setText(String.valueOf(newMessagesCount()));
                    // play sound
                    TimelineBuilder.create()
                            .delay(Duration.millis(200))
                            .keyFrames(new KeyFrame(Duration.ZERO, lblChatMessagesHidden))
                            .keyFrames(new KeyFrame(Duration.millis(400), lblChatMessagesShown))
                            .build().play();
                }
            })
                    .build().play();
        } else {
            if (lblChatMessages.opacityProperty().get() > 0) {
                TimelineBuilder.create()
                        .keyFrames(new KeyFrame(Duration.ZERO, lblChatMessagesShown))
                        .keyFrames(new KeyFrame(Duration.seconds(1), lblChatMessagesHidden))
                        .build().play();
            }
        }
    }

    public Tab getChatTab(String username) {
        Tab messageTab;
        boolean alreadyOpen = false;
        int index;
        index = 0;
        for (Tab tab : chatTabs.getTabs()) {
            if (username.equals(tab.getText())) {
                alreadyOpen = true;
                break;
            }
            index++;
        }
        if (alreadyOpen) {
            messageTab = chatTabs.getTabs().get(index);
        } else {
            if (!chatBoxes.containsKey(username)) {
                chatBoxes.put(username, makeChatPane(username));
            }
            messageTab = TabBuilder.create()
                    .closable(true)
                    .content(chatBoxes.get(username))
                    .text(username)
                    .build();
            final int __index = index;
            final Tab __messageTab = messageTab;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatTabs.getTabs().add(__index, __messageTab);
                }
            });
        }

        chatTabs.getSelectionModel().select(index);
        return messageTab;
    }

    public void newMessage(String sender, String message) {
        if (message != null) {
            appendRecievedMessages(sender, message, getChatTab(sender));
            addToNewMessagesCount(sender);
        }
    }

    private void appendRecievedMessages(String sender, String message, Tab messageTab) {
        SplitPane targetChatBox;
        targetChatBox = ((SplitPane) ((AnchorPane) messageTab.getContent()).getChildren().get(0));
        StringProperty recievedMessagesTextProperty;
        TextArea targetTextArea = (TextArea) ((AnchorPane) targetChatBox.getItems().get(0)).getChildren().get(0);
        recievedMessagesTextProperty = targetTextArea.textProperty();
        recievedMessagesTextProperty.setValue(recievedMessagesTextProperty.getValue() + sender + ": " + message);
        targetTextArea.positionCaret(targetTextArea.getLength() - 1);
    }

    private AnchorPane makeChatPane(final String username) {
        double dividerPosition = 0.8;
        double splitPaneHeight = 470;
        final TextArea typeArea = TextAreaBuilder.create().prefHeight(splitPaneHeight * dividerPosition).wrapText(false).build();
        typeArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                if (t1.length() < 2) {
                    return;
                }
                if (t1.charAt(t1.length() - 1) == '\n') {
                    try {
                        client.sendChatMessage(username, typeArea.getText());
                        appendRecievedMessages(client.getUsername(), typeArea.getText(), getChatTab(username));
                        typeArea.textProperty().set("");
                    } catch (IOException ex) {
                        Logger.getLogger(GameMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
                        javax.swing.JOptionPane.showMessageDialog(null, "Couldn't send chat message", ex.getMessage(), javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return AnchorPaneBuilder.create()
                .children(SplitPaneBuilder.create()
                .orientation(Orientation.VERTICAL)
                .items(
                AnchorPaneBuilder.create().children(TextAreaBuilder.create().editable(false).wrapText(true).build()).build(),
                AnchorPaneBuilder.create().children(typeArea).build())
                .dividerPositions(new double[]{dividerPosition})
                .prefHeight(splitPaneHeight)
                .build())
                .build();
    }

    public void userWentOffline(String username) {
        appendRecievedMessages("ERROR OCUURED", "User went offline and your message didn't recieve your last message.", getChatTab(username));
    }

    protected void showActionTab() {
        int index = 0;
        if (activeObjectID == null) {
            index = 0;
            lblSelectedUnitInfo.setText("Unit actions:");
        } else {
            Class activeObjectType = activeObjectID.getType();
            //
            if (activeObjectType == celization.civilians.Worker.class) {
                index = 1;
                if (currentGameState.getWorkerByUID(activeObjectID).workState != celization.civilians.CivilianState.Free) {
                    btnWorkerBuild.setDisable(true);
                    btnWorkerMove.setDisable(true);
                    btnWorkerWork.setDisable(true);
                } else {
                    btnWorkerBuild.setDisable(false);
                    btnWorkerMove.setDisable(false);
                    btnWorkerWork.setDisable(false);
                }
            } else if (activeObjectType == celization.civilians.soldiers.JockeyHorseman.class
                    || activeObjectType == celization.civilians.soldiers.MaceHorseman.class
                    || activeObjectType == celization.civilians.soldiers.SapperInfantry.class
                    || activeObjectType == celization.civilians.soldiers.SpearInfantry.class) {
                index = 2;
            } else if (activeObjectType == HeadQuarters.class) {
                index = 3;
                if (currentGameState.getBuildingByUID(activeObjectID).busy()) {
                    btnHQSell.setDisable(true);
                    btnHQTrain.setDisable(true);
                } else {
                    btnHQSell.setDisable(false);
                    btnHQTrain.setDisable(false);
                }
            } else if (activeObjectType == University.class) {
                index = 4;
                if (currentGameState.getBuildingByUID(activeObjectID).busy()) {
                    btnUniversitySell.setDisable(true);
                    btnUniversityTrain.setDisable(true);
                } else {
                    btnHQSell.setDisable(false);
                    btnHQTrain.setDisable(false);
                }
            } else if (activeObjectType == Stable.class) {
                index = 5;
                if (currentGameState.getBuildingByUID(activeObjectID).busy()) {
                    btnStableTrainJockey.setDisable(true);
                    btnStableTrainMace.setDisable(true);
                    btnStableSell.setDisable(true);
                } else {
                    btnStableTrainJockey.setDisable(false);
                    btnStableTrainMace.setDisable(false);
                    btnStableSell.setDisable(false);
                }
            } else if (activeObjectType == Barracks.class) {
                index = 6;
                if (currentGameState.getBuildingByUID(activeObjectID).busy()) {
                    btnBarracksSapper.setDisable(true);
                    btnBarracksSpear.setDisable(true);
                    btnBarracksSell.setDisable(true);
                } else {
                    btnBarracksSapper.setDisable(false);
                    btnBarracksSpear.setDisable(false);
                    btnBarracksSell.setDisable(false);
                }
            } else if (activeObjectType == Market.class) {
                index = 7;
            } else if (activeObjectType == Storage.class
                    || activeObjectType == GoldMine.class
                    || activeObjectType == StoneMine.class
                    || activeObjectType == Farm.class
                    || activeObjectType == WoodCamp.class) {
                index = 8;
            } else if (activeObjectType == Port.class) {
                index = 9;
                if (currentGameState.getBuildingByUID(activeObjectID).busy()) {
                    btnPortSell.setDisable(true);
                } else {
                    btnPortSell.setDisable(false);
                }
            }
            if (activeObjectType == celization.civilians.Worker.class
                    || activeObjectType == celization.civilians.soldiers.JockeyHorseman.class
                    || activeObjectType == celization.civilians.soldiers.MaceHorseman.class
                    || activeObjectType == celization.civilians.soldiers.SapperInfantry.class
                    || activeObjectType == celization.civilians.soldiers.SpearInfantry.class) {
                lblSelectedUnitInfo.setText(activeObjectID.getType().getSimpleName() + " " + currentGameState.getCivilianByUID(activeObjectID).getName() + " Actions:");
            } else {
                lblSelectedUnitInfo.setText(activeObjectID.getType().getSimpleName() + " Actions:");
            }
        }

        actionsTabs.getSelectionModel().select(index);
    }

    private class ChatMessagesTabsSelectionChangedHandler implements ChangeListener<Tab> {

        @Override
        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
            int selectedIndex;
            selectedIndex = chatTabs.getSelectionModel().getSelectedIndex();
            String username = chatTabs.getTabs().get(selectedIndex).getText();
            usersNewMessagesCount.remove(username);
            refreshMessagesCounter();
        }
    }

    public void setOnlineList(final ArrayList<String> usersList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lstOnlineUsers.getItems().clear();
                lstOnlineUsers.getItems().addAll(usersList);
            }
        });
    }

    private void refreshResourcesPage() {
        prgGold.setProgress(1.f * currentGameState.getNaturalResources().numberOfGolds / currentGameState.getParams().goldCapacity);
        prgStone.setProgress(1.f * currentGameState.getNaturalResources().numberOfStones / currentGameState.getParams().stoneCapacity);
        prgFood.setProgress(1.f * currentGameState.getNaturalResources().numberOfFood / currentGameState.getParams().foodCapacity);
        prgLumber.setProgress(1.f * currentGameState.getNaturalResources().numberOfWoods / currentGameState.getParams().woodCapacity);
        lblKnowledge.setText(String.valueOf(currentGameState.getNaturalResources().numberOfScience));

        int foodExtractionEstimate = 0;
        int goldExtractionEstimate = 0;
        int stoneExtractionEstimate = 0;
        int lumberExtractionEstimate = 0;
        int foodConsumptionEstimate = 0;
        int knowledgeGenerationEstimate = currentGameState.getNumberOfScholars();

        for (Civilian civ : currentGameState.getCivilians().values()) {
            foodConsumptionEstimate += civ.getFoodConsumption();
        }
        for (Building bil : currentGameState.getBuildings().values()) {
            if (!(bil instanceof Extractable)) {
                continue;
            }
            if (bil instanceof Farm) {
                foodExtractionEstimate += currentGameState.getParams().minesExtractionRatioFOOD * 5;
            } else if (bil instanceof StoneMine) {
                stoneExtractionEstimate += currentGameState.getParams().minesExtractionRatioSTONE * 5;
            } else if (bil instanceof GoldMine) {
                goldExtractionEstimate += currentGameState.getParams().minesExtractionRatioGOLD * 5;
            } else if (bil instanceof WoodCamp) {
                lumberExtractionEstimate += currentGameState.getParams().minesExtractionRatioWOOD * 5;
            }
        }
        foodConsumptionEstimate += currentGameState.getBoats().size() * currentGameState.getParams().minesExtractionRatioFOOD * currentGameState.getParams().boatFoodProduction;

        lblFoodExtraction.setText(String.valueOf(foodExtractionEstimate));
        lblFoodConsumption.setText('-' + String.valueOf(foodConsumptionEstimate));
        lblStoneExtraction.setText(String.valueOf(stoneExtractionEstimate));
        lblGoldsExtraction.setText(String.valueOf(goldExtractionEstimate));
        lblLumberExtraction.setText(String.valueOf(lumberExtractionEstimate));
        lblKnowledgeGeneration.setText(String.valueOf(knowledgeGenerationEstimate));
    }

    @Override
    public void setTurnEvents(final ArrayList<TurnEvent> events) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < events.size(); i++) {
                    TurnEvent turnEvent = events.get(i);
                    int eventType;
                    String description;

                    eventType = turnEvent.getEventType();
                    description = turnEvent.getDescription();

                    switch (eventType) {
                        case TurnEvent.CONSTRUCTION_STARTED:
                            description = "Construction started: " + description;
                            break;
                        case TurnEvent.CONSTRUCTION_FINISHED:
                            description = "Construction finished: " + description;
                            break;
                        case TurnEvent.HUNGER_DEATH:
                            description = "Death from hunger: " + description;
                            break;
                        case TurnEvent.RESEARCH_STARTED:
                            description = "Research started: " + description;
                            break;
                        case TurnEvent.RESEARCH_FINISHED:
                            description = "Research finished: " + description;
                            break;
                        case TurnEvent.TRAINING_STARTED:
                            description = "Building/Training started: " + description;
                            break;
                        case TurnEvent.TRAINING_FINISHED:
                            description = "Building/Training finished: " + description;
                            break;
                        case TurnEvent.WAR_DEATH:
                            description = "Killed in action: " + description;
                            break;
                    }
                    lstTurnEvents.getItems().clear();
                    lstTurnEvents.getItems().add(description);
                }
            }
        });
    }

    private void checkResearches() {
        lstResearches.getItems().clear();
        for (GameObjectID courseID : currentGameState.getCourseManager().getCourses().keySet()) {
            String name = currentGameState.getCourseManager().getName(courseID);
            if (!currentGameState.getCourseManager().get(name).hasBeenDone()
                    && currentGameState.getCourseManager().dependencyMet(name)) {
                lstResearches.getItems().add(name);
            }
        }

        // TODO
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//        if (currentGameState.getCourseManager().hasBeenDone("")) {
//
//        }
//
    }

    @FXML
    private void btnUniversityResearchClick(ActionEvent event) {
        try {
            ObservableList<String> items;
            ArrayList<String> availableItems = new ArrayList<>();
            items = lstResearches.getItems();
            for (String item : items) {
                if (currentGameState.getCourseManager().dependencyMet(item)) {
                    availableItems.add(item);
                }
            }
            Stage selectResearch = new Stage();
            FXMLLoader loader;
            loader = new FXMLLoader();
            loader.setLocation(CourseSelectionWindowController.class.getResource("CourseSelectionWindow.fxml"));
            Parent root = (Parent) loader.load(CourseSelectionWindowController.class.getResourceAsStream("CourseSelectionWindow.fxml"));

            CourseSelectionWindowController controller;
            controller = (CourseSelectionWindowController) loader.getController();
            controller.setCourseList(availableItems);
            selectResearch.setScene(new Scene(root));
            selectResearch.initModality(Modality.WINDOW_MODAL);
            selectResearch.initOwner(super.application.stage.getScene().getWindow());
            selectResearch.setResizable(false);
            selectResearch.showAndWait();
            String researchName = controller.getResearchName();
            if (researchName == null) {
                return;
            }
            client.addTurnAction(new UniversityResearchTurnAction(activeObjectID, researchName));
            lstTurnActions.getItems().add("Research " + researchName);
            btnUniversitySell.setDisable(true);
            btnUniversityTrain.setDisable(true);
            btnUniversityResearch.setDisable(true);
        } catch (IOException ex) {
        }
    }

    private class InfoRefreshThread implements Runnable {

        @Override
        public void run() {
            try {
                currentGameState = client.getGameState();
            } catch (IOException ex) {
                Logger.getLogger(GameMainFrameController.class.getName()).log(Level.SEVERE, null, ex);
                javax.swing.JOptionPane.showMessageDialog(null, "Couldn't get new game map from server!\n" + ex.getMessage(), "Map not available!", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            // refresh looks and data
            refreshMessagesCounter();
            refreshResourcesPage();
            checkResearches();
            mapPreviewPane.updateMap(client);
        }
    }
}
