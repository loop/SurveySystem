CREATE TABLE IF NOT EXISTS 'Patient' (
    'P_NHS_number' varchar(10) NOT NULL,
    'P_first_name' varchar(20) NOT NULL,
    'P_middle_name' varchar(20) NOT NULL,
    'P_surname' varchar(20) NOT NULL,
    'P_date_of_birth' date NOT NULL,
    'P_postcode' varchar(8) NOT NULL,
    PRIMARY KEY ('P_NHS_number')
);