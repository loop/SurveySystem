package model.logs;

/**
 * This class is the questionnaire log object.
 */

public class QuestionnaireLog {
    private final int id;
    private final int oldID;
    private final int newID;
    private final String oldTitle;
    private final String newTitle;
    private final String oldState;
    private final String newState;
    private final String sqlAction;
    private final String time;

    public QuestionnaireLog(int id, int oldID, int newID, String oldTitle, String newTitle, String oldState, String newState, String sqlAction, String time) {
        this.id = id;
        this.oldID = oldID;
        this.newID = newID;
        this.oldTitle = oldTitle;
        this.newTitle = newTitle;
        this.oldState = oldState;
        this.newState = newState;
        this.sqlAction = sqlAction;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public int getOldID() {
        return oldID;
    }

    public int getNewID() {
        return newID;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public String getNewTitle() {
        return newTitle;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }

    public String getSqlAction() {
        return sqlAction;
    }

    public String getTime() {
        return time;
    }
}
