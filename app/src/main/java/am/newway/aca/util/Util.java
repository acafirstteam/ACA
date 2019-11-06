package am.newway.aca.util;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import am.newway.aca.R;
import am.newway.aca.service.AlertService;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public
class Util {
    private static final String TAG = "Util";

    public static
    void scheduleJob ( Context context ) {

        Log.e( TAG , "scheduleJob: is starting" );
        ComponentName serviceComponent = new ComponentName( context , AlertService.class );

        JobInfo.Builder builder = new JobInfo.Builder( 0 , serviceComponent );
        builder.setMinimumLatency( 0 ); // Wait at least 30s
        //        builder.setOverrideDeadline(60 * 1000); // Maximum delay 60s

        JobScheduler jobScheduler =
                ( JobScheduler ) context.getSystemService( context.JOB_SCHEDULER_SERVICE );
        if ( jobScheduler != null )
            jobScheduler.schedule( builder.build() );
    }

    public static
    Bitmap getBitmapFromVectorDrawable ( Context context , int drawableId ) {
        Drawable drawable = ContextCompat.getDrawable( context , drawableId );

        Bitmap bitmap =
                Bitmap.createBitmap( drawable.getIntrinsicWidth() , drawable.getIntrinsicHeight() ,
                        Bitmap.Config.ARGB_8888 );
        Canvas canvas = new Canvas( bitmap );
        drawable.setBounds( 0 , 0 , canvas.getWidth() , canvas.getHeight() );
        drawable.setColorFilter( Color.RED , PorterDuff.Mode.MULTIPLY );
        drawable.draw( canvas );

        return bitmap;
    }

    public static
    void callPhone ( final Context context , final String phone ) {

        View view =
                LayoutInflater.from( context ).inflate( R.layout.phone_permission_dialog , null );
        final LottieAnimationView anim = view.findViewById( R.id.animation_view );
        final TextView text = view.findViewById( R.id.text );
        anim.setRepeatCount( LottieDrawable.INFINITE );
        anim.setAnimation( "call.json" );
        anim.playAnimation();
        text.setText( R.string.like_to_call );

        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setView( view )
                .setPositiveButton( context.getString( R.string.call ) ,
                        new DialogInterface.OnClickListener() {
                            @SuppressLint ( "MissingPermission" )
                            @Override
                            public
                            void onClick ( final DialogInterface dialogInterface , final int i ) {
                                context.startActivity( new Intent( Intent.ACTION_CALL ,
                                        Uri.parse( "tel:" + phone ) ) );
                            }
                        } )
                .setNegativeButton( context.getString( R.string.no ) , null )
                .show();
    }

    public static
    void sendEmail (Context context, String sendEmail) {
        if ( !sendEmail.isEmpty() ) {
            Intent intent = new Intent( Intent.ACTION_SENDTO );
            intent.setData( Uri.parse( "mailto:" + sendEmail ) );
            intent.putExtra( Intent.EXTRA_SUBJECT , "ACA Administration" );
            context.startActivity(
                    Intent.createChooser( intent , "choose an studentEmail client" ) );
        }
        else {
            Toast.makeText( context , "Student's studentEmail does not right" ,
                    Toast.LENGTH_SHORT ).show();
        }
    }
}
