package com.yogeshn.fyp.androidclient.activities;

import java.util.LinkedList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yogeshn.fyp.R;
import com.yogeshn.fyp.androidclient.backend.connectivity.SocketAPI;
import com.yogeshn.fyp.androidclient.backend.model.json.QuestionnaireJSON;

/**
 * This class is resposible for loading the list of avaible questionnaires and the tutorial
 * questionnaire.
 *
 * Cross fading views and animation originally by Android:
 * http://developer.android.com/training/animation/crossfade.html
 */

@SuppressWarnings({"WeakerAccess", "UnusedParameters"})
public class AvailableQuestionnairesActivity extends Activity {
    protected List<QuestionnaireJSON> listofQuestionnaires;
    int animationTimeMedium = 0;
    protected GetPatientQuestionnairesAsyncTask getPatientQuestionnairesAsyncTask;
    protected static String questionnaireID = "";
    protected View noQuestionnairesView, questionnaireAvailableView, loadingView;
    protected TextView availiableQuestionnairesTextView, noQuestionnairesTextView;
    protected ListView avalibaleQuestionnaireListView;


    /**
     * Creates the and intialises the views and the lists needed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_questionnaires);

        ImageView spinner = (ImageView) findViewById(R.id.spinnerAvailableQuestionnaires);
        spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));

        noQuestionnairesView = findViewById(R.id.noQuestionnaireAvailable);
        availiableQuestionnairesTextView = (TextView) findViewById(R.id.availableQuestionnairesTV);
        avalibaleQuestionnaireListView = (ListView) findViewById(R.id.availableQuestionnairesList);
        questionnaireAvailableView = findViewById(R.id.questionnairesAvailable);
        loadingView = findViewById(R.id.waitAvailableQuestionnaires);
        noQuestionnairesTextView = (TextView) findViewById(R.id.noQuestionnaires);
        animationTimeMedium = getResources().getInteger(android.R.integer.config_mediumAnimTime);

    }

    /**
     * Upon starting the activity it will retrive the questionnaires aviable to the patient.
     */
    @Override
    public void onStart() {
        super.onStart();

        findViewById(R.id.noQuestionnaireAvailable).setVisibility(View.GONE);
        findViewById(R.id.questionnairesAvailable).setVisibility(View.GONE);
        findViewById(R.id.waitAvailableQuestionnaires).setVisibility(View.VISIBLE);

        getPatientQuestionnairesAsyncTask = new GetPatientQuestionnairesAsyncTask();
        getPatientQuestionnairesAsyncTask.execute();
    }

    /**
     * Starts the tutorial questionnaire activity.
     *
     * @param view
     */
    public void openTutorial(View view) {
        startActivity(new Intent(this, TutorialQuestionnaireActivity.class));
        finish();
    }

    /**
     * Sends request to server to get the list of questionnaires. If network error
     * occurs it will show error dialog and reload the login activity.
     *
     * @param nhsNumber NHS number to get questionnaires for
     * @return list of questionnaire pointers.
     */
    protected List<QuestionnaireJSON> getPatientQuestionnaires(String nhsNumber) {
        String serverReply = SocketAPI.getQuestionnairesForPatient(nhsNumber);
        if (!serverReply.equals(SocketAPI.SOCKET_TIMEOUT_EXCEPTION)) {
            serverReply = "{\"pointers\":" + serverReply + "}";
            return new Gson().fromJson(serverReply, QuestionnaireJSON.class).getPointers();
        } else {
            Intent i = new Intent(this, ConnectActivity.class);
            i.putExtra(ConnectActivity.SAME_ACTIVITY, true).putExtra(ConnectActivity.NO_CONNECTION_DIALOG, true);
            startActivity(i);
            onStop();
            return new LinkedList<>();
        }
    }

    /**
     * Loads the apporiate view if questionnaies are aaible else it will
     * show no questionnaires aviable text.
     *
     * @param found boolean to indicate questionnaires are avaiable
     */
    protected void areQuestionnairesAvalible(boolean found) {
        loadingView.setVisibility(View.VISIBLE);

        if (!found) {
            noQuestionnairesView.setVisibility(View.VISIBLE);

            loadingView.animate().setDuration(animationTimeMedium)
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadingView.setVisibility(View.GONE);
                        }
                    });

            noQuestionnairesView.animate().setDuration(animationTimeMedium)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            noQuestionnairesView.setVisibility(View.VISIBLE);
                        }
                    });

            noQuestionnairesTextView.setText(getResources().getString(R.string.no_available_questionnaires));
        } else {
            questionnaireAvailableView.setVisibility(View.VISIBLE);

            loadingView.animate().setDuration(animationTimeMedium)
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadingView.setVisibility(View.GONE);
                        }
                    });

            questionnaireAvailableView.animate().setDuration(animationTimeMedium)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            questionnaireAvailableView.setVisibility(View.VISIBLE);
                        }
                    });

            availiableQuestionnairesTextView.setText(getResources().getString(R.string.questionnaires_available));

            avalibaleQuestionnaireListView.setAdapter(new AvailableQuestionnairesAdapter(this, listofQuestionnaires));
            avalibaleQuestionnaireListView.setOnItemClickListener((parent, view, position, id) -> {
                questionnaireID = "" + listofQuestionnaires.get(position).getId();
                startActivity(new Intent(AvailableQuestionnairesActivity.this, QuestionActivity.class));
                finish();
            });
        }
    }

    /**
     * Questionnaire ID getter
     * @return questionnaire ID
     */
    public static String getQuestionnaireID() {
        return questionnaireID;
    }

    /**
     * If back button is pressed it will take it to the patient login activity.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginPatientDetailActivity.class);
        startActivity(intent);
    }

    /**
     * Close action that will take it to the patient login activity.
     *
     * @param view
     */
    public void close(View view) {
        Intent intent = new Intent(this, LoginPatientDetailActivity.class);
        startActivity(intent);
    }

    /**
     * This class is resposible for retriveing questionnaires from the server and is executed
     * in the background once login has been successful and updates the AvaibleQuestionnairesActivity
     * views accordingly.
     */
    public class GetPatientQuestionnairesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            listofQuestionnaires = getPatientQuestionnaires(LoginPatientDetailActivity.getPatientNHSNumber());
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            areQuestionnairesAvalible(listofQuestionnaires.size() == 0 ? false : true);
            getPatientQuestionnairesAsyncTask = null;
        }

        @Override
        protected void onCancelled() {
            getPatientQuestionnairesAsyncTask = null;
        }
    }
class AvailableQuestionnairesAdapter extends ArrayAdapter<QuestionnaireJSON> {
        Context context;
        List<QuestionnaireJSON> pointers;

        public AvailableQuestionnairesAdapter(Context context, List<QuestionnaireJSON> pointers) {
            super(context, R.layout.layout_dynamic_list_view_row, pointers);
            this.pointers = pointers;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.layout_dynamic_list_view_row, parent, false);

            TextView pointer = (TextView) view.findViewById(R.id.dynamic_list_row);
            pointer.setText(pointers.get(position).getTitle());
            pointer.setTextColor(context.getResources().getColorStateList(R.color.white));

            view.setBackground(context.getResources().getDrawable(R.color.button_background_color));

            return view;
        }
    }
}