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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonLogout;
    private Button buttonGraph;

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

        buttonGraph=findViewById(R.id.buttonGraph);
        buttonGraph.setOnClickListener(v -> openGraph());
    }

    //updates buttons when returning to main activity.
    @Override
    protected void onStart() {
        super.onStart();
        updateAuthButtons();
    }

    //opening different activities
    private void openBarcode() {
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivity(intent);
    }
    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void openRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void openGraph() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    //signs current user out of Firebase, checks to make sure the sign out was successful, then updates buttons.
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(MainActivity.this, "Logged out.", Toast.LENGTH_SHORT).show();
            updateAuthButtons();
        } else {
            Toast.makeText(MainActivity.this, "Something went wrong. Please try again."
                    , Toast.LENGTH_SHORT).show();
        }

    }

    //if no user is currently logged in, hides logout button, shows login & register.
    //if a user is logged in, hides login/register, shows logout.
    private void updateAuthButtons() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            buttonRegister.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.INVISIBLE);
        } else {
            buttonRegister.setVisibility(View.INVISIBLE);
            buttonLogin.setVisibility(View.INVISIBLE);
            buttonLogout.setVisibility(View.VISIBLE);
        }
    }
}