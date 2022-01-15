package com.csc.plastictracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirm;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button buttonConfirmReg = findViewById(R.id.buttonConfirmReg);
        buttonConfirmReg.setOnClickListener(v -> registerUser());
        email = findViewById(R.id.textRegEmail);
        password = findViewById(R.id.textRegPassword);
        confirm = findViewById(R.id.textConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            returnToMain();
        }
    }

    public void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registerUser() {
        if (password.getText().toString().equals(confirm.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnFailureListener(e -> {
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(RegisterActivity.this,
                                    "Password is not strong enough. Please increase the length and/or complexity.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(RegisterActivity.this,
                                    "Please enter a valid email address.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterActivity.this,
                                    "This email address is already in use.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "An unknown error has occurred: "+e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "An unknown error has occurred due to Firebase.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
    }
}
