package celizationclient.frontend;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mjafar
 */
public class BuildingSelectionWindowController implements Initializable {

    @FXML
    private ChoiceBox lstAvailableTypes;
    @FXML
    private Button btnOk;

    private Stage stage;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    protected void setBuildingsList(final ArrayList<String> items) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lstAvailableTypes.getItems().addAll(items);
                btnOk.setDisable(false);
            }
        });
    }

    protected String getTypeName() {
        return (String) lstAvailableTypes.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    protected void btnOKClick(ActionEvent e) {
        stage.close();
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
