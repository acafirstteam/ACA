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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
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

import java.util.List;

import am.newway.aca.MainActivity;
import am.newway.aca.R;
import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.ui.student.StudentActivity;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public
class AlertService extends JobService {

    private static final String TAG = "AlertService";
    Firestore FIRESTORE = Firestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser;
    DatabaseHelper DATABASE = DatabaseHelper.getInstance( this );

    @Override
    public
    boolean onStartJob ( final JobParameters jobParameters ) {
        String uID = "-1";
        firebaseUser = mAuth.getCurrentUser();
        if ( firebaseUser != null )
            uID = firebaseUser.getUid();

        Student student = DATABASE.getStudent();
        if ( student.getId() == null ) {
            student = new Student();
            student.setId( uID );
        }

        initNotifications();

        initNewStudentNotification( student );

        return true;
    }

    private
    void initNewStudentNotification ( Student student ) {
        FIRESTORE.checkStudent( student , false , new Firestore.OnStudentCheckListener() {
            @Override
            public
            void OnStudentChecked ( Student student ) {
                Log.d( "Service" , "OnStudentChecked" );

                if ( student != null ) {
                    //DATABASE.setStudent( student );
                    if ( student.getType() == 2 ) {
                        FIRESTORE.addListenerNewStudent( new Firestore.OnNewStudentListener() {
                            @Override
                            public
                            void OnNewStudentAdded ( @Nullable final Student student ) {
                                Log.d( "Service" , "OnNewStudentAdded" );

                                if ( student != null ) {
                                    notificationDialog( "01" ,
                                            new am.newway.aca.template.Notification() );
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
    }

    @Override
    public
    boolean onStopJob ( final JobParameters jobParameters ) {
        Log.d( "Service" , "on job finish" );
        return true;
    }

    private
    void initNotifications () {
        if ( DATABASE.getSettings().isNotification() ) {
            FIRESTORE.addListenerNotifications( DATABASE.getStudent().getType() ,
                    new Firestore.OnNotificationListener() {
                        @Override
                        public
                        void OnNotificationRead (
                                final List<am.newway.aca.template.Notification> notifications ) {
                        }

                        @Override
                        public
                        void OnNotificationFaild () {
                        }

                        @Override
                        public
                        void OnNewNotification (
                                final am.newway.aca.template.Notification notification ) {
                            notificationDialog( "02" , notification );
                        }
                    } );
        }
    }

    public
    boolean isActive () {
        return PreferenceManager.getDefaultSharedPreferences( this )
                .getBoolean( "isActive" , false );
    }

    private
    void notificationDialog ( final String NOTIFICATION_CHANNEL_ID ,
            am.newway.aca.template.Notification notification ) {
        if ( !DATABASE.getSettings().isNotification() )
            return;

        final NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE );

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
            @SuppressLint ( "WrongConstant" ) NotificationChannel notificationChannel =
                    new NotificationChannel( NOTIFICATION_CHANNEL_ID , "My Notifications" ,
                            NotificationManager.IMPORTANCE_MAX );
            // Configure the notification channel.
            notificationChannel.setDescription( "" );
            notificationChannel.enableLights( true );
            notificationChannel.setLightColor( Color.RED );
            notificationChannel.setVibrationPattern( new long[]{ 0 , 1000 , 500 , 1000 } );
            notificationChannel.enableVibration( true );
            if ( notificationManager != null )
                notificationManager.createNotificationChannel( notificationChannel );
        }

        Log.e( TAG , "notificationDialog: " + NOTIFICATION_CHANNEL_ID );
        PendingIntent contentIntent = null;
        if ( NOTIFICATION_CHANNEL_ID.equals( "02" ) ) {
            Intent intent = new Intent( this , MainActivity.class );
            intent.putExtra( "message" , 1 );
            contentIntent = PendingIntent.getActivity( this , 0 , intent ,
                    PendingIntent.FLAG_UPDATE_CURRENT );
        }
        else {
            Intent intent = new Intent( this , StudentActivity.class );
            intent.putExtra( "new" , true );
            contentIntent = PendingIntent.getActivity( this , 0 , intent ,
                    PendingIntent.FLAG_UPDATE_CURRENT );
        }
        String[] notifSegment = getResources().getStringArray( R.array.notification_type );

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder( this , NOTIFICATION_CHANNEL_ID );
        notificationBuilder.setAutoCancel( true )
                .setDefaults( Notification.DEFAULT_ALL )
                .setWhen( System.currentTimeMillis() )
                .setSmallIcon( R.drawable.ic_book_black_24dp )
                .setTicker( getString( R.string.new_visitor ) )
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle( NOTIFICATION_CHANNEL_ID.equals( "02" ) ? notification.getTitle(
                        notifSegment ) : getString( R.string.new_student ) )
                .setContentText( NOTIFICATION_CHANNEL_ID.equals( "02" ) ?
                        notification.getMessage() : getString( R.string.waiting ));

        notificationBuilder.setContentIntent( contentIntent );

        if ( NOTIFICATION_CHANNEL_ID.equals( "01" ) ) {
            Uri uri = Uri.parse(
                    "https://firebasestorage.googleapis.com/v0/b/acafirst-a0a43.appspot.com/o/33308.png?alt=media&token=8adbf375-9d17-4557-86ac-9c437ce8484e" );
            getBitmapFromUrl( uri , new bitmapLoading() {
                @Override
                public
                void OnLoaded ( final Bitmap bmp ) {
                    notificationBuilder.setLargeIcon( bmp );
                    if ( notificationManager != null )
                        notificationManager.notify( Integer.valueOf( NOTIFICATION_CHANNEL_ID ) ,
                                notificationBuilder.build() );

                }
            } );
        }
        else if ( NOTIFICATION_CHANNEL_ID.equals( "02" ) ) {

            int nRes = R.drawable.message;
            switch ( notification.getMessageType() ) {
                case 0:
                    nRes = R.drawable.message;
                    break;
                case 1:
                    nRes = R.drawable.alert;
                    break;
                case 2:
                    nRes = R.drawable.news;
                    break;
            }
            Bitmap icon = getBitmapFromVectorDrawable( this , nRes );

            notificationBuilder.setLargeIcon( icon );
            if ( notificationManager != null )
                notificationManager.notify( Integer.valueOf( NOTIFICATION_CHANNEL_ID ) ,
                        notificationBuilder.build() );
        }
    }

    public static
    Bitmap getBitmapFromVectorDrawable ( Context context , int drawableId ) {
        Drawable drawable = ContextCompat.getDrawable( context , drawableId );

        Bitmap bitmap =
                Bitmap.createBitmap( drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight() ,
                        Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas( bitmap );
        drawable.setBounds( 0 , 0 , canvas.getWidth() , canvas.getHeight() );
        drawable.draw( canvas );

        return bitmap;
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
