package model.logs;

/**
 * This class is the patient-questionnaire log object.
 */

public class PatientQuestionnaireLog {
    private final int id;
    private final String oldNHSNumber;
    private final String neNHSNumber;
    private final String oldID;
    private final String newID;
    private final String oldCompleted;
    private final String newComplete;
    private final String sqlAction;
    private final String time;

    public PatientQuestionnaireLog(int id, String oldNHSNumber, String neNHSNumber, String oldID, String newID, String oldCompleted, String newComplete, String sqlAction, String time) {
        this.oldNHSNumber = oldNHSNumber;
        this.id = id;
        this.neNHSNumber = neNHSNumber;
        this.oldID = oldID;
        this.newID = newID;
        this.oldCompleted = oldCompleted;
        this.newComplete = newComplete;
        this.sqlAction = sqlAction;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getOldNHSNumber() {
        return oldNHSNumber;
    }

    public String getNeNHSNumber() {
        return neNHSNumber;
    }

    public String getOldID() {
        return oldID;
    }

    public String getNewID() {
        return newID;
    }

    public String getOldCompleted() {
        return oldCompleted;
    }

    public String getNewComplete() {
        return newComplete;
    }

    public String getSqlAction() {
        return sqlAction;
    }

    public String getTime() {
        return time;
    }
}