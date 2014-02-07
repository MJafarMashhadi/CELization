package celizationclient.frontend;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author mjafar
 */
public class LoginFormController extends FormsParent implements Initializable {

    @FXML
    protected TextField txtUsername;
    @FXML
    protected PasswordField txtPassword;
    @FXML
    protected TextField txtIP;
    @FXML
    protected Button btnLogin;
    @FXML
    protected Label lblMessage;
    @FXML
    protected ValidateForm formValidatior;
    @FXML
    protected ChoiceBox lstGames;

    protected HashMap<String, Integer> gamesMap;
    private int port = 0;

    public LoginFormController() {
        super();
        this.formValidatior = new ValidateForm();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtIP.textProperty().addListener(formValidatior);
        txtPassword.textProperty().addListener(formValidatior);
        txtUsername.textProperty().addListener(formValidatior);
        lstGames.getSelectionModel().selectedItemProperty().addListener(formValidatior);
    }

    @FXML
    protected void getGamesList() {
        try {
            txtIP.disableProperty().setValue(Boolean.TRUE);
            // get list through network
            gamesMap = client.getGamesList(txtIP.textProperty().get());
            //
            lstGames.getItems().clear();
            if (gamesMap.size() > 0) {
                lstGames.getItems().addAll(gamesMap.keySet());
                lstGames.disableProperty().setValue(Boolean.FALSE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "No games are running on server",
                        "No games hosting", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (UnknownHostException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Server is unreachable. probably it's not running.\n" + ex.getMessage(),
                    "Server unreachable", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Server is unreachable. probably it's not running.\n" + ex.getMessage(),
                    "Server unreachable", javax.swing.JOptionPane.ERROR_MESSAGE);
        } finally {
            txtIP.disableProperty().setValue(Boolean.FALSE);
        }
    }

    @FXML
    protected void btnLoginClick() {
        try {
            boolean login;
            btnLogin.setDisable(true);
            lblMessage.setText("Connecting...");

            port = gamesMap.get((String) lstGames.getItems().get(lstGames.getSelectionModel().getSelectedIndex())).intValue();

            super.client.startConnection(txtIP.getText(), port);
            lblMessage.setText("Logging in...");
            login = super.client.login(txtUsername.getText(), txtPassword.getText());
            if (login) {
                lblMessage.setText("Logged in");
                super.loginSuccess();
            } else {
                lblMessage.setText("Failed to login");
            }
        } catch (IOException ex) {
            lblMessage.setText("Failed to connect");
        } finally {
            btnLogin.setDisable(false);
        }
    }

    protected class ValidateForm implements ChangeListener<String> {

        @Override
        public void changed(ObservableValue<? extends String> ov, String t, String t1) {
            boolean valid = true;
            //
            fixTrim(txtIP);
            fixTrim(txtUsername);
            // Check game
            if (lstGames.getSelectionModel().getSelectedIndex() < 0) {
                valid = false;
            }
            // Check username
            if (!txtUsername.getText().matches(celization.GameParameters.usernamePattern)) {
                valid = false;
            }
            // Check password
            if (txtPassword.getText().length() < 5 || txtPassword.getText().length() > 25) {
                valid = false;
            }
            // Check IP
            if (txtIP.getText().length() < 3) {
                valid = false;
            }
            // Set button enabled
            btnLogin.setDisable(!valid);
        }

        private void fixTrim(TextField t) {
            if (!t.getText().equals(t.getText().trim())) {
                t.setText(t.getText().trim());
            }
        }
    }
}