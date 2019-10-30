package am.newway.aca;

import am.newway.aca.adapter.CourseSpinnerAdapter;
import am.newway.aca.adapter.StudentAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

import android.content.Intent;
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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public
class AdminStudentStatus extends BaseActivity {
    //private ArrayList<CustomItemSpinner> customList, customListTwo;

    private List<Course> courses;
    private List<String> groups;
    private String strGroup = "";
    private Spinner customSpinner;
    private Spinner customSpinner2;
    private Spinner customSpinnerStatus;
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

        Intent intent = getIntent();
        HashMap<String, String> map =
                ( HashMap<String, String> ) intent.getSerializableExtra( "map" );

        //Views
        final TextView textNameStudentItm = findViewById( R.id.textNameStudentItmA );
        final TextView textCourseStudentItm = findViewById( R.id.textCourseStudentItmA );
        final TextView textPhoneStudentItm = findViewById( R.id.textPhoneStudentItmA );
        final TextView textEmailStudentItm = findViewById( R.id.textEmailStudentItmA );


        final ImageView imageViewStudentItem = findViewById( R.id.imageViewStudentItemA );

        //Spinners
        customSpinner = findViewById( R.id.spinner1 );
        customSpinner2 = findViewById( R.id.spinner2 );
        customSpinnerStatus = findViewById( R.id.spinner );


        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource( this , R.array.statusVsisitours ,
                        android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        final FloatingActionButton buttonSave = findViewById( R.id.btndoneForstatus );
        customSpinnerStatus.setAdapter( adapter );
        customSpinnerStatus.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                String state = adapterView.getItemAtPosition( i ).toString();
                Toast.makeText( adapterView.getContext() , state , Toast.LENGTH_SHORT );

            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );

        ObjectMapper maper = new ObjectMapper();
        final Student student = maper.convertValue( map , Student.class );

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
        textCourseStudentItm.setText( student.getCourse() );
        imageViewStudentItem.setImageURI( Uri.parse( student.getPicture() ) );

        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {

                Course course = (Course)customSpinner2.getSelectedItem();

                student.setCourse( course.getName() );

                FIRESTORE.updateStudent( student , new Firestore.OnStudentUpdateListener() {
                    @Override
                    public
                    void OnStudentUpdated () {

                    }

                    @Override
                    public
                    void OnStudentUpdateFailed () {

                    }
                } );
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
                adapter2 = new CourseSpinnerAdapter( AdminStudentStatus.this ,
                        R.layout.custom_spinner_layout , android.R.layout.simple_spinner_item ,
                        course );
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
