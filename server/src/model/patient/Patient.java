package model.patient;

/**
 * This class is the patient object.
 */

public class Patient
{
    private final String nhsNumber;
    private final String firstName;
    private final String middleName;
    private final String surname;
    private final String dateOfBirth;
    private final String postcode;

    public Patient(String nhsNumber, String firstName, String middleName, String surname, String dateOfBirth, String postcode)
    {
        this.nhsNumber = nhsNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.postcode = postcode;
    }

    public String getNhsNumber()
    {
        return nhsNumber;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public String getSurname()
    {
        return surname;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public String getPostcode()
    {
        return postcode;
    }
}