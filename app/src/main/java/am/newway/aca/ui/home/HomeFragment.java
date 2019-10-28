package am.newway.aca.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.List;

import am.newway.aca.MainActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.ViewPagerAdapter;
import am.newway.aca.template.Course;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.fragments.BaseFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public
class HomeFragment extends BaseFragment {
    private HomeViewModel homeViewModel;
    private CardView cardView;
    private TextView time;
    private TextView course;
    private ViewPagerAdapter pagerAdapter;

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        homeViewModel = ViewModelProviders.of( this ).get( HomeViewModel.class );
        View root = inflater.inflate( R.layout.fragment_home , container , false );

        cardView = root.findViewById( R.id.user_card );
        time = root.findViewById( R.id.time );
        course = root.findViewById( R.id.course );
        final ViewPager viewPager = root.findViewById( R.id.view_pager );

        pagerAdapter = new ViewPagerAdapter( getChildFragmentManager() ,
                DATABASE.getSettings().getLanguage() );
        viewPager.setAdapter( pagerAdapter );
        viewPager.setOffscreenPageLimit( 3 );

        final DotsIndicator dotsIndicator = root.findViewById( R.id.dots_indicator );
        dotsIndicator.setViewPager( viewPager );

        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public
            void onPageScrolled ( final int position , final float positionOffset ,
                    final int positionOffsetPixels ) {

            }

            @Override
            public
            void onPageSelected ( final int position ) {
                setSubTitle(pagerAdapter.getGroup( position ));
            }

            @Override
            public
            void onPageScrollStateChanged ( final int state ) {

            }
        } );

        if ( DATABASE.getVisit() != null )
            addNewVisit( DATABASE.getVisit() );

        homeViewModel.getData().observe( this , new Observer<List<Course>>() {
            @Override
            public
            void onChanged ( @Nullable List<Course> courses ) {
                if ( courses != null ) {
                    pagerAdapter.setCourses( courses , getActivity() );
                    setSubTitle(pagerAdapter.getGroup( 0 ));
                }
            }
        } );
        return root;
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        homeViewModel.getCourses();
    }

    private void setSubTitle(String text){

        String strTitle = getResources().getString( R.string.menu_home );
        Activity activity = getActivity();
        if ( activity != null ) {
            ActionBar mActionBar = ( ( MainActivity ) activity ).getSupportActionBar();
            if ( mActionBar != null )
                mActionBar.setSubtitle( text.replace( strTitle.toUpperCase(), "" ) );
        }
    }

    @Override
    public
    void onPrepareOptionsMenu ( @NonNull Menu menu ) {
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