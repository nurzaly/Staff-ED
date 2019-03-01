package my.gov.ilpsdk.apps.stafed;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import my.gov.ilpsdk.apps.stafed.data.Constant;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _loginButton = findViewById(R.id.btn_login);
        _signupLink = findViewById(R.id.link_signup);
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apps.ilpsdk.gov.my/stafed/password/reset"));
                startActivity(browserIntent);
            }
        });
    }

    public void login() {
        //Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(true);



        String email = _emailText.getText().toString() + "@jtm.gov.my";
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        doLogin(email, password);


        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    private void doLogin(final String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_LOGIN)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        //Log.d(TAG, "onResponse: " + response.toString());
                        try {
                            if(response.get("status").equals("success")){
                                SharedPreferences.Editor editor = getSharedPreferences(Constant.KEY_CONFIG, MODE_PRIVATE).edit();
                                editor.putString(Constant.KEY_EMAIL, email);
                                editor.putBoolean(Constant.KEY_LOGIN, true);
                                editor.putString(Constant.KEY_NAMA,response.getJSONObject("data").getString("nama"));
                                editor.apply();
                                onLoginSuccess();
                            }
                            else{
                                onLoginFailed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onError: " + anError.getErrorDetail());
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        onLoginFailed();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        home();
        finish();
    }
    private void home(){
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString() + "@jtm.gov.my";
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
