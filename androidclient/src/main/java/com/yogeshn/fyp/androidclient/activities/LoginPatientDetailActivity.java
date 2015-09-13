package com.yogeshn.fyp.androidclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.connectivity.SocketAPI;
import com.yogeshn.fyp.androidclient.backend.model.json.PatientJSON;

/**
 * This activity displays the patient login and transforms into the patient details
 * and creates accessbility choices.
 * <p>
 */
@SuppressWarnings({"UnnecessaryReturnStatement", "ConstantConditions"})
public class LoginPatientDetailActivity extends Activity {

    protected PatientDetailsTask task = null;
    protected PatientJSON patient = null;
    protected EditText nhs;
    protected TextView name, dob, postcode;
    protected static String patientName = "";
    protected static String patientNHSNumber = "";

    /**
     * Instantiates the UI elements.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);

        name = (TextView) findViewById(R.id.patient_name);
        dob = (TextView) findViewById(R.id.patient_dob);
        postcode = (TextView) findViewById(R.id.patient_postcode);
        nhs = (EditText) findViewById(R.id.patientNHS);
    }

    /**
     * Nullifies the NHS number if the app is restarted so user has to
     * login again for security purposes.
     */
    @Override
    public void onStart() {
        super.onStart();
        nhs.setError(null);
    }

    /**
     * Patient login action that validates the NHS number.
     *
     * @param v
     */
    public void getPatientClick(View v) {
        validateNHS();
        hideKeyboard();
    }

    /**
     * Confirm button action that starts the avaible quesitonnaires acivity
     * and sets up the images needed if different accessbility is selected.
     *
     * @param v
     */
    public void confirmPatientDetailsClick(View v) {
        startActivity(new Intent(this, AvailableQuestionnairesActivity.class));
        finish();
        getDisability();
    }

    /**
     * Hides the soft keyboard for UX purposes.
     */
    protected void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * Validates the NHS patient number.
     */
    protected void validateNHS() {
        patientNHSNumber = nhs.getText().toString();

        if (!patientNHSNumber.matches("^\\d{10}$")) {
            nhs.setError(getString(R.string.incorrect_nhs_format));
        } else {
            showLoadingScreen(true);
            task = new PatientDetailsTask();
            task.execute(patientNHSNumber);
        }
    }

    /**
     * Sends a request to the server to check patient NHS number and get the patient details.
     * If connection error it will reload the main connection screen to reconnect.
     *
     * @param NHS patient NHS number
     * @return The patient object if found else null
     */
    protected PatientJSON findPatient(String NHS) {
        Gson gson = new Gson();
        String patient = SocketAPI.findPatient(NHS);

        if (!patient.contains("error")) {
            switch (patient) {
                case SocketAPI.SOCKET_TIMEOUT_EXCEPTION:
                    Intent i = new Intent(this, ConnectActivity.class);
                    i.putExtra(ConnectActivity.NO_CONNECTION_DIALOG, true);
                    i.putExtra(ConnectActivity.SAME_ACTIVITY, false);
                    startActivity(i);
                    onStop();
                    return null;
            }
        } else {
            return null;
        }
        return gson.fromJson(patient, PatientJSON.class);
    }

    /**
     * Shows the loading screen between showing the NHS patient view.
     *
     * @param load true to load the pateitn login loading screen
     */
    protected void showLoadingScreen(final boolean load) {
        int animTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        ImageView spinner = (ImageView) findViewById(R.id.spinnerPatientDetail);
        spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));

        final View loginStatusView = findViewById(R.id.login_status_patient);
        loginStatusView.setVisibility(View.VISIBLE);
        loginStatusView.animate().setDuration(animTime)
                .alpha(load ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginStatusView.setVisibility(load ? View.VISIBLE : View.GONE);
                    }
                });

        final View searchForm = findViewById(R.id.search_form);
        searchForm.setVisibility(View.VISIBLE);
        searchForm.animate().setDuration(animTime)
                .alpha(load ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchForm.setVisibility(load ? View.GONE : View.VISIBLE);
                    }
                });
    }

    /**
     * Shows the loading screen between showing the patient details patient view.
     *
     * @param patient the patient details to display on screen
     */
    protected void PatientDetailsAnimation(final PatientJSON patient) {
        int animationTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

        final View patientDetails = findViewById(R.id.patient_details);
        patientDetails.setVisibility(View.VISIBLE);
        patientDetails.animate().setDuration(animationTime)
                .alpha(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        patientDetails.setVisibility(View.VISIBLE);
                        name.setText(patient.getName());
                        patientName = patient.getName();
                        dob.setText(patient.getDateOfBirth());
                        postcode.setText(patient.getPostcode());
                    }
                });

        final View loginStatusView = findViewById(R.id.login_status_patient);
        loginStatusView.setVisibility(View.VISIBLE);
        loginStatusView.animate().setDuration(animationTime)
                .alpha(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginStatusView.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * Clears the patient data.
     */
    public static void clearPatientData() {
        patientName = "";
        patientNHSNumber = "";
    }

    /**
     * Getter for patient name.
     *
     * @return patient name
     */
    public static String getPatientName() {
        return patientName;
    }

    /**
     * Getter for NHS number.
     *
     * @return NHS number
     */
    public static String getPatientNHSNumber() {
        return patientNHSNumber;
    }

    /**
     * Getters the disability options chosen by the user and sets up the relevant
     * XML/images needed to enable the option.
     */
    protected void getDisability() {
        RadioGroup g = (RadioGroup) findViewById(R.id.disability);
        switch (g.getCheckedRadioButtonId()) {
            case R.id.no_dissability:
                return;
            case R.id.high_contrast_mode:
                QuestionActivity.setHighContrastModeFlag(true);
                break;
            default:
                return;
        }
    }

    /**
     * Back button press will just restart the current login activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * This AsynTask ensures that the loading screens matches the data flow from
     * the server.
     */
    @SuppressWarnings("WeakerAccess")
    public class PatientDetailsTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... args) {
            patient = findPatient(args[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if (patient != null) {
                PatientDetailsAnimation(patient);
            } else {
                nhs.setError(getString(R.string.no_patient_found));
                showLoadingScreen(false);
            }
            task = null;
        }

        @Override
        protected void onCancelled() {
            task = null;
            showLoadingScreen(false);
        }
    }
}