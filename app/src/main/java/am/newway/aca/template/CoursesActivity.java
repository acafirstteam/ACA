package am.newway.aca.template;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;

public class CoursesActivity extends BaseActivity {

    private final String TAG = "CoursecActivity";

    private GridAdapter adapter;
    private ArrayList<Courses> logos;
    private GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_activity_layout);

        logos = new ArrayList<Courses>();

        Courses android, iOS, cPlus, java,cSharp, css3, javaScript, nodeJS, php, python, react;

        android = new Courses(R.drawable.android_vector, "Android");
        iOS = new Courses(R.drawable.ios_vector, "iOS");
        cPlus = new Courses(R.drawable.c_plus_vector, "C++");
        java = new Courses(R.drawable.java_vector, "Java");
        cSharp = new Courses(R.drawable.csharp, "C#");
        css3 = new Courses(R.drawable.css3, "CSS3");
        javaScript = new Courses(R.drawable.java_script, "JavaScript");
        nodeJS = new Courses(R.drawable.nodejs, "NodeJS#");
        php = new Courses(R.drawable.php, "PHP");
        python = new Courses(R.drawable.python, "Python");
        react = new Courses(R.drawable.react, "React");

        logos.add(android);
        logos.add(iOS);
        logos.add(cPlus);
        logos.add(java);
        logos.add(cSharp);
        logos.add(css3);
        logos.add(javaScript);
        logos.add(nodeJS);
        logos.add(php);
        logos.add(python);
        logos.add(react);

        gridView = (GridView) findViewById(R.id.gridView_CoursesAct_id);

        adapter = new GridAdapter(this, logos);

        gridView.setAdapter(adapter);
    }
}
