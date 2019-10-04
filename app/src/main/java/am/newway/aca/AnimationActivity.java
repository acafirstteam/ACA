package am.newway.aca;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import am.newway.aca.database.Firestore;
import am.newway.aca.template.Student;
import am.newway.aca.ui.QrActivity;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import static am.newway.aca.AnimationActivity.activity.MAIN;
import static am.newway.aca.AnimationActivity.activity.QR;

public class AnimationActivity extends BaseActivity {
    private int nAnimation = 0;
    private TextView textView3;

    @Override
    public void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        // inflate your main layout here (use RelativeLayout or whatever your root ViewGroup type is
        final ConstraintLayout mainLayout = ( ConstraintLayout ) this.getLayoutInflater().inflate( R.layout.animation , null );

        // set a global layout listener which will be called when the layout pass is completed and the view is drawn
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout () {
                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener( this );

                startSpringAnimation( findViewById( R.id.text_View1 ) , 1 , new OnAnimationListener() {
                    @Override
                    public void OnAnimationEnded () {
                        if ( nAnimation == 2 )
                            checkStudent();
                        else
                            nAnimation++;
                    }
                } );
                startSpringAnimation( findViewById( R.id.text_View2 ) , 2 , new OnAnimationListener() {
                    @Override
                    public void OnAnimationEnded () {
                        if ( nAnimation == 2 )
                            checkStudent();
                        else
                            nAnimation++;
                    }
                } );
                startSpringAnimation( findViewById( R.id.text_View3 ) , 3 , new OnAnimationListener() {
                    @Override
                    public void OnAnimationEnded () {
                        if ( nAnimation == 2 )
                            checkStudent();
                        else
                            nAnimation++;
                    }
                } );
            }
        } );

        setContentView( mainLayout );
        textView3 = findViewById( R.id.text_View3 );
        Typeface font = Typeface.createFromAsset( getAssets() , "AGREV4.TTF" );
        textView3.setTypeface( font );
    }

    enum activity {
        MAIN,
        QR
    }

    interface OnAnimationListener {
        void OnAnimationEnded ();
    }

    private void startSpringAnimation (
            View view , int nType , final OnAnimationListener listener
    ) {
        SpringAnimation springAnimation = new SpringAnimation( view , nType == 1 ? DynamicAnimation.X : nType == 2 ? DynamicAnimation.Y : DynamicAnimation.ROTATION );

        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition( nType == 1 ? view.getX() : view.getY() );
        springForce.setDampingRatio( nType == 1 || nType == 3?
                SpringForce.DAMPING_RATIO_HIGH_BOUNCY : SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY );
        springForce.setStiffness( SpringForce.STIFFNESS_LOW );

        springAnimation.setSpring( springForce );
        springAnimation.setStartVelocity( 30000f );
        springAnimation.start();

        springAnimation.addEndListener( new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd (
                    DynamicAnimation animation , boolean canceled , float value , float velocity
            ) {
                if ( listener != null )
                    listener.OnAnimationEnded();

            }
        } );
    }

    private void checkStudent () {
        //նոր աշակերտի ավելացնել կամ ստուգել
        Student st = new Student( 21 , "aaa123@bbb.com" , "Name" , "093381919" , "www.picture.com" , "Surname" );
        st.setId( App.userID );
        FIRESTORE.checkStudent( st , false , new Firestore.OnStudentCheckListener() {
            @Override
            public void OnStudentChecked ( final Student student ) {
                if ( student != null ) {
                    if ( student.getType() == 1 ) {
                        startActivity( QR );
                        return;
                    }
                }
                startActivity( MAIN );
            }

            @Override
            public void OnStudentCheckFailed ( final String exception ) {
                startActivity( MAIN );
            }

            @Override
            public void OnStudentIdentifier ( final Student student ) {

            }
        } );
    }

    private void startActivity ( activity nType ) {
        startActivity( new Intent( AnimationActivity.this , nType == MAIN ? MainActivity.class : QrActivity.class ) );
        finish();
    }
}
