package am.newway.aca.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.adapter.CourseAdapter;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.Course;
import am.newway.aca.util.RecyclerViewMargin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public
class HomeFragmentChild extends Fragment {
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private List<Course> courses;
    private Context context;
    private int nIndex = 0;
    private String lang;

    @Override
    public
    void onCreate ( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        Bundle bundle = getArguments();
        if ( bundle != null ) {
            this.nIndex = bundle.getInt( "index" , -1 );
            this.lang = bundle.getString( "lang" );
        }

        if(courses != null && courses.size() > 0) {
            List<Course> co = new ArrayList<>();
            for ( Course course : courses ) {
                if ( course.getGroup() == nIndex )
                    co.add( course );
            }
            this.courses = co;
        }
    }

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_home_child , container , false );

        final ProgressBar progressBar = root.findViewById( R.id.loading );
        recyclerView = root.findViewById( R.id.recycler_view );
        recyclerView.setHasFixedSize( true );
        recyclerView.setItemViewCacheSize( 20 );
        recyclerView.setDrawingCacheEnabled( true );
        recyclerView.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_HIGH );

        courseAdapter =
                new CourseAdapter( context , new RecyclerViewAnimator( recyclerView ) );
        RecyclerViewMargin decoration = new RecyclerViewMargin(
                ( int ) getResources().getDimension( R.dimen.recycler_item_margin ) );
        recyclerView.addItemDecoration( decoration );
        //setHasOptionsMenu( true );

        courseAdapter.setCourses( courses );
        courseAdapter.setLanguage( lang );

        recyclerView.setAdapter( courseAdapter );

        progressBar.setVisibility( View.GONE );

        return root;
    }

    public
    void setValues ( List<Course> courses ) {
        this.courses = courses;
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        RecyclerView.LayoutManager mManager = new GridLayoutManager( getActivity() , 3 );
        mManager.setItemPrefetchEnabled( true );
        //RecyclerView.LayoutManager mManager = new LinearLayoutManager( getActivity() );

        recyclerView.setLayoutManager( mManager );
    }

    @Override
    public
    void onPrepareOptionsMenu ( Menu menu ) {
        //menu.findItem( R.id.app_bar_search ).setVisible( true );
        //menu.findItem( R.id.action_filter ).setVisible( true );
        super.onPrepareOptionsMenu( menu );
    }

    public
    void setValues ( final List<Course> courses , final CourseAdapter courseAdapter ) {
        this.courses = courses;
        this.courseAdapter = courseAdapter;
    }

    public
    void setContext ( Context context ) {
        this.context = context;
    }
}