package am.newway.aca.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.database.Firestore;

public class QrActivity extends BaseActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_qr );

        final ImageView image = findViewById( R.id.qrgenerator );

        FIRESTORE.qrGenerator( new Firestore.OnQRGenerator() {
            @Override
            public void OnQrGenerated (String qr) {
                generateQR(qr, image);
            }

            @Override
            public void OnQrGenerationFailed () {
                Toast.makeText( QrActivity.this, "Qr code don't created.\nЕhe program cannot "
                                + "continue.",
                        Toast.LENGTH_LONG ).show();
                finish();
            }
        } );
    }
}
