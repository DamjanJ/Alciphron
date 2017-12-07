package rs.org.habiprot.alciphron.alci;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.zerocodeteam.network.ZctNetwork;
import com.zerocodeteam.network.ZctResponse;

import java.util.HashMap;
import java.util.Map;

import rs.org.habiprot.alciphron.alci.common.AppUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private Button mEmailSignInButton;
    private ZctResponse<String> mLoginResponse;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private String musername,mpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.checkBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (saveLogin == true) {
            mEmailView.setText(loginPreferences.getString("username", ""));
            mPasswordView.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        // (Milorad) Trebalo bi sve da ide preko ovih funkcija i da se smanji kod sto je vise moguce
        setupViews();
        setupListeners();

        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    public void setupViews() {
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
    }
    public void setupListeners() {
        prepareListeners();
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void prepareListeners() {

        mLoginResponse = new ZctResponse<String>() {
            @Override
            public void onSuccess(String data, Map<String, String> responseHeaders, Object cookie) {
                showProgress(false);

                for (String s : responseHeaders.keySet()) {
                    Log.e("Header  ", s + ":" + responseHeaders.get(s));
                }
                Log.e("Response data  ", data);


                if(responseHeaders.get("refresh") == null || !responseHeaders.get("refresh").contains("/unos")) {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Log.e("Set-Cookie: ", responseHeaders.get("Set-Cookie"));
                Log.e("Login Response ", "Login Successful");
                App.getInstance().setCookie(responseHeaders.get("Set-Cookie"));
                AppUtils.go2MainActivity(LoginActivity.this);
            }

            @Override
            public void onError(VolleyError error, ZctNetwork.ErrorType type, Map<String, String> responseHeaders, Object cookie) {
                App.getInstance().setCookie(null);
                showProgress(false);

                Toast.makeText(getApplicationContext(), "Login unsuccessful", Toast.LENGTH_LONG).show();
            }
        };
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // custom <code></code>

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);

        musername = mEmailView.getText().toString();
        mpassword = mPasswordView.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", musername);
            loginPrefsEditor.putString("password", mpassword);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }


        //custom <code></code>

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);




            HashMap<String, String> fields = new HashMap<>();
            fields.put("password", password);
            fields.put("identity", email);

            API.getInstance().loginUser(mLoginResponse, fields);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

