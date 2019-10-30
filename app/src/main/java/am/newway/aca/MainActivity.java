package am.newway.aca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.util.Locale;

import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.FirebaseLogin;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.InfoActivity;
import am.newway.aca.util.Util;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;

public
class MainActivity extends BaseActivity {

    private static long back_pressed;
    private String TAG = getClass().getSimpleName();
    //private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        int nType = DATABASE.getStudent().getType();
        if ( nType == 2 ) {
            //addOnNewStudentListener();
            Util.scheduleJob( this );
        }

        initNavigationBar();

        updateNavigationBar();

        initNotifications();

        if ( !DATABASE.getSettings().isFirstStart() ) {
            DATABASE.getSettings().setFirstStart( true );
            startActivity( new Intent( MainActivity.this , InfoActivity.class ) );
        }

        //FIRESTORE.addCourses( CoursesInit.addCourse() );

        FIRESTORE.checkVisit( DATABASE.getStudent() , new Firestore.OnVisitCheckListener() {

            @Override
            public
            void OnVisitExisted ( final Visit visit ) {
                DATABASE.setVisit( visit );
                if ( getHomeFragment() != null )
                    getHomeFragment().addNewVisit( visit );
            }

            @Override
            public
            void OnVisitNotExist () {

            }
        } );

        fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {

            @Override
            public
            void onClick ( View view ) {

                firebaseUser = mAuth.getCurrentUser();
                if ( firebaseUser != null )
                    Log.e( TAG , "onClick: " + firebaseUser.getDisplayName() );
                if ( firebaseUser == null ) {
                    Log.d( TAG , "Starting SignUp Activity" );
                    startActivityForResult( new Intent( MainActivity.this , FirebaseLogin.class ) ,
                            1 );
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
        if ( id == R.id.action_lincenses ) {
            new LibsBuilder().withActivityStyle( Libs.ActivityStyle.LIGHT_DARK_TOOLBAR )
                    .withAboutIconShown( true )
                    .withAboutAppName( getString( R.string.app_name ) )
                    .withAboutVersionShown( true )
                    .withActivityTitle( getString( R.string.licenses ) )
                    .withAutoDetect( true )
                    //.withAboutDescription(getString(R.string.app_desc))
                    .withLicenseDialog( true )
                    .withLicenseShown( true )
                    .start( this );

            //            Intent intent = new Intent();
            //            intent.setType("image/*");
            //            intent.setAction(Intent.ACTION_GET_CONTENT);
            //            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected
    void onActivityResult ( int requestCode , int resultCode , Intent data ) {

        if ( requestCode == 1 && resultCode == 1 ) {

            updateNavigationBar();

            showNavigationItem( DATABASE.getStudent().getId().equals( "-1" ) &&
                    DATABASE.getStudent().getType() == 2 );

            Student student = DATABASE.getStudent();

            firebaseUser = mAuth.getCurrentUser();
            if ( firebaseUser != null ) {

                FIRESTORE.checkStudent( student , true , new Firestore.OnStudentCheckListener() {
                    @Override
                    public
                    void OnStudentChecked ( @Nullable final Student student ) {
                        //if ( student != null )
                        //DATABASE.setStudent( student );
                    }

                    @Override
                    public
                    void OnStudentCheckFailed ( final String exception ) {

                    }

                    @Override
                    public
                    void OnStudentIdentifier ( final Student student ) {
                        //if ( student != null )
                        //DATABASE.setStudent( student );
                    }
                } );
            }

            super.onActivityResult( requestCode , resultCode , data );
            return;
        }
        if ( requestCode != CUSTOMIZED_REQUEST_CODE &&
                requestCode != IntentIntegrator.REQUEST_CODE ) {

            super.onActivityResult( requestCode , resultCode , data );
            return;
        }
        if ( requestCode == CUSTOMIZED_REQUEST_CODE ) {
            Toast.makeText( this , "REQUEST_CODE = " + requestCode , Toast.LENGTH_LONG ).show();
        }

        final IntentResult result = IntentIntegrator.parseActivityResult( resultCode , data );

        if ( result.getContents() != null ) {

            final Visit visit = DATABASE.getVisit();
            if ( visit != null ) {
                new AlertDialog.Builder( MainActivity.this ).setTitle( "Ուշադրություն" )
                        .setMessage( "Ցանկանում ե՞ք ավարտել դասաժամը" )
                        .setPositiveButton( "Այո" , new DialogInterface.OnClickListener() {
                            @Override
                            public
                            void onClick ( final DialogInterface dialogInterface , final int i ) {
                                completeVisit( visit.getId() );
                            }
                        } )
                        .setNegativeButton( "Ոչ" , null )
                        .show();
            }
            else {
                FIRESTORE.addNewVisit( DATABASE.getStudent() , result.getContents() ,
                        new Firestore.OnVisitAddListener() {
                            @Override
                            public
                            void OnChangeConfirmed ( final Visit visit ) {
                                if ( getHomeFragment() != null ) {
                                    getHomeFragment().addNewVisit( visit );
                                    DATABASE.setVisit( visit );
                                    Log.e( TAG , "OnChangeConfirmed: " + visit.getId() );
                                }
                            }
                        } );
            }
        }
    }

    private
    void completeVisit ( String Id ) {
        FIRESTORE.completeVisit( Id , new Firestore.OnVisitCompleteListener() {
            @Override
            public
            void OnVisitCompleted () {
                if ( getHomeFragment() != null ) {
                    getHomeFragment().completeVisit();
                    DATABASE.setVisit( null );
                }
                Log.e( TAG , "OnVisitCompleted: " );
            }
        } );
    }

    @Override
    public
    void onResume () {
        super.onResume();

        String lang = DATABASE.getSettings().getLanguage();

        Locale locale = new Locale( lang );
        Locale.setDefault( locale );
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources()
                .updateConfiguration( config ,
                        getBaseContext().getResources().getDisplayMetrics() );
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

    public
    DatabaseHelper getDATABASE () {
        return DATABASE;
    }

    public
    Firestore getFIRESTORE () {
        return FIRESTORE;
    }
}
