package am.newway.aca;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

//@ReportsCrashes ( mailTo = "acafirst@gmail.com", mode = ReportingInteractionMode.TOAST,
//                  resToastText = R.string.app_name )
public
class App extends Application {
    @Override
    public
    void onCreate () {
        super.onCreate();
        //ACRA.init( this );
        Fresco.initialize( this );
    }
}
