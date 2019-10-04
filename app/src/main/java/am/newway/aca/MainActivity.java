package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import am.newway.aca.database.Firestore;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.home.HomeFragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BaseActivity {
    //private static long back_pressed;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private DrawerLayout drawer;
    //private SearchView searchView;
    private String TAG = getClass().getSimpleName();
    //private MenuItem searchMenu;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle( R.string.nav_header_title );

        final FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                scanBarcode( view );
            }
        } );

        //Կուրսերի ցանկի ներբեռնում
        //        FIRESTORE.getCuorces( new Firestore.OnCourseReadListener() {
        //            @Override
        //            public void OnCourseRead ( final List<Course> courses ) {
        //                for ( int i = 0; i != courses.size(); i++ ) {
        //                    Log.e( "#################" , "OnCourseRead: " + courses.get( i ).getUrl() );
        //                }
        //            }
        //        } );

        //Նոր այցելության գրանցում
        //        FIRESTORE.addNewVisit( ""+App.userID , "A1B2C3" , new Firestore
        //        .OnVisitChangeListener() {
        //            @Override
        //            public void OnChangeConfirmed ( final Visit visit ) {
        //
        //            }
        //        } );

        drawer = findViewById( R.id.drawer_layout );
        navController = Navigation.findNavController( this , R.id.nav_host_fragment );
        NavigationView navigationView = findViewById( R.id.nav_view );
        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.nav_home , R.id.nav_company , R.id.nav_about ).setDrawerLayout( drawer ).build();
        NavigationUI.setupActionBarWithNavController( this , navController , mAppBarConfiguration );
        NavigationUI.setupWithNavController( navigationView , navController );

        navigationView.setNavigationItemSelectedListener( new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected ( @NonNull MenuItem menuItem ) {
                int id = menuItem.getItemId();
                navController.navigate( id );
                //navController.popBackStack();
                drawer.closeDrawer( GravityCompat.START );
                return false;
            }
        } );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this , drawer , toolbar , R.string.navigation_drawer_open , R.string.navigation_drawer_close ) {
            @Override
            public void onDrawerSlide ( View drawerView , float slideOffset ) {
                if ( fab.getVisibility() == View.VISIBLE ) {
                    fab.setTranslationX( slideOffset * 100 );
                    fab.setAlpha( 1 - slideOffset );
                }
                super.onDrawerSlide( drawerView , slideOffset );
            }
        };

        drawer.addDrawerListener( toggle );
        toggle.syncState();

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

        final IntentResult result = IntentIntegrator.parseActivityResult( resultCode , data );

        if ( result.getContents() != null ) {
            final HomeFragment homeFrag = ( HomeFragment )
                    getSupportFragmentManager().getFragments().get( 0 ).getChildFragmentManager().getFragments().get( 0 );
            FIRESTORE.addNewVisit( ""+App.userID , result.getContents() ,
                    new Firestore.OnVisitChangeListener() {
                @Override
                public void OnChangeConfirmed ( final Visit visit ) {
                    Toast.makeText( MainActivity.this , visit.toString() , Toast.LENGTH_SHORT ).show();
                    if(homeFrag != null) {
                        homeFrag.addNewVisit( visit );
                    }
                }
            } );
        }else{
            //Toast.makeText( this , R.string.cancelled , Toast.LENGTH_LONG ).show();
        }
    }
}
