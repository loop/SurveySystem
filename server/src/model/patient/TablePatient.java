package model.patient;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Patient model for table views.
 */

public class TablePatient extends Patient {

    private final StringProperty propertyNHSNumber;
    private final StringProperty propertyFullName;
    private final BooleanProperty propertyIsAssigned;
    private Boolean orignalAssignment;

    public TablePatient(String nhsNumber, String first_name, String middle_name, String surname, String dateOfBirth, String postcode) {
        super(nhsNumber, first_name, middle_name, surname, dateOfBirth, postcode);

        propertyNHSNumber = new SimpleStringProperty(nhsNumber);
        propertyFullName = new SimpleStringProperty(getFirstName() + " " + getSurname());
        propertyIsAssigned = new SimpleBooleanProperty(false);
        orignalAssignment = false;
    }

    public String getPropertyNHSNumber() {
        return propertyNHSNumber.get();
    }

    public StringProperty propertyNHSNumberProperty() {
        return propertyNHSNumber;
    }

    public void setPropertyNHSNumber(String propertyNHSNumber) {
        this.propertyNHSNumber.set(propertyNHSNumber);
    }

    public String getPropertyFullName() {
        return propertyFullName.get();
    }

    public StringProperty propertyFullNameProperty() {
        return propertyFullName;
    }

    public void setPropertyFullName(String propertyFullName) {
        this.propertyFullName.set(propertyFullName);
    }

    public boolean getPropertyIsAssigned() {
        return propertyIsAssigned.get();
    }

    public BooleanProperty propertyIsAssignedProperty() {
        return propertyIsAssigned;
    }

    public void setPropertyIsAssigned(boolean propertyIsAssigned) {
        this.propertyIsAssigned.set(propertyIsAssigned);
    }

    public Boolean getOrignalAssignment() {
        return orignalAssignment;
    }

    public void setOrignalAssignment(Boolean orignalAssignment) {
        this.orignalAssignment = orignalAssignment;
    }

    @Override
    public String toString() {
        return super.toString() + "  isAssigned: " + ((getPropertyIsAssigned()) ? "true" : "false");
    }

}
