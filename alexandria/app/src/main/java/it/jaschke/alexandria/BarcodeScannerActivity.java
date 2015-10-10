package it.jaschke.alexandria;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.jar.Attributes;

import it.jaschke.alexandria.services.BookService;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeScannerActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler {

    public static void launch(Context launchContext){
        Intent activityIntent = new Intent(launchContext, BarcodeScannerActivity.class);
        launchContext.startActivity(activityIntent);
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
        Log.d("SCAN_RESULTS", scanResult.getText() + "," + scanResult.getBarcodeFormat().toString());

        if(scanResult.getBarcodeFormat().equals(BarcodeFormat.EAN_13)){
            sendScanResultToBookService(scanResult.getText());
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
