package am.newway.aca.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import am.newway.aca.R;
import am.newway.aca.adapter.InfoPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public
class InfoActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private int mCurrentPage = 0;
    private Button mNextBtn;
    private Button mBackBtn;
    private InfoPagerAdapter sliderAdapter;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_info );

        mSlideViewPager = findViewById( R.id.view_pager );
        mNextBtn = findViewById( R.id.btn_next );
        mBackBtn = findViewById( R.id.btn_skip );

        sliderAdapter = new InfoPagerAdapter( this );
        mSlideViewPager.setAdapter( sliderAdapter );
        mSlideViewPager.setOffscreenPageLimit( 4 );

        mSlideViewPager.addOnPageChangeListener( viewListener );

        mNextBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View v ) {
                if ( mCurrentPage == sliderAdapter.getCount() - 1 )
                    finish();
                else
                    mSlideViewPager.setCurrentItem( mCurrentPage + 1 );
            }
        } );
        mBackBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View v ) {
                if ( mCurrentPage == 0 )
                    finish();
                else
                    mSlideViewPager.setCurrentItem( mCurrentPage - 1 );
            }
        } );

        final DotsIndicator dotsIndicator = findViewById( R.id.dots_indicator );
        dotsIndicator.setViewPager( mSlideViewPager );
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public
        void onPageScrolled ( int i , float v , int i1 ) {
        }

        @Override
        public
        void onPageSelected ( int i ) {
            mCurrentPage = i;
            mNextBtn.setText( i == sliderAdapter.getCount() - 1 ? getString( R.string.finish )
                    : getString( R.string.next ) );
            mBackBtn.setText( i == 0 ? getString( R.string.skip ) : getString( R.string.back ) );
        }

        @Override
        public
        void onPageScrollStateChanged ( int i ) {
        }
    };

    @Override
    public
    void onBackPressed () {
    }
}