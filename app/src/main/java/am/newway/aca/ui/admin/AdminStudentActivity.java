package am.newway.aca.ui.admin;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.spinner.MessageTypeSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import am.newway.aca.adapter.spinner.CourseSpinnerAdapter;
import androidx.annotation.NonNull;

public
class AdminStudentActivity extends BaseActivity {
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
        setContentView( R.layout.activity_admin_student );

        initNavigationBar( 2 );

        Intent intent = getIntent();


        //Views
        final TextView textNameStudentItm = findViewById( R.id.textNameStudentItmA );
        final TextView textPhoneStudentItm = findViewById( R.id.textPhoneStudentItmA );
        final TextView textEmailStudentItm = findViewById( R.id.textEmailStudentItmA );


        final ImageView imageViewStudentItem = findViewById( R.id.imageViewStudentItemA );

        //Spinners
        customSpinner = findViewById( R.id.spinner1 );
        customSpinner2 = findViewById( R.id.spinner2 );
        customSpinnerStatus = findViewById( R.id.spinner );


        final ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource( this , R.array.statusVisitors ,
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

        FIRESTORE.getCourses( new Firestore.OnCourseReadListener() {
            @Override
            public
            void OnCourseRead ( final List<Course> courses ) {
                setCourses( courses );
            }
        } );

        HashMap<String, String> map =
                ( HashMap<String, String> ) intent.getSerializableExtra( "map" );
        ObjectMapper mapper = new ObjectMapper(  );
       final Student student = mapper.convertValue( map, Student.class );

        textNameStudentItm.setText( student.getName() );
        textEmailStudentItm.setText( student.getEmail() );
        textPhoneStudentItm.setText( student.getPhone() );
        imageViewStudentItem.setImageURI( Uri.parse( student.getPicture() ) );

        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {

                Course course = ( Course ) customSpinner2.getSelectedItem();

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
                adapter2 =
                        new CourseSpinnerAdapter( AdminStudentActivity.this , R.layout.custom_spinner_layout ,
                                android.R.layout.simple_spinner_item , course );
                customSpinner2.setAdapter( adapter2 );
            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );
    }

    private
    void initSpinners () {
        List<String> strings =
                Arrays.asList( getResources().getStringArray( R.array.statusVisitors ) );
        List<Integer> images =
                Arrays.asList( R.drawable.student , R.drawable.administrator ,
                        R.drawable.lecturer, R.drawable.qr );
        MessageTypeSpinnerAdapter messageTypeSpinnerAdapter =
                new MessageTypeSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , strings );
        messageTypeSpinnerAdapter.setImages( images );
        customSpinnerStatus.setAdapter( messageTypeSpinnerAdapter );

        messageTypeSpinnerAdapter =
                new MessageTypeSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , groups );
        List<Integer> imagesIcon = new ArrayList<>(  );
        for(String s : groups){
            imagesIcon.add(R.drawable.ic_group);
        }
        messageTypeSpinnerAdapter.setImages( imagesIcon );
        customSpinner.setAdapter( messageTypeSpinnerAdapter );
    }

    @Override
    public
    boolean onOptionsItemSelected ( @NonNull final MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
