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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.RotationOptions;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.FirebaseLogin;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.ui.QrActivity;
import am.newway.aca.ui.home.HomeFragment;
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
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

@SuppressLint ( "Registered" )
public
class BaseActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    //protected final String ENGLISH = "en";
    //protected final String ARMENIAN = "hy";

    protected Firestore FIRESTORE;
    protected DatabaseHelper DATABASE;
    @VisibleForTesting

    public ProgressDialog mProgressDialog;
    protected FloatingActionButton fab;
    private NavController navController;
    protected DrawerLayout drawer;
    protected FirebaseAuth mAuth;
    protected FirebaseUser firebaseUser;
    private HomeFragment homeFragment;
    private Toolbar toolbar;

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

        DATABASE = DatabaseHelper.getInstance( getApplicationContext() );
    }

    public
    void scanBarcode ( View view ) {
        //        Toast.makeText(this, new IntentIntegrator(this).getCaptureActivity().getCanonicalName(),
        //        Toast.LENGTH_LONG).show();
        new IntentIntegrator( this ).initiateScan();
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
                    am.newway.aca.template.Notification notification =
                            new am.newway.aca.template.Notification();

                    notification.setMessage( getString( R.string.reception ) );
                    notification.setTitle( getString( R.string.new_student ) );
                    notification.setUser( student.getId() );
                    notification.setLargeBitmap( student.getPicture() );

                    notificationDialog( "01" , notification );
                }
            }
        } );
    }

    //@RequiresApi ( api = Build.VERSION_CODES.O)
    private
    void notificationDialog ( String NOTIFICATION_CHANNEL_ID ,
            am.newway.aca.template.Notification notification ) {
        final NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            @SuppressLint ( "WrongConstant" ) NotificationChannel notificationChannel =
                    new NotificationChannel( NOTIFICATION_CHANNEL_ID , "My Notifications" ,
                            NotificationManager.IMPORTANCE_MAX );
            // Configure the notification channel.
            notificationChannel.setDescription( "Empty description" );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.RED );
            notificationChannel.setVibrationPattern( new long[]{ 0 , 1000 , 500 , 1000 } );
            notificationChannel.enableVibration( true );
            if ( notificationManager != null )
                notificationManager.createNotificationChannel( notificationChannel );
        }

        PendingIntent contentIntent =
                PendingIntent.getActivity( this , 0 , new Intent( this , QrActivity.class ) ,
                        PendingIntent.FLAG_UPDATE_CURRENT );

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder( this , NOTIFICATION_CHANNEL_ID );

        String[] notifSegment =
                getResources().getStringArray( R.array.notification_type );

        notificationBuilder.setAutoCancel( true )
                .setDefaults( Notification.DEFAULT_ALL )
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( R.drawable.ic_book_black_24dp )
                //.setTicker( "TutorialsPoint" )
                //.setPriority(Notification.PRIORITY_MAX)
                .setContentTitle( NOTIFICATION_CHANNEL_ID.equals( "02" ) ?
                        notification.getTitle(notifSegment) : notification.getTitle())
                .setContentText( notification.getMessage() )
                .setContentIntent( contentIntent )
        //.setContentInfo( "Ինֆորմացիա" )
        ;

        if ( notificationManager != null ) {

            Uri uri = Uri.parse(
                    notification.getLargeBitmap() != null ? notification.getLargeBitmap() : "" );
            getBitmapFromUrl( uri , new bitmapLoading() {
                @Override
                public
                void OnLoaded ( final Bitmap bmp ) {
                    notificationBuilder.setLargeIcon( bmp );
                    notificationManager.notify( 1 , notificationBuilder.build() );
                }

                @Override
                public
                void OnFailureLoad () {
                    notificationManager.notify( 1 , notificationBuilder.build() );
                }
            } );
        }
    }

    interface bitmapLoading {
        void OnLoaded ( Bitmap bmp );

        void OnFailureLoad ();
    }

    private
    void getBitmapFromUrl ( Uri uri , final bitmapLoading listener ) {

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource( uri )
                .setRotationOptions( RotationOptions.autoRotate() )
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
    void initNotifications () {
        if ( DATABASE.getSettings().isNotification() ) {
            FIRESTORE.addListenerNotifications( DATABASE.getStudent().getType() ,
                    new Firestore.OnNotificationListener() {
                        @Override
                        public
                        void OnNotification (
                                final am.newway.aca.template.Notification notification ) {
                            notificationDialog( "02" , notification );
                        }
                    } );
        }
    }

    protected
    void initNavigationBar () {
        initNavigationBar( 1 );
    }

    protected
    void initNavigationBar ( int nType ) {
        toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        if ( nType == 1 )
            initNavigationView();
        else if ( nType == 2 ) {
            if ( getSupportActionBar() != null )
                getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        }
    }

    private
    void initNavigationView () {
        drawer = findViewById( R.id.drawer_layout );
        navController = Navigation.findNavController( this , R.id.nav_host_fragment );
        NavigationView navigationView = findViewById( R.id.nav_view );

        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_admin).setVisible(false);

        if(DATABASE.getStudent().getType() == 2)
            nav_Menu.findItem(R.id.nav_admin).setVisible(true);

        final AppBarConfiguration mAppBarConfiguration =
                new AppBarConfiguration.Builder( R.id.nav_home , R.id.nav_settings ,
                        R.id.nav_history , R.id.nav_alert , R.id.nav_admin, R.id.nav_company ,
                        R.id.nav_about ).setDrawerLayout(
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

    protected
    void updateNavigationBar () {
        NavigationView navigationView = findViewById( R.id.nav_view );
        View headerLayout = navigationView.getHeaderView( 0 );
        TextView textName = headerLayout.findViewById( R.id.title );
        TextView textDescription = headerLayout.findViewById( R.id.description );
        SimpleDraweeView imageView = headerLayout.findViewById( R.id.imageView );

        Student student = DATABASE.getStudent();

        if ( student.getId() != null && !student.getId().equals( "-1" ) ) {
            imageView.setImageURI( Uri.parse( student.getPicture() ) );

            textName.setText( student.getName() );

            textDescription.setText( student.getEmail() );

            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    startActivityWithIntent(
                            new Intent( BaseActivity.this , FirebaseLogin.class ) );
                }
            } );

            textName.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    startActivityWithIntent(
                            new Intent( BaseActivity.this , FirebaseLogin.class ) );
                }
            } );

            textDescription.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    startActivityWithIntent(
                            new Intent( BaseActivity.this , FirebaseLogin.class ) );
                }
            } );
        }
        else {
            imageView.setImageResource( R.mipmap.ic_launcher );

            textName.setText( getResources().getString( R.string.nav_header_title ) );

            textDescription.setText( getResources().getString( R.string.nav_header_subtitle ) );

            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    String url = "http://www.aca.am";
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( url ) );
                    startActivityWithIntent( i );
                }
            } );

            textName.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    Intent emailIntent = new Intent( Intent.ACTION_SENDTO ,
                            Uri.fromParts( "mailto" , "info@aca.am" , null ) );
                    startActivityWithIntent(
                            Intent.createChooser( emailIntent , "Send email..." ) );
                }
            } );

            textDescription.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( final View view ) {
                    Intent emailIntent = new Intent( Intent.ACTION_SENDTO ,
                            Uri.fromParts( "mailto" , "info@aca.am" , null ) );
                    startActivityWithIntent(
                            Intent.createChooser( emailIntent , "Send email..." ) );
                }
            } );
        }
    }

    private
    void startActivityWithIntent ( Intent intent ) {
        if ( drawer.isDrawerOpen( GravityCompat.START ) ) {
            drawer.closeDrawer( GravityCompat.START );
        }
        startActivityForResult( intent , 1 );
    }

    @Override
    public
    boolean onSupportNavigateUp () {
        return ( navController != null && drawer != null ) &&
                NavigationUI.navigateUp( navController , drawer );
    }

    private
    boolean isDestination ( int destination ) {
        NavDestination view = Navigation.findNavController( this , R.id.nav_host_fragment )
                .getCurrentDestination();
        if ( view != null )
            return destination != view.getId();
        return true;
    }

    public
    HomeFragment getHomeFragment () {
        if ( homeFragment != null )
            return homeFragment;

        homeFragment = ( HomeFragment ) getSupportFragmentManager().getFragments()
                .get( 0 )
                .getChildFragmentManager()
                .getFragments()
                .get( 0 );

        return homeFragment;
    }
}
