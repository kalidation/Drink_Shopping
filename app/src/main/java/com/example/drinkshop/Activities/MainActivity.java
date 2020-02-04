package com.example.drinkshop.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drinkshop.Model.RegisterResponse;
import com.example.drinkshop.Model.User;
import com.example.drinkshop.R;
import com.example.drinkshop.Retrofit.RetrofitClient;
import com.example.drinkshop.Storage.SharedPreferences.SharedPrefManager;
import com.example.drinkshop.Utils.Common;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1001;
    private EditText editTextPhone, editTextPassword, editTextName, editTextBrth, editTextAddress;
    private Button btnRegister;
    private TextView textViewRegister;

    public long backSeconde;
    public Toast toast;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSION);
        }


        editTextPhone = findViewById(R.id.edit_text_phone_register);
        editTextPassword = findViewById(R.id.edit_text_password_register);
        editTextName = findViewById(R.id.edit_text_name_register);
        editTextBrth = findViewById(R.id.edit_text_brthd_register);
        editTextAddress = findViewById(R.id.edit_text_Address_register);
        textViewRegister = findViewById(R.id.text_view_register);

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).getUser().getPhone() != "") {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    public void createUser() {

        String Phone = editTextPhone.getText().toString();
        String Password = editTextPassword.getText().toString();
        String Name = editTextName.getText().toString();
        String Birth = editTextBrth.getText().toString();
        String Address = editTextAddress.getText().toString();

        if (verifyField(Phone, Password, Name, Birth, Address)) {

            final AlertDialog dialog = new SpotsDialog(MainActivity.this);
            dialog.show();
            dialog.setMessage("Wait Please ...");
            Call<RegisterResponse> call = RetrofitClient.getInstance().getApi().register(Phone, Password, Name, Birth, Address);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                    if (response.code() == 200) {

                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        SharedPrefManager.getInstance(MainActivity.this).saveUser(response.body().getUser());
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();

                    } else if (response.code() == 201) {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {

                }
            });

        }

    }

    private boolean verifyField(String Phone, String Password, String Name, String Birth, String Address) {

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

        if (Name.isEmpty()) {
            editTextName.setError("Field Requiered");
            editTextName.requestFocus();
            return false;
        }

        if (Birth.isEmpty()) {
            editTextBrth.setError("Field Requiered");
            editTextBrth.requestFocus();
            return false;
        }
        if (Address.isEmpty()) {
            editTextAddress.setError("Field Requiered");
            editTextAddress.requestFocus();
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

}
