package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class AnimationActivity extends BaseActivity {
    TextView tv;
  //  LottieAnimationView load;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        tv = findViewById(R.id.text_View);
        Animation combo = AnimationUtils.loadAnimation(this, R.anim.combo);
        tv.startAnimation(combo);
        startSpringAnimation(tv);


       // load=(LottieAnimationView)findViewById(R.id.av_from_code);
        //load.setAnimation("");
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);

    }

    private void startSpringAnimation( View view){
        // create an animation for your view and set the property you want to animate
        SpringAnimation animation = new SpringAnimation(view, SpringAnimation.X);
        // create a spring with desired parameters
        SpringForce spring = new SpringForce();
        // can also be passed directly in the constructor
        spring.setFinalPosition(100f);
        // optional, default is STIFFNESS_MEDIUM
        spring.setStiffness(SpringForce.STIFFNESS_LOW);
        // optional, default is DAMPING_RATIO_MEDIUM_BOUNCY
        spring.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        // set your animation's spring
        animation.setSpring(spring);
        // animate!
        animation.start();
    }
}
