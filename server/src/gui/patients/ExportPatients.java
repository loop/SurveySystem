package gui.patients;

import datacontrollers.DataControllerAPI;
import helpers.Exporter;
import model.patient.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for setting up the view and buttons to export patients.
 */

public class ExportPatients implements Initializable {

    private static final Logger log = Logger.getLogger(ExportPatients.class.getName());

    private final ObservableList<Patient> patients = FXCollections.observableArrayList();
    @FXML
    TableView<Patient> patientTableView;
    @FXML
    private Parent root;
    @FXML
    private TableColumn<Patient, String> tablePatientNHSColumn;
    @FXML
    private TableColumn<Patient, String> tablePatientFirstNameColumn;
    @FXML
    private TableColumn<Patient, String> tablePatientSecondNameColumn;
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupAnswerTable();
        setupButtons();
    }

    /**
     * Sets up the table of the list of patients.
     */
    public void setupAnswerTable() {
        tablePatientNHSColumn.setCellValueFactory(new PropertyValueFactory<>("nhsNumber"));
        tablePatientFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tablePatientSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        try {
            patients.setAll(DataControllerAPI.getAllPatients());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        patientTableView.setItems(patients);
    }

    /**
     * Sets up the export button and file chooser.
     */
    private void setupButtons() {
        this.exportButton.setOnAction(actionEvent -> {
            try {
                Patient patient = patientTableView.getSelectionModel().getSelectedItem();
                if (patient == null) {
                } else {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Location");
                    fileChooser.setInitialFileName(patient.getNhsNumber() + "_patient_" + ".csv");
                    File fileChoice = fileChooser.showSaveDialog(root.getScene().getWindow());
                    if (fileChoice == null) {
                    } else {
                        String path = fileChoice.getPath();
                        try {
                            Exporter.exportPatient(patient, path);
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "Could not export patient! Something went wrong.");
                        }
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
    }

}