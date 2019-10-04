package am.newway.aca.template;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import am.newway.aca.R;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Courses> logos;

    public GridAdapter(Context context, ArrayList<Courses> logos){
        this.context = context;
        this.logos = logos;
    }


    @Override
    public int getCount() {
        return logos.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Courses course = logos.get(position);

        if (convertView == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.grid_item_view_courses,null);
        }

        final ImageView imageCourse = (ImageView) convertView.findViewById(R.id.image_grid_item_courses);
        final TextView nameCourse = (TextView) convertView.findViewById(R.id.name_grid_item_courses);

        imageCourse.setImageResource(course.getImage());
        nameCourse.setText(course.getName());


        return convertView;
    }
}
