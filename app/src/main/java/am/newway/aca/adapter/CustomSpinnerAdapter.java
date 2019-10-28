package am.newway.aca.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.template.Course;
import androidx.annotation.NonNull;

public
class CustomSpinnerAdapter extends ArrayAdapter<Course> {

    private ArrayList<Course> courses;
    private Context context;

    public
    CustomSpinnerAdapter ( Context context , int layout, ArrayList<Course> customList ) {
        super( context , layout , customList );

        this.courses = customList;
        this.context = context;
    }

//    @NonNull
//    @Override
//    public
//    View getView ( int position , @Nullable View convertView , @NonNull ViewGroup parent ) {
//        if ( convertView == null ) {
//            convertView = LayoutInflater.from( getContext() )
//                    .inflate( R.layout.custom_spinner_layout1 , parent , false );
//        }
//
//        Course itemSpinner = getItem( position );
//        //SimpleDraweeView spinnerIV = convertView.findViewById( R.id.icon_spinner_levl );
//        TextView spinnerTV = convertView.findViewById( R.id.icon_spinner_levl_text );
//
//        if ( itemSpinner != null ) {
//            //spinnerIV.setImageURI( itemSpinner.getRes() );
//            //spinnerTV.setText( itemSpinner.getGroup_name().get( "en" ).toString() );
//            spinnerTV.setText( itemSpinner.getName() );
//        }
//
//        return convertView;
//    }

//    @Nullable
//    @Override
//    public
//    Course getItem ( final int position ) {
//        return courses.get( position );
//    }

    @NonNull
    @Override
    public
    View getView ( int position , View convertView , @NonNull ViewGroup parent ) {
        View row;
        LayoutInflater inflater =
                ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        if ( convertView == null && inflater != null )
            row = inflater.inflate( R.layout.custom_spinner_layout , parent , false );
        else
            row = convertView;

        Course course = courses.get( position );
        assert row != null;
        //SimpleDraweeView image = row.findViewById( R.id.imageViewStudent );
        TextView name = row.findViewById( R.id.icon_spinner_levl_text );
        //TextView course = row.findViewById( R.id.textCourseStudent );

        Log.e( "#########" , "getView: " + course.getName()  );
        name.setText( course.getName() );

        return row;
    }
}
