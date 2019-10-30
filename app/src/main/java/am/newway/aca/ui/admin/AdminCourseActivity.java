package am.newway.aca.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.AdminEditCourseActivity;
import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.admin.AdminCourseAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public
class AdminCourseActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AdminCourseActivity";
    private AdminCourseAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Course> courseItems;

    @Override
    protected
    void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_course );
        setTitle(R.string.courses);

        initNavigationBar( 2 );

        recyclerView = findViewById( R.id.recycler_view_adminPage_id );
        final Button addCourseBtn = findViewById( R.id.admin_add_course_btn_id );
        addCourseBtn.setOnClickListener( this );
        LinearLayoutManager layoutManager =
                new LinearLayoutManager( getApplicationContext() , RecyclerView.VERTICAL , false );
        recyclerView.setLayoutManager( layoutManager );

        FIRESTORE.getCourses( new Firestore.OnCourseReadListener() {
            @Override
            public
            void OnCourseRead ( List<Course> courses ) {
                courseItems = new ArrayList<>( courses );
                adapter = new AdminCourseAdapter(getApplicationContext(), courseItems );
                recyclerView.setAdapter( adapter );
                Log.d( TAG , "-------------------------ListSize = " + courseItems.size() );
            }
        } );
    }

    @Override
    public
    void onClick ( View v ) {
        Intent intent = new Intent(AdminCourseActivity.this, AdminEditCourseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("action", "add");
        bundle.putInt("pos", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public
    boolean onOptionsItemSelected ( @NonNull final MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
