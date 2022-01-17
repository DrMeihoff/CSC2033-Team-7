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
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

public class BarcodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;
    private String barcodeId = null;
    private DbHandler dbHandler = new DbHandler();
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
                        dbHandler.existsRecyclable(barcodeId, new DbHandler.OnGetDataListener() {
                            @Override
                            public void onSuccess(boolean exists) {
                                setUpButton(exists);
                            }
                        });
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



    private void setUpButton(boolean check) {
        EditText weight_edit = findViewById(R.id.weight_edit);
        EditText name_edit = findViewById(R.id.name_edit);
        EditText description_edit = findViewById(R.id.description_edit);
        Button addDbButton = findViewById(R.id.add_db_button);
        addDbButton.findViewById(R.id.add_db_button);
        addDbButton.setVisibility(View.VISIBLE);
        if(check) {
            addDbButton.setText("Add");
        }
        else {
            addDbButton.setText("Add to database");
        }
        addDbButton.setOnClickListener(v -> addTo(weight_edit, name_edit, description_edit, check));
    }


    private void addTo(EditText weight_edit, EditText name_edit, EditText description_edit, boolean check) {
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
        }
        else {
            Recyclable rec = new Recyclable(
                    barcodeId,
                    Math.round(Float.valueOf(weight_edit.getText().toString()) * 100) / 100,
                    name_edit.getText().toString(),
                    description_edit.getText().toString()
            );
            if(check) {
                dbHandler.addUserRecyclable(rec.getBarcodeId(), FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnSuccessListener(suc -> {
                    Toast.makeText(BarcodeActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(er -> {
                    Toast.makeText(BarcodeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                });
            }
            else {
                dbHandler.addRecyclable(rec).addOnSuccessListener(suc -> {
                    Toast.makeText(BarcodeActivity.this, "Added to the database successfully", Toast.LENGTH_SHORT).show();
                    setUpButton(true);
                }).addOnFailureListener(er -> {
                    Toast.makeText(BarcodeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                });
            }

        }
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