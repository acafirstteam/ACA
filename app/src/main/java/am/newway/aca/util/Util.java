package am.newway.aca.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

import am.newway.aca.service.AlertService;
import androidx.core.content.ContextCompat;

public
class Util {
    private static final String TAG = "scheduleJob";

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
        drawable.setColorFilter( Color.RED, PorterDuff.Mode.MULTIPLY );
        drawable.draw( canvas );

        return bitmap;
    }
}
