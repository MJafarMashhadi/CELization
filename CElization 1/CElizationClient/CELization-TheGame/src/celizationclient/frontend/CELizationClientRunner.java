package celizationclient.frontend;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author mjafar
 */
public class CELizationClientRunner extends Application {

    Stage stage;
    FXMLLoader fxmlLoader;
    FormsParent formsControllersParents;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        showLogin();
    }

    private void replaceSceneContent(String fxml) {
        Pane page;
        InputStream fxmlIS = null;
        try {
            fxmlLoader = new FXMLLoader();
            stage.hide();
            fxmlIS = FormsParent.class.getResourceAsStream(fxml);
            fxmlLoader.setLocation(FormsParent.class.getResource(fxml));
            
            page = (Pane) fxmlLoader.load(fxmlIS);
            Scene scene;
//            scene = stage.getScene();
//            if (scene == null) {
            scene = new Scene(page);
            stage.setScene(scene);
//            } else {
//                stage.getScene().setRoot(page);
//            }
            stage.sizeToScene();
//            stage.setResizable(false);
            stage.show();
            formsControllersParents = (FormsParent) fxmlLoader.getController();
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Failed to load " + fxml + "\n" + ex.toString());
            Platform.exit();
        } finally {
            if (fxmlIS != null) {
                try {
                    fxmlIS.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private void showLogin() {
        replaceSceneContent("LoginFormFXML.fxml");
        formsControllersParents.setApplicationInstance(this);
        stage.setResizable(false);
    }

    public void showGame() {
        replaceSceneContent("GameMainFrameFXML.fxml");
        formsControllersParents.setApplicationInstance(this);
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                formsControllersParents.client.logout();
                Platform.exit();
            }
        });
        stage.setResizable(true);
    }
}
