package gui.questionnaires.creator;

import datacontrollers.DataControllerAPI;
import helpers.PopUpCreator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import model.questionnaire.Questionnaire;
import model.questions.*;
import model.types.QuestionnaireStates;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This class is responsible for setting up the view and buttons for creating export.
 * <p>
 */

public class CreatorController implements Initializable {

    private static final Logger log = Logger.getLogger(CreatorController.class.getName());

    private final String[] questionTypesFXMLViews = {
            null,
            null,
            "yesNoQuestion.fxml",
            "rangeQuestion.fxml",
            "rankQuestion.fxml",
            "freeTextQuestion.fxml",
            "multipleChoiceQuestion.fxml",
            "singleChoiceQuestion.fxml",
    };
    private Questionnaire tempQuestionnaire;
    private boolean hasQuestionnaireAlreadyBeenCreated = false;
    private QuestionTypeController questionTypeController;
    private final ObservableList<Questionnaire> questionnaireObservableList
            = FXCollections.observableArrayList();
    private final ObservableList<Questionnaire> questionnaireObservableList1
            = FXCollections.observableArrayList();
    @FXML
    private Parent root;
    @FXML
    private TextField questionnaireSearchField, questionnaireTitleField;
    @FXML
    private ListView<Questionnaire> questionnaireListView;
    @FXML
    private StackPane stackPane, stackPane1;
    @FXML
    private SplitPane questionnaireSplitPane;
    @FXML
    private Button deleteButton, saveDraftButton,
            saveNewQuestionButton = new Button("Save New"),
            cancelQuestionEditButton = new Button("Cancel"),
            saveChangesQuestionButton = new Button("Save Changes"),
            deleteExistingQuestionButton = new Button("Delete"),
            clearQuestionFieldsButton = new Button("Clear");
    @FXML
    private TreeView<Question> questionTreeView;
    @FXML
    private ToolBar questionnaireToolbar, questionToolbar;
    @FXML
    private ChoiceBox<Object> questionTypeChooser;
    @FXML
    private CheckBox requiredCheckBox;
    @FXML
    private Label infoQuestionnaire;

    /**
     * Generates random SHA1 hashes using a pseudo random generator.
     *
     * @return SHA hash
     * @throws Exception
     */
    public static String generateRandomID() throws Exception {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            String randomNum = Integer.toString((secureRandom.nextInt()));

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] digestResult = messageDigest.digest(randomNum.getBytes());

