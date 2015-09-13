package model.logs;

/**
 * This class is the user log object.
 */
public class UserLog {
    private final int id;
    private final String a_username;
    private final String a_affected_user;
    private final String SQL_action;
    private final String Time_enter;

    public UserLog(int id, String a_username, String a_affected_user, String sql_action, String time_enter) {
        this.id = id;
        this.a_username = a_username;
        this.a_affected_user = a_affected_user;
        SQL_action = sql_action;
        Time_enter = time_enter;
    }

    public int getId() {
        return id;
    }

    public String getTime_enter() {
        return Time_enter;
    }

    public String getSQL_action() {
        return SQL_action;
    }

    public String getA_affected_user() {
        return a_affected_user;
    }

    public String getA_username() {
        return a_username;
    }

}
