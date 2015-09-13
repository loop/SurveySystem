package datacontrollers;

import helpers.AppFolderHelper;
import helpers.JSONHelper;
import model.answers.AnswerSet;
import model.questionnaire.Questionnaire;
import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class consists exclusively of methods that operate for creating managing JSON export
 * files on disk.
 */

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class QuestionnaireJSONFileController {

    private static final Gson json = JSONHelper.getInstance();
    private static final String questionnaireDataStoragePath = AppFolderHelper.getAppFolder() + "/questionnaires/";

    /**
     * Constructor that checks if the storage path exists or not. If it does not exist it will create it.
     */
    public QuestionnaireJSONFileController() {
        Path path = Paths.get(questionnaireDataStoragePath);
        if (!Files.notExists(path)) {
            return;
        }
        File directory = new File(path.toString());
        directory.mkdir();
    }

    /**
     * Reads the questionnaire JSON file with particular ID.
     *
     * @param questionnaireId quesitonnaire ID
     * @return Questionnaire object
     * @throws Exception
     */
    public Questionnaire getQuestionnaireById(int questionnaireId) throws Exception {
        Path path = Paths.get(questionnaireDataStoragePath + questionnaireId);
        Questionnaire questionnaire;

        if (Files.exists(path)) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(questionnaireDataStoragePath + questionnaireId + "/questionnaire.json"));
                questionnaire = json.fromJson(reader, Questionnaire.class);
            } catch (IOException error) {
                return null;
            }

            return questionnaire;
        } else {
            throw new Exception();
        }
    }

    /**
     * Checks to see if questionnaire JSON exists
     *
     * @param questionnaire questionnaire to check
     * @return true if it exists
     */
    public boolean questionnaireExists(Questionnaire questionnaire) {
        Path path = Paths.get(questionnaireDataStoragePath + questionnaire.getId());
        return !(Files.notExists(path));
    }

    /**
     * Saves questionnaire object to JSON on disk
     *
     * @param questionnaire questionnaire to save
     * @return true if it has been successfully saved
     */
    public boolean saveQuestionnaire(Questionnaire questionnaire) {
        Path path = Paths.get(questionnaireDataStoragePath + questionnaire.getId());

        if (Files.notExists(path)) {
            File directory = new File(path.toString());
            directory.mkdir();
        }

        String raw = json.toJson(questionnaire);

        try {
            FileWriter writer = new FileWriter(path.toString() + "/questionnaire.json");
            writer.write(raw);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Deletes questionnaire data from disk
     *
     * @param questionnaire questionnaire to delete
     * @return true if deleted
     * @throws Exception
     */
    public boolean deleteQuestionnaire(Questionnaire questionnaire) throws Exception {
        Path path = Paths.get(questionnaireDataStoragePath + questionnaire.getId());
        File dir = new File(path.toString());

        if (Files.exists(path)) {
            if (dir.delete() || !dir.isDirectory()) {
                return true;
            }
            File[] listFiles = dir.listFiles();
            for (File file : listFiles) {
                file.delete();
            }
            dir.delete();
        } else {
            throw new Exception();
        }

        return true;
    }

    /**
     * Saves answers in JSON in the appropriate questionnaire ID folder.
     *
     * @param answers answers to save to disk
     * @return true if successfully saved
     * @throws Exception
     */
    public boolean saveAnswers(AnswerSet answers) throws Exception {
        Path path = Paths.get(questionnaireDataStoragePath + answers.getQuestionnaireID());

        if (Files.exists(path)) {
            String raw = json.toJson(answers);

            try {
                FileWriter writer = new FileWriter(path.toString() + "/" + answers.getPatientNHS() + ".json");
                writer.write(raw);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        } else {
            throw new Exception();
        }
        return true;
    }

    /**
     * Reads answers JSON in the questionnaire folder for particular questionnaire.
     *
     * @param questionnaire questionnaire to retrieve answers for
     * @return list of AnswerSet objects
     * @throws Exception
     */
    public ArrayList<AnswerSet> getAllAnswersForQuestionnaire(Questionnaire questionnaire) throws Exception {
        String dasPath = questionnaireDataStoragePath + questionnaire.getId();
        Path path = Paths.get(dasPath);

        if (Files.exists(path)) {
            try {
                File folder = new File(dasPath);
                ArrayList<AnswerSet> answers = new ArrayList<>();
                for (File file : folder.listFiles()) {
                    if (!file.getName().startsWith("questionnaire")) {
                        BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
                        AnswerSet a = json.fromJson(reader, AnswerSet.class);
                        answers.add(a);
                    }
                }
                return answers;
            } catch (IOException error) {
                return null;
            }
        } else {
            throw new Exception();
        }
    }
}