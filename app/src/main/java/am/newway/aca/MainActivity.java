package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import am.newway.aca.firebase.FirebaseLogin;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.home.HomeFragment;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

public
class MainActivity extends BaseActivity {

    private static long back_pressed;
    private String TAG = getClass().getSimpleName();

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        initNavigationBar();

        if ( firebaseUser != null ) {
            Log.e( TAG , "onCreate: " + firebaseUser.getUid() );
        }
        else
            Log.e( TAG , "onCreate: firebase is null" );

        Student student = getGlobStudent();


        int nType = student.getType();
        Log.e( TAG , "not nullik:  " +nType);
        if ( nType == 2 )
            addOnNewStudentListener();

        fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public
            void onClick ( View view ) {

                firebaseUser = mAuth.getCurrentUser();
                if ( firebaseUser == null ) {
                    Log.d( TAG , "Starting SignUp Activity" );
                    startActivity( new Intent( MainActivity.this , FirebaseLogin.class ) );
                }
                else {
                    Log.d( TAG , "Scan Bar code is ready" );
                    scanBarcode( view );
                }
            }
        } );
    }

    @Override
    public
    boolean onCreateOptionsMenu ( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.menu_main , menu );
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected ( MenuItem item ) {
        int id = item.getItemId();
        if ( id == R.id.action_settings ) {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected
    void onActivityResult ( int requestCode , int resultCode , Intent data ) {
        if ( requestCode != CUSTOMIZED_REQUEST_CODE &&
                requestCode != IntentIntegrator.REQUEST_CODE ) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult( requestCode , resultCode , data );
            return;
        }
        if ( requestCode == CUSTOMIZED_REQUEST_CODE ) {
            Toast.makeText( this , "REQUEST_CODE = " + requestCode , Toast.LENGTH_LONG ).show();
        }

        final IntentResult result = IntentIntegrator.parseActivityResult( resultCode , data );

        if ( result.getContents() != null ) {
            final HomeFragment homeFrag =
                    ( HomeFragment ) getSupportFragmentManager().getFragments()
                            .get( 0 )
                            .getChildFragmentManager()
                            .getFragments()
                            .get( 0 );
            Student student = getGlobStudent();
            if ( student == null ) {
                String uID = "-1";
                firebaseUser = mAuth.getCurrentUser();
                if ( firebaseUser != null )
                    uID = firebaseUser.getUid();

                FIRESTORE.checkStudent( new Student( uID ) , true , new Firestore.OnStudentCheckListener() {
                    @Override
                    public
                    void OnStudentChecked ( @Nullable final Student student ) {
                        setGlobStudent( student );
                    }

                    @Override
                    public
                    void OnStudentCheckFailed ( final String exception ) {

                    }

                    @Override
                    public
                    void OnStudentIdentifier ( final Student student ) {
                        setGlobStudent( student );
                    }
                } );
            }
            else {
                FIRESTORE.addNewVisit( new Student( getGlobStudent().getId() ) ,
                        result.getContents() , new Firestore.OnVisitChangeListener() {
                            @Override
                            public
                            void OnChangeConfirmed ( final Visit visit ) {
                                if ( homeFrag != null ) {
                                    homeFrag.addNewVisit( visit );
                                }
                            }
                        } );
            }
        }
    }

    @Override
    public
    void onBackPressed () {
        Snackbar snake = Snackbar.make( drawer , getResources().getString( R.string.snackbar ) ,
                Snackbar.LENGTH_SHORT );

        if ( getBackStackCount() > 0 )
            super.onBackPressed();
        else if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        }
        else if ( back_pressed + 2000 > System.currentTimeMillis() && !snake.isShown() ) {
            super.onBackPressed();
            overridePendingTransition( R.anim.enter_dropin , R.anim.exit_dropout );
        }
        else if ( fab.getVisibility() == View.VISIBLE ) {
            hideFabButtons( fab );
            snake.addCallback( new Snackbar.Callback() {
                @Override
                public
                void onDismissed ( Snackbar snackbar , int event ) {
                    if ( event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT ) {
                        showFabButtons();
                    }
                }

                @Override
                public
                void onShown ( Snackbar snackbar ) {
                    hideFabButtons( fab );
                }
            } ).setAction( getResources().getString( R.string.close ) , new View.OnClickListener() {
                @Override
                public
                void onClick ( View v ) {
                    finish();
                    overridePendingTransition( R.anim.enter_dropin , R.anim.exit_dropout );
                }
            } ).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
