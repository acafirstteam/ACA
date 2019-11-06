package am.newway.aca.ui.admin.student;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.fasterxml.jackson.databind.ObjectMapper;

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
class AdminStudentActivity extends BaseActivity {
    public static final int UPDATE_STUDENT_LIST = 1;
    private ProgressBar progressBar;
    private AdminStudentAdapter adminStudentAdapter;
    private AdminStudentViewModel studentViewModel;
    public static final int PERMISSIONS_REQUEST_CALL_PHONE = 1;

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
                ViewModelProviders.of( this ).get( AdminStudentViewModel.class );

        RecyclerViewMargin decoration = new RecyclerViewMargin(
                ( int ) getResources().getDimension( R.dimen.recycler_item_margin2 ), 1 );
        recyclerView.addItemDecoration( decoration );
        //setHasOptionsMenu( true );
        RecyclerView.LayoutManager mManager =
                new LinearLayoutManager( AdminStudentActivity.this , LinearLayoutManager.VERTICAL ,
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    adminStudentAdapter.callPhone( );
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected
    void onActivityResult ( final int requestCode , final int resultCode ,
            @Nullable final Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );

        if(requestCode == UPDATE_STUDENT_LIST && data != null){
            ObjectMapper mapper = new ObjectMapper();
            Student student = mapper.convertValue( data.getSerializableExtra( "map" ),
                    Student.class );
            adminStudentAdapter.setStudent( student, resultCode );
        }
    }
}
