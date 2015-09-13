package gui.users;

import datacontrollers.DataControllerAPI;
import gui.main.MainController;
import helpers.PopUpCreator;
import model.types.UserTypes;
import model.user.User;
import model.logs.UserLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * This controller is responsible for setting up the view and managing users for the system.
 */
public class CreateUserController implements Initializable {
    private final ObservableList<User> allUsers
            = FXCollections.observableArrayList();
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public ChoiceBox usertype;
    public ListView<User> adminListView;
    @FXML
    ObservableList<String> userTypesOptions =
            FXCollections.observableArrayList(
                    "Admin",
                    "Staff",
                    "Limited"
            );
    String currentDate;
    private String userTypeSelected;
    private User user = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllAdmins();
        initialiseUsersListView();
        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        usertype.setItems(FXCollections.observableArrayList("Select a user type",
                new Separator(),
                "Admin",
                "Staff",
                "Limited"
        ));
        usertype.getSelectionModel().select(0);
        usertype.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int userTypeChoiceInt;
            userTypeChoiceInt = newValue.intValue() - 1;
            if (userTypeChoiceInt == 1) {
                userTypeSelected = UserTypes.ADMIN;
            } else if (userTypeChoiceInt == 2) {
                userTypeSelected = UserTypes.STAFF;
            } else if (userTypeChoiceInt == 3) {
                userTypeSelected = UserTypes.LIMITED;
            }
            System.out.println("Selected choice: " + userTypeSelected);
        });
    }

    /**
     * Creates a new user object and adds it to the database.
     */
    public void addUser() {
        if (!StringUtils.isBlank(usernameField.getText()) || !StringUtils.isBlank(passwordField.getText())) {
            user = new User(usernameField.getText(), passwordField.getText(), currentDate, MainController.currentUserLoggedIn, userTypeSelected);
        }
        try {
            if (user != null) {
                if (DataControllerAPI.addAdminUser(user)) {
                    PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "New Admin User", "New user with name \"" + user.getUsername() + "\" added", "A new user has been added successfully!");
                    UserLog userLog = new UserLog(1, MainController.currentUserLoggedIn, user.getUsername(), "INSERT NEW USER", currentDate);
                    DataControllerAPI.actionAdminLog(userLog);
                    usernameField.clear();
                    passwordField.clear();
                }
            } else {
                PopUpCreator.getAlert(Alert.AlertType.WARNING, "New Admin User", "New user failure", "A new user could not be added because some fields were left empty.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            PopUpCreator.getAlert(Alert.AlertType.WARNING, "New Admin User", "New user failure", "A new user could not be added because username already exists.");
        }
        getAllAdmins();
        initialiseUsersListView();
    }

    /**
     * Gets all the existing admins from the database.
     */
    public void getAllAdmins() {
        try {
            allUsers.clear();
            allUsers.setAll(DataControllerAPI.getAllAdmins());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the user list view and adds usernames.
     */
    public void initialiseUsersListView() {
        adminListView.setItems(allUsers);
        adminListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> p) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean aBool) {
                        super.updateItem(user, aBool);
                        if (user != null) {
                            setText(user.getUsername());
                        }
                    }

                };
            }
        });
    }

    /**
     * Deletes selected user from the database and updates the list.
     */
    public void deletesUsers() {
        User selectedUser = adminListView.getSelectionModel().getSelectedItem();
        try {
            DataControllerAPI.removeAdmin(selectedUser);
            UserLog userLog = new UserLog(1, MainController.currentUserLoggedIn, selectedUser.getUsername(), "DELETE USER", currentDate);
            DataControllerAPI.actionAdminLog(userLog);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getAllAdmins();
        initialiseUsersListView();
    }
}
