package helpers;

import datacontrollers.DataControllerAPI;
import gui.main.MainController;
import model.types.UserTypes;
import model.answers.AnswerSet;
import model.answers.Answer;
import model.patient.Patient;
import model.questionnaire.Questionnaire;
import model.questions.Question;
import au.com.bytecode.opencsv.CSV;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class is to help with the creation of CSV files when exporting from database. It is supplied with
 * the data and CSV file is built. CSV Writer from https://code.google.com/p/opencsv/.
 *
 * Example provided by OpenCSV
 */
public class Exporter {

    final static ArrayList<String[]> STRING_ARRAY_LIST = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Exporter.class.getName());

    /**
     * Exports export answers into CSV
     *
     * @param inputQuestionnaire questionnaire to export
     * @param inputPath CSV save path
     * @return true if CSV saved
     * @throws Exception
     */
    public static boolean exportQuestionnaireData(Questionnaire inputQuestionnaire, String inputPath) throws Exception {
        final ArrayList<AnswerSet> answerSets;
        final Questionnaire questionnaire = inputQuestionnaire;

        try {
            if (DataControllerAPI.getQuestionnaireByID(questionnaire.getId()) != null) {
                answerSets = DataControllerAPI.getAnswerSetsForQuestionnaire(questionnaire);
            } else {
                return false;
            }

            CSV csv = CSV.separator(',').quote('"').create();

            csv.write(inputPath, out -> {
                STRING_ARRAY_LIST.add(getQuestionnaireQuestionIDs(questionnaire));
                STRING_ARRAY_LIST.add(getQuestionnaireQuestionTitles(questionnaire));
                STRING_ARRAY_LIST.addAll(answerSets.stream().map(Exporter::getAnswersFromSet).collect(Collectors.toList()));
                STRING_ARRAY_LIST.remove(0);

                out.writeAll(STRING_ARRAY_LIST);

                STRING_ARRAY_LIST.clear();
            });
            return true;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Something went wrong, could not export data!");
            return false;
        }

    }

    /**
     * Reads the answerset into string array
     *
     * @param answerSet
     * @return AnswerSet in string array
     */
    private static String[] getAnswersFromSet(AnswerSet answerSet) {
        switch (MainController.userType) {
            case UserTypes.LIMITED: {
                String[] IDs = STRING_ARRAY_LIST.get(0);
                String[] results = new String[IDs.length];

                int i = 0;
                while (i < IDs.length) {
                    results[i] = getAnswerFor(IDs[i], answerSet);
                    i++;
                }
                return results;
            }
            default: {
                String[] IDs = STRING_ARRAY_LIST.get(0);
                int size = IDs.length + 1;
                String[] results = new String[size];

                int i = 0;
                while (i < IDs.length) {
                    results[i] = getAnswerFor(IDs[i], answerSet);
                    i++;
                }
                results[size - 1] = answerSet.getPatientNHS();
                return results;
            }
        }
    }

    /**
     * Gets answers for a patient.
     *
     * @param ID
     * @param answerSet
     * @return
     */
    private static String getAnswerFor(String ID, AnswerSet answerSet) {
        String nhsNumber = answerSet.getPatientNHS();
        for (Answer a : answerSet.getAnswers()) {
            if (!a.getID().equals(ID)) {
            } else {
                return joinAnswers(a);
            }
        }
        return "(No Answer)";
    }

    /**
     * Gets the questionnaire titles only
     *
     * @param questionnaire
     * @return array of questionnaire titles
     */
    private static String[] getQuestionnaireQuestionTitles(Questionnaire questionnaire) {
        if (MainController.userType.equals(UserTypes.LIMITED)) {
            String[] IDs = STRING_ARRAY_LIST.get(0);
            String[] titles = new String[IDs.length];

            int i = 0;
            while (i < IDs.length) {
                titles[i] = findTitleFor(IDs[i], questionnaire);
                i++;
            }
            return titles;
        } else {
            String[] IDs = STRING_ARRAY_LIST.get(0);
            int size = IDs.length + 1;
            String[] titles = new String[size];

            int i = 0;
            while (i < IDs.length) {
                titles[i] = findTitleFor(IDs[i], questionnaire);
                i++;
            }
            titles[size - 1] = "NHS Number";
            return titles;
        }
    }

    /**
     * Gets title for particular questionnaire ID.
     *
     * @param ID
     * @param questionnaire
     * @return quesiton title
     */
    private static String findTitleFor(String ID, Questionnaire questionnaire) {
        for (Iterator<Question> iterator = questionnaire.getQuestions().iterator(); iterator.hasNext(); ) {
            Question q = iterator.next();
            String title = checkAndQuestionAndDependentsForID(q, ID);
            if (title != null) {
                return title;
            }
        }
        return "Not found";
    }

    /**
     * Checks and returns depedendent questions for questionnaire
     *
     * @param question
     * @param ID
     * @return dependent question titles
     */
    private static String checkAndQuestionAndDependentsForID(Question question, String ID) {
        return question.getID().equals(ID) ? question.getTitle() : null;
    }

    /**
     * Gets question IDs for a questionnaire.
     *
     * @param questionnaire
     * @return list of question IDs.
     */
    private static String[] getQuestionnaireQuestionIDs(Questionnaire questionnaire) {
        ArrayList<String> tmpIDs = new ArrayList<>();


        String[] ids = new String[tmpIDs.size()];
        int i = 0;
        while (i < ids.length) {
            ids[i] = tmpIDs.get(i);
            i++;
        }
        return ids;
    }

    /**
     * Concatenates answers for multiple answers.
     *
     * @param answer
     * @return concatenated answer
     */
    private static String joinAnswers(Answer answer) {
        String answerString = "";
        for (Iterator<String> iterator = answer.getAnswers().iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (answerString.length() != 0) {
                answerString = answerString + " | ";
            }
            answerString = answerString + s;
        }
        return answerString;
    }

    /**
     * Exports the questionnaire data
     *
     * @param inputQuestionnaire questionnaire to be exported
     * @param inputPath path to save the CSV
     * @return true if CSV has been saved
     * @throws Exception
     */
    public static boolean exportQuestionnaire(Questionnaire inputQuestionnaire, String inputPath) throws Exception {
        final Questionnaire questionnaire = inputQuestionnaire;

        {
            CSV csv = CSV.separator(',').quote('"').create();

            csv.write(inputPath, out -> {
                STRING_ARRAY_LIST.add(getQuestionnaireQuestionIDs(questionnaire));
                STRING_ARRAY_LIST.add(getQuestionnaireQuestionTitles(questionnaire));
                STRING_ARRAY_LIST.add(getQuestionnaireQuestionDescriptions(questionnaire));

                STRING_ARRAY_LIST.remove(0);

                out.writeAll(STRING_ARRAY_LIST);

                STRING_ARRAY_LIST.clear();
            });
            return true;
        }
    }

    /**
     * Exports the patient data
     *
     * @param inputPatient patient to be exported
     * @param inputPath path to save the CSV
     * @return true if CSV has been saved
     * @throws Exception
     */
    public static boolean exportPatient(Patient inputPatient, String inputPath) throws Exception {
        final Patient patient = inputPatient;

        {
            CSV csv = CSV.separator(',').quote('"').create();

            csv.write(inputPath, out -> {
                STRING_ARRAY_LIST.add(getPatientDetails(patient));


                out.writeAll(STRING_ARRAY_LIST);

                STRING_ARRAY_LIST.clear();
            });
            return true;
        }
    }

    /**
     * Breaks up the patient object into a string array.
     *
     * @param patient
     * @return array of the patient details
     */
    private static String[] getPatientDetails(Patient patient) {

        String[] ids = new String[6];
        ids[0] = patient.getNhsNumber();
        ids[1] = patient.getFirstName();
        ids[2] = patient.getMiddleName();
        ids[3] = patient.getSurname();
        ids[4] = patient.getDateOfBirth();
        ids[5] = patient.getPostcode();
        return ids;
    }

    /**
     * Gets the questionnaire questions descriptions.
     *
     * @param questionnaire
     * @return array of descriptions
     */
    private static String[] getQuestionnaireQuestionDescriptions(Questionnaire questionnaire) {
        String[] IDs = STRING_ARRAY_LIST.get(0);
        String[] descriptions = new String[IDs.length];

        int i = 0;
        while (i < IDs.length) {
            descriptions[i] = getQuestionDescription(questionnaire);
            i++;
        }

        return descriptions;
    }

    /**
     * Gets the questionnaire description.
     *
     * @param questionnaire
     * @return question description
     */
    private static String getQuestionDescription(Questionnaire questionnaire) {
        for (Iterator<Question> iterator = questionnaire.getQuestions().iterator(); iterator.hasNext(); ) {
            Question q = iterator.next();
            String description = q.getDescription();
            if (description != null) {
                return description;
            }
        }
        return "Not found";
    }
}