package am.newway.aca.template;

import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;

public class CoursesActivity extends BaseActivity {

    private GridAdapter adapter;
    private ArrayList<Courses> logos;
    private GridView gridView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_activity_layout);
        logos = new ArrayList<Courses>();
        Courses ob1, ob2, ob3, ob4;
        ob1 = new Courses(R.drawable.android_vector, "Android");
        ob2 = new Courses(R.drawable.ios_vector, "iOS");
        ob3 = new Courses(R.drawable.c_plus_vector, "C++");
        ob4 = new Courses(R.drawable.java_vector, "Java");

        logos.add(ob1);
        logos.add(ob2);
        logos.add(ob3);
        logos.add(ob4);

        gridView = (GridView) findViewById(R.id.gridView_CoursesAct_id);

        adapter = new GridAdapter();
    }
}
