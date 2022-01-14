package com.csc.plastictracker;

import static android.widget.TextView.BufferType.EDITABLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class BarcodeActivity extends AppCompatActivity {

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(v-> {

        });

        final EditText barcode_id_edit = findViewById(R.id.barcode_id_edit);
        final EditText weight_edit = findViewById(R.id.weight_edit);
        final EditText name_edit = findViewById(R.id.name_edit);
        final EditText description_edit = findViewById(R.id.description_edit);

        CodeScannerView scannerView = findViewById(R.id.barcode_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarcodeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                        barcode_id_edit.setText(result.getText(), EDITABLE);
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