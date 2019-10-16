package am.newway.aca.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.adapter.CourseAdapter;
import am.newway.aca.template.Course;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.BaseFragment;
import am.newway.aca.util.RecyclerViewMargin;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public
class HomeFragment extends BaseFragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private CourseAdapter courseAdapter;
    private CardView cardView;
    private TextView time;
    private TextView course;

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        homeViewModel = ViewModelProviders.of( this ).get( HomeViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home , container , false );

        cardView = root.findViewById( R.id.user_card );
        time = root.findViewById( R.id.time );
        course = root.findViewById( R.id.course );

        progressBar = root.findViewById( R.id.loading );
        recyclerView = root.findViewById( R.id.recycler_view );
        recyclerView.setHasFixedSize( true );
        recyclerView.setItemViewCacheSize( 20 );
        recyclerView.setDrawingCacheEnabled( true );
        recyclerView.setDrawingCacheQuality( View.DRAWING_CACHE_QUALITY_HIGH );

        RecyclerViewMargin decoration = new RecyclerViewMargin(
                ( int ) getResources().getDimension( R.dimen.recycler_item_margin ) );
        recyclerView.addItemDecoration( decoration );
        //setHasOptionsMenu( true );

        if(DATABASE.getVisit() != null)
            addNewVisit( DATABASE.getVisit() );

        courseAdapter = new CourseAdapter( new ArrayList<Course>() , recyclerView ,
                new CourseAdapter.OnOrientationChangingListener() {
                    @Override
                    public
                    void OnChanged ( boolean isLarge , int position ) {
                        RecyclerView.LayoutManager mManager =
                                new GridLayoutManager( getActivity() , 3 );
                        recyclerView.setLayoutManager( mManager );
                        recyclerView.scrollToPosition( position );
                    }
                } );
        recyclerView.setAdapter( courseAdapter );

        homeViewModel.getData().observe( this , new Observer<List<Course>>() {
            @Override
            public
            void onChanged ( @Nullable List<Course> courses ) {
                courseAdapter.setProducts( courses );
                progressBar.setVisibility( View.GONE );
            }
        } );
        return root;
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        RecyclerView.LayoutManager mManager = new GridLayoutManager( getActivity() , 3 );
        mManager.setItemPrefetchEnabled( true );
        recyclerView.setLayoutManager( mManager );
        homeViewModel.getCourses();
    }

    @Override
    public
    void onPrepareOptionsMenu ( Menu menu ) {
        //menu.findItem( R.id.app_bar_search ).setVisible( true );
        //menu.findItem( R.id.action_filter ).setVisible( true );
        super.onPrepareOptionsMenu( menu );
    }

    public
    void addNewVisit ( Visit visit ) {
        if ( visit != null ) {
            time.setText( visit.getDateTime() );
            course.setText( DATABASE.getStudent().getCourse() );
            cardView.setVisibility( View.VISIBLE );
        }
    }

    public
    void completeVisit () {
        if ( cardView.getVisibility() == View.VISIBLE )
            cardView.setVisibility( View.GONE );
    }
}