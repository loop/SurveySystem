package com.yogeshn.fyp.androidclient.backend.filegeneration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

import com.yogeshn.fyp.androidclient.backend.connectivity.TrafficEncryptor;
import com.yogeshn.fyp.androidclient.backend.connectivity.SocketAPI;

/**
 * Class that is responsible for reading and writing the answer file file. It also fixed problems
 * if there are network errors when send it to the server.
 *
 */
@SuppressWarnings({"SameParameterValue", "ConstantConditions"})
public class AnswerFile {
    private static Context context = null;
    private static final String FILENAME = "answers.temp";

    /**
     * Reads the answer file and sends to server.
     *
     * @param context
     */
    public static void sendFileToServer(Context context) {
        AnswerFile.context = context;
        String content = readFile(context);
        switch (content) {
            case "":
                return;
        }
        SendAnswerFile f = new SendAnswerFile();
        f.execute(content);
    }

    /**
     * Reads the files content and encrypts it.
     *
     * @param context
     * @return Ciphertext of file.
     */
    private static String readFile(Context context) {
        if (!fileExists(context)) {
            return "";
        } else {
            String result;
            try {
                DataInputStream in = new DataInputStream(context.openFileInput(FILENAME));
                result = in.readUTF();
                if (in == null) {
                } else {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return result;
        }
    }

    /**
     * Check to see if answer file exists.
     *
     * @param context
     * @return true if file exists else false
     */
    private static boolean fileExists(Context context) {
        return context.getFileStreamPath(FILENAME).exists();
    }

    /**
     * Creates empty answer file file.
     *
     * @param context
     * @param filename filename for the answer file
     */
    private static void createEmptyFile(Context context, String filename) {
        try {
            DataOutputStream out = new DataOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Writes the encrypted content to the file.
     *
     * @param context
     * @param content files content
     */
    public static void writeFile(Context context, String content) {
        try {
            context.deleteFile(FILENAME);
            DataOutputStream out = new DataOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));

            try {
                out.writeUTF(String.valueOf(TrafficEncryptor.encrypt(content)));
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (out != null) {
                out.close();
            } else {
                return;
            }

        } catch (IOException e) {
            createEmptyFile(context, FILENAME);
            writeFile(context, content);
            e.printStackTrace();
        }
    }

    /**
     * This AsyncTask class is responsible for sending data to server.
     */
    private static class SendAnswerFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return SocketAPI.sendAnswersToServer(TrafficEncryptor.decrypt(params[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "string";
        }

        @Override
        public void onPostExecute(String result) {
            if (result.contains("true")) {
                context.deleteFile(FILENAME);
            }
        }

    }
}
