package gui.questionnaires.distribute;

import datacontrollers.DataControllerAPI;
import helpers.PopUpCreator;
import model.types.QuestionnaireStates;
import model.questionnaire.Questionnaire;
import model.patient.TablePatient;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * This controller is responsible for setting up the view and assigning export to patients.
 */

public class DistributeController implements Initializable {

    protected static HashMap<String, Boolean> assignedHashMap;
    @FXML
    private ListView<Questionnaire> questionnairePointerListView;
    @FXML
    private TableView<TablePatient> patientTableView;
    @FXML
    private TableColumn<TablePatient, String> nhsnumber;
    @FXML
    private TableColumn<TablePatient, String> name;
    @FXML
    private TableColumn<TablePatient, Boolean> checkBoxAssignmentBox;
    private Questionnaire selectedQuestionnairePointer;
    private final ObservableList<Questionnaire> questionnaires2
            = FXCollections.observableArrayList();
    private final ObservableList<Questionnaire> questionnaires1
            = FXCollections.observableArrayList();
    private final ObservableList<TablePatient> patients
            = FXCollections.observableArrayList();
    private final ObservableList<TablePatient> patients1
            = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupQuestionnairePointerListView();
        setupTableView();

        nhsnumber.setCellValueFactory(new PropertyValueFactory<>("propertyNHSNumber"));
        name.setCellValueFactory(new PropertyValueFactory<>("propertyFullName"));
        checkBoxAssignmentBox.setCellValueFactory(new PropertyValueFactory<>("propertyIsAssigned"));
        checkBoxAssignmentBox.setCellFactory(p -> new TableCellCheckBox<>());


        patients1.clear();
        try {
            patients.setAll(DataControllerAPI.getAllTablePatients());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getDeployedQuestionnaires();

    }

    /**
     * Sets up a table with the patient details and checkbox to assign export.
     */
    public void setupQuestionnairePointerListView() {
        questionnairePointerListView.setItems(questionnaires2);
        questionnairePointerListView.setCellFactory(new Callback<ListView<Questionnaire>, ListCell<Questionnaire>>() {
            @Override
            public ListCell<Questionnaire> call(ListView<Questionnaire> p) {
                return new ListCell<Questionnaire>() {
                    @Override
                    protected void updateItem(Questionnaire pointer, boolean aBool) {
                        super.updateItem(pointer, aBool);
                        if (pointer == null) {
                            return;
                        }
                        setText(pointer.getTitle());
                    }
                };
            }
        });
        questionnairePointerListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Questionnaire>() {
                    @Override
                    public void changed(ObservableValue<? extends Questionnaire> observableValue, Questionnaire oldValue, Questionnaire newValue) {
                        if (oldValue == null) {
                        } else {
                            assignAction();
                        }
                        selectedQuestionnairePointer = newValue;
                        if (newValue != null) {
                            ArrayList<TablePatient> patients = new ArrayList<>(DistributeController.this.patients);
                            try {
                                assignedHashMap = DataControllerAPI.areTablePatientsAssignedToQuestionnaire(patients, selectedQuestionnairePointer);
                                for (Iterator<TablePatient> iterator = DistributeController.this.patients.iterator(); iterator.hasNext(); ) {
                                    TablePatient patient = iterator.next();
                                    if (!assignedHashMap.get(patient.getNhsNumber())) {
                                        patient.setPropertyIsAssigned(false);
                                        patient.setOrignalAssignment(false);
                                    } else {
                                        patient.setPropertyIsAssigned(true);
                                        patient.setOrignalAssignment(true);
                                    }
                                }
                                for (Iterator<TablePatient> iterator = patients1.iterator(); iterator.hasNext(); ) {
                                    TablePatient patient = iterator.next();
                                    if (!assignedHashMap.get(patient.getNhsNumber())) {
                                        patient.setPropertyIsAssigned(false);
                                        patient.setOrignalAssignment(false);
                                    } else {
                                        patient.setPropertyIsAssigned(true);
                                        patient.setOrignalAssignment(true);
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (!patientTableView.getColumns().contains(checkBoxAssignmentBox)) {
                                patientTableView.getColumns().add(checkBoxAssignmentBox);
                            }
                        } else {
                            for (TablePatient patient : patients) {
                                patient.setPropertyIsAssigned(false);
                                patient.setOrignalAssignment(false);
                            }
                            for (TablePatient patient : patients1) {
                                patient.setPropertyIsAssigned(false);
                                patient.setOrignalAssignment(false);
                            }
                            assignedHashMap.clear();
                            patientTableView.getColumns().remove(checkBoxAssignmentBox);
                        }
                    }
                }
        );
    }

    /**
     * Allows changing of assignments of export to patients.
     */
    public void setupTableView() {
        patientTableView.setEditable(true);
        patientTableView.getColumns().remove(checkBoxAssignmentBox);
        patientTableView.setItems(patients);
    }

    /**
     * Gets the deployed state export.
     */
    public void getDeployedQuestionnaires() {
        try {
            questionnaires1.clear();
            questionnaires2.setAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.DEPLOYED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the database to assign/unassign questionnaire to patient.
     */
    public void assignAction() {
        if (selectedQuestionnairePointer == null) {
            return;
        }
        boolean assignmentsChanged = false;

        for (Iterator<TablePatient> iterator = patients.iterator(); iterator.hasNext(); ) {
            TablePatient patient = iterator.next();

                try {
                    DataControllerAPI.unassignQuestionnaire(patient, selectedQuestionnairePointer);
                    patient.setOrignalAssignment(false);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            assignmentsChanged = true;
        }
        if (assignmentsChanged) {
            PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "Questionnaire Distribute", "Changes have been saved!", "The questionnaire assignments have been successfully changed!");
        }
    }

    /**
     * Class to create the checkbox.
     * @param <S>
     * @param <T>
     *
     * Source: http://www.java2s.com/Tutorial/Java/0280__SWT/TableWithCheckBoxCell.htm
     */
    public static class TableCellCheckBox<S, T> extends TableCell<S, T> {
        private final CheckBox checkBox;
        private ObservableValue<T> ov;

        public TableCellCheckBox() {
            checkBox = new CheckBox();
            checkBox.setAlignment(Pos.CENTER);

            setAlignment(Pos.CENTER);
            setGraphic(checkBox);
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setGraphic(checkBox);
                if (!(ov instanceof BooleanProperty)) {
                } else {
                    checkBox.selectedProperty().unbindBidirectional((BooleanProperty) ov);
                }
                ov = getTableColumn().getCellObservableValue(getIndex());
                if (!(ov instanceof BooleanProperty)) {
                    return;
                }
                checkBox.selectedProperty().bindBidirectional((BooleanProperty) ov);
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}