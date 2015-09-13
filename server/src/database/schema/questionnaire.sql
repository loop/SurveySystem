CREATE TABLE IF NOT EXISTS 'Questionnaire' (
  'Q_id' INTEGER PRIMARY KEY AUTOINCREMENT,
  'Q_title' varchar(250) DEFAULT NULL,
  'Q_state' TEXT CHECK (Q_state IN ('Draft', 'Deployed', 'Archived')) NOT NULL
);
