package com.csc.plastictracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class LandingScreenActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private Button buttonGraph;
    private Button buttonScanner;
    private Button buttonLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingscreen);

        drawerLayout=findViewById(R.id.my_drawer_layout);

        //listener for the barcode scanner button
        //checks for camera permission before attempting to openBarcode
        buttonScanner = findViewById(R.id.buttonScanner);
        buttonScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(LandingScreenActivity.this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LandingScreenActivity.this, new String[] {Manifest.permission.CAMERA},
                            50); }
                else {
                    openBarcode();
                }
            }
        });

        //listeners for buttons
        buttonLogout=findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(v -> logout());
        buttonGraph=findViewById(R.id.buttonGraph);
        buttonGraph.setOnClickListener(v -> openGraph());

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //methods for changing activity
    private void openGraph() {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }
    private void openBarcode(){
        Intent intent = new Intent(this, BarcodeActivity.class);
        startActivity(intent);
    }
    private void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(LandingScreenActivity.this, "Logged out.", Toast.LENGTH_SHORT).show();
            openMain();
        } else {
            Toast.makeText(LandingScreenActivity.this, "Something went wrong. Please try again."
                    , Toast.LENGTH_SHORT).show();
        }
    }


    // override the onOptionsItemSelected()
    // function to implement
    // the item click listener callback
    // to open and close the navigation
    // drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
