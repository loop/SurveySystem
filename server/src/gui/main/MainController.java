package gui.main;

import datacontrollers.DataControllerAPI;
import helpers.PopUpCreator;
import model.types.UserTypes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.user.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is responsible for changing views selected by the user and remembers which
 * user is currently logged in. It controls the whole main window scene.
 */

public class MainController implements Initializable {

    private static final Logger log = Logger.getLogger(MainController.class.getName());


    public static String currentUserLoggedIn;
    public static String userType;

    @FXML public Button questionnaireBuilderButton, patientsButton, questionnaireDeployButton;
    @FXML public MenuItem menuItemAddUser, exportQuestionnaireMenuItem, exportPatientsMenuItem, importQuestionnaireMenuItem;

    private final String[] systemViewsArray = {
            "/gui/home/home.fxml",
            "/gui/patients/patients.fxml",
            "/gui/questionnaires/creator/questionnaireCreator.fxml",
            "/gui/questionnaires/deployment/deployment.fxml",
            "/gui/questionnaires/distribute/distribute.fxml",
            "/gui/answers/Answers.fxml",
            "/gui/changelogs/logs.fxml",
            "/gui/questionnaires/export/exportQuestionnaires.fxml",
            "/gui/patients/exportPatients.fxml",
            "/gui/users/createUser.fxml",
            "/gui/users/changeMyPassword.fxml",
            "/gui/patients/importPatients.fxml"
    };
    @FXML
    private StackPane displayPane;

    /**
     * Initialises the view and checks the user type who is logged in and disables/enables necessary
     * features.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        goHome();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                }
                catch(InterruptedException ignored) {
                }
                try {
                    System.out.println(DataControllerAPI.getUserType(currentUserLoggedIn));
                    userType = DataControllerAPI.getUserType(currentUserLoggedIn);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> {
                    switch (userType) {
                        case UserTypes.LIMITED:
                            questionnaireBuilderButton.setDisable(true);
                            patientsButton.setDisable(true);
                            questionnaireDeployButton.setDisable(true);
                            menuItemAddUser.setDisable(true);
                            exportQuestionnaireMenuItem.setDisable(true);
                            exportPatientsMenuItem.setDisable(true);
                            importQuestionnaireMenuItem.setDisable(true);
                            break;
                    }
                    switch (userType) {
                        case UserTypes.STAFF:
                            menuItemAddUser.setDisable(true);
                            break;
                    }
                    if (MainController.currentUserLoggedIn.equals("fyp")) {
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        User user = new User("fyp", "fyp", currentDate, MainController.currentUserLoggedIn, "3");
                        try {
                            if (DataControllerAPI.checkLogin(user)) {
                                PopUpCreator.getAlert(Alert.AlertType.WARNING, "Please change password!", "Please change password", "It looks like you are using the default login details, Please change your password!");
                                goChangePassword();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }.start();
    }

    /**
     * Goes to the home view.
     */
    public void goHome() {
        setDisplayPane(systemViewsArray[0]);
    }

    /**
     * Goes to the patient view.
     */
    public void goPatients() {
        setDisplayPane(systemViewsArray[1]);
    }

    /**
     * Goes to the questionnaire builder view.
     */
    public void goQuestionnaireBuilder() {
        setDisplayPane(systemViewsArray[2]);
    }

    /**
     * Goes to the questionnaire deployment view.
     */
    public void goQuestionnaireDeploy() {
        setDisplayPane(systemViewsArray[3]);

    }

    /**
     * Goes to the questionnaire distribute view.
     */
    public void goQuestionnaireDistribute() {
        setDisplayPane(systemViewsArray[4]);

    }

    /**
     * Goes to the answers view.
     */
    public void viewAnswers() {
        setDisplayPane(systemViewsArray[5]);

    }

    /**
     * Goes to the logs view.
     */
    public void goLogs() {
        setDisplayPane(systemViewsArray[6]);

    }

    /**
     * Goes to the export export view.
     */
    public void goExportQuestionnaires() {
        setDisplayPane(systemViewsArray[7]);

    }

    /**
     * Goes to the export patients view.
     */
    public void goExportPatients() {
        setDisplayPane(systemViewsArray[8]);
    }

    /**
     * Goes to the user management view.
     */
    public void goAddUser() {
        setDisplayPane(systemViewsArray[9]);
    }

    /**
     * Goes to the import patients view.
     */
    public void goImportPatients() {
        setDisplayPane(systemViewsArray[11]);
    }

    /**
     * Closes the application and terminates all threads.
     */
    public void goClose() {
        Platform.exit();
    }

    /**
     * Opens about dialog.
     */
    public void goAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About Desktop Server");
        about.setHeaderText("Final Year Project");
        about.setContentText("This is a desktop application created for final year project. \n");
        about.showAndWait();
    }

    /**
     * Goes to the change password view.
     */
    public void goChangePassword() {
        setDisplayPane(systemViewsArray[10]);
    }

    /**
     * Changes the display pane to the FXML of the view clicked by the user.
     *
     * @param viewPath
     */
    private void setDisplayPane(String viewPath) {
        log.log(Level.INFO, "Current user logged in is: " + MainController.currentUserLoggedIn);
        displayPane.getChildren().clear();
        displayPane.requestLayout();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Pane pane = fxmlLoader.load();
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            displayPane.getChildren().add(0, pane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}