package am.newway.aca.template;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import java.util.Locale;

public
class Settings {

    private boolean login;
    private boolean notification;
    private String language;

    public
    Settings ( boolean login , boolean notification , String language ) {

        this.login = login;
        this.notification = notification;
        this.language = language;
    }

    public Settings(){

    }

    public
    boolean isLogin () {
        return login;
    }

    public
    void setLogin ( boolean login ) {
        this.login = login;
    }

    public
    boolean isNotification () {
        return notification;
    }

    public
    void setNotification ( boolean notification ) {
        this.notification = notification;
    }

    public
    String getLanguage () {
        return language == null ? "" : language;
    }

    public
    void setLanguage ( String language , Context context ) {
        this.language = language;

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale( new Locale( language.toLowerCase() ) );
        res.updateConfiguration( conf , dm );
    }
}
