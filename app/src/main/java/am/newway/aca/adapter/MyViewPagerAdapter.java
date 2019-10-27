package am.newway.aca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.common.util.concurrent.ServiceManager;

import am.newway.aca.R;

public class MyViewPagerAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public MyViewPagerAdapter(Context context){
        this.context=context;
    }

    public int[] slideImage={
            R.drawable.back,
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3
    };
    public int[] slide_heading={
            R.string.slide_1_title,
            R.string.slide_2_title,
            R.string.slide_3_title,
            R.string.slide_4_title
    };
    public int[] slide_descs={
            R.string.slide_1_desc,
            R.string.slide_2_desc,
            R.string.slide_3_desc,
            R.string.slide_4_desc
    };

    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view!=(RelativeLayout) object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.infoslide2,container,false);

        ImageView slideImageView=view.findViewById(R.id.image_slide);
        TextView slideHeading=view.findViewById(R.id.slide_heading);
        TextView slideDescription=view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slideImage[position]);
        slideHeading.setText(slide_heading[position]);
        slideDescription.setText(slide_descs[position]);
        container.addView(view);
        return  view;
    }
    public void destroyItem(ViewGroup container,int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}