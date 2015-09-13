package gui.questionnaires.export;

import datacontrollers.DataControllerAPI;
import helpers.Exporter;
import model.questionnaire.Questionnaire;
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
 * This class is responsible for setting up the view and buttons to export export into CSV.
 */

public class ExportQuestionnairesController implements Initializable {

    private static final Logger log = Logger.getLogger(ExportQuestionnairesController.class.getName());

    private final ObservableList<Questionnaire> questionnaires = FXCollections.observableArrayList();
    @FXML
    TableView<Questionnaire> questionnaireTable;
    @FXML
    private Parent root;
    @FXML
    private TableColumn<Questionnaire, String> tableQuestionnaireNameColumn;
    @FXML
    private TableColumn<Questionnaire, String> tableNumberOfQuestionsColumn;
    @FXML
    private TableColumn<Questionnaire, String> tableStateColumn;
    @FXML
    private Button exportButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupQuestionnaireTable();
        setupButtons();
    }

    /**
     * Sets up the table for the list of export.
     */
    public void setupQuestionnaireTable() {
        tableQuestionnaireNameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableNumberOfQuestionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        try {
            questionnaires.setAll(DataControllerAPI.getQuestionnairePointers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        questionnaireTable.setItems(questionnaires);
    }

    /**
     * Sets up the buttons to export the questionnaire and the filechooser for the location of the save location.
     */
    private void setupButtons() {
        this.exportButton.setOnAction(actionEvent -> {
            try {
                Questionnaire questionnairePointer = questionnaireTable.getSelectionModel().getSelectedItem();
                if (questionnairePointer != null) {
                    Questionnaire questionnaire = DataControllerAPI.getQuestionnaireWithPointer(questionnairePointer);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save Location");
                    fileChooser.setInitialFileName(questionnairePointer.getTitle() + "_questionnaire_only" + ".csv");
                    File fileChoice = fileChooser.showSaveDialog(root.getScene().getWindow());
                    if (fileChoice != null) {
                        String path = fileChoice.getPath();
                        try {
                            Exporter.exportQuestionnaire(questionnaire, path);
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "Something went wrong! Could not export.");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
