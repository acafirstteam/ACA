package am.newway.aca.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.Locale;

import am.newway.aca.BaseActivity;
import am.newway.aca.MainActivity;
import am.newway.aca.R;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import static am.newway.aca.ui.SplashScreen.activity.MAIN;
import static am.newway.aca.ui.SplashScreen.activity.QR;

public
class SplashScreen extends BaseActivity {

    private int nAnimation = 0;
    private String TAG = getClass().getSimpleName();

    @Override
    public
    void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        String lang = Locale.getDefault().getLanguage();
        Log.e( TAG , "onCreate: " + lang  );
        Log.e( TAG , "onCreate123: " + DATABASE.getSettings().getLanguage()  );



        // inflate your main layout here (use RelativeLayout or whatever your root ViewGroup type is
        final ConstraintLayout mainLayout = ( ConstraintLayout ) this.getLayoutInflater()
                .inflate( R.layout.activity_splash_screen , null );

        mainLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                    public
                    void onGlobalLayout () {
                        mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener( this );

                        startSpringAnimation( findViewById( R.id.text_View1 ) , 1 ,
                                new OnAnimationListener() {
                                    @Override
                                    public
                                    void OnAnimationEnded () {
                                        startStudentChecking( nAnimation++ );
                                    }
                                } );
                        startSpringAnimation( findViewById( R.id.text_View2 ) , 2 ,
                                new OnAnimationListener() {
                                    @Override
                                    public
                                    void OnAnimationEnded () {
                                        startStudentChecking( nAnimation++ );
                                    }
                                } );
                        startSpringAnimation( findViewById( R.id.text_View3 ) , 3 ,
                                new OnAnimationListener() {
                                    @Override
                                    public
                                    void OnAnimationEnded () {
                                        startStudentChecking( nAnimation++ );
                                    }
                                } );
                    }
                } );

        setContentView( mainLayout );
        final TextView textView3 = findViewById( R.id.text_View3 );
        Typeface font = Typeface.createFromAsset( getAssets() , "AGREV4.TTF" );
        textView3.setTypeface( font );
    }

    private
    void startStudentChecking ( int nIterator ) {
        if ( nIterator == 2 )
            checkStudent();
    }

    enum activity {
        MAIN,
        QR
    }

    interface OnAnimationListener {
        void OnAnimationEnded ();
    }

    private
    void startSpringAnimation ( View view , int nType , final OnAnimationListener listener ) {
        SpringAnimation springAnimation = new SpringAnimation( view ,
                nType == 1 ? DynamicAnimation.X
                        : nType == 2 ? DynamicAnimation.Y : DynamicAnimation.ROTATION );

        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition( nType == 1 ? view.getX() : view.getY() );
        springForce.setDampingRatio(
                nType == 1 || nType == 3 ? SpringForce.DAMPING_RATIO_HIGH_BOUNCY
                        : SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY );
        springForce.setStiffness( SpringForce.STIFFNESS_VERY_LOW );

        springAnimation.setSpring( springForce );
        springAnimation.setStartVelocity( 30000f );
        springAnimation.start();

        springAnimation.addEndListener( new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public
            void onAnimationEnd ( DynamicAnimation animation , boolean canceled , float value ,
                    float velocity ) {
                if ( listener != null )
                    listener.OnAnimationEnded();

            }
        } );
    }

    private
    void checkStudent () {
        //նոր աշակերտի ավելացնել կամ ստուգել
        String uID = "-1";
        firebaseUser = mAuth.getCurrentUser();
        if ( firebaseUser != null )
            uID = firebaseUser.getUid();

        Student student = new Student( uID );

        FIRESTORE.checkStudent( student , false , new Firestore.OnStudentCheckListener() {
            @Override
            public
            void OnStudentChecked ( Student student ) {

                if ( student != null ) {
                    DATABASE.setStudent( student );
                    if ( student.getType() == 1 ) {
                        startActivity( QR );
                        return;
                    }
                }
                else
                    Log.e( TAG , "OnStudentCheckFailed null" );

                startActivity( MAIN );
            }

            @Override
            public
            void OnStudentCheckFailed ( final String exception ) {
                Log.e( TAG , "OnStudentCheckFailed" );
                startActivity( MAIN );
            }

            @Override
            public
            void OnStudentIdentifier ( final Student student ) {
            }
        } );
    }

    private
    void startActivity ( activity nType ) {
        startActivity( new Intent( SplashScreen.this ,
                nType == MAIN ? MainActivity.class : QrActivity.class ) );
        finish();
    }
}
