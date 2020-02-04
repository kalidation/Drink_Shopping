package com.example.drinkshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkshop.Model.RegisterResponse;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhone, editTextPassword;
    private Button btnlogin;
    private TextView textViewLogin;

    public long backSeconde;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.edit_text_phone_login);
        editTextPassword = findViewById(R.id.edit_text_password_login);
        textViewLogin = findViewById(R.id.text_view_login);
        btnlogin = findViewById(R.id.btn_login);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).getUser().getPhone() != "") {
            newToken();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void login() {
        String Phone = editTextPhone.getText().toString();
        String Password = editTextPassword.getText().toString();

        if (verifyField(Phone, Password)) {

            final AlertDialog dialog = new SpotsDialog(LoginActivity.this);
            dialog.show();
            dialog.setMessage("Wait Please ...");

            Call<RegisterResponse> call = RetrofitClient.getInstance()
                    .getApi()
                    .login(Phone, Password);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                    if (response.code() == 200) {
                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(LoginActivity.this).saveUser(response.body().getUser());
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    } else if (response.code() == 201) {

                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {

                        dialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                }
            });
        }
    }

    private boolean verifyField(String Phone, String Password) {

        if (Phone.isEmpty()) {
            editTextPhone.setError("Field Requiered");
            editTextPhone.requestFocus();
            return false;
        }

        if (!Patterns.PHONE.matcher(Phone).matches()) {
            editTextPhone.setError("Enter a Correct Phone Number");
            editTextPhone.requestFocus();
            return false;
        }

        if (Password.isEmpty()) {
            editTextPassword.setError("Field Requiered");
            editTextPassword.requestFocus();
            return false;
        }

        if (Password.length() < 6) {
            editTextPassword.setError("At Least 6 Charecters ");
            editTextPassword.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backSeconde + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            super.onBackPressed();
            return;
        } else {
            toast = Toast.makeText(this, "Please Click Back Again to Exit", Toast.LENGTH_SHORT);
            toast.show();
        }
        backSeconde = System.currentTimeMillis();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void newToken() {
        if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getPhone() != "") {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "There is an Error when getting Token", Toast.LENGTH_SHORT).show();
                            }
                            String token = task.getResult().getToken();
                            RetrofitClient.getInstance().getApi().isertToken(
                                    SharedPrefManager.getInstance(getApplicationContext()).getUser().getPhone(),
                                    token,
                                    "0"
                            ).enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d("onResponse", "onResponse: " + response.message());
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("onResponse", "onResponse: " + t.getMessage());
                                }
                            });
                        }
                    });
        }

    }


}
