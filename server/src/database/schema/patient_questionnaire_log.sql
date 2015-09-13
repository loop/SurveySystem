CREATE TABLE IF NOT EXISTS 'Patient_Questionnaire_Log' (
   'pqlkey' INTEGER PRIMARY KEY AUTOINCREMENT,
   'P_NHS_number_OLD' INT(10),
   'P_NHS_number_NEW' INT(10),
   'Q_id_OLD' INT(8),
   'Q_id_NEW' INT (8),
   'Completed_OLD' tinyint(1),
   'Completed_NEW' tinyint(1),
   'SQL_action' varchar(15),
   'Time_enter' DATE
);