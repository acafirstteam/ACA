package am.newway.aca;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {
   TextView tv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        tv=(TextView)findViewById(R.id.animation_textview);
        Animation combo=AnimationUtils.loadAnimation(this,R.anim.combo);
        tv.startAnimation(combo);
    }
}
