package am.newway.aca.ui.student;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.adapter.admin.AdminStudentAdapter;
import am.newway.aca.template.Student;
import am.newway.aca.util.RecyclerViewMargin;

public class StudentActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private AdminStudentAdapter adminStudentAdapter;
    private CardView cardView;
    private StudentViewModel studentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        progressBar = findViewById(R.id.loading);
        recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        studentViewModel = ViewModelProviders.of( this ).get( StudentViewModel.class );

        RecyclerViewMargin decoration = new RecyclerViewMargin((int) getResources().getDimension(R.dimen.recycler_item_margin));
        recyclerView.addItemDecoration(decoration);
        //setHasOptionsMenu( true );
        RecyclerView.LayoutManager mManager = new LinearLayoutManager( StudentActivity.this, LinearLayoutManager.VERTICAL, false);

        adminStudentAdapter = new AdminStudentAdapter(new ArrayList<Student>(), this);

        recyclerView.setLayoutManager( mManager );
        studentViewModel.getStudents();

        recyclerView.setAdapter( adminStudentAdapter );

        studentViewModel.getData().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                adminStudentAdapter.setStudents( students );
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
