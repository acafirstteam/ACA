package am.newway.aca.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import am.newway.aca.service.AlertService;

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
}
