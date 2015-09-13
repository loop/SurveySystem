CREATE TABLE IF NOT EXISTS 'Admin' (
  'A_username' VARCHAR(12) PRIMARY KEY NOT NULL,
  'A_passcode' VARCHAR(100) NOT NULL,
  'A_created_on' DATE NULL,
  'A_created_by' VARCHAR(20) NOT NULL,
  'A_Type' INTEGER NULL
);