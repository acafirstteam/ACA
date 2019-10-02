package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

import am.newway.aca.database.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.CoursesActivity;

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends BaseActivity {

    private final String TAG = "MainAcivity";


    private Button toCoursesAct;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        toCoursesAct = (Button) findViewById(R.id.to_courses_activity_btn_id);
        toCoursesAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(v.getContext(), CoursesActivity.class);
                startActivity(mIntent);
            }
        });

        final FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                scanBarcode( view );
            }
        } );

        //Կուրսերի ցանկի ներբեռնում
        FIRESTORE.getCuorces( new Firestore.OnCourseReadListener() {
            @Override
            public void OnCourseRead ( final List<Course> courses ) {
                for ( int i = 0; i != courses.size(); i++ ) {
                    Log.e( "#################" , "OnCourseRead: " + courses.get( i ).getUrl() );
                }
            }
        } );

        //Նոր այցելության գրանցում
        //        FIRESTORE.addNewVisit( "147258369" , "A1B2C3" , new Firestore.OnVisitChangeListener() {
        //            @Override
        //            public void OnChangeConfirmed ( final Visit visit ) {
        //
        //            }
        //        } );

        //նոր աշակերտի ավելացնել կամ ստուգել
        //        Student st = new Student( 21 , "aaa123@bbb.com" , "Name" , "093381919" , "www.picture.com" ,
        //                "Surname" );
        //        st.setId( 147258 );
        //        FIRESTORE.checkStudent( st , new Firestore.OnStudentCheckListener() {
        //            @Override
        //            public void OnStudentChecked ( final Student student ) {
        //                if(student != null) {
        //                    if ( student.getType() == 1 )
        //                        startActivity( new Intent( MainActivity.this , QrActivity.class ) );
        //                }
        //            }
        //        } );
    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main , menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if ( id == R.id.action_settings ) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onActivityResult ( int requestCode , int resultCode , Intent data ) {
        if ( requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE ) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult( requestCode , resultCode , data );
            return;
        }
        switch ( requestCode ) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText( this , "REQUEST_CODE = " + requestCode , Toast.LENGTH_LONG ).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult( resultCode , data );

        if ( result.getContents() == null ) {
            Toast.makeText( this , R.string.cancelled , Toast.LENGTH_LONG ).show();
        }
        else {
            Toast.makeText( this , "Scanned: " + result.getContents() , Toast.LENGTH_LONG ).show();
        }
    }
}
