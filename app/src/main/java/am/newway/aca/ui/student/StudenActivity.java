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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.adapter.StudentAdapter;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;
import am.newway.aca.ui.home.HomeViewModel;
import am.newway.aca.util.RecyclerViewMargin;

public class StudenActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private StudentAdapter studentAdapter;
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
        RecyclerView.LayoutManager mManager = new LinearLayoutManager(StudenActivity.this, LinearLayoutManager.VERTICAL, false);

        studentAdapter = new StudentAdapter(new ArrayList<Student>(), recyclerView);

        recyclerView.setLayoutManager( mManager );
        studentViewModel.getStudents();

        recyclerView.setAdapter(studentAdapter);

        studentViewModel.getData().observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(@Nullable List<Student> students) {
                studentAdapter.setStudents( students );
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
