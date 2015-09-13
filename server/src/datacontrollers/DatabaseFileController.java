package datacontrollers;

import gui.main.MainController;
import helpers.AppFolderHelper;
import helpers.PasswordEncryptor;
import model.logs.PatientLog;
import model.logs.PatientQuestionnaireLog;
import model.logs.QuestionnaireLog;
import model.logs.UserLog;
import model.patient.Patient;
import model.patient.TablePatient;
import model.questionnaire.Questionnaire;
import model.user.User;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains the SQL methods to interact with the database for the whole system.
 */

public class DatabaseFileController {

    private static final Logger log = Logger.getLogger(DatabaseFileController.class.getName());
    private Connection connection;


    /**
     * Retrieves the connection to the database
     */
    public DatabaseFileController() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Database JDBC driver not found!");
            e.printStackTrace();
        }
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + AppFolderHelper.getAppFolder() + "/" + AppFolderHelper.getDatabaseName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Queries the database for all existing export.
     *
     * @return the list of questionnaire pointers along with the the ID, name and its state. The ArrayList
     * contains the list of QuesitonnairePointers.
     * @throws SQLException
     */
    public ArrayList<Questionnaire> getAllQuestionnairesPointersList() throws SQLException {
        String query = "SELECT * FROM Questionnaire;";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet result = statement.executeQuery();

        ArrayList<Questionnaire> pointerList = new ArrayList<>();

        if (!result.next()) {
            return pointerList;
        }
        do {
            Questionnaire questionnairePointer = new Questionnaire(result.getInt(1), result.getString(2), result.getString(3));
            pointerList.add(questionnairePointer);
        } while (result.next());

        return pointerList;
    }

    /**
     * Queries the database to return a specific record of a questionnaire with a ID.
     *
     * @param id ID of questionnaire
     * @return Questionnaire object of the queried questionnaire
     * @throws SQLException
     */
    public Questionnaire getQuestionnaireByID(int id) throws SQLException {
        String query = "SELECT * FROM Questionnaire WHERE Q_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        ResultSet result = statement.executeQuery();

        return result.next() ? new Questionnaire(result.getInt(1), result.getString(2), result.getString(3)) : null;
    }

    /**
     * Queries the database for export with specific questionnaire states {@link model.types.QuestionnaireStates}.
     *
     * @param state
     * @return list of Questionnaire objects of the questionnaire
     * @throws SQLException
     */
    public ArrayList<Questionnaire> getAllQuestionnairesForState(String state) throws SQLException {
        String query = "SELECT * FROM Questionnaire WHERE Q_state = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, state);

        ResultSet result = statement.executeQuery();

        ArrayList<Questionnaire> pointerList = new ArrayList<>();

        if (!result.next()) {
            return pointerList;
        }
        do {
            Questionnaire questionnairePointer = new Questionnaire(result.getInt(1), result.getString(2), result.getString(3));
            pointerList.add(questionnairePointer);
        } while (result.next());

        return pointerList;
    }


    /**
     * Inserts a questionnaire into the database
     *
     * @param questionnaire
     * @return the inserted Questionnaire object
     * @throws SQLException
     */
    public Questionnaire insertQuestionnaire(Questionnaire questionnaire) throws SQLException {
        String query = "INSERT INTO Questionnaire (Q_title, Q_state) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, questionnaire.getTitle());
        statement.setString(2, questionnaire.getState());
        statement.execute();
        ResultSet keys = statement.getGeneratedKeys();
        if (!keys.next()) {
            throw new SQLException("Error inserting Questionnaire record.");
        } else {
            questionnaire.setId(keys.getInt(1));
            return questionnaire;
        }
    }

    /**
     * Removes a questionnaire from the database.
     *
     * @param questionnaire questionnaire to be removed
     * @return true
     * @throws SQLException
     */
    public boolean removeQuestionnaire(Questionnaire questionnaire) throws SQLException {
        switch (questionnaire.getId()) {
            case 0:
                return false;
        }
        String query = "DELETE FROM Questionnaire WHERE Q_id= ? ;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, questionnaire.getId());
        statement.execute();
        return true;
    }

    /**
     * Updates a questionnaire record
     *
     * @param questionnaire questionnaire to be updated
     * @return true if questionnaire updated
     * @throws Exception
     */
    public boolean updateQuestionnaire(Questionnaire questionnaire) throws Exception {
        String query = "UPDATE Questionnaire SET Q_state = ?, Q_title = ? WHERE Q_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, questionnaire.getState());
        statement.setString(2, questionnaire.getTitle());
        statement.setInt(3, questionnaire.getId());
        int result = statement.executeUpdate();

        return (result > 0);
    }

    /**
     * Changes the questionnaire state.
     *
     * @param pointer questionnaire to be updated
     * @param state state to be updated to
     * @return true if updated
     * @throws Exception
     */
    public boolean setQuestionnairePointerState(Questionnaire pointer, String state) throws Exception {

        String query = "UPDATE Questionnaire SET Q_state = ? WHERE Q_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, state);
        statement.setInt(2, pointer.getId());
        int result = statement.executeUpdate();

        return (result > 0);
    }

    /**
     * Queries the database for all existing patients.
     *
     * @return ArrayList of Patient objects
     * @throws SQLException
     */
    public ArrayList<Patient> getAllPatients() throws SQLException {
        ArrayList<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patient";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        ResultSet result = statement.getResultSet();
        if (result.next()) {
            do {
                Patient patient = new Patient(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
                patients.add(patient);
            } while (result.next());
        }
        return patients;
    }

    /**
     * Queries the database for all existing patients.
     *
     * @return ArrayList of TablePatient objects
     * @throws SQLException
     */
    public ArrayList<TablePatient> getAllTablePatients() throws SQLException {
        ArrayList<TablePatient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patient";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        ResultSet result = statement.getResultSet();
        if (!result.next()) {
            return patients;
        }
        do {
            TablePatient patient = new TablePatient(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6));
            patients.add(patient);
        } while (result.next());
        return patients;
    }

    /**
     * Queries the database fo a patient with specific NHS number
     *
     * @param nhsNumber Patient NHS number
     * @return Patient object
     * @throws SQLException
     */
    public Patient getPatientByNHSNumber(String nhsNumber) throws SQLException {
        String query = "SELECT * FROM Patient WHERE P_NHS_Number = ? ;";
        PreparedStatement statement = connection.prepareStatement(query);

        statement.setString(1, nhsNumber);
        statement.execute();
        ResultSet result = statement.getResultSet();

        return result.next() ? new Patient(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6)) : null;
    }

    /**
     * Updates a patient record
     *
     * @param patient Patient object with new details
     * @return true if updated
     * @throws SQLException
     */
    public boolean updatePatientRecord(Patient patient) throws SQLException {
        String query = "UPDATE Patient SET P_first_name = ?, P_middle_name = ?, P_surname = ?, P_date_of_birth = ?, P_postcode = ? WHERE P_NHS_number = ?;";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, patient.getFirstName());
        statement.setString(2, patient.getMiddleName());
        statement.setString(3, patient.getSurname());
        statement.setString(4, patient.getDateOfBirth());
        statement.setString(5, patient.getPostcode());
        statement.setString(6, patient.getNhsNumber());

        int result = statement.executeUpdate();

        return (result > 0);
    }

    /**
     * Inserts a new patient into the database
     * @param patient
     * @return the inserted Patient object
     * @throws SQLException
     */
    public Patient insertPatient(Patient patient) throws SQLException {
        Statement statement = createStatement();
        String query = "INSERT INTO Patient(P_NHS_number, P_first_name, P_middle_name, P_surname, P_date_of_birth, P_postcode) VALUES('" +
                patient.getNhsNumber() + "','" +
                patient.getFirstName() + "','" +
                patient.getMiddleName() + "','" +
                patient.getSurname() + "','" +
                patient.getDateOfBirth() + "','" +
                patient.getPostcode() + "');";
        statement.execute(query);
        return patient;
    }

    /**
     * Deletes a patient record from the database and removes any questionnaire assignments
     *
     * @param patient Patient to be removed
     * @return true
     * @throws SQLException
     */
    public boolean removePatient(Patient patient) throws SQLException {
        String query = "DELETE FROM Patient WHERE P_NHS_number = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, patient.getNhsNumber());
        statement.execute();
        linkRemovedPatientAndMultipleQuestionnairePointers(patient, getQuestionnairePointersForPatient(patient));
        unlinkPatientAndMultipleQuestionnairePointers(patient, getQuestionnairePointersForPatient(patient));

        return true;
    }

    /**
     * Removes assignments from patient to multiple questionnaire in database
     *
     * @param patient the patient
     * @param questionnaires list of export
     * @return true
     * @throws SQLException
     */
    public boolean linkRemovedPatientAndMultipleQuestionnairePointers(Patient patient, ArrayList<Questionnaire> questionnaires) throws SQLException {
        for (Iterator<Questionnaire> iterator = questionnaires.iterator(); iterator.hasNext(); ) {
            Questionnaire questionnairePointer = iterator.next();
            if (questionnairePointer.getId() != 0 && !StringUtils.isBlank(patient.getNhsNumber())) {
                Statement statement = createStatement();
                statement.execute("INSERT INTO Removed_Patient_Questionnaire (P_NHS_number, Q_id, Completed) VALUES ('" +
                        patient.getNhsNumber() + "','" +
                        questionnairePointer.getId() + "', '0');");
            } else {
                return false;
            }

        }
        return true;

    }


    /**
     * Queries the database for assigned export for patient
     *
     * @param patient
     * @return list of QuestionnairePointers
     * @throws SQLException
     */
    public ArrayList<Questionnaire> getQuestionnairePointersForPatient(Patient patient) throws SQLException {
        Statement statement = createStatement();
        String query = "SELECT Questionnaire.Q_id, Questionnaire.Q_title, Questionnaire.Q_state " +
                "FROM Patient_Questionnaire " +
                "INNER JOIN Questionnaire " +
                "ON Patient_Questionnaire.Q_id = Questionnaire.Q_id  " +
                "WHERE Completed = 0 AND Q_state = 'Deployed' AND P_NHS_number ='" + patient.getNhsNumber() + "';";
        ResultSet result = statement.executeQuery(query);

        ArrayList<Questionnaire> pointerList = new ArrayList<>();
        if (result.next()) {
            do {
                Questionnaire questionnairePointer = new Questionnaire(result.getInt(1), result.getString(2), result.getString(3));
                pointerList.add(questionnairePointer);
            } while (result.next());
        }
        return pointerList;
    }


    /**
     * Assigns patient to questionnaire in database
     *
     * @param patient the patient
     * @return true if assignment is successful
     * @throws SQLException
     */
    public boolean linkPatientAndQuestionnairePointer(Patient patient, Questionnaire questionnaire) throws SQLException {
        if (questionnaire.getId() != 0 && !StringUtils.isBlank(patient.getNhsNumber())) {
            Statement statement = createStatement();
            statement.execute("INSERT INTO Patient_Questionnaire (P_NHS_number, Q_id, Completed) VALUES ('" +
                    patient.getNhsNumber() + "','" +
                    questionnaire.getId() + "', '0');");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes assignments from patient to questionnaire in database
     *
     * @param patient the patient
     * @return true if assignment is removed
     * @throws SQLException
     */
    public boolean unlinkPatientAndQuestionnairePointer(Patient patient, Questionnaire questionnaire) throws SQLException {
        if (questionnaire.getId() != 0 && !StringUtils.isBlank(patient.getNhsNumber())) {
            Statement statement = createStatement();
            statement.execute("DELETE FROM Patient_Questionnaire WHERE P_NHS_number = '" +
                    patient.getNhsNumber() + "' AND Q_id = '" +
                    questionnaire.getId() + "'");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes assignments from patient to multiple questionnaire in database
     *
     * @param patient the patient
     * @param questionnaires list of export
     * @return true
     * @throws SQLException
     */
    public boolean unlinkPatientAndMultipleQuestionnairePointers(Patient patient, ArrayList<Questionnaire> questionnaires) throws SQLException {
        for (Iterator<Questionnaire> iterator = questionnaires.iterator(); iterator.hasNext(); ) {
            Questionnaire questionnairePointer = iterator.next();
            if (questionnairePointer.getId() != 0 && !StringUtils.isBlank(patient.getNhsNumber())) {
                Statement statement = createStatement();
                statement.execute("DELETE FROM Patient_Questionnaire WHERE P_NHS_number = '" +
                        patient.getNhsNumber() + "' AND Q_id = '" +
                        questionnairePointer.getId() + "'");
            } else {
                return false;
            }
        }
        return true;

    }

    /**
     * Checks database to see if patient is assigned to questionnaire
     *
     * @param patient patient to check for
     * @param questionnaire question to be checked against
     * @return true if assignment exist
     * @throws SQLException
     */
    public boolean isPatientAssignedToQuestionnaire(Patient patient, Questionnaire questionnaire) throws SQLException {
        if (questionnaire == null || questionnaire.getId() == 0 || StringUtils.isBlank(patient.getNhsNumber())) {
            return false;
        } else {
            Statement statement = createStatement();
            String query = "SELECT * FROM Patient_Questionnaire WHERE P_NHS_Number = '" + patient.getNhsNumber() + "' AND Q_id = '" + questionnaire.getId() + "';";

            ResultSet result = statement.executeQuery(query);
            return (result.next());
        }
    }

    /**
     * Changes the assignment for questionnaire to completed
     *
     * @param questionnaire questionnaire completed
     * @param patient
     * @return true if changing completion has been successful
     * @throws SQLException
     */
    public boolean setPatientQuestionnaireAsCompleted(Questionnaire questionnaire, Patient patient) throws SQLException {
        if (!isPatientAssignedToQuestionnaire(patient, questionnaire)) {
            return false;
        } else {
            String query = "UPDATE Patient_Questionnaire SET Completed = 1 WHERE Q_id = ? AND P_NHS_Number = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, questionnaire.getId());
            statement.setString(2, patient.getNhsNumber());
            int success = statement.executeUpdate();
            return (success == 1);
        }
    }

    /**
     * Changes the assignment for questionnaire to not completed
     *
     * @param questionnaire questionnaire completed
     * @param patient
     * @return true if changing completion has been successful
     * @throws SQLException
     */
    public boolean setPatientQuestionnaireAsNotCompleted(Questionnaire questionnaire, Patient patient) throws SQLException {
        if (!isPatientAssignedToQuestionnaire(patient, questionnaire)) {
            return false;
        } else {
            String query = "UPDATE Patient_Questionnaire SET Completed = 0 WHERE Q_id = ? AND P_NHS_Number = ?;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, questionnaire.getId());
            statement.setString(2, patient.getNhsNumber());
            int success = statement.executeUpdate();
            return (success == 1);
        }
    }

    /**
     * Gets the patient logs from the database
     *
     * @return list of PatientLog objects for each record in the database
     * @throws SQLException
     */
    public ArrayList<PatientLog> getAllPatientLogs() throws SQLException {
        ArrayList<PatientLog> patientLogs = new ArrayList<>();
        Statement statement = createStatement();
        String query = "SELECT * FROM Patient_Log";
        ResultSet result = statement.executeQuery(query);
        if (!result.next()) {
            return patientLogs;
        }
        do {
            PatientLog patientLog = new PatientLog(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6),
                    result.getString(7), result.getString(8), result.getString(9), result.getString(10), result.getString(11),
                    result.getString(12), result.getString(13), result.getString(14), result.getString(15));
            patientLogs.add(patientLog);
        } while (result.next());
        return patientLogs;
    }

    /**
     * Gets the questionnaire logs from the database
     *
     * @return list of QuestionnaireLog objects for each record in the database
     * @throws SQLException
     */
    public ArrayList<QuestionnaireLog> getAllQuestionnaireLogs() throws SQLException {
        ArrayList<QuestionnaireLog> questionnaireLogs = new ArrayList<>();
        Statement statement = createStatement();
        String query = "SELECT * FROM Questionnaire_Log";
        ResultSet result = statement.executeQuery(query);
        if (!result.next()) {
            return questionnaireLogs;
        }
        do {
            QuestionnaireLog questionnaireLog = new QuestionnaireLog(result.getInt(1), result.getInt(2), result.getInt(3),
                    result.getString(4), result.getString(5), result.getString(6),
                    result.getString(7), result.getString(8), result.getString(9));
            questionnaireLogs.add(questionnaireLog);
        } while (result.next());
        return questionnaireLogs;
    }

    /**
     * Inserts the database with logs with SQL action Update for export.
     *
     * @throws SQLException
     */
    public ArrayList<UserLog> getAllAdminLogs() throws SQLException {
        ArrayList<UserLog> userLogs = new ArrayList<>();
        Statement statement = createStatement();
        String query = "SELECT * FROM Admin_Log";
        ResultSet result = statement.executeQuery(query);
        if (!result.next()) {
            return userLogs;
        }
        do {
            UserLog userLog = new UserLog(result.getInt(1), result.getString(2), result.getString(3),
                    result.getString(4), result.getString(5));
            userLogs.add(userLog);
        } while (result.next());
        return userLogs;
    }

    /**
     * Gets the admin logs from the database
     *
     * @return list of AdminLog objects for each record in the database
     * @throws SQLException
     */
    public boolean actionAdminLog(UserLog userLog) throws SQLException{
        String query = "INSERT INTO Admin_Log"
                + "(a_username, a_affected_user, SQL_action, Time_enter) VALUES"
                + "(?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, userLog.getA_username());
        statement.setString(2, userLog.getA_affected_user());
        statement.setString(3, userLog.getSQL_action());
        statement.setString(4, userLog.getTime_enter());
        int results = statement.executeUpdate();
        return results > 0;
    }

    /**
     * Gets the questionnaire and patient assignments logs from the database
     *
     * @return list of PatientQuestionnaireLog objects for each record in the database
     * @throws SQLException
     */
    public ArrayList<PatientQuestionnaireLog> getAllPatientQuestionnaireLogs() throws SQLException {
        ArrayList<PatientQuestionnaireLog> patientQuestionnaireLogs = new ArrayList<>();
        Statement statement = createStatement();
        String query = "SELECT * FROM Patient_Questionnaire_Log";
        ResultSet result = statement.executeQuery(query);
        if (!result.next()) {
            return patientQuestionnaireLogs;
        }
        do {
            PatientQuestionnaireLog patientQuestionnaireLog = new PatientQuestionnaireLog(result.getInt(1), result.getString(2), result.getString(3),
                    result.getString(4), result.getString(5), result.getString(6),
                    result.getString(7), result.getString(8), result.getString(9));
            patientQuestionnaireLogs.add(patientQuestionnaireLog);
        } while (result.next());
        return patientQuestionnaireLogs;
    }

    /**
     * Inserts the database with logs with SQL action Update for patient and questionnaire interaction.
     *
     * @throws SQLException
     */
    public void populatePatientQuestionnaireLogsUpdate() throws SQLException {
        Statement statement = createStatement();
        statement.execute("CREATE TRIGGER IF NOT EXISTS update_Patient_Questionnaire_Log AFTER UPDATE  ON Patient_Questionnaire\n" +
                "BEGIN\n" +
                "  INSERT INTO Patient_Questionnaire_Log  (P_NHS_number_OLD, P_NHS_number_NEW ,Q_id_OLD, Q_id_NEW,\n" +
                "                            Completed_OLD, Completed_NEW, SQL_action, Time_enter)\n" +
                "      VALUES (old.P_NHS_number, new.P_NHS_number, old.Q_id,new.Q_id,\n" +
                "                  old.Completed, new.Completed, 'UPDATE', DATETIME('NOW') );\n" +
                "END");
    }


    /**
     * Inserts the database with logs with SQL action Insert for patient and questionnaire interaction.
     *
     * @throws SQLException
     */
    public void populatePatientQuestionnaireLogsInsert() throws SQLException {
        Statement statement = createStatement();
        statement.execute("CREATE TRIGGER IF NOT EXISTS insert_Patient_Questionnaire_Log AFTER INSERT  ON Patient_Questionnaire\n" +
                "BEGIN\n" +
                "  INSERT INTO Patient_Questionnaire_Log  (P_NHS_number_NEW, Q_id_NEW, Completed_NEW,\n" +
                "                            SQL_action, Time_enter)\n" +
                "      VALUES (new.P_NHS_number, new.Q_id, new.Completed, 'INSERT', DATETIME('NOW') );\n" +
                "END");
    }

    /**
     * Inserts the database with logs with SQL action Delete for patient and questionnaire interaction.
     *
     * @throws SQLException
     */
    public void populatePatientQuestionnaireLogsDelete() throws SQLException {
        Statement statement = createStatement();
        statement.execute("CREATE TRIGGER IF NOT EXISTS delete_Patient_Questionnaire_Log DELETE  ON Patient_Questionnaire\n" +
                "BEGIN\n" +
                "  INSERT INTO Patient_Questionnaire_Log  (P_NHS_Number_OLD, Q_id_OLD, Completed_OLD, SQL_action, Time_enter)\n" +
                "      VALUES (old.P_NHS_number, old.Q_id, old.Completed, 'DELETE', DATETIME('NOW') );\n" +
                "END");
    }

    /**
     * Checks the password for certain user
     *
     * @param user Admin details to check
     * @return true if password matches username
     * @throws SQLException
     */
    public boolean checkUsernamePassword(User user) throws SQLException{
        String query = "SELECT * FROM Admin WHERE A_passcode=? AND A_username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, PasswordEncryptor.generateSHA256(user.getPassword()));
        statement.setString(2, user.getUsername());

        ResultSet results = statement.executeQuery();

        return results.next();
    }

    /**
     * Inserts a new admin into the database
     * @param user new admin to be inserted
     * @return true if insertion has been successful
     * @throws SQLException
     */
    public boolean insertAdmin(User user) throws SQLException {
        String query = "INSERT INTO Admin VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, PasswordEncryptor.generateSHA256(user.getPassword()));
        statement.setString(3, user.getCreatedOn());
        statement.setString(4, user.getCreatedBy());
        statement.setString(5, user.getUserType());
        int results = statement.executeUpdate();
        return results > 0;
    }

    /**
     * Checks the password for certain user
     *
     * @param user Admin details to check
     * @return true if password matches username
     * @throws SQLException
     */
    public boolean checkPasswordForUsername(User user) throws SQLException {
        String query = "SELECT * FROM Admin WHERE A_username=? AND A_passcode=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());
        statement.setString(2, PasswordEncryptor.generateSHA256(user.getPassword()));
        ResultSet results = statement.executeQuery();

        return results.next();
    }

    /**
     * Updates the password for a user
     *
     * @param user Admin object with the new details
     * @return true if record has been updated successfully
     * @throws SQLException
     */
    public boolean updateAdminPassword(User user) throws SQLException {
        String query = "UPDATE Admin SET A_passcode=? WHERE A_username=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, PasswordEncryptor.generateSHA256(user.getPassword()));
        statement.setString(2, user.getUsername());
        int results = statement.executeUpdate();
        return results > 0;
    }

    /**
     * Gets all the admin records from the database
     *
     * @return list of Admin objects
     * @throws SQLException
     */
    public ArrayList<User> getAllAdmins() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM Admin";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.execute();
        ResultSet result = statement.getResultSet();
        if (!result.next()) {
            return users;
        }
        do {
            User user = new User(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
            if (user.getUsername().equals(MainController.currentUserLoggedIn)) {
            } else {
                users.add(user);
            }
        } while (result.next());
        return users;
    }

    /**
     * Removes admin record from database
     *
     * @param user record to be removed
     * @return true if record has been removed successfully
     * @throws SQLException
     */
    public boolean removeAdmin(User user) throws SQLException {
        String query = "DELETE FROM Admin WHERE A_username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUsername());

        statement.execute();

        return true;
    }

    /**
     * Gets the user type of a user
     * @param admin Admin object to get the user type for
     * @return user type
     * @throws SQLException
     */
    public String getUserType(String admin) throws SQLException {
        String query = "SELECT A_Type FROM Admin WHERE A_username = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, admin);
        String type = "";
        statement.execute();
        ResultSet result = statement.getResultSet();
        if (!result.next()) {
            return type;
        }
        do {
            type = result.getString(1);
        } while (result.next());
        return type;
    }

    private Statement createStatement() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }


}