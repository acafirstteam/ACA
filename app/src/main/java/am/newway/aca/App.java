package am.newway.aca;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    //public static String userID = "147259";
    //public static boolean isAdmin = false;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
