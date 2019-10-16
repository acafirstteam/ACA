package am.newway.aca.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import am.newway.aca.R;
import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.ui.QrActivity;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


public
class AlertService extends JobService {

    Firestore FIRESTORE = Firestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    DatabaseHelper DATABASE = DatabaseHelper.getInstance( this );

    @Override
    public
    boolean onStartJob ( final JobParameters jobParameters ) {
        Log.e( "########" , "onStartJob: ######"   );
        String uID = "-1";
        firebaseUser = mAuth.getCurrentUser();
        if ( firebaseUser != null )
            uID = firebaseUser.getUid();

        Student student = DATABASE.getStudent();
        if ( student.getId() == null ) {
            student = new Student(  );
            student.setId( uID );
        }

        FIRESTORE.checkStudent( student , false , new Firestore.OnStudentCheckListener() {
            @Override
            public
            void OnStudentChecked ( Student student ) {
                Log.d( "Servce", "OnStudentChecked" );

                if ( student != null ) {

                    //DATABASE.setStudent( student );
                    if ( student.getType() == 2 ) {
                        FIRESTORE.addListenerNewStudent( new Firestore.OnNewStudentListener() {
                            @Override
                            public
                            void OnNewStudentAdded ( @Nullable final Student student ) {
                                Log.d( "Servce", "OnNewStudentAdded" );

                                if ( student != null ) {
                                    notificationDialog();
                                }
                            }
                        } );
                    }
                }
            }

            @Override
            public
            void OnStudentCheckFailed ( final String exception ) {
            }

            @Override
            public
            void OnStudentIdentifier ( final Student student ) {
            }
        } );
        return false;
    }

    @Override
    public
    boolean onStopJob ( final JobParameters jobParameters ) {
        Log.d( "Servce", "on job finish" );
        return false;
    }

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
            if ( notificationManager != null )
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
                if ( notificationManager != null )
                    notificationManager.notify( 1 , notificationBuilder.build() );

            }
        } );
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

    interface bitmapLoading {
        void OnLoaded ( Bitmap bmp );
    }
}
