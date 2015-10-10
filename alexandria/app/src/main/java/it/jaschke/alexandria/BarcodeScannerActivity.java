package it.jaschke.alexandria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import it.jaschke.alexandria.services.BookService;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler {

    public static final int SCAN_BOOK = 1;

    public static final String SCANNED_EAN_RESULT_EXTRA = "ean";

    public static void launchForScanResult(Fragment launchFragment){
        Intent activityIntent = new Intent(launchFragment.getActivity(), BarcodeScannerActivity.class);
        launchFragment.startActivityForResult(activityIntent, SCAN_BOOK);
    }

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }


    @Override
    public void onResume(){
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result scanResult){

        if(scanResult.getBarcodeFormat().equals(BarcodeFormat.EAN_13)){
            sendScanResultToBookService(scanResult.getText());
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SCANNED_EAN_RESULT_EXTRA, scanResult.getText());
            setResult(RESULT_OK, returnIntent);
            finish();
            return;
        }

        scannerView.startCamera();
    }


    private void sendScanResultToBookService(String ean){

        Intent bookIntent = new Intent(this, BookService.class);
        bookIntent.putExtra(BookService.EAN, ean);
        bookIntent.setAction(BookService.FETCH_BOOK);

        startService(bookIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_barcode_scanner, menu);
        return true;
    }

}
