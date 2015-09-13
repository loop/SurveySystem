package database;

import helpers.AppFolderHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class helps with the initialising of the database and creates the tables using the schema SQL files.
 */

public class DatabaseInitialiser {

    private static final Logger log = Logger.getLogger(DatabaseInitialiser.class.getName());

    /**
     * Sets up the connection to the database file and executed the schema files.
     *
     * @return true is connection established and schemas executed
     */
    public static boolean initialise() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + AppFolderHelper.getAppFolder() + "/" + AppFolderHelper.getDatabaseName());
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.execute(readFile("admin"));
            try {
                statement.execute(readFile("default_admin"));
            } catch (SQLException e) {
                log.log(Level.INFO, "Default admin already inserted.");
            }
            statement.execute(readFile("admin_log"));
            statement.execute(readFile("patient"));
            statement.execute(readFile("patient_log"));
            statement.execute(readFile("patient_questionnaire"));
            statement.execute(readFile("patient_questionnaire_log"));
            statement.execute(readFile("questionnaire"));
            statement.execute(readFile("questionnaire_log"));
            statement.execute(readFile("removed_patient"));
            statement.execute(readFile("removed_patient_questionnaire"));


            log.log(Level.INFO, "Database has been created.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection == null) {
                } else {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    /**
     * Reads the SQL files content.
     *
     * @param filename file to be read
     * @return the file content
     * @throws IOException
     */
    private static String readFile(String filename) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(DatabaseInitialiser.class.getResourceAsStream("schema/" + filename + ".sql")));
        String queryContents;

        if ((queryContents = bufferedReader.readLine()) != null) {
            stringBuilder.append(queryContents).append("\n ");
            while ((queryContents = bufferedReader.readLine()) != null) {
                stringBuilder.append(queryContents).append("\n ");
            }
            return stringBuilder.toString();
        } else {
            return stringBuilder.toString();
        }
    }
}
