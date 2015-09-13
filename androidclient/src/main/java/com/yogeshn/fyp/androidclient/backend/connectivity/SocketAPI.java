package com.yogeshn.fyp.androidclient.backend.connectivity;

import java.util.concurrent.CountDownLatch;

import com.yogeshn.fyp.androidclient.activities.ConnectActivity;

/**
 * This class mains the API methods that communicate with the server.
 *
 */
public class SocketAPI
{
    public static final String SOCKET_TIMEOUT_EXCEPTION = "Socket has timed out";

    /**
     * Calls 'FindPatient' method at the server side.
     *
     * @param NHS Patient's NHS number.
     * @return JSON String result.
     */
    public static String findPatient(String NHS)
    {
        return runServerMethod("FindPatient: " + NHS);
    }

    /**
     * Calls 'GetAllQuestionnairesForPatient' method at the server side.
     *
     * @param NHS Patient's NHS number
     * @return JSON String result.
     */
    public static String getQuestionnairesForPatient(String NHS)
    {
        return runServerMethod("GetAllQuestionnairesForPatient: " + NHS);
    }

    /**
     * Calls 'GetQuestionnaireByName' method at the server side
     *
     * @param questionnaireID Name of the questionnaire.
     * @return JSON String result.
     */
    public static String getQuestionnaireByName(String questionnaireID)
    {
        return runServerMethod("GetQuestionnaireByID: " + questionnaireID);
    }

    /**
     * Calls 'CheckPasscode' method at the server side.
     * Format = { "result": true/false }
     *
     * @return TRUE String if the combination is correct, FALSE String otherwise.
     */
    public static String checkServerConnection()
    {
        return runServerMethod("CheckConnection");
    }

    /**
     * Calls 'SendAnswers' method at the server side.
     *
     * @param answersJSON JSON formatted String of answers.
     * @return TRUE if received correctly, FALSE otherwise.
     */
    public static String sendAnswersToServer(String answersJSON)
    {
        return runServerMethod("SendAnswers: " + answersJSON);
    }

    /**
     * Calls 'Close' method at the server side.
     */
    public static void close()
    {
        runServerMethod("Close");
    }

    /**
     * Method that runs a new Thread that retrieves the answer from the server.
     *
     * @param command Command send to the server.
     * @return String response of a server to the command.
     */
    private static String runServerMethod(String command)
    {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        SocketThread socketThread = new SocketThread(ConnectActivity.getServerIP(), command, countDownLatch);
        socketThread.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {e.printStackTrace();}
        return socketThread.getResult();
    }
}
