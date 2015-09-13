package gui.login;

import datacontrollers.DataControllerAPI;
import gui.main.MainController;
import model.user.User;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * This class is responsible for controlling the login view.
 */
public class LoginController implements Initializable {
    private final ReadOnlyBooleanWrapper loggedIn = new ReadOnlyBooleanWrapper();
    public Label incorrectLabel;
    public Button loginButton;
    User user = null;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    public boolean isLoggedIn() {
        return loggedIn.get();
    }

    public String getLoggedInUsername() {
        return username.getText();
    }

    public ReadOnlyBooleanProperty loggedInProperty() {
        return loggedIn.getReadOnlyProperty();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        handleLoginAttempt();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Checks user login details when login button is pressed.
     *
     * @throws SQLException
     */
    @FXML
    private void handleLoginAttempt() throws SQLException {
        if (StringUtils.isBlank(username.getText()) || StringUtils.isBlank(password.getText())) {
        } else {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            user = new User(username.getText(), password.getText(), currentDate, MainController.currentUserLoggedIn, "3");
        }
        if (!(user != null & DataControllerAPI.checkLogin(user))) {
            incorrectLabel.setText("Username or password is incorrect.");
        } else {
                loggedIn.set(true);
            }
    }
}