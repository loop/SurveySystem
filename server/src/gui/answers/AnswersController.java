package gui.answers;

import datacontrollers.DataControllerAPI;
import helpers.Exporter;
import model.types.QuestionnaireStates;
import javafx.scene.layout.*;
import model.answers.AnswerSet;
import model.answers.QuestionAnswerTableColumn;
import model.patient.Patient;
import model.questionnaire.Questionnaire;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This controller is responsible for setting up the view for viewing answers for
 * completed export.
 */

public class AnswersController implements Initializable {

    private static final Logger log = Logger.getLogger(AnswersController.class.getName());
    @FXML
    private Parent root;
    @FXML
    private ListView<Questionnaire> questionnairePointerListView;
    @FXML
    private AnchorPane noQuestionnaireSelectedPane, questionnaireSelectedPane, answersPane;
    @FXML
    private ToolBar answerControlToolBar;
    @FXML
    private TableView<Patient> answerTable;
    @FXML
    private TableColumn<Patient, String> tableNSHColumn, tableFirstNameColumn, tableLastNameColumn;
    @FXML
    private Button backButton, exportAnswersButton;
    @FXML
    private Label answerViewNSH, answerViewQuestionnaireTitle, numberOfQuestions, numberOfSubmissions, questionnaireTitleLabel, placeholder;
    @FXML
    private TableView<QuestionAnswerTableColumn> answerViewTable;
    @FXML
    private TableColumn<QuestionAnswerTableColumn, String> requiredColumn, questionnaireTitleColumn, answerColumn;

    private final ObservableList<Patient> patientsThatHaveAnswered = FXCollections.observableArrayList();
    private final ObservableList<QuestionAnswerTableColumn> displayAnswersList = FXCollections.observableArrayList();
    private final ArrayList<Pane> secondryAnswersPane = new ArrayList<>();
    private final ObservableList<Questionnaire> addedQuestionnaires
            = FXCollections.observableArrayList();
    private final ObservableList<Questionnaire> questionnairePointersAll
            = FXCollections.observableArrayList();
    private ArrayList<AnswerSet> selectedQuestionnaireAnswerSets;
    private Questionnaire selectedQuestionnaire;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        answerControlToolBar.getItems().add(1, HBox.setHgrow(this, Priority.ALWAYS));

        secondryAnswersPane.add(noQuestionnaireSelectedPane);
        secondryAnswersPane.add(questionnaireSelectedPane);
        secondryAnswersPane.add(answersPane);

        changeToView(noQuestionnaireSelectedPane);
        setupAnswerTable();
        setupQuestionnairePointerListView();
        backButton.setOnAction(actionEvent -> changeToView(questionnaireSelectedPane));
        exportAnswersButton.setOnAction(actionEvent -> fileChooserForSavingCSV());
        getQuestionnaires();

        answerViewTable.setPlaceholder(new Label("This questionnaire has not been answered yet."));
        placeholder.setText("This is where you can view answers for completed surveys.\n You can view answers for a paticular patient and questionnaire\n or export as CSV.");
    }


    /**
     * Populates the questionnaire list.
     */
    public void setupQuestionnairePointerListView() {
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
    }

    /**
     * Sets up the answer table.
     */
    public void setupAnswerTable() {
        tableNSHColumn.setCellValueFactory(new PropertyValueFactory<>("nhsNumber"));
        tableFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        answerTable.setItems(patientsThatHaveAnswered);
    }

    /**
     * Gets the export of states with deployed and archived.
     */
    public void getQuestionnaires() {
        try {
            questionnairePointersAll.clear();
            addedQuestionnaires.clear();
            addedQuestionnaires.addAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.DEPLOYED));
            addedQuestionnaires.addAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.ARCHIVED));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Changges the view from the list to the answers pane.
     * @param pane
     */
    public void changeToView(Pane pane) {
        for (Iterator<Pane> iterator = secondryAnswersPane.iterator(); iterator.hasNext(); ) {
            Pane pane1 = iterator.next();
            pane1.setVisible((pane == pane1));
        }
    }


    /**
     * Creates the file chooser for the location of exported questionnaire.
     */
    private void fileChooserForSavingCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Answer Save Location");
        fileChooser.setInitialFileName(selectedQuestionnaire.getTitle() + "_questionnaire.csv");
        File fileChoice = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (fileChoice != null) {
            try {
                Exporter.exportQuestionnaireData(selectedQuestionnaire, fileChoice.getPath());
            } catch (Exception e) {
                log.log(Level.SEVERE, "Tried to get a questionnaire that didn't exist!");
            }
        } else {
            return;
        }
    }

}