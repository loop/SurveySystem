package model.logs;

/**
 * This class is the patient log object.
 */

public class PatientLog {
    private final int id;
    private final String oldNHSNumber;
    private final String newNHSNumber;
    private final String oldFirstName;
    private final String newFirstName;
    private final String oldMiddleName;
    private final String newMiddleName;
    private final String oldSurname;
    private final String newSurname;
    private final String oldDoB;
    private final String newDoB;
    private final String oldPostCode;
    private final String newPostCode;
    private final String sqlAction;
    private final String time;

    public PatientLog(int id, String oldNHSNumber, String newNHSNumber, String oldFirstName, String newFirstName, String oldMiddleName, String newMiddleName, String oldSurname, String newSurname, String oldDoB, String newDoB, String oldPostCode, String newPostCode, String sqlAction, String time) {
        this.id = id;
        this.oldNHSNumber = oldNHSNumber;
        this.newNHSNumber = newNHSNumber;
        this.oldFirstName = oldFirstName;
        this.newFirstName = newFirstName;
        this.oldMiddleName = oldMiddleName;
        this.newMiddleName = newMiddleName;
        this.oldSurname = oldSurname;
        this.newSurname = newSurname;
        this.oldDoB = oldDoB;
        this.newDoB = newDoB;
        this.oldPostCode = oldPostCode;
        this.newPostCode = newPostCode;
        this.sqlAction = sqlAction;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getOldNHSNumber() {
        return oldNHSNumber;
    }

    public String getNewNHSNumber() {
        return newNHSNumber;
    }

    public String getOldFirstName() {
        return oldFirstName;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getOldMiddleName() {
        return oldMiddleName;
    }

    public String getNewMiddleName() {
        return newMiddleName;
    }

    public String getOldSurname() {
        return oldSurname;
    }

    public String getNewSurname() {
        return newSurname;
    }

    public String getOldDoB() {
        return oldDoB;
    }

    public String getNewDoB() {
        return newDoB;
    }

    public String getOldPostCode() {
        return oldPostCode;
    }

    public String getNewPostCode() {
        return newPostCode;
    }

    public String getSqlAction() {
        return sqlAction;
    }

    public String getTime() {
        return time;
    }
}