package am.newway.aca;

import am.newway.aca.adapter.CourseSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public
class AdminStudentStatus extends BaseActivity {
    //private ArrayList<CustomItemSpinner> customList, customListTwo;
    private List<Course> courses;
    private List<String> groups;
    private String strGroup = "";
    private Spinner customSpinner;
    private Spinner customSpinner2;
    private CourseSpinnerAdapter adapter2;

    private
    void setCourses ( List<Course> courses ) {
        this.courses = new ArrayList<>( courses );
        String lang = DATABASE.getSettings().getLanguage();
        groups = new ArrayList<>();

        for ( Course course : courses ) {
            if ( !strGroup.equals( course.getGroup_name().get( lang ) ) ) {
                strGroup = course.getGroup_name().get( lang ).toString();
                groups.add( strGroup );
            }
        }
        initSpinners();
    }

    private
    List<Course> getFilteredCourse ( String strGroup ) {
        List<Course> co = new ArrayList<>();
        String lang = DATABASE.getSettings().getLanguage();
        for ( Course course : courses ) {
            if ( course.getGroup_name().get( lang ).equals( strGroup ) ) {
                co.add( course );
            }
        }
        return co;
    }

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_admin_student_status );

        //Views
        final TextView textNameStudentItm = findViewById( R.id.textNameStudentItmA );
        final TextView textCourseStudentItm = findViewById( R.id.textCourseStudentItmA );
        final TextView textPhoneStudentItm = findViewById( R.id.textPhoneStudentItmA );
        final TextView textEmailStudentItm = findViewById( R.id.textEmailStudentItmA );
        final TextView textIdStudentItm = findViewById( R.id.textIdStudentItmA );
        final ImageView imageViewStudentItem = findViewById( R.id.imageViewStudentItemA );

        //Spinners
        customSpinner = findViewById( R.id.spinner1 );
        customSpinner2 = findViewById( R.id.spinner2 );

        final Button buttonSave = findViewById( R.id.btndoneForstatus );

        final Student student = DATABASE.getStudent();

        FIRESTORE.getCuorces( new Firestore.OnCourseReadListener() {
            @Override
            public
            void OnCourseRead ( final List<Course> courses ) {
                setCourses( courses );
            }
        } );

        textNameStudentItm.setText( student.getName() );
        textEmailStudentItm.setText( student.getEmail() );
        textPhoneStudentItm.setText( student.getPhone() );
        imageViewStudentItem.setImageURI( Uri.parse( student.getPicture() ) );

        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {
                finish();
            }
        } );

        customSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                String group = adapterView.getItemAtPosition( i ).toString();
                List<Course> course = getFilteredCourse( group );
                adapter2 =
                        new CourseSpinnerAdapter( AdminStudentStatus.this , R.layout.custom_spinner_layout ,
                                android.R.layout.simple_spinner_item , course );
                customSpinner2.setAdapter( adapter2 );
            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );
        //
        //        customSpinner2.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
        //            @Override
        //            public
        //            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
        //                    final long l ) {
        //
        //            }
        //
        //            @Override
        //            public
        //            void onNothingSelected ( final AdapterView<?> adapterView ) {
        //
        //            }
        //        } );

        //        customList = getCustomList();
        //        customListTwo = getCustomListTwo();


    }

    private
    void initSpinners () {

        @SuppressWarnings ( "unchecked" ) ArrayAdapter<?> adapter =
                new ArrayAdapter( this , android.R.layout.simple_spinner_item , groups );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        customSpinner.setAdapter( adapter );
    }
}
