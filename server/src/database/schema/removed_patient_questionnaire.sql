CREATE TABLE IF NOT EXISTS 'Removed_Patient_Questionnaire' (
    'P_NHS_number' int(10) NOT NULL,
    'Q_id' int(8) NOT NULL,
    'Completed' tinyint(1) NOT NULL,
    PRIMARY KEY ('P_NHS_number','Q_id')
);