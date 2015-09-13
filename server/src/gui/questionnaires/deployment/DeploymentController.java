package gui.questionnaires.deployment;

import datacontrollers.DataControllerAPI;
import model.types.QuestionnaireStates;
import model.questionnaire.Questionnaire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * This class is responsible for setting up the view and controlling the list for
 * changing the states of export.
 */

public class DeploymentController implements Initializable {

    @FXML
    private ListView<Questionnaire> deployedListView;
    @FXML
    private ListView<Questionnaire> archivedListView;
    @FXML
    private ListView<Questionnaire> draftListView;

    private final ObservableList<Questionnaire> deployedQuestionnaires
            = FXCollections.observableArrayList();
    private final ObservableList<Questionnaire> archivedQuestionnaires
            = FXCollections.observableArrayList();
    private final ObservableList<Questionnaire> draftQuestionnaires
            = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupListViews();
    }

    /**
     * Sets up the tables with the questionnaire names.
     */
    public void setupListViews() {
        Callback<ListView<Questionnaire>, ListCell<Questionnaire>> callback = new Callback<ListView<Questionnaire>, ListCell<Questionnaire>>() {
            @Override
            public ListCell<Questionnaire> call(ListView<Questionnaire> p) {
                return new ListCell<Questionnaire>() {
                    @Override
                    protected void updateItem(Questionnaire pointer, boolean aBool) {
                        super.updateItem(pointer, aBool);
                        if (pointer == null) {
                            return;
                        }
                        setText(" " + pointer.getTitle());
                    }
                };
            }
        };

        deployedListView.setCellFactory(callback);
        archivedListView.setCellFactory(callback);
        draftListView.setCellFactory(callback);

        deployedListView.setItems(deployedQuestionnaires);
        archivedListView.setItems(archivedQuestionnaires);
        draftListView.setItems(draftQuestionnaires);

        getDeployedStateQuestionnaires();
        getArchivedStateQuestionnaires();
        getDraftStateQuestionnaires();
    }

    /**
     * Gets the deployed state export.
     */
    public void getDeployedStateQuestionnaires() {
        try {
            deployedQuestionnaires.clear();
            deployedQuestionnaires.setAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.DEPLOYED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the archived state export.
     */
    public void getArchivedStateQuestionnaires() {
        try {
            archivedQuestionnaires.clear();
            archivedQuestionnaires.setAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.ARCHIVED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the draft state export.
     */
    public void getDraftStateQuestionnaires() {
        try {
            draftQuestionnaires.clear();
            draftQuestionnaires.setAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.DRAFT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changes questionnaire states to deployed.
     */
    public void redeployStateQuestionnairesAction() {
        ObservableList<Questionnaire> selectedItems = archivedListView.getSelectionModel().getSelectedItems();
        for (Iterator<Questionnaire> iterator = selectedItems.iterator(); iterator.hasNext(); ) {
            Questionnaire pointer = iterator.next();
            try {
                DataControllerAPI.setQuestionnairePointerStateToDeployed(pointer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Changes questionnaire states to archive.
     */
    public void archiveStateQuestionnairesAction() {
        ObservableList<Questionnaire> questionnaires = deployedListView.getSelectionModel().getSelectedItems();
        for (Iterator<Questionnaire> iterator = questionnaires.iterator(); iterator.hasNext(); ) {
            Questionnaire pointer = iterator.next();
            try {
                DataControllerAPI.setQuestionnairePointerStateToArchived(pointer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Changes questionnaire states to draft.
     */
    public void draftStateQuestionnairesAction() {
        ObservableList<Questionnaire> questionnaires = deployedListView.getSelectionModel().getSelectedItems();
        for (Iterator<Questionnaire> iterator = questionnaires.iterator(); iterator.hasNext(); ) {
            Questionnaire pointer = iterator.next();
            try {
                DataControllerAPI.setQuestionnairePointerStateToDraft(pointer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes questionnaire states to deployed.
     */
    public void deployQuestionnairesAction() {
        ObservableList<Questionnaire> questionnaires = draftListView.getSelectionModel().getSelectedItems();
        for (Iterator<Questionnaire> iterator = questionnaires.iterator(); iterator.hasNext(); ) {
            Questionnaire pointer = iterator.next();
            try {
                Questionnaire questionnaire = DataControllerAPI.getQuestionnaireWithPointer(pointer);
                if (questionnaire.getQuestions().size() <= 0) {
                } else {
                    try {
                        DataControllerAPI.setQuestionnairePointerStateToDeployed(pointer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}