package am.newway.aca.ui;

import android.os.Bundle;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;

public class QrActivity extends BaseActivity {

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_qr );

        ImageView image = findViewById( R.id.qrgenerator );

        SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy" , Locale.US );
        Date date = new Date();
        generateQR( formatter.format(date), image );
    }
}
