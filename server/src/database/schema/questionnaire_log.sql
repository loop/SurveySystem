CREATE TABLE IF NOT EXISTS 'Questionnaire_Log' (
   'qlkey' INTEGER PRIMARY KEY AUTOINCREMENT,
   'Q_id_OLD' INTEGER,
   'Q_id_NEW' INTEGER,
   'Q_title_OLD' VARCHAR(250),
   'Q_title_NEW' VARCHAR(250),
   'Q_state_OLD' TEXT,
   'Q_state_NEW' TEXT,
   'SQL_action' varchar(15),
   'Time_enter' DATE
);
