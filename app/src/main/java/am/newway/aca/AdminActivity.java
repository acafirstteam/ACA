package am.newway.aca;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.ArrayList;
import java.util.List;
import am.newway.aca.adapter.AdminPageAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;

public class AdminActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AdminActivity";
    private AdminPageAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Course> courseItems;
    private Button addCourseBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_view_adminPage_id);
        addCourseBtn = (Button) findViewById(R.id.admin_add_course_btn_id);
        addCourseBtn.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(),
                RecyclerView.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);


         FIRESTORE.getCuorces(new Firestore.OnCourseReadListener() {
             @Override
             public void OnCourseRead(List<Course> courses) {
                 courseItems = new ArrayList<Course>(courses);
                 adapter = new AdminPageAdapter(getApplicationContext(), courseItems);
                 recyclerView.setAdapter(adapter);
                 Log.d(TAG,"-------------------------ListSize = " + courseItems.size());
             }
         });



    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(AdminActivity.this, AdminEditCourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("action", "add");
        bundle.putInt("pos", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
