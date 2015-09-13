package datacontrollers;

import model.answers.AnswerSet;
import model.logs.UserLog;
import model.patient.Patient;
import model.patient.TablePatient;
import model.questionnaire.Questionnaire;
import model.types.QuestionnaireStates;
import model.user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class contains the API methods to interact with the database for the whole system.
 * The system should only interface with the database via this class, all the methods from
 * DatabaseFileController are called here.
 */

public class DataControllerAPI {

    private static final QuestionnaireJSONFileController questionnaireJSONFileController = new QuestionnaireJSONFileController();
    private static final DatabaseFileController databaseFileController = new DatabaseFileController();

    public static synchronized boolean addPatient(Patient patient) throws SQLException {
        try {
            databaseFileController.insertPatient(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public static synchronized Patient updatePatient(Patient patient) throws SQLException {
        try {
            databaseFileController.updatePatientRecord(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return patient;
    }

    public static synchronized boolean removePatient(Patient patient) throws SQLException {
        try {
            databaseFileController.removePatient(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public static synchronized Patient getPatientByNSHNUmber(String nhs) throws SQLException {
        Patient patient;
        try {
            patient = databaseFileController.getPatientByNHSNumber(nhs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return patient;
    }


    public static synchronized ArrayList<Patient> getAllPatients() throws SQLException {
        ArrayList<Patient> patients;
        try {
            patients = databaseFileController.getAllPatients();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return patients;
    }

    public static synchronized ArrayList<TablePatient> getAllTablePatients() throws SQLException {
        ArrayList<TablePatient> patients;
        try {
            patients = databaseFileController.getAllTablePatients();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return patients;
    }

    public static synchronized ArrayList<Questionnaire> getQuestionnairePointers() throws SQLException {
        return databaseFileController.getAllQuestionnairesPointersList();
    }

    public static synchronized Questionnaire getQuestionnairePointerByID(int ID) throws SQLException {
        try {
            return databaseFileController.getQuestionnaireByID(ID);
        } catch (@SuppressWarnings("CaughtExceptionImmediatelyRethrown") SQLException e) {
            throw e;
        }
    }

    public static synchronized ArrayList<Questionnaire> getQuestionnairePointersForState(String state) throws Exception {
        try {
            return databaseFileController.getAllQuestionnairesForState(state);
        } catch (@SuppressWarnings("CaughtExceptionImmediatelyRethrown") SQLException e) {
            throw e;
        }
    }

    public static synchronized Questionnaire getQuestionnaireByID(int id) throws Exception {
        try {
            return questionnaireJSONFileController.getQuestionnaireById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized Questionnaire getQuestionnaireWithPointer(Questionnaire pointer) throws Exception {
        try {
            Questionnaire questionnaire = questionnaireJSONFileController.getQuestionnaireById(pointer.getId());
            questionnaire.setState(pointer.getState());
            return questionnaire;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized Questionnaire updateQuestionnaire(Questionnaire questionnaire) throws Exception {
        if (questionnaireJSONFileController.questionnaireExists(questionnaire)) {
            questionnaireJSONFileController.saveQuestionnaire(questionnaire);
            databaseFileController.updateQuestionnaire(questionnaire);
        } else {
            throw new Exception();
        }
        return questionnaire;
    }

    public static synchronized Questionnaire addQuestionnaire(Questionnaire questionnaire) throws SQLException {
        Questionnaire savedQuestionnaire = databaseFileController.insertQuestionnaire(questionnaire);
        if (questionnaireJSONFileController.saveQuestionnaire(savedQuestionnaire)) {
            return savedQuestionnaire;
        } else {
            databaseFileController.removeQuestionnaire(savedQuestionnaire);
            return null;
        }
    }

    public static synchronized boolean removeQuestionnaire(Questionnaire questionnaire) throws Exception {
        try {
            databaseFileController.removeQuestionnaire(questionnaire);
            questionnaireJSONFileController.deleteQuestionnaire(questionnaire);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }

    public static synchronized Questionnaire setQuestionnairePointerStateToDeployed(Questionnaire pointer) throws Exception {
        try {
            if (!databaseFileController.setQuestionnairePointerState(pointer, QuestionnaireStates.DEPLOYED)) {
            } else {
                Questionnaire questionnaire = questionnaireJSONFileController.getQuestionnaireById(pointer.getId());
                questionnaire.setState(QuestionnaireStates.DEPLOYED);
                questionnaireJSONFileController.saveQuestionnaire(questionnaire);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        pointer.setState(QuestionnaireStates.DEPLOYED);
        return pointer;
    }

    public static synchronized Questionnaire setQuestionnairePointerStateToDraft(Questionnaire pointer) throws Exception {
        try {
            if (!databaseFileController.setQuestionnairePointerState(pointer, QuestionnaireStates.DRAFT)) {
            } else {
                Questionnaire questionnaire = questionnaireJSONFileController.getQuestionnaireById(pointer.getId());
                questionnaire.setState(QuestionnaireStates.DRAFT);
                questionnaireJSONFileController.saveQuestionnaire(questionnaire);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        pointer.setState(QuestionnaireStates.DRAFT);
        return pointer;
    }

    public static synchronized Questionnaire setQuestionnairePointerStateToArchived(Questionnaire pointer) throws Exception {
        try {
            if (!databaseFileController.setQuestionnairePointerState(pointer, QuestionnaireStates.ARCHIVED)) {
            } else {
                Questionnaire questionnaire = questionnaireJSONFileController.getQuestionnaireById(pointer.getId());
                questionnaire.setState(QuestionnaireStates.ARCHIVED);
                questionnaireJSONFileController.saveQuestionnaire(questionnaire);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        pointer.setState(QuestionnaireStates.ARCHIVED);
        return pointer;
    }

    public static synchronized boolean saveAnswer(AnswerSet answers) throws Exception {
        Questionnaire questionnairePointer = getQuestionnairePointerByID(answers.getQuestionnaireID());
        Questionnaire questionnaire = getQuestionnaireByID(answers.getQuestionnaireID());
        Patient patient = getPatientByNSHNUmber(answers.getPatientNHS());
        if (!answers.isAnswerComplete(questionnaire)) {
            throw new Exception();
        } else {
            if (!databaseFileController.setPatientQuestionnaireAsCompleted(questionnairePointer, patient)) {
                return false;
            } else {
                if (!questionnaireJSONFileController.saveAnswers(answers)) {
                    databaseFileController.setPatientQuestionnaireAsNotCompleted(questionnairePointer, patient);
                    return false;
                } else {
                    return true;
                }
            }
        }
    }

    public static synchronized ArrayList<AnswerSet> getAnswerSetsForQuestionnaire(Questionnaire questionnaire) throws Exception {
        return questionnaireJSONFileController.getAllAnswersForQuestionnaire(questionnaire);
    }

    public static synchronized ArrayList<Questionnaire> getQuestionnairePointersForPatient(Patient patient) throws SQLException {
        try {
            return databaseFileController.getQuestionnairePointersForPatient(patient);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized boolean assignQuestionnaire(Patient patient, Questionnaire questionnaire) throws SQLException {
        return databaseFileController.linkPatientAndQuestionnairePointer(patient, questionnaire);
    }

    public static synchronized boolean unassignQuestionnaire(Patient patient, Questionnaire questionnaire) throws SQLException {
        return databaseFileController.unlinkPatientAndQuestionnairePointer(patient, questionnaire);
    }

    public static synchronized HashMap<String, Boolean> areTablePatientsAssignedToQuestionnaire(ArrayList<TablePatient> patients, Questionnaire questionnaire) throws SQLException {
        HashMap<String, Boolean> patientIsAssigned = new HashMap<>();
        for (Iterator<TablePatient> iterator = patients.iterator(); iterator.hasNext(); ) {
            TablePatient patient = iterator.next();
            Boolean isAssigned = databaseFileController.isPatientAssignedToQuestionnaire(patient, questionnaire);
            patientIsAssigned.put(patient.getNhsNumber(), isAssigned);
        }
        return patientIsAssigned;
    }

    public static synchronized boolean checkLogin(User user) throws SQLException {
        try {
            if (databaseFileController.checkUsernamePassword(user)) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

    public static synchronized boolean checkPasswordForUsername(User user) throws SQLException {
        try {
            return databaseFileController.checkPasswordForUsername(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized boolean addAdminUser(User user) throws SQLException {
        try {
            return databaseFileController.insertAdmin(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized boolean updateAdminPassword(User user) throws SQLException {
        try {
            return databaseFileController.updateAdminPassword(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static synchronized ArrayList<User> getAllAdmins() throws SQLException {
        ArrayList<User> users;
        try {
            users = databaseFileController.getAllAdmins();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return users;
    }

    public static synchronized boolean removeAdmin(User user) throws SQLException {
        try {
            databaseFileController.removeAdmin(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return true;
    }


    public static synchronized ArrayList<UserLog> getAllAdminLogs() throws SQLException {
        return databaseFileController.getAllAdminLogs();
    }

    public static synchronized boolean actionAdminLog(UserLog userLog) throws SQLException {
        try {
            return databaseFileController.actionAdminLog(userLog);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static String getUserType(String admin) throws SQLException {
        return databaseFileController.getUserType(admin);
    }

}