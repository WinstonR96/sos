package com.sostrackapp.Ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ArrayList<Integer> mSelectedIndices;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);    // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        setupFormats();
        mScannerView.setAutoFocus(true);
        List<BarcodeFormat> format = (List<BarcodeFormat>) mScannerView.getFormats();
        /*for (int i = 0; i < format.size(); i++) {
            Toast.makeText(getApplicationContext(), format.get(i).toString(), Toast.LENGTH_LONG).show();
        }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.i("Contenido", rawResult.getText()); // Prints scan results
        Log.i("Formato", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
        String contenido = rawResult.getText();
        String formato = rawResult.getBarcodeFormat().toString();

        Intent i = getIntent();
        i.putExtra("contenido", contenido);
        i.putExtra("formato", formato);
        setResult(RESULT_OK, i);
        // Finalizamos la Activity para volver a la anterior
        finish();
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);

    }


    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        /*if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < BarcodeFormat.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(BarcodeFormat.ALL_FORMATS.get(index));
        }*/
        formats.add(BarcodeFormat.PDF_417);
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.AZTEC);
        formats.add(BarcodeFormat.CODABAR);
        formats.add(BarcodeFormat.CODABAR);
        formats.add(BarcodeFormat.DATA_MATRIX);
        formats.add(BarcodeFormat.EAN_8);
        formats.add(BarcodeFormat.EAN_13);
        formats.add(BarcodeFormat.CODE_39);
        formats.add(BarcodeFormat.CODE_93);
        formats.add(BarcodeFormat.CODE_128);
        formats.add(BarcodeFormat.MAXICODE);
        if (mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }
}
