package gui.changelogs;

import datacontrollers.DataControllerAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.logs.UserLog;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is responsible for populating the lists and passing the data to the view for the logs.
 */

public class LogsController implements Initializable {

    @FXML
    private ListView<UserLog> patientLogList;
    @FXML
    private ListView<UserLog> questionnaireLogList;
    @FXML
    private ListView<UserLog> distributeLogList;
    @FXML
    private ListView<UserLog> userLogList;


    private final ObservableList<UserLog> allPatientsLog
            = FXCollections.observableArrayList();
    private final ObservableList<UserLog> allQuestionnairesLog
            = FXCollections.observableArrayList();
    private final ObservableList<UserLog> allDistributeLog
            = FXCollections.observableArrayList();
    private final ObservableList<UserLog> allUserLog
            = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            allDistributeLog.setAll(DataControllerAPI.getAllAdminLogs());
            allUserLog.setAll(DataControllerAPI.getAllAdminLogs());
            allQuestionnairesLog.setAll(DataControllerAPI.getAllAdminLogs());
            allPatientsLog.setAll(DataControllerAPI.getAllAdminLogs());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        patientLogList.setItems(allPatientsLog);
        questionnaireLogList.setItems(allQuestionnairesLog);
        distributeLogList.setItems(allDistributeLog);
        userLogList.setItems(allUserLog);
    }
}