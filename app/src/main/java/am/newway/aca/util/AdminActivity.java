package am.newway.aca.util;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.AdminPageAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;

public class AdminActivity extends BaseActivity {

    private final String TAG = "AdminActivity";
    private AdminPageAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Course> courseItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_view_adminPage_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(),
                RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);


         FIRESTORE.getCuorces(new Firestore.OnCourseReadListener() {
             @Override
             public void OnCourseRead(List<Course> courses) {
                 courseItems = new ArrayList<Course>(courses);
                 adapter = new AdminPageAdapter(courseItems);
                 recyclerView.setAdapter(adapter);
             }
         });


    }
}
