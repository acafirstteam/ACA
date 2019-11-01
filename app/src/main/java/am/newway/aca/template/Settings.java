package am.newway.aca.template;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public
class Settings {

    private final String TAG = getClass().getSimpleName();
    private boolean login;
    private boolean notification;
    private boolean firstAnimation;
    private boolean firstStart = false;
    private String language;
    private OnSettingsChangeListener listener;
    private int notifId = 0;

    public
    Settings ( boolean login , boolean notification , String language , boolean anim ) {

        this.login = login;
        this.notification = notification;
        this.language = language;
        this.firstAnimation = anim;
    }

    public
    void addOnSettingsChangeListener ( OnSettingsChangeListener listener ) {
        this.listener = listener;
    }

    public
    Settings () {

    }

    public
    int getNotifId () {
        return notifId;
    }

    public
    void setNotifId ( final int notifId ) {
        this.notifId = notifId;
        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    boolean isFirstStart () {
        return firstStart;
    }

    public
    void setFirstStart ( final boolean firstStart ) {
        this.firstStart = firstStart;
        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    boolean isFirstAnimation () {
        return firstAnimation;
    }

    public
    void setFirstAnimation ( final boolean firstAnimation ) {
        this.firstAnimation = firstAnimation;
        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    boolean isLogin () {
        return login;
    }

    public
    void setLogin ( boolean login ) {
        this.login = login;
        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    boolean isNotification () {
        return notification;
    }

    public
    void setNotification ( boolean notification ) {
        this.notification = notification;
        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    String getLanguage () {
        return language == null ? "" : language;
    }

    public
    void setLanguage ( String language , Context context ) {
        this.language = language;

        Log.e( TAG , "setLanguage: " + language );

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale( new Locale( language.toLowerCase() ) );
        res.updateConfiguration( conf , dm );

        if ( listener != null )
            listener.OnSettingsChanged();
    }

    public
    interface OnSettingsChangeListener {
        void OnSettingsChanged ();
    }
}
