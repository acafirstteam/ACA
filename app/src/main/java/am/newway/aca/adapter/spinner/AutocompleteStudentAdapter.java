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
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;

public
class AutocompleteStudentAdapter extends ArrayAdapter<Student> {
    private List<Student> students;
    private List<Student> studentsAll;
    private List<Student> suggestions;
    private Context context;

    @SuppressWarnings( "unchecked" )
    public
    AutocompleteStudentAdapter ( Context context , int layout , ArrayList<Student> list ) {

        super( context , layout , list );
        this.context = context;
        this.students = list;
        this.studentsAll = (ArrayList<Student>)list.clone();
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
            return ( ( Student ) ( resultValue ) ).getName();
        }

        @Override
        protected
        FilterResults performFiltering ( CharSequence constraint ) {
            if ( constraint != null ) {
                suggestions.clear();
                for ( Student student : studentsAll ) {
                    if ( student.getName()
                            .toLowerCase()
                            .startsWith( constraint.toString().toLowerCase() ) ) {
                        suggestions.add( student );
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
            List<Student> filteredList = ( List<Student> ) results.values;
            if ( results.count > 0 ) {
                clear();
                for ( Student c : filteredList ) {
                    add( c );
                }
            }
//            else {
//                addAll( studentsAll );
//            }
            notifyDataSetChanged();
        }
    };

//    @Override
//    public
//    int getCount () {
//        return students.size();
//    }
//
//    @Override
//    public
//    Student getItem ( int position ) {
//        return students.get( position );
//    }
//
//    @Override
//    public
//    long getItemId ( int position ) {
//        return position;
//    }



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

        Student student = students.get( position );
        assert row != null;
        SimpleDraweeView image = row.findViewById( R.id.imageViewStudent );
        TextView name = row.findViewById( R.id.textNameStudent );
        TextView course = row.findViewById( R.id.textCourseStudent );

        name.setText( student.getName() );
        course.setText( student.getCourse() );
        if ( student.getPicture() != null )
            image.setImageURI( Uri.parse( student.getPicture() ) );

        return row;
    }
}
