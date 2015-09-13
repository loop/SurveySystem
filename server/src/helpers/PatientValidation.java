package helpers;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is responsible for validating the input fields for patients.
 * <p>
 */

public class PatientValidation {

    /**
     * Checks that the patient input fields are of correct format.
     *
     * @param requiredFields fields to check
     * @param patientDetails patient to check
     * @return true if data is valid, else false
     */
    public static boolean checkFieldValidation(boolean requiredFields, String[] patientDetails) {
        boolean isPatientInputDataValid = true;

        try {

            if (!requiredFields) {
                return false;
            }

            String nhsNumberString = patientDetails[0].trim();
            if (!nhsNumberString.matches("^\\d{10}$")) {
                isPatientInputDataValid = false;
            }

            String firstNameString = patientDetails[1].trim();
            if (!firstNameString.matches("^[A-Za-z]{3,20}$")) {
                isPatientInputDataValid = false;
            }

            String middleNameString = patientDetails[2].trim();
            if (!StringUtils.isBlank(middleNameString)) {
                if (!middleNameString.matches("^[A-Za-z]{3,20}$")) {
                    isPatientInputDataValid = false;
                }
            }

            String lastNameString = patientDetails[3].trim();
            if (!lastNameString.matches("^[A-Za-z]{3,20}$")) {
                isPatientInputDataValid = false;
            }

            String day = patientDetails[4].trim();
            String month = patientDetails[5].trim();
            String year = patientDetails[6].trim();
            String dob = day + "-" + month + "-" + year;
            if (!validateDate(dob)) {
                isPatientInputDataValid = false;
            }

            String postcode = patientDetails[7];
            postcode = postcode.toUpperCase();
            if (!StringUtils.isBlank(postcode)) {
                if (!validatePostcode(postcode)) {
                    isPatientInputDataValid = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPatientInputDataValid;
    }

    /**
     * Checks that the patient row of a CSV file are of correct format.
     *
     * @param patientDetails patient to check
     * @return true if data is valid, else false
     */
    public static boolean validateCSVPatientRow(String[] patientDetails) {
        boolean isPatientInputDataValid = true;

        try {
            String nhsNumberString = patientDetails[0].trim();
            if (!nhsNumberString.matches("^\\d{10}$")) {
                isPatientInputDataValid = false;
            }

            String firstNameString = patientDetails[1].trim();
            if (!firstNameString.matches("^[A-Za-z ]{3,20}$")) {
                isPatientInputDataValid = false;
            }

            String middleNameString = patientDetails[2].trim();
            if (!StringUtils.isBlank(patientDetails[2])) {
                if (!middleNameString.matches("^[A-Za-z ]{3,20}$")) {
                    isPatientInputDataValid = false;
                }
            }

            String lastNameString = patientDetails[3].trim();
            if (!lastNameString.matches("^[A-Za-z ]{3,20}$")) {
                isPatientInputDataValid = false;
            }

            if (!validateDate(patientDetails[4])) {
                isPatientInputDataValid = false;
            }

            String postcode = patientDetails[5];
            postcode = postcode.toUpperCase();
            if (!StringUtils.isBlank(patientDetails[5])) {
                if (!validatePostcode(postcode)) {
                    isPatientInputDataValid = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isPatientInputDataValid;
    }

    /**
     * @param postcode postcode to check
     * @return boolean true if postcode is valid, else false
     * <p>
     * The RegEx is the supplied by the UK Government.
     * @see <a href="http://www.cabinetoffice.gov.uk/media/291370/bs7666-v2-0-xsd-PostCodeType.htm">UK Government Data Standard for postcodes</a>
     */
    private static boolean validatePostcode(String postcode) {
        String regexp = "^(GIR 0AA)|((([A-Z-[QVX]][0-9][0-9]?)|(([A-Z-[QVX]][A-Z-[IJZ]][0-9][0-9]?)|(([A-Z-[QVX]][0-9][A-HJKSTUW])|([A-Z-[QVX]][A-Z-[IJZ]][0-9][ABEHMNPRVWXY])))) [0-9][A-Z-[CIKMOV]]{2})";
        if (postcode != null) {
            Pattern pattern = Pattern.compile(regexp);
            Matcher matcher = pattern.matcher(postcode.toUpperCase());
            return matcher.matches();
        }
        return false;
    }

    /**
     * Checks that the date is in the correct format and date exists.
     *
     * @param dateToValidate date to validate
     * @return
     */
    public static boolean validateDate(String dateToValidate) {
        if (dateToValidate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            try {
                Date date = sdf.parse(dateToValidate);
            } catch (ParseException e) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }
}