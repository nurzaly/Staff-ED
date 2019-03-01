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

import my.gov.ilpsdk.apps.stafed.Utils.Helper;
import my.gov.ilpsdk.apps.stafed.data.Constant;

public class ActivityChangePassword extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    EditText _oldPassword;
    EditText _newPassword;
    EditText _confirmPassword;
    Button _submitButton;
    SharedPreferences config;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        _submitButton = findViewById(R.id.btn_login);
        _oldPassword = findViewById(R.id.et_old_password);
        _newPassword = findViewById(R.id.et_new_password);
        _confirmPassword = findViewById(R.id.et_confirm_password);

        config = getSharedPreferences(Constant.KEY_CONFIG, 0);

        _submitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {
        //Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Password not valid");
            return;
        }

        _submitButton.setEnabled(true);



        String old_password = _oldPassword.getText().toString();
        String new_password = _newPassword.getText().toString();
        String confirm_password = _confirmPassword.getText().toString();

        // TODO: Implement your own authentication logic here.
        doChangePassword(old_password, new_password);


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

    private void doChangePassword(String old_password, String new_password) {
        final ProgressDialog progressDialog = new ProgressDialog(ActivityChangePassword.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_CHANGE_PASSWORD)
                .addBodyParameter("email", config.getString(Constant.KEY_EMAIL,null))
                .addBodyParameter("old_password", old_password)
                .addBodyParameter("new_password", new_password)
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
                                editor.putBoolean(Constant.KEY_LOGIN, false);
                                editor.apply();
                                onLoginSuccess();
                            }
                            else{
                                onLoginFailed(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                        onLoginFailed("Connection error");
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
        //moveTaskToBack(true);
        super.onBackPressed();
    }

    public void onLoginSuccess() {
        _submitButton.setEnabled(true);
        //home();
        Helper.showToast(getApplicationContext(),"Success change password");
        finish();
    }
    private void home(){
        Intent intent = new Intent(this, ActivityMain.class);
        startActivity(intent);
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), "Failed Change Password. " + message, Toast.LENGTH_LONG).show();

        _submitButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String new_password = _newPassword.getText().toString();
        String confirm_password = _confirmPassword.getText().toString();

        if (new_password.isEmpty() || new_password.length() < 6) {
            _newPassword.setError("password not valid");
            valid = false;
        } else {
            _newPassword.setError(null);
        }

        if (!new_password.equals(confirm_password)) {
            _confirmPassword.setError("password not match");
            valid = false;
        } else {
            _confirmPassword.setError(null);
        }

        return valid;
    }
}
