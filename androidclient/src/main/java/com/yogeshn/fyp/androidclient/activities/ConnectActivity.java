package com.yogeshn.fyp.androidclient.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.yogeshn.fyp.androidclient.backend.connectivity.NetworkThread;
import com.yogeshn.fyp.androidclient.backend.connectivity.SocketAPI;
import com.yogeshn.fyp.androidclient.backend.filegeneration.AnswerFile;
import com.yogeshn.fyp.R;

/**
 * Activity which displays a connect button when tapped will communicate and connect
 * to the desktop server.
 *
 * @author Android
 */
@SuppressWarnings({"unchecked", "CanBeFinal", "UnusedParameters"})
public class ConnectActivity extends Activity {

    public static final String NO_CONNECTION_DIALOG = "NO_CONNECTION_DIALOG";
    public static final String SAME_ACTIVITY = "SAME_ACTIVITY";

    protected boolean activityRunning = false;
    protected Context context = this;
    protected ServerConnectTask connectTask = null;
    protected TextView mLoginStatusMessageView;
    protected ImageView spinner;
    protected View loginStatusView;
    protected View connectView;

    /**
     * Upon creation of actity, it instantiates the UI elements and starts the network thread
     * to retrive the server IP in the background.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
        spinner = (ImageView) findViewById(R.id.spinnerLogin);
        loginStatusView = findViewById(R.id.login_status);
        connectView = findViewById(R.id.login_form);

        new NetworkThread().execute();

    }

    /**
     * When the applicaiton is restarted it will check if activity is running and if it is not
     * it will show a no connection dilog and try to run the activity again.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (activityRunning) {
            return;
        }
        if (getIntent().getBooleanExtra(NO_CONNECTION_DIALOG, false))
            showNoConnectionDialog();
        activityRunning = true;
    }

    /**
     * Hides the login button once tapped and shows a loading animation.
     *
     * @param isProgressShowing hide or show the loading screen
     */
    protected void showProgress(final boolean isProgressShowing) {
        hideConnectionButton(isProgressShowing);
        loadLoadingStatusAnimation(isProgressShowing);
    }

    /**
     * Fades the connect button in to the background.
     *
     * @param isProgressShowing hide or show the connect button
     */
    protected void hideConnectionButton(final boolean isProgressShowing) {
        connectView.setVisibility(View.VISIBLE);
        connectView.animate().setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime))
                .alpha(isProgressShowing ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        connectView.setVisibility(isProgressShowing ? View.GONE : View.VISIBLE);
                    }
                });
    }

    /**
     * Loads the loading graphic and adds a circular animation to represent loading.
     *
     * @param isProgressShowing hide or show the loading animation
     */
    protected void loadLoadingStatusAnimation(final boolean isProgressShowing) {
        spinner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading_bar));
        loginStatusView.setVisibility(View.VISIBLE);
        loginStatusView.animate().setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime))
                .alpha(isProgressShowing ? 1 : 0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginStatusView.setVisibility(isProgressShowing ? View.VISIBLE : View.GONE);
                    }
                });
    }

    /**
     * Builds the no connectiong pop up and displays it.
     */
    protected void showNoConnectionDialog() {
        Builder builder = new Builder(context);
        builder.setMessage(context.getResources().getString(R.string.no_server_connection));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Connect action that is executed when the connect button is pressed.
     * Displays message and shows the connectiong progress and execited the
     * connectiong task.
     *
     * @param view
     */
    public void attemptLogin(View view) {
        mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
        showProgress(true);

        connectTask = new ServerConnectTask();
        connectTask.execute((Void) null);
    }

    /**
     * Returns the desktop server IP address.
     *
     * @return server IP address
     */
    public static String getServerIP() {
        return NetworkThread.ipAddress;
    }

    /**
     * This class is responsible to run in the background when connect is pressed and
     * it checks the connection with the server is working. Once it has establised a
     * connection it will load the patient login details activity. If connection is
     * not avaible it will show the no connection dialog.
     */
    protected class ServerConnectTask extends AsyncTask<Void, Void, Boolean> {

        /**
         * Checks the connection.
         *
         * @param params
         * @return true if connected.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            String s = SocketAPI.checkServerConnection();
            if (s.equals(SocketAPI.SOCKET_TIMEOUT_EXCEPTION)) {
                new Thread() {
                    public void run() {
                        ConnectActivity.this.runOnUiThread(ConnectActivity.this::showNoConnectionDialog);
                    }


                }.start();
                return false;
            }
            return s.contains("true");
        }

        /**
         * Loads the login patient details screen if connection is successful.
         *
         * @param success connection successful or not
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            connectTask = null;
            showProgress(false);

            if (success) {
                if (!getIntent().getBooleanExtra(SAME_ACTIVITY, false)) {
                    startActivity(new Intent(ConnectActivity.this, LoginPatientDetailActivity.class));
                }
                finish();
                activityRunning = false;
                AnswerFile.sendFileToServer(context);
            }
        }

        /**
         * Resets the connection task.
         */
        @Override
        protected void onCancelled() {
            connectTask = null;
            showProgress(false);
        }
    }
}