package gui.patients;

import datacontrollers.DataControllerAPI;
import helpers.PopUpCreator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import model.patient.Patient;
import helpers.PatientValidation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Responsible for setting up the view and buttons to for patient management.
 */
public class PatientsController implements Initializable {

    private final ObservableList<Patient> patientSearchResultList
            = FXCollections.observableArrayList();
    private final ObservableList<Patient> allPatientSearchResultList
            = FXCollections.observableArrayList();
    @FXML private Label infoPatients;
    @FXML
    private ListView<Patient> patientListView;
    @FXML
    private TextField patientSearchField, nhsNumberField, firstNameField, middleNameField, lastNameField, dayDOBField, monthDOBField, yearDOBField, postcodeField;
    @FXML
    private Button addNewPatientButton;
    private TextField[] dataInputFields, requiredFields;
    @FXML
    private ToolBar controlToolBar;
    private final Button saveNewButton = new Button("Save New Patient");
    private final Button cancelNewPatientButton = new Button("Cancel");
    private final Button saveChangesButton = new Button("Save Changes");
    private final Button deletePatientButton = new Button("Delete Patient");
    private final Button deselectPatientButton = new Button("Deselect Patient");
    private final Button clearFieldsButton = new Button("Clear Fields");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dataInputFields = new TextField[]{nhsNumberField, firstNameField, middleNameField, lastNameField, dayDOBField,
                monthDOBField, yearDOBField, postcodeField};
        requiredFields = new TextField[]{nhsNumberField, firstNameField, lastNameField, dayDOBField,
                monthDOBField, yearDOBField};

        initialiseButtons();
        initialisePatientsListView();

        setInputFieldsEnabled(false, dataInputFields);
        setInputFieldsVisible(false);
        fetchAllPatients();
    }

    /**
     * Sets up the buttons
     */
    public void initialiseButtons() {
        saveNewButton.setOnAction(this::savePatientData);
        clearFieldsButton.setOnAction(actionEvent -> clearInputFields(dataInputFields));
        cancelNewPatientButton.setOnAction(actionEvent -> clearWorkspace());
        saveChangesButton.setOnAction(this::savePatientData);
        deletePatientButton.setOnAction(actionEvent -> deleteExistingPatient());
        deselectPatientButton.setOnAction(actionEvent -> patientListView.getSelectionModel().clearSelection());
        addNewPatientButton.setOnAction(event -> setupViewForEditingOrAddingPatientAction(1));
    }

    /**
     * Sets up the patients list.
     */
    public void initialisePatientsListView() {
        patientListView.setItems(patientSearchResultList);
        patientListView.setCellFactory(new Callback<ListView<Patient>, ListCell<Patient>>() {
            @Override
            public ListCell<Patient> call(ListView<Patient> p) {
                return new ListCell<Patient>() {
                    @Override
                    protected void updateItem(Patient patient, boolean aBool) {
                        super.updateItem(patient, aBool);
                        if (patient != null) {
                            setText("#" + patient.getNhsNumber() + ": \n" + patient.getFirstName() + " " + patient.getSurname());
                        }
                    }

                };
            }
        });
        getSelectedPatientDetailsAction();
    }

    /**
     * Gets the selected patient details from the database.
     */
    public void getSelectedPatientDetailsAction() {
        patientListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Patient>() {
                    public void changed(ObservableValue<? extends Patient> ov, Patient oldValue, Patient newValue) {
                        existingPatientSelected(newValue);
                    }
                });
    }

    /**
     * Updates the editing pane depending on if it is for a new patient or a exsiting patient.
     *
     * @param editingOrAdding
     */
    public void setupViewForEditingOrAddingPatientAction(int editingOrAdding) {
        clearWorkspace();
        setInputFieldsEnabled(true, dataInputFields);
        switch (editingOrAdding) {
            case 1:
                setInputFieldsVisible(true);
                controlToolBar.getItems().addAll(new FlexibleToolbarSpace(), saveNewButton, cancelNewPatientButton, clearFieldsButton, new FlexibleToolbarSpace());
                break;
            case 0:
                setInputFieldsVisible(true);
                nhsNumberField.setDisable(true);
                controlToolBar.getItems().addAll(new FlexibleToolbarSpace(), saveChangesButton, deselectPatientButton, deletePatientButton, new FlexibleToolbarSpace());
                break;
        }
    }

    /**
     * Gets all the existing patients from the database.
     */
    public void fetchAllPatients() {
        try {
            allPatientSearchResultList.clear();
            patientSearchResultList.setAll(DataControllerAPI.getAllPatients());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the input fields for the selected patient details.
     *
     * @param patient selected patient
     */
    public void existingPatientSelected(Patient patient) {
        setupViewForEditingOrAddingPatientAction(0);
        if (patient == null) {
            clearWorkspace();
        } else {
            String[] dob = patient.getDateOfBirth().split("-");
            nhsNumberField.setText(patient.getNhsNumber());
            firstNameField.setText(patient.getFirstName());
            middleNameField.setText(patient.getMiddleName());
            lastNameField.setText(patient.getSurname());
            yearDOBField.setText(dob[0]);
            monthDOBField.setText(dob[1]);
            dayDOBField.setText(dob[2]);
            postcodeField.setText(patient.getPostcode());
        }
    }

    /**
     * Validates and checks the inputted patients details before saving to database.
     * @param actionEvent
     */
    public void savePatientData(ActionEvent actionEvent) {
        boolean saveEditPatientProcess = false;
        boolean patientAlreadyExists = false;
        if (PatientValidation.checkFieldValidation(checkRequiredFieldsAreCompleted(requiredFields), getPatientFields())) {
            String dob = yearDOBField.getText().trim() + "-" + monthDOBField.getText().trim() + "-" + dayDOBField.getText().trim();
            Patient patient = new Patient(nhsNumberField.getText(), firstNameField.getText().trim(),
                    middleNameField.getText().trim(), lastNameField.getText().trim(), dob, postcodeField.getText().toUpperCase().trim());
            try {
                if (actionEvent.getSource() != saveChangesButton) {
                    if (actionEvent.getSource() == saveNewButton) {
                        DataControllerAPI.addPatient(patient);
                    }
                } else {
                    DataControllerAPI.updatePatient(patient);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                patientAlreadyExists = true;
                PopUpCreator.getAlert(Alert.AlertType.ERROR, "Patient Save Error", "Patient Save Error", "NHS Number already exists!");
            }
            if (!patientAlreadyExists) {
                saveEditPatientProcess = true;
            }
        } else {
            PopUpCreator.getAlert(Alert.AlertType.ERROR, "Patient Save Error", "Patient Save Error", "Looks like some details are in incorrect format. Try again.");
        }
        if (saveEditPatientProcess) {
            clearWorkspace();
            initialisePatientsListView();
            fetchAllPatients();
            PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Patient Saved", "Patient Saved Successfully", "Patient has been successfully saved!");

        }
    }

    /**
     * Deletes the patient and updates the list view.
     */
    public void deleteExistingPatient() {
        Patient selectedPatient = patientListView.getSelectionModel().getSelectedItem();
        try {
            DataControllerAPI.removePatient(selectedPatient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        clearWorkspace();
        initialisePatientsListView();
        fetchAllPatients();
    }

    /**
     * Gets the content of the patient input fields.
     *
     * @return
     */
    public String[] getPatientFields() {
        return new String[]{nhsNumberField.getText(), firstNameField.getText().trim(),
                middleNameField.getText().trim(), lastNameField.getText().trim(), dayDOBField.getText().trim(), monthDOBField.getText().trim(), yearDOBField.getText().trim(), postcodeField.getText().toUpperCase().trim()};
    }

    /**
     * Clears the view.
     */
    public void clearWorkspace() {
        setInputFieldsVisible(false);
        clearInputFields(dataInputFields);
        setInputFieldsEnabled(false, dataInputFields);
        controlToolBar.getItems().clear();
    }

    /**
     * Makes the editing pane visible.
     *
     * @param inputFieldsHidden boolean to hide or show editing pane
     */
    public void setInputFieldsVisible(boolean inputFieldsHidden) {
        infoPatients.setText("This is where you can modify or add new patients to the system.\n Add a new patient by clicking the '+' button on the right or select an existing patient.");
        infoPatients.setVisible(!inputFieldsHidden);
        for (TextField textfield : dataInputFields) {
            textfield.setVisible(inputFieldsHidden);
        }
    }

    public static void clearInputFields(TextField[] dataFields) {
        for (TextField textField : dataFields) {
            textField.clear();
        }
    }

    public static void setInputFieldsEnabled(boolean enabled, TextField[] dataInputFields) {
        for (TextField textField : dataInputFields) {
            textField.setDisable(!enabled);
        }
    }

    public static boolean checkRequiredFieldsAreCompleted(TextField[] fieldsToCheck) {
        boolean allFieldsFilled = true;
        for (TextField textField : fieldsToCheck) {
            if (textField.getText().isEmpty()) {
                allFieldsFilled = false;
            }
        }
        return allFieldsFilled;
    }

    class FlexibleToolbarSpace extends Region {
        public FlexibleToolbarSpace() {
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    }
}