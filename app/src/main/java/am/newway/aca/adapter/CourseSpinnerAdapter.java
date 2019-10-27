package am.newway.aca.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.Course;

public
class CourseSpinnerAdapter extends ArrayAdapter<Course> {

    public
    CourseSpinnerAdapter ( Activity context , int resourceId , int textViewId , List<Course> list ) {

        super( context , resourceId , textViewId , list );
        //        flater = context.getLayoutInflater();
    }

    @Override
    public
    View getView ( int position , View convertView , ViewGroup parent ) {
        return rowView( convertView , position );
    }

    @Override
    public
    View getDropDownView ( int position , View convertView , ViewGroup parent ) {
        return rowView( convertView , position );
    }

    private
    View rowView ( View convertView , int position ) {

        Course course = getItem( position );

        ViewHolder holder;
        View rowview = convertView;
        if ( rowview == null ) {
            holder = new ViewHolder();
            final LayoutInflater flater = ( LayoutInflater ) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE );
            rowview = flater.inflate( R.layout.custom_spinner_layout , null , false );

            holder.txtTitle = rowview.findViewById( R.id.icon_spinner_levl_text );
            holder.imageView = rowview.findViewById( R.id.icon_spinner_levl );
            rowview.setTag( holder );
        }
        else {
            holder = ( ViewHolder ) rowview.getTag();
        }
        holder.imageView.setImageURI( course.getUrl() );
        holder.txtTitle.setText( course.getName() );

        return rowview;
    }

    private
    class ViewHolder {
        TextView txtTitle;
        SimpleDraweeView imageView;
    }
}
