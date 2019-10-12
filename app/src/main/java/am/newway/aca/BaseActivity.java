package am.newway.aca;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.FirebaseLogin;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.ui.QrActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public
class BaseActivity extends AppCompatActivity {
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    private final String TAG = getClass().getSimpleName();
    protected Firestore FIRESTORE;
    protected DatabaseHelper DATABASE;
    @VisibleForTesting
    public ProgressDialog mProgressDialog;
    protected FloatingActionButton fab;
    private NavController navController;
    protected DrawerLayout drawer;
    protected FirebaseAuth mAuth;
    protected FirebaseUser firebaseUser;

    public
    BaseActivity () {
        FIRESTORE = Firestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    protected
    void onCreate ( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        DATABASE = DatabaseHelper.getInstance( BaseActivity.this );
    }

    //    public
//    Student getGlobStudent () {
//        return globStudent;
//    }
//
//    public
//    void setGlobStudent ( Student globStudent ) {
//        this.globStudent = globStudent;
//        if(globStudent != null)
//            Log.e( TAG , "setGlobStudent: " + globStudent.getType() );
//    }

    public
    void scanBarcode ( View view ) {
        //        Toast.makeText(this, new IntentIntegrator(this).getCaptureActivity().getCanonicalName(),
        //        Toast.LENGTH_LONG).show();
        new IntentIntegrator( this ).initiateScan();
    }

    public
    void scanBarcodeWithCustomizedRequestCode ( View view ) {
        new IntentIntegrator( this ).setRequestCode( CUSTOMIZED_REQUEST_CODE ).initiateScan();
    }

    public
    void scanBarcodeInverted ( View view ) {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.addExtra( Intents.Scan.SCAN_TYPE , Intents.Scan.INVERTED_SCAN );
        integrator.initiateScan();
    }

    public
    void scanMixedBarcodes ( View view ) {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.addExtra( Intents.Scan.SCAN_TYPE , Intents.Scan.MIXED_SCAN );
        integrator.initiateScan();
    }

    public
    void scanPDF417 ( View view ) {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.setDesiredBarcodeFormats( IntentIntegrator.PDF_417 );
        integrator.setPrompt( "Scan something" );
        integrator.setOrientationLocked( false );
        integrator.setBeepEnabled( false );
        integrator.initiateScan();
    }

    public
    void scanBarcodeFrontCamera ( View view ) {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.setCameraId( Camera.CameraInfo.CAMERA_FACING_FRONT );
        integrator.initiateScan();
    }

    public
    void generateQR ( String text , ImageView view ) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap( text , BarcodeFormat.QR_CODE , 400 , 400 );
            view.setImageBitmap( bitmap );
        } catch ( Exception e ) {
            Log.e( TAG , "" + e.getMessage() );
        }
    }

    public
    void scanWithTimeout ( View view ) {
        IntentIntegrator integrator = new IntentIntegrator( this );
        integrator.setTimeout( 8000 );
        integrator.initiateScan();
    }

    public
    void showProgressDialog () {
        if ( mProgressDialog == null ) {
            mProgressDialog = new ProgressDialog( this );
            mProgressDialog.setMessage( "Loading.." );
            mProgressDialog.setIndeterminate( true );
        }

        mProgressDialog.show();
    }

    public
    void hideProgressDialog () {
        if ( mProgressDialog != null && mProgressDialog.isShowing() ) {
            mProgressDialog.hide();
            mProgressDialog.dismiss();
        }
    }

    protected
    void hideFabButtons ( FloatingActionButton fb ) {
        Log.i( TAG , "hideFabButtons" );
        if ( fb != null ) {
            if ( fb.getVisibility() == View.VISIBLE ) {
                fb.animate();
                fb.hide();
            }
        }
    }

    protected
    void showFabButtons () {
        Log.i( TAG , "showFabButtons" );
        if ( fab != null &&
                ( fab.getVisibility() == View.GONE || fab.getVisibility() == View.INVISIBLE ) ) {
            fab.animate();
            fab.show();
        }
    }

    protected
    int getBackStackCount () {
        return getSupportFragmentManager().getFragments()
                .get( 0 )
                .getChildFragmentManager()
                .getBackStackEntryCount();
    }

    protected
    void addOnNewStudentListener () {
        FIRESTORE.addListenerNewStudent( new Firestore.OnNewStudentListener() {
            @Override
            public
            void OnNewStudentAdded ( @Nullable final Student student ) {
                if ( student != null ) {
                    notificationDialog();
                }
            }
        } );
    }

    //@RequiresApi ( api = Build.VERSION_CODES.O)
    private
    void notificationDialog () {
        final NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );
        String NOTIFICATION_CHANNEL_ID = "tutorialspoint_01";
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            @SuppressLint ( "WrongConstant" ) NotificationChannel notificationChannel =
                    new NotificationChannel( NOTIFICATION_CHANNEL_ID , "My Notifications" ,
                            NotificationManager.IMPORTANCE_MAX );
            // Configure the notification channel.
            notificationChannel.setDescription( "Sample Channel description" );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.RED );
            notificationChannel.setVibrationPattern( new long[]{ 0 , 1000 , 500 , 1000 } );
            notificationChannel.enableVibration( true );
            notificationManager.createNotificationChannel( notificationChannel );
        }

        PendingIntent contentIntent =
                PendingIntent.getActivity( this , 0 , new Intent( this , QrActivity.class ) ,
                        PendingIntent.FLAG_UPDATE_CURRENT );

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder( this , NOTIFICATION_CHANNEL_ID );
        notificationBuilder.setAutoCancel( true )
                .setDefaults( Notification.DEFAULT_ALL )
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( R.drawable.ic_book_black_24dp )
                .setTicker( "TutorialsPoint" )
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle( "Նոր ուսանող" )
                .setContentText( "Ընդունարանում Ձեզ սպասում են" )
                .setContentIntent( contentIntent )
                .setContentInfo( "Ինֆորմացիա" );

        Uri uri = Uri.parse(
                "https://lh3.googleusercontent.com/a-/AAuE7mBCjmyxKyXqdMLU5NMgCsNsqS5Z7ceBGKSlRJvJ4A=s96-cc" );
        getBitmapFromUrl( uri , new bitmapLoading() {
            @Override
            public
            void OnLoaded ( final Bitmap bmp ) {
                notificationBuilder.setLargeIcon( bmp );
                notificationManager.notify( 1 , notificationBuilder.build() );

            }
        } );
    }

    interface bitmapLoading {
        void OnLoaded ( Bitmap bmp );
    }

    private
    void getBitmapFromUrl ( Uri uri , final bitmapLoading listener ) {

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource( uri )
                .setAutoRotateEnabled( true )
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage( imageRequest , this );

        dataSource.subscribe( new BaseBitmapDataSubscriber() {

            @Override
            public
            void onNewResultImpl ( @Nullable Bitmap bitmap ) {
                if ( dataSource.isFinished() && bitmap != null ) {
                    Bitmap bmp = Bitmap.createBitmap( bitmap );
                    dataSource.close();

                    if ( listener != null ) {
                        listener.OnLoaded( bmp );
                    }
                }
            }

            @Override
            public
            void onFailureImpl ( DataSource dataSource ) {
                if ( dataSource != null ) {
                    dataSource.close();
                    if ( listener != null ) {
                        listener.OnLoaded( null );
                    }
                }
            }
        } , CallerThreadExecutor.getInstance() );
    }

    protected
    void initNavigationBar () {
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        drawer = findViewById( R.id.drawer_layout );
        navController = Navigation.findNavController( this , R.id.nav_host_fragment );
        NavigationView navigationView = findViewById( R.id.nav_view );
        View headerLayout = navigationView.getHeaderView( 0 );
        TextView textName = headerLayout.findViewById( R.id.title );
        TextView textDescription = headerLayout.findViewById( R.id.description );
        ImageView imageView = headerLayout.findViewById( R.id.imageView );

        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                String url = "http://www.aca.am";
                Intent i = new Intent( Intent.ACTION_VIEW );
                i.setData( Uri.parse( url ) );
                startActivityWithIntent( new Intent( BaseActivity.this , FirebaseLogin.class ) );
            }
        } );

        textName.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                Intent emailIntent = new Intent( Intent.ACTION_SENDTO ,
                        Uri.fromParts( "mailto" , "info@aca.am" , null ) );
                startActivityWithIntent( Intent.createChooser( emailIntent , "Send email..." ) );
            }
        } );

        textDescription.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                Intent emailIntent = new Intent( Intent.ACTION_SENDTO ,
                        Uri.fromParts( "mailto" , "info@aca.am" , null ) );
                startActivityWithIntent( Intent.createChooser( emailIntent , "Send email..." ) );
            }
        } );

        final AppBarConfiguration mAppBarConfiguration =
                new AppBarConfiguration.Builder( R.id.nav_home , R.id.nav_settings ,
                        R.id.nav_history , R.id.nav_company , R.id.nav_about ).setDrawerLayout(
                        drawer ).build();
        NavigationUI.setupActionBarWithNavController( this , navController , mAppBarConfiguration );
        NavigationUI.setupWithNavController( navigationView , navController );

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public
                    boolean onNavigationItemSelected ( @NonNull MenuItem menuItem ) {
                        int id = menuItem.getItemId();
                        if ( id == R.id.nav_home ) {
                            NavOptions options =
                                    new NavOptions.Builder().setPopUpTo( R.id.mobile_navigation ,
                                            true ).build();
                            navController.navigate( id , null , options );
                        }
                        else {
                            if ( isDestination( id ) ) {
                                NavOptions options =
                                        new NavOptions.Builder().setPopUpTo( R.id.nav_home , false )
                                                .build();
                                navController.navigate( id , null , options );
                            }
                        }

                        drawer.closeDrawer( GravityCompat.START );
                        return false;
                    }
                } );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this , drawer , toolbar ,
                R.string.navigation_drawer_open , R.string.navigation_drawer_close ) {
            @Override
            public
            void onDrawerSlide ( View drawerView , float slideOffset ) {
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

    private
    void startActivityWithIntent ( Intent intent ) {
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        }
        startActivity( intent );
    }

    @Override
    public
    boolean onSupportNavigateUp () {
        return NavigationUI.navigateUp( navController , drawer );
    }

    private
    boolean isDestination ( int destination ) {
        return destination != Navigation.findNavController( this , R.id.nav_host_fragment )
                .getCurrentDestination()
                .getId();
    }
}
