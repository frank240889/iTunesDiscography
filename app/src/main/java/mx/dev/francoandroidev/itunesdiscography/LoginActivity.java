package mx.dev.francoandroidev.itunesdiscography;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.dev.francoandroidev.itunesdiscography.Utilities.StringUtilities;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    //SharedPreferences we use for saving data from app, this data is only accessible for this app.
    //needed for store current login state
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Dummy authentication
     */
    private static final String DUMMY_USER = "admin";

    private  static final String DUMMY_PASSWORD = "admin";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask asyncLogin = null;

    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private View progressView;
    private View loginFormView;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setup xml layout
        setContentView(R.layout.activity_login);

        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.email);

        passwordView = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                disableFields();
                attemptLogin();
            }
        });

        loginFormView = findViewById(R.id.login_form);
        progressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (asyncLogin != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();


        // Check for a valid username.
        if(StringUtilities.isFieldEmpty(email)) {
            enableFields();
            emailView.setError(getString(R.string.error_field_required));
            emailView.requestFocus();
            return;
        }

        // Check for a valid password.
        if(StringUtilities.isFieldEmpty(password)) {
            enableFields();
            passwordView.setError(getString(R.string.error_field_required));
            passwordView.requestFocus();
            return;
        }

        //we should make all network request in asynchronous way
        //because they can take few seconds and the  main process could
        //block our UI
        asyncLogin = new UserLoginTask(email, password);
        asyncLogin.execute();

    }


    private void enableFields(){
        emailView.setEnabled(true);
        passwordView.setEnabled(true);
        loginButton.setEnabled(true);

    }

    private void disableFields(){
        emailView.setEnabled(false);
        passwordView.setEnabled(false);
        loginButton.setEnabled(false);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            loginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String email;
        private final String password;
        private boolean correctPassword = false;
        private boolean correctUser = false;
        private Toast toast;

        UserLoginTask(String email, String password) {
            this.email = email;
            this.password = password;
        }
        @Override
        protected void onPreExecute(){

            toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.setText(getString(R.string.autenticating));
            toast.show();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            //verify credentials
            correctPassword = DUMMY_PASSWORD.equals(password);
            correctUser = DUMMY_USER.equals(email);

            if(correctPassword && correctUser) {
                return true;
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            asyncLogin = null;



            if (success) {
                toast.setText(R.string.success_login);
                toast.show();
                //save status as logged in
                sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn",true);
                editor.apply();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {
                showProgress(false);
                toast.setText(R.string.error_incorrect_user_or_password);
                enableFields();
                toast.show();
            }
        }

        @Override
        protected void onCancelled() {
            asyncLogin = null;
            showProgress(false);
        }
    }
}