            return hexEncode(digestResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error generate random ID.");
        }
    }

    /**
     * The byte[] returned by MessageDigest is encoding for petter character
     * representation.
     * Credit: http://www.javapractices.com/topic/TopicAction.do?Id=56
     * <p>
     * This implementation follows the example of David Flanagan's book
     * "Java In A Nutshell".
     */
    static private String hexEncode(byte[] aInput) {
        StringBuilder result = new StringBuilder();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (byte b : aInput) {
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        saveNewQuestionButton.setOnAction(actionEvent -> saveNewQuestion());
        saveChangesQuestionButton.setOnAction(actionEvent -> saveQuestionChanges());
        deleteExistingQuestionButton.setOnAction(actionEvent -> deleteQuestion());
        cancelQuestionEditButton.setOnAction(actionEvent -> endEditingQuestion());
        clearQuestionFieldsButton.setOnAction(actionEvent -> {
            questionTypeController.clearInputFields();
            requiredCheckBox.setSelected(false);
        });

        TreeItem<Question> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);
        questionTreeView.setRoot(rootItem);
        questionTreeView.setShowRoot(false);
        questionTreeView.setCellFactory(new Callback<TreeView<Question>, TreeCell<Question>>() {
            @Override
            public TreeCell<Question> call(TreeView<Question> questionTreeView) {
                return new TreeCell<Question>() {
                    @Override
                    protected void updateItem(Question question, boolean aBool) {
                        super.updateItem(question, aBool);
                        if (question != null) {
                            setText("Title: " + question.getTitle() +
                                    "   Required: " + ((question.isRequired()) ? "Yes" : "No") +
                                    (StringUtils.isBlank(question.getCondition()) ? "" : "   Condition: " + question.getCondition()));
                        }
                    }
                };
            }
        });
        questionTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Question>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Question>> observableValue, TreeItem<Question> old_item, TreeItem<Question> newItem) {
                existingQuestionSelected(newItem);
            }
        });

        setupQuestionnairesList();
        setupQuestionTypeChooser();
        finishUpEditingView();

        try {
            questionnaireObservableList1.clear();
            questionnaireObservableList.clear();
            questionnaireObservableList.addAll(DataControllerAPI.getQuestionnairePointersForState(QuestionnaireStates.DRAFT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        infoQuestionnaire.setText("This is where you can modify or add new questionnaires to the system.\n Add a new questionnaire by clicking the '+' button on the right or select an existing questionnaire.");
        infoQuestionnaire.setVisible(true);
    }

    /**
     * Sets up a list of export in the listview.
     */
    public void setupQuestionnairesList() {
        questionnaireListView.setCellFactory(new Callback<ListView<Questionnaire>, ListCell<Questionnaire>>() {
            @Override
            public ListCell<Questionnaire> call(ListView<Questionnaire> p) {
                return new ListCell<Questionnaire>() {
                    @Override
                    protected void updateItem(Questionnaire pointer, boolean aBool) {
                        super.updateItem(pointer, aBool);
                        if (pointer != null) {
                            setText(pointer.getTitle());
                        }
                    }
                };
            }
        });
        questionnaireListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Questionnaire>() {
                    @Override
                    public void changed(ObservableValue<? extends Questionnaire> observableValue, Questionnaire old_pointer, Questionnaire new_pointer) {
                        if (new_pointer == null) {
                            finishUpEditingView();
                        } else {
                            setupViewForEditingExistingQuestionnaire();
                        }
                    }
                }
        );

        questionnaireListView.setItems(questionnaireObservableList);
    }

    /**
     * Sets up a dropdown for the types of questions.
     */
    public void setupQuestionTypeChooser() {
        questionTypeChooser.setItems(FXCollections.observableArrayList("Select a Question Type to create",
                new Separator(),
                "Yes or No Choice",
                "Range",
                "Rank",
                "Free Text",
                "Multiple Choice",
                "Single Choice"));
        questionTypeChooser.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNumber, Number newNumber) {
                questionTreeView.getSelectionModel().clearSelection();
                if (newNumber.intValue() <= 1) {
                    setQuestionControlsVisible(false);
                } else {
                    setupViewForAddingQuestion();
                    setQuestionTypeView(newNumber.intValue());
                }
            }
        });
    }

    /**
     * Clears the editing pane of the selected questionnaire.
     */
    public void questionnaireListViewSelectNone() {
        questionnaireListView.getSelectionModel().clearSelection();
        questionnaireSearchField.requestFocus();
        finishUpEditingView();
    }

    /**
     * Saves questionnaire and clears up the workspace.
     */
    public void saveDraftQuestionnaire() {
        saveQuestionnaire();
        finishUpEditingView();
    }

    /**
     * Saves questionnaire into the database.
     */
    public void saveQuestionnaire() {
        try {
            if (!hasQuestionnaireAlreadyBeenCreated) {
                tempQuestionnaire = DataControllerAPI.addQuestionnaire(tempQuestionnaire);
                if (tempQuestionnaire != null) {
                    hasQuestionnaireAlreadyBeenCreated = true;
                }
            } else {
                DataControllerAPI.updateQuestionnaire(tempQuestionnaire);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes questionnaire from database and updates the list.
     */
    public void deleteQuestionnaire() {
        try {
            DataControllerAPI.removeQuestionnaire(tempQuestionnaire);
            finishUpEditingView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the question and adds it to the questionnaire.
     */
    public void saveNewQuestion() {
        Question question = getQuestions();

        try {
            if (question == null || question.getTitle().equals("2bda2998d9b0ee197da142a0447f6725")) {
                if (question.getTitle().equals("2bda2998d9b0ee197da142a0447f6725")) {

                } else {
                    PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "New Question", "Save New Question", "Please ensure all fields have been filled before saving.");
                }
            } else {
                tempQuestionnaire.addQuestion(question);
                endEditingQuestion();
                populateQuestionList();
            }
        } catch (NullPointerException ignored) {

        }
    }

    /**
     * Saves question changes.
     */
    public void saveQuestionChanges() {
        Optional result = null;
        Question question = questionTreeView.getSelectionModel().getSelectedItem().getValue();
        Question editedQuestion = getQuestions();

        if (editedQuestion == null && question.getTitle().equals("2bda2998d9b0ee197da142a0447f6725")) {
            PopUpCreator.getAlert(Alert.AlertType.INFORMATION, "New Question", "Save New Question", "Please ensure all fields have been filled before saving.");
        } else {
            if (question.getClass() != SingleChoiceQuestion.class) {
            } else {
            }
            try {
                question.updateQuestion(editedQuestion);
                endEditingQuestion();
                populateQuestionList();
            } catch (NullPointerException ignored) {

            }
            if (result == null) {
                return;
            }
            if (result.get() == ButtonType.OK) {
                question.updateQuestion(editedQuestion);
                if (question.getClass() == SingleChoiceQuestion.class) {
                    SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
                }
            }
        }
    }

    /**
     * Deletes questions from the tree and the questionnaire.
     */
    public void deleteQuestion() {
        TreeItem<Question> treeItem = questionTreeView.getSelectionModel().getSelectedItem();
        Alert deleteQuestionAlert;
        Optional<ButtonType> result;
        deleteQuestionAlert = PopUpCreator.getResponse(Alert.AlertType.CONFIRMATION, "Delete Question", "Are you sure you want to delete this question?", "Deleting question will remove it from the questionnaire.");
        result = deleteQuestionAlert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }

        populateQuestionList();
        endEditingQuestion();
    }

    /**
     * Gets the constructed question object.
     *
     * @return the question object
     */
    public Question getQuestions() {
        try {
            return questionTypeController.getQuestion(generateRandomID(), requiredCheckBox.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates the editing pane with the question selected.
     *
     * @param question
     */
    public void existingQuestionSelected(TreeItem<Question> question) {
        infoQuestionnaire.setVisible(false);
        if (question == null) {
            setQuestionControlsVisible(false);
        } else {
            questionTypeChooser.getSelectionModel().select(0);
            setQuestionTypeViewForQuestion(question.getValue());
            setupViewForEditingQuestion(question);
            questionTypeController.overwriteExsitingQuestion(question.getValue());
        }
    }

    /**
     * Populates the question list with the questionnaire questions.
     */
    public void populateQuestionList() {
        questionTreeView.getSelectionModel().clearSelection();
        questionTreeView.getRoot().getChildren().clear();
        for (Iterator<Question> iterator = tempQuestionnaire.getQuestions().iterator(); iterator.hasNext(); ) {
            Question question = iterator.next();
            question.setCondition("");
            TreeItem<Question> leaf = new TreeItem<>(question);
            questionTreeView.getRoot().getChildren().add(leaf);
            leaf.setExpanded(true);
        }
    }

    /**
     * Sets up the view for creating a new questionnaire.
     */
    public void setupViewForBuildingNewQuestionnaire() {
        infoQuestionnaire.setVisible(false);
        questionnaireListView.getSelectionModel().clearSelection();
        tempQuestionnaire = new Questionnaire("", 0);
        hasQuestionnaireAlreadyBeenCreated = false;
        prepareForEditingQuestionnaire();
        saveDraftButton.setDisable(!(tempQuestionnaire.getQuestions().size() > 0));
    }

    /**
     * Sets up the view for editing a existing questionnaire.
     */
    public void setupViewForEditingExistingQuestionnaire() {
        infoQuestionnaire.setVisible(false);
        try {
            Questionnaire pointer = questionnaireListView.getSelectionModel().getSelectedItem();
            tempQuestionnaire = DataControllerAPI.getQuestionnaireWithPointer(pointer);
            hasQuestionnaireAlreadyBeenCreated = true;
            prepareForEditingQuestionnaire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the buttons the editing the questionnaire.
     */
    public void prepareForEditingQuestionnaire() {
        infoQuestionnaire.setVisible(false);
        setQuestionnairePaneVisible(true);
        questionnaireTitleField.setText(tempQuestionnaire.getTitle());
        saveDraftButton.setDisable(!hasQuestionnaireAlreadyBeenCreated);
        if (!hasQuestionnaireAlreadyBeenCreated) {
            saveDraftButton.setText("Save Draft");
            deleteButton.setText("Cancel");
        } else {
            saveDraftButton.setText("Save Changes");
            deleteButton.setText("Delete");
        }
        if (tempQuestionnaire.getQuestions().size() <= 0) {
        } else {
            populateQuestionList();
        }
        questionTypeChooser.getSelectionModel().select(0);
    }

    /**
     * Cleans up the view after questionnaire editing has finished.
     */
    public void finishUpEditingView() {
        infoQuestionnaire.setVisible(true);
        setQuestionnairePaneVisible(false);
        questionnaireTitleField.clear();
        questionTreeView.getRoot().getChildren().clear();
        questionTypeChooser.getSelectionModel().select(0);
        tempQuestionnaire = null;
        questionnaireListView.getSelectionModel().clearSelection();
    }

    /**
     * Hides or shows the questionnaire editing pane.
     *
     * @param visible
     */
    public void setQuestionnairePaneVisible(boolean visible) {
        stackPane.getChildren().clear();
        if (visible) {
            stackPane.getChildren().add(questionnaireSplitPane);
        }
    }

    /**
     * Sets up the view for adding a new question.
     */
    public void setupViewForAddingQuestion() {
        setQuestionControlsVisible(true);
        questionToolbar.getItems().setAll(saveNewQuestionButton, cancelQuestionEditButton, new FlexibleToolbarSpace(), clearQuestionFieldsButton);
        requiredCheckBox.setDisable(false);
    }

    /**
     * Sets up the view for editng a new question.
     *
     * @param item
     */
    public void setupViewForEditingQuestion(TreeItem<Question> item) {
        setQuestionControlsVisible(true);
        questionToolbar.getItems().setAll(saveChangesQuestionButton, deleteExistingQuestionButton, cancelQuestionEditButton, new FlexibleToolbarSpace(), clearQuestionFieldsButton);
        requiredCheckBox.setDisable(!item.getParent().equals(questionTreeView.getRoot()));
        requiredCheckBox.setSelected(item.getValue().isRequired());
    }

    /**
     * Cleans up the view after question adding as finished.
     */
    public void endEditingQuestion() {
        questionTypeChooser.getSelectionModel().select(0);
        questionTreeView.getSelectionModel().clearSelection();
        requiredCheckBox.setSelected(false);
        stackPane1.getChildren().clear();
        questionToolbar.getItems().clear();
        questionTypeController = null;
        saveDraftButton.setDisable(!(tempQuestionnaire.getQuestions().size() > 0));
        setQuestionControlsVisible(false);
        if (questionnaireTitleField.getText().length() > 0) {
            saveQuestionnaire();
        }
    }

    /**
     * Hide or show the question options controls.
     *
     * @param visible
     */
    public void setQuestionControlsVisible(boolean visible) {
        requiredCheckBox.setVisible(visible);
        stackPane1.setVisible(visible);
        questionToolbar.setVisible(visible);
    }

    /**
     * Loads the appropriate question type view.
     *
     * @param viewIndex
     */
    public void setQuestionTypeView(int viewIndex) {
        stackPane1.getChildren().clear();
        String viewPath = questionTypesFXMLViews[viewIndex];
        if (viewPath != null && viewPath.length() > 0) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(viewPath));
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Pane pane = fxmlLoader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setBottomAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                stackPane1.getChildren().add(0, pane);
                questionTypeController = fxmlLoader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the appropriate question FXML
     *
     * @param question
     */
    public void setQuestionTypeViewForQuestion(Question question) {

        if (question.getClass() == FreeTextQuestion.class) {
            setQuestionTypeView(2);
        }
        if (question.getClass() == MultipleChoiceQuestion.class) {
            setQuestionTypeView(3);
        }
        if (question.getClass() == SingleChoiceQuestion.class) {
            setQuestionTypeView(4);
        }
        if (question.getClass() == YesNoQuestion.class) {
            setQuestionTypeView(5);
        }
        if (question.getClass() == RangeQuestion.class) {
            setQuestionTypeView(6);
        }
        if (question.getClass() == RankQuestion.class) {
            setQuestionTypeView(7);
        }
    }

    class FlexibleToolbarSpace extends Region {
        public FlexibleToolbarSpace() {
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    }
}