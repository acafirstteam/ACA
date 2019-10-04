package am.newway.aca.anim;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class PositionSpringAnimation {
    private View animatedView;

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private float dX;
    private float dY;

    public PositionSpringAnimation ( final View animatedView ) {
        if ( animatedView != null ) {
            this.animatedView = animatedView;
            animatedView.getViewTreeObserver().addOnGlobalLayoutListener( globalLayoutListener );
            this.animatedView.setOnTouchListener( touchListener );
        }
    }

    // create X and Y animations for view's initial position once it's known
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout () {
            xAnimation = SpringAnimationUtil.createSpringAnimation( animatedView ,
                    SpringAnimation.X , animatedView.getX() , SpringForce.STIFFNESS_MEDIUM , SpringForce.DAMPING_RATIO_HIGH_BOUNCY );
            yAnimation = SpringAnimationUtil.createSpringAnimation( animatedView ,
                    SpringAnimation.Y , animatedView.getY() , SpringForce.STIFFNESS_MEDIUM , SpringForce.DAMPING_RATIO_HIGH_BOUNCY );
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch ( View v , MotionEvent event ) {
            switch ( event.getActionMasked() ) {
                case MotionEvent.ACTION_DOWN:
                    // capture the difference between view's top left corner and touch point
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    // cancel animations
                    xAnimation.cancel();
                    yAnimation.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //  a different approach would be to change the view's LayoutParams.
                    animatedView.animate().x( event.getRawX() + dX ).y( event.getRawY() + dY ).setDuration( 0 ).start();
                    break;
                case MotionEvent.ACTION_UP:
                    xAnimation.start();
                    yAnimation.start();
                    break;
            }
            return true;
        }
    };

    static class SpringAnimationUtil {

        static SpringAnimation createSpringAnimation (
                View view , DynamicAnimation.ViewProperty property , float finalPosition ,
                float stiffness , float dampingRatio
        ) {
            SpringAnimation animation = new SpringAnimation( view , property );
            SpringForce springForce = new SpringForce( finalPosition );
            springForce.setStiffness( stiffness );
            springForce.setDampingRatio( dampingRatio );
            animation.setSpring( springForce );
            return animation;
        }
    }
}
