package am.newway.aca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am.newway.aca.template.Course;
import am.newway.aca.ui.home.HomeFragmentChild;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

public
class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<HomeFragmentChild> homeFragmentChildList;
    private Map<Integer, String> courseGroup;
    private int nGroup = -1;
    private String lang;

    public
    ViewPagerAdapter ( androidx.fragment.app.FragmentManager fm, String lang ) {
        super( fm );
        this.lang = lang;
        homeFragmentChildList = new ArrayList<>();
    }

    @Override
    public
    Fragment getItem ( int position ) {
        return homeFragmentChildList.get( position );
    }

    @Override
    public
    int getCount () {
        return homeFragmentChildList.size();
    }

    public
    String getGroup (int nIndex) {
        return courseGroup.get( nIndex );
    }

    @SuppressLint ( "UseSparseArrays" )
    public
    void setCourses ( final List<Course> courses, Context context ) {

        courseGroup = new HashMap<>();
        homeFragmentChildList.clear();

        for(Course co : courses){
            if(co.getGroup() != nGroup){
                HomeFragmentChild child = new HomeFragmentChild();

                Bundle args = new Bundle();
                args.putInt("index", homeFragmentChildList.size());
                args.putString("lang", lang);
                child.setArguments(args);
                child.setContext( context );

                homeFragmentChildList.add( child );
                nGroup = co.getGroup();
                Map<String, Object> map = co.getGroup_name();
                courseGroup.put( nGroup, map.get( lang ).toString() );
                homeFragmentChildList.get( homeFragmentChildList.size() - 1 ).setValues( courses );
            }
        }
        notifyDataSetChanged();
    }
}