package am.newway.aca.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.admin.AdminCourseAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.util.RecyclerViewMargin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public
class AdminCourseActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private AdminCourseAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected
    void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_course );
        setTitle( R.string.courses );

        initNavigationBar( 2 );

        recyclerView = findViewById( R.id.recycler_view_adminPage_id );
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                Intent intent =
                        new Intent( AdminCourseActivity.this , AdminCourseEditActivity.class );
                intent.putExtra( "action" , "add" );
                intent.putExtra( "pos" , 0 );
                intent.putExtra( "groups" , adapter.getGroups() );
                startActivity( intent );
            }
        } );

        LinearLayoutManager layoutManager =
                new LinearLayoutManager( this , RecyclerView.VERTICAL , false );
        recyclerView.setLayoutManager( layoutManager );
        RecyclerViewMargin decoration = new RecyclerViewMargin(
                ( int ) getResources().getDimension( R.dimen.recycler_item_margin2 ), 1 );
        recyclerView.addItemDecoration( decoration );

        FIRESTORE.getCourses( new Firestore.OnCourseReadListener() {
            @Override
            public
            void OnCourseRead ( List<Course> courses ) {
                adapter = new AdminCourseAdapter( AdminCourseActivity.this ,
                        ( ArrayList<Course> ) courses , DATABASE.getSettings().getLanguage() );
                recyclerView.setAdapter( adapter );
                Log.d( TAG , "-------------------------ListSize = " + courses.size() );
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
