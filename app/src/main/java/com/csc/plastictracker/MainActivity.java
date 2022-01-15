package com.csc.plastictracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA},
                            50); }
                else {
                    openBarcode();
                }
            }
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(v -> openLogin());buttonLogin.setOnClickListener(v -> openLogin());

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(v -> openRegister());

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logout());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            buttonRegister.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.INVISIBLE);
        } else {
            buttonRegister.setVisibility(View.INVISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
        }
    }

    public void openBarcode() {
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivity(intent);
    }

    public void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}