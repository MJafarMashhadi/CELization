/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 * FXML Controller class
 *
 * @author mjafar
 */
public class CourseSelectionWindowController implements Initializable {

    @FXML
    private ChoiceBox lstAvailableCourses;
    @FXML
    private Button btnOk;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    protected void setCourseList(final ArrayList<String> items) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lstAvailableCourses.getItems().addAll(items);
                btnOk.setDisable(false);
            }
        });
    }

    protected String getResearchName() {
        return (String) lstAvailableCourses.getSelectionModel().getSelectedItem();
    }
    
    @FXML
    protected void btnOKClick(ActionEvent e) {
        
    }
}
