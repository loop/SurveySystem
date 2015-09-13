package com.yogeshn.fyp.androidclient.backend.model.json;

/**
 * JSON template for Patient object.
 *
 */

public class PatientJSON
{
    private String NHS;
    private String firstName;
    private String middleName;
    private String surname;
    private String dateOfBirth;
    private String postcode;

    /**
     * Getter for NHS number.
     *
     * @return nhs number
     */
    public String getNHS() {
        return NHS;
    }

    /**
     * Getter for first name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter for middle name.
     *
     * @return middle name
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Getter for surname.
     *
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Getter for date of birth.
     *
     * @return date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Getter for postcode.
     *
     * @return postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Getter for full name combined.
     *
     * @return full name
     */
    public String getName(){
        return middleName.equals("") ? firstName + " " + surname : firstName + " " + middleName + " " + surname;
    }
}
