package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AnimationActivity extends AppCompatActivity {
    TextView tv;
  //  LottieAnimationView load;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation);

        tv = (TextView) findViewById(R.id.text_View);
        Animation combo = AnimationUtils.loadAnimation(this, R.anim.combo);
        tv.startAnimation(combo);

       // load=(LottieAnimationView)findViewById(R.id.av_from_code);
        //load.setAnimation("");
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);

    }
}
