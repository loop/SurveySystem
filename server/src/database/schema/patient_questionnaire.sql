CREATE TABLE IF NOT EXISTS 'Patient_Questionnaire' (
    'P_NHS_number' int(10) NOT NULL,
    'Q_id' int(8) NOT NULL,
    'Completed' tinyint(1) NOT NULL,
    PRIMARY KEY ('P_NHS_number','Q_id'),
    FOREIGN KEY ('P_NHS_number') REFERENCES 'Patient'('P_NHS_number') ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY ('Q_id') REFERENCES 'Questionnaire'('Q_id') ON DELETE CASCADE ON UPDATE CASCADE
);