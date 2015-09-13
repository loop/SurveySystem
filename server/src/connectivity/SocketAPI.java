package connectivity;

import datacontrollers.DataControllerAPI;
import com.google.gson.Gson;
import helpers.JSONHelper;
import model.answers.AnswerSet;
import model.patient.Patient;
import model.questionnaire.Questionnaire;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class mains the API methods that communicate with the client.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class SocketAPI {

    private static final Logger log = Logger.getLogger(SocketAPI.class.getName());


    public static String getResponseFor(String encoded) {
        String incomingCommand = TrafficEncryptor.decrypt(encoded);
        System.out.println(TrafficEncryptor.decrypt(encoded));
        if (incomingCommand.matches("(FindPatient:).*")) {
            log.log(Level.INFO, "Finding patient");

            Patient patient;
            try {
                patient = DataControllerAPI.getPatientByNSHNUmber(incomingCommand.split(": ")[1]);
                if (patient == null) {
                    return TrafficEncryptor.encryptAndFormat("error");
                } else {
                    Gson json = JSONHelper.getInstance();
                    return TrafficEncryptor.encryptAndFormat(json.toJson(patient));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return TrafficEncryptor.encryptAndFormat("error");
            }
        } else {
            if (!incomingCommand.matches("(GetAllQuestionnairesForPatient:).*")) {
                if (!incomingCommand.matches("(GetQuestionnaireByID:).*")) {
                    if (!incomingCommand.equals("CheckConnection")) {
                        if (!incomingCommand.matches("(SendAnswers: ).*")) {
                            switch (incomingCommand) {
                                case "Close":
                                    log.log(Level.INFO, "Closing connection");

                                    return "Close";
                                default:
                                    return TrafficEncryptor.encryptAndFormat("error");
                            }
                        } else {
                            log.log(Level.INFO, "Sending answers");

                            try {
                                String answerJSON = incomingCommand.replaceFirst("SendAnswers: ", "");
                                answerJSON.replace("\n", "");
                                answerJSON.replace("\r", "");

                                Gson json = JSONHelper.getInstance();

                                AnswerSet answerSet = json.fromJson(answerJSON, AnswerSet.class);

                                if (DataControllerAPI.getPatientByNSHNUmber(answerSet.getPatientNHS()) != null) {
                                    Questionnaire questionnaire = DataControllerAPI.getQuestionnaireByID(answerSet.getQuestionnaireID());

                                    switch (questionnaire.getState()) {
                                        case "Deployed":
                                            return DataControllerAPI.saveAnswer(answerSet) ? TrafficEncryptor.encryptAndFormat("{ 'result': true }") : TrafficEncryptor.encryptAndFormat("{ 'result': false }");
                                        default:
                                            return TrafficEncryptor.encryptAndFormat("error");
                                    }
                                } else {
                                    return TrafficEncryptor.encryptAndFormat("error");
                                }
                            } catch (Exception e) {
                                return TrafficEncryptor.encryptAndFormat("error");
                            }
                        }
                    } else {
                        log.log(Level.INFO, "Checking connection");
                        try {
                            return TrafficEncryptor.encryptAndFormat("{ 'result': true }");
                        } catch (Exception e) {
                            return TrafficEncryptor.encryptAndFormat("error");
                        }
                    }
                } else {
                    log.log(Level.INFO, "Getting questionnaire by id");

                    try {
                        Questionnaire questionnaire = DataControllerAPI.getQuestionnaireByID(Integer.parseInt(incomingCommand.split(": ")[1]));
                        if (questionnaire != null) {
                            Gson json = JSONHelper.getInstance();
                            return TrafficEncryptor.encryptAndFormat(json.toJson(questionnaire));
                        } else {
                            return TrafficEncryptor.encryptAndFormat("error");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return TrafficEncryptor.encryptAndFormat("error");
                    } catch (Exception e) {
                        return TrafficEncryptor.encryptAndFormat("error");
                    }
                }
            } else {
                log.log(Level.INFO, "Getting questionnaires");

                try {
                    Patient patient = DataControllerAPI.getPatientByNSHNUmber(incomingCommand.split(": ")[1]);
                    if (patient == null) {
                        return TrafficEncryptor.encryptAndFormat("error");
                    } else {
                        ArrayList<Questionnaire> questionnaires = DataControllerAPI.getQuestionnairePointersForPatient(patient);
                        Gson json = JSONHelper.getInstance();
                        return TrafficEncryptor.encryptAndFormat(json.toJson(questionnaires));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    return TrafficEncryptor.encryptAndFormat("error");
                }
            }
        }
    }
}