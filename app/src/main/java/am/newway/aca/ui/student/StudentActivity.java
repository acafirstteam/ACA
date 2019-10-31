package am.newway.aca.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.admin.AdminStudentAdapter;
import am.newway.aca.template.Student;
import am.newway.aca.util.RecyclerViewMargin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public
class StudentActivity extends BaseActivity {
    private ProgressBar progressBar;
    private AdminStudentAdapter adminStudentAdapter;
    private StudentViewModel studentViewModel;

    @Override
    protected
    void onNewIntent ( Intent intent ) {
        super.onNewIntent( intent );
        studentViewModel.getStudents(intent.getBooleanExtra( "new" , false ));
    }

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_student );

        initNavigationBar(2);

        Intent intent = getIntent();

        progressBar = findViewById( R.id.loading );
        final RecyclerView recyclerView = findViewById( R.id.recycler_view2 );
        recyclerView.setHasFixedSize( true );
        recyclerView.setItemViewCacheSize( 20 );
        recyclerView.setDrawingCacheEnabled( true );
        recyclerView.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_HIGH );
        studentViewModel =
                ViewModelProviders.of( this ).get( StudentViewModel.class );

        RecyclerViewMargin decoration = new RecyclerViewMargin(
                ( int ) getResources().getDimension( R.dimen.recycler_item_margin ) );
        recyclerView.addItemDecoration( decoration );
        //setHasOptionsMenu( true );
        RecyclerView.LayoutManager mManager =
                new LinearLayoutManager( StudentActivity.this , LinearLayoutManager.VERTICAL ,
                        false );

        adminStudentAdapter = new AdminStudentAdapter( new ArrayList<Student>() , this );

        recyclerView.setLayoutManager( mManager );

        studentViewModel.getStudents(intent.getBooleanExtra( "new" , false ));

        recyclerView.setAdapter( adminStudentAdapter );

        studentViewModel.getData().observe( this , new Observer<List<Student>>() {
            @Override
            public
            void onChanged ( @Nullable List<Student> students ) {
                adminStudentAdapter.setStudents( students );
                progressBar.setVisibility( View.GONE );
            }
        } );
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
