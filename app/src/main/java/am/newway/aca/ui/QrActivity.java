package am.newway.aca.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import androidx.cardview.widget.CardView;

public class QrActivity extends BaseActivity implements Firestore.OnStudentCheckListener {

    private CardView cardView;
    private TextView name;
    private TextView surName;
    private SimpleDraweeView imageView;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_qr );

        final ImageView image = findViewById( R.id.qrgenerator );
        imageView = findViewById( R.id.user_picture);
        cardView = findViewById( R.id.user_card);
        name = findViewById( R.id.name);
        surName = findViewById( R.id.surname);

        FIRESTORE.qrGenerator( new Firestore.OnQRGenerator() {
            @Override
            public void OnQrGenerated (String qr) {
                generateQR(qr, image);
            }

            @Override
            public void OnQrGenerationFailed () {
                Toast.makeText( QrActivity.this, "Qr code don't created.\nThe program cannot "
                                + "continue.",
                        Toast.LENGTH_LONG ).show();
                finish();
            }
        } );

        FIRESTORE.addListenerNewVisit();

        FIRESTORE.addOnStudentCheckListener( this );
    }

    @Override
    public void OnStudentChecked ( final Student student ) {

    }

    @Override
    public void OnStudentCheckFailed ( final String exception ) {

    }

    @Override
    public void OnStudentIdentifier ( final Student student ) {
        name.setText( student.getName() );
        surName.setText( student.getSurname() );
        cardView.setVisibility( View.VISIBLE );
        imageView.setImageURI( student.getPicture() );

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        name.setText( "" );
                        surName.setText( "" );
                        cardView.setVisibility( View.INVISIBLE );
                    }
                },
                7000);
    }
}
