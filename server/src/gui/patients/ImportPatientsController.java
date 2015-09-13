package gui.patients;

import datacontrollers.DataControllerAPI;
import helpers.PopUpCreator;
import model.patient.Patient;
import helpers.PatientValidation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Responsible for setting up the view and buttons to import patient CSV.
 */
public class ImportPatientsController implements Initializable {
    @FXML
    public Button openFileButton;
    @FXML
    public Label errorPatients, updatedPatients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openFileButton.setOnAction(event -> {
            try {
                openFileChooser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Opens the file chooser to select the location of the patient CSV for importing.
     *
     * @throws IOException
     */
    public void openFileChooser() throws IOException {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if (!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        fileChooser.setInitialDirectory(userDirectory);

        File chosenFile = fileChooser.showOpenDialog(null);
        String path;
        if (chosenFile != null) {
            path = chosenFile.getPath();
        } else {
            path = null;
        }

        if (path!=null) {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            ArrayList<String> exsitingPatients = new ArrayList<>();
            boolean firstLine = true;
            boolean validHeaders = false;
            int rowNumber = 2;
            boolean errorShown = false;
            int updatedNumberTotal = 0;
            int totalNumberRecords = 0;
            while ( (line=br.readLine()) != null)
            {
                String[] values = line.replaceAll("\"", "").split(",");
                if (firstLine) {
                    if (values.length == 6) {
                        validHeaders = true;
                    }
                    firstLine=false;
                } else {
                    if (validHeaders) {
                        totalNumberRecords++;
                        if (PatientValidation.validateCSVPatientRow(values)) {
                            Patient patient = new Patient(values[0], values[1].trim(),
                                    values[2].trim(), values[3].trim(), values[4].trim(), values[5].toUpperCase().trim());
                            try {
                                if (DataControllerAPI.addPatient(patient)) {
                                    updatedNumberTotal++;
                                }
                            } catch (SQLException e) {
                                exsitingPatients.add("Row Number: " + rowNumber++ + ", Patient Number: " + patient.getNhsNumber());
                            }
                        }
                    } else {
                        PopUpCreator.getAlert(Alert.AlertType.ERROR, "CSV Import Error", "CSV is in not the correct format", "CSV header is not valid. It needs to have 6 values for a valid patient");
                        errorShown = true;
                    }
                }
            }
            String patientsToShow = "The following patients were not added." + "\n" + "They already exist in the database or contain data errors." + "\n\n\n";
            for (String patient : exsitingPatients) {
               patientsToShow += patient + "\n";
            }
            br.close();
            if (!errorShown) {
                errorPatients.setText(patientsToShow);
                errorPatients.setVisible(true);
                updatedPatients.setText(updatedNumberTotal + " out of " + totalNumberRecords + " patients successfully saved!");
                updatedPatients.setVisible(true);
            } else {
                errorPatients.setText("CSV was invalid. Please try again.");
                errorPatients.setVisible(true);
            }
        }
    }
}
