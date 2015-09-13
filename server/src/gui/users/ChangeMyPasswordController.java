package gui.users;

import datacontrollers.DataControllerAPI;
import gui.main.MainController;
import helpers.PopUpCreator;
import model.user.User;
import model.logs.UserLog;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * This controller is responsible for setting up the view for changing password user logged in.
 */

public class ChangeMyPasswordController implements Initializable {
    public PasswordField currentPassword, newPassword, newPasswordAgain;
    public Button changePassword;
    private final String currentUser = MainController.currentUserLoggedIn;
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changePassword.setOnAction(event -> changeOldPasswordToNewPassword());
    }

    /**
     * Checks typed old password of the user
     * @return true is password matches old password
     */
    public boolean checkOldPassword(){
        if (!StringUtils.isBlank(currentPassword.getText())) {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            user = new User(currentUser, currentPassword.getText(), currentDate, currentUser, "3");
        }
        try {
            if(DataControllerAPI.checkPasswordForUsername(user)) {
                System.out.println("Correct previous password");
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Changes the old password to newly typed in password.
     */
    public void changeOldPasswordToNewPassword() {
        if (checkFields()) {
            if (checkOldPassword()) {
                try {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    User newPasswordUser = new User(currentUser, newPassword.getText(), currentDate, currentUser, "3");
                    if (DataControllerAPI.updateAdminPassword(newPasswordUser)) {
                        PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Admin Password", "Password change successfully", "Your password has been changed successfully.");
                        UserLog userLog = new UserLog(1, MainController.currentUserLoggedIn, "NONE", "USER PASSWORD CHANGE", currentDate);
                        DataControllerAPI.actionAdminLog(userLog);
                    } else {
                        PopUpCreator.getAlert(Alert.AlertType.WARNING, "Admin Password", "Password change failed", "Your password change has failed. Please try again.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                PopUpCreator.getAlert(Alert.AlertType.WARNING, "Admin Password", "Password change failed", "Your old password did not match. Please try again");
            }
        } else {
            PopUpCreator.getAlert(Alert.AlertType.WARNING, "Admin Password", "Password change failed", "It looks like not all fields were filled. Please enter all the fields and try again");
        }
    }

    /**
     * Ensures that the fields are not blank when update button is clicked.
     * @return
     */
    public boolean checkFields() {
        return !(StringUtils.isBlank(currentPassword.getText()) || StringUtils.isBlank(newPassword.getText()) || StringUtils.isBlank(newPasswordAgain.getText()));
    }
}
