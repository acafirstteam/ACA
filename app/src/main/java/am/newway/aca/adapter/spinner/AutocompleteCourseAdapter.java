package am.newway.aca.adapter.spinner;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.Course;
import androidx.annotation.NonNull;

public
class AutocompleteCourseAdapter extends ArrayAdapter<Course> {
    private List<Course> courses;
    private List<Course> coursesAll;
    private List<Course> suggestions;
    private Context context;

    @SuppressWarnings( "unchecked" )
    public
    AutocompleteCourseAdapter ( Context context , int layout , ArrayList<Course> list ) {

        super( context , layout , list );
        this.context = context;
        this.courses = list;
        this.coursesAll = (ArrayList<Course>)list.clone();
        this.suggestions = new ArrayList<>();
    }

    @NonNull
    @Override
    public
    Filter getFilter () {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {

        @Override
        public
        String convertResultToString ( Object resultValue ) {
            return ( ( Course ) ( resultValue ) ).getName();
        }

        @Override
        protected
        FilterResults performFiltering ( CharSequence constraint ) {
            if ( constraint != null ) {
                suggestions.clear();
                for ( Course course : coursesAll ) {
                    if ( course.getName()
                            .toLowerCase()
                            .startsWith( constraint.toString().toLowerCase() ) ) {
                        suggestions.add( course );
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            else {
                return new FilterResults();
            }
        }

        @Override
        @SuppressWarnings ( "unchecked" )
        protected
        void publishResults ( CharSequence constraint , FilterResults results ) {
            List<Course> filteredList = ( List<Course> ) results.values;
            if ( results.count > 0 ) {
                clear();
                for ( Course c : filteredList ) {
                    add( c );
                }
            }
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public
    View getView ( int position , View convertView , @NonNull ViewGroup parent ) {
        View row;
        LayoutInflater inflater =
                ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        if ( convertView == null && inflater != null )
            row = inflater.inflate( R.layout.student_autocomplete_item , parent , false );
        else
            row = convertView;

        Course course = courses.get( position );
        assert row != null;
        SimpleDraweeView image = row.findViewById( R.id.imageViewStudent );
        TextView name = row.findViewById( R.id.textNameStudent );

        name.setText( course.getName() );
        if ( course.getUrl() != null )
            image.setImageURI( Uri.parse( course.getUrl() ) );

        return row;
    }
}
