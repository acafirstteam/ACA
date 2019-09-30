package am.newway.aca;

import android.graphics.Bitmap;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.newway.aca.database.Firestore;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    private final String TAG = getClass().getSimpleName();
    protected Firestore FIRESTORE;

    public BaseActivity (  ) {
        FIRESTORE =  Firestore.getInstance();
    }

    public void scanBarcode( View view) {
        new IntentIntegrator(this).initiateScan();
    }

    public void scanBarcodeWithCustomizedRequestCode(View view) {
        new IntentIntegrator(this).setRequestCode(CUSTOMIZED_REQUEST_CODE).initiateScan();
    }

    public void scanBarcodeInverted(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra( Intents.Scan.SCAN_TYPE, Intents.Scan.INVERTED_SCAN);
        integrator.initiateScan();
    }

    public void scanMixedBarcodes(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN);
        integrator.initiateScan();
    }

    public void scanPDF417(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PDF_417);
        integrator.setPrompt("Scan something");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    public void scanBarcodeFrontCamera(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCameraId( Camera.CameraInfo.CAMERA_FACING_FRONT);
        integrator.initiateScan();
    }

    public void generateQR(String text, ImageView view){
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);
            view.setImageBitmap(bitmap);
        } catch(Exception e) {
            Log.e(TAG, ""+e.getMessage());
        }
    }

    public void scanWithTimeout(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setTimeout(8000);
        integrator.initiateScan();
    }
}
