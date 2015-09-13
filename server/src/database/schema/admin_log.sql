CREATE TABLE IF NOT EXISTS 'Admin_Log' (
   'id' INTEGER PRIMARY KEY AUTOINCREMENT,
   'a_username' VARCHAR(100),
   'a_affected_user' VARCHAR(100),
   'SQL_action' VARCHAR(15),
   'Time_enter' DATE
);
