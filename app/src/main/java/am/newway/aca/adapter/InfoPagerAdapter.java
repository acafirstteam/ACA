package am.newway.aca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import am.newway.aca.R;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public
class InfoPagerAdapter extends PagerAdapter {

    private int[] slideImage =
            { R.drawable.ic_language_black_24dp , R.drawable.slide1 , R.drawable.slide2 , R.drawable.slide3 };
    private String[] slide_heading;
    private String[] slide_desc;

    public
    InfoPagerAdapter ( @NonNull Context context ) {

        slide_heading = context.getResources().getStringArray( R.array.info_title );

        slide_desc = context.getResources().getStringArray( R.array.info_description );
    }

    @Override
    public
    int getCount () {
        return slide_heading.length;
    }

    @Override
    public
    boolean isViewFromObject ( @NonNull View view , @NonNull Object object ) {
        return view == object;
    }

    @NonNull
    @Override
    public
    Object instantiateItem ( @NonNull ViewGroup container , int position ) {

        View view = LayoutInflater.from( container.getContext() )
                .inflate( R.layout.info_item , container , false );

        ImageView slideImageView = view.findViewById( R.id.image_slide );
        TextView slideHeading = view.findViewById( R.id.slide_heading );
        TextView slideDescription = view.findViewById( R.id.slide_desc );
        LottieAnimationView lottieAnimationView = view.findViewById( R.id.animation_view );

        slideImageView.setImageResource( slideImage[position] );
        slideHeading.setText( slide_heading[position] );
        slideDescription.setText( slide_desc[position] );

        slideImageView.setVisibility( position != 0 ? View.VISIBLE : View.GONE );
        lottieAnimationView.setVisibility( position == 0 ? View.VISIBLE : View.GONE );
        container.addView( view );

        return view;
    }

    public
    void destroyItem ( ViewGroup container , int position , @NonNull Object object ) {
        container.removeView( ( LinearLayout ) object );
    }
}