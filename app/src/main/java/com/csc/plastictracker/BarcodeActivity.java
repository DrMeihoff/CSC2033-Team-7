package com.csc.plastictracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

public class BarcodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private String barcodeId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        initBarcodeScanner();
    }

    protected void initBarcodeScanner() {
        CodeScannerView scannerView = findViewById(R.id.barcode_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarcodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        barcodeId = result.getText();
                        checkIfExists();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    protected void setUpButton(boolean check) {
        EditText weight_edit = findViewById(R.id.weight_edit);
        EditText name_edit = findViewById(R.id.name_edit);
        EditText description_edit = findViewById(R.id.description_edit);
        Button addDbButton = findViewById(R.id.add_db_button);
        addDbButton.findViewById(R.id.add_db_button);
        addDbButton.setVisibility(View.VISIBLE);
        if(check) {
            addDbButton.setText("Add to database");
            addDbButton.setOnClickListener(addToDb(weight_edit, name_edit, description_edit));
        }
        else {
            addDbButton.setText("Add");
            addDbButton.setOnClickListener(addToUser(weight_edit, name_edit, description_edit));
        }
    }

    protected View.OnClickListener addToUser(EditText weight_edit, EditText name_edit, EditText description_edit) {
        //TODO: do this
        return null;
    }

    protected View.OnClickListener addToDb(EditText weight_edit, EditText name_edit, EditText description_edit) {
        if (TextUtils.isEmpty(barcodeId)
                || TextUtils.isEmpty(weight_edit.getText().toString())
                || TextUtils.isEmpty(name_edit.getText().toString())
                || TextUtils.isEmpty(description_edit.getText().toString())) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BarcodeActivity.this, "Empty field is not allowed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            FBRecyclable fbr = new FBRecyclable();
            Recyclable rec = new Recyclable(
                    barcodeId,
                    Math.round(Float.valueOf(weight_edit.getText().toString()) * 100) / 100,
                    name_edit.getText().toString(),
                    description_edit.getText().toString()
            );
            fbr.add(rec).addOnSuccessListener(suc -> {
                Toast.makeText(BarcodeActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(er -> {
                Toast.makeText(BarcodeActivity.this, "Added unsuccessfully", Toast.LENGTH_SHORT).show();
            });
        }
        return null;
    }

    protected void checkIfExists() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://plastic-tracker-bfb8b-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("barcodeId");
        ref.orderByChild("barcodeId").equalTo(barcodeId).addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                setUpButton(dataSnapshot.exists());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}