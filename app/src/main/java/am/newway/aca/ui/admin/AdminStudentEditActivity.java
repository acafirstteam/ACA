package am.newway.aca.ui.admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.spinner.CourseSpinnerAdapter;
import am.newway.aca.adapter.spinner.SingleLineIconSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;
import am.newway.aca.util.Util;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static am.newway.aca.ui.admin.student.AdminStudentActivity.PERMISSIONS_REQUEST_CALL_PHONE;

public
class AdminStudentEditActivity extends BaseActivity {

    private List<Course> courses;
    private List<String> groups;
    private String strGroup = "";
    private Student student;
    private Spinner spinnerGroup;
    private Spinner spinnerCourse;
    private Spinner spinnerSegment;
    private CourseSpinnerAdapter adapterCourse;
    private TextView studentPhone;
    private int nPosition;

    private
    void setCourses ( List<Course> courses ) {
        this.courses = new ArrayList<>( courses );
        String lang = DATABASE.getSettings().getLanguage();
        groups = new ArrayList<>();

        groups.add( getString( R.string.select_group ) );
        for ( Course course : courses ) {
            if ( !strGroup.equals( course.getGroup_name( lang ) ) ) {
                strGroup = course.getGroup_name( lang );
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
            if ( course.getGroup_name( lang ).equals( strGroup ) ) {
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

        final Intent intent = getIntent();
        nPosition = intent.getIntExtra( "index" , -1 );

        //Views
        studentPhone = findViewById( R.id.student_phone );
        final TextView studentName = findViewById( R.id.student_name );
        final TextView studentEmail = findViewById( R.id.student_email );
        final ImageView imageView = findViewById( R.id.imageViewStudentItemA );
        final FloatingActionButton buttonSave = findViewById( R.id.fab );

        //Spinners
        spinnerGroup = findViewById( R.id.spinner_group );
        spinnerCourse = findViewById( R.id.spinner_course );
        spinnerSegment = findViewById( R.id.spinner_segment );

        spinnerSegment.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                //String state = adapterView.getItemAtPosition( i ).toString();
                //Toast.makeText( adapterView.getContext() , state , Toast.LENGTH_SHORT ).show();
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

        HashMap<String, String> map;
        map = ( HashMap<String, String> ) intent.getSerializableExtra( "map" );
        ObjectMapper mapper = new ObjectMapper();
        student = mapper.convertValue( map , Student.class );

        studentName.setText( student.getName() );
        studentEmail.setText( student.getEmail() );
        studentPhone.setText( student.getPhone().isEmpty() ? getString( R.string.empty_phone )
                : student.getPhone() );
        imageView.setImageURI( Uri.parse( student.getPicture() ) );

        studentPhone.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                makePhoneCall();
            }
        } );

        studentEmail.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                Util.sendEmail( AdminStudentEditActivity.this , studentEmail.getText().toString() );
            }
        } );

        buttonSave.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {

                Course course = ( Course ) spinnerCourse.getSelectedItem();
                String segment = ( String ) spinnerSegment.getSelectedItem();

                student.setCourse( course.getName() );
                student.setType( toSegment( segment ) );

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
                ObjectMapper mapper = new ObjectMapper();
                HashMap<String, Object> map = mapper.convertValue( student , HashMap.class );
                Intent intentL = new Intent();
                intentL.putExtra( "map" , map );
                setResult( nPosition , intentL );
                finish();
            }
        } );

        spinnerGroup.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                String group = adapterView.getItemAtPosition( i ).toString();
                List<Course> course = getFilteredCourse( group );
                adapterCourse = new CourseSpinnerAdapter( AdminStudentEditActivity.this ,
                        R.layout.custom_spinner_layout , android.R.layout.simple_spinner_item ,
                        course );
                spinnerCourse.setAdapter( adapterCourse );
                spinnerCourse.setSelection( getCourseIndex( spinnerCourse , student.getCourse() ) );
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
                Arrays.asList( R.drawable.ic_x , R.drawable.student , R.drawable.administrator ,
                        R.drawable.lecturer , R.drawable.qr );
        SingleLineIconSpinnerAdapter singleLineIconSpinnerAdapter =
                new SingleLineIconSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , strings );
        singleLineIconSpinnerAdapter.setImages( images );
        spinnerSegment.setAdapter( singleLineIconSpinnerAdapter );

        singleLineIconSpinnerAdapter =
                new SingleLineIconSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , groups );
        List<Integer> imagesIcon = new ArrayList<>();
        imagesIcon.add( R.drawable.ic_x );
        for ( String s : groups ) {
            imagesIcon.add( R.drawable.ic_group );
        }
        singleLineIconSpinnerAdapter.setImages( imagesIcon );
        spinnerGroup.setAdapter( singleLineIconSpinnerAdapter );

        spinnerSegment.setSelection( getIndex( spinnerSegment , student.getType() ) );
        spinnerGroup.setSelection( getGroupIndex( spinnerGroup , student.getCourse() ) );
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

    private
    int getIndex ( Spinner spinner , int nType ) {
        String segment = fromSegment( nType );

        for ( int i = 0; i < spinner.getCount(); i++ ) {
            if ( spinner.getItemAtPosition( i ).toString().equalsIgnoreCase( segment ) ) {
                return i;
            }
        }
        return 0;
    }

    private
    int getGroupIndex ( Spinner spinner , String course ) {
        String lang = DATABASE.getSettings().getLanguage();

        String group = "";
        for ( Course co : courses ) {
            if ( co.getName().equals( course ) ) {
                group = co.getGroup_name( lang );
            }
        }

        for ( int i = 0; i < spinner.getCount(); i++ ) {
            if ( spinner.getItemAtPosition( i ).toString().equalsIgnoreCase( group ) ) {
                return i;
            }
        }
        return 0;
    }

    private
    int getCourseIndex ( Spinner spinner , String course ) {

        for ( int i = 0; i < spinner.getCount(); i++ ) {
            if ( ( ( Course ) spinner.getItemAtPosition( i ) ).getName()
                    .equalsIgnoreCase( course ) ) {
                return i;
            }
        }
        return 0;
    }

    private
    int toSegment ( String segment ) {
        final String[] arr = getResources().getStringArray( R.array.statusVisitors );
        int nIndex = -1;
        for ( int i = 0; i != arr.length; i++ ) {
            if ( arr[i].equals( segment ) ) {
                nIndex = i;
                break;
            }
        }

        switch ( nIndex ) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 1;
        }

        return -1;
    }

    private
    String fromSegment ( int index ) {
        int nIndex = -1;

        switch ( index ) {
            case 0:
                nIndex = 1;
                break;
            case 1:
                nIndex = 4;
                break;
            case 2:
                nIndex = 2;
                break;
            case 3:
                nIndex = 3;
                break;
        }
        final String[] arr = getResources().getStringArray( R.array.statusVisitors );

        if ( nIndex != -1 )
            return arr[nIndex];

        return "";
    }

    @SuppressLint ( "MissingPermission" )
    private
    void makePhoneCall () {

        if ( !studentPhone.getText().toString().equals( getString( R.string.empty_phone ) ) ) {
            String phone = studentPhone.getText().toString();
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                if ( ContextCompat.checkSelfPermission( this , Manifest.permission.CALL_PHONE ) !=
                        PackageManager.PERMISSION_GRANTED ) {

                    if ( ActivityCompat.shouldShowRequestPermissionRationale( this ,
                            Manifest.permission.CALL_PHONE ) ) {
                        AlertDialog.Builder builder = new AlertDialog.Builder( this );
                        builder.setView( R.layout.phone_permission_dialog )
                                .setPositiveButton( getString( R.string.call ) ,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public
                                            void onClick ( final DialogInterface dialogInterface ,
                                                    final int i ) {
                                                requestPermissions(
                                                        new String[]{ Manifest.permission.CALL_PHONE } ,
                                                        PERMISSIONS_REQUEST_CALL_PHONE );
                                            }
                                        } )
                                .setNegativeButton( getString( R.string.no ) , null )
                                .show();
                    }
                    else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions( this ,
                                new String[]{ Manifest.permission.CALL_PHONE } ,
                                PERMISSIONS_REQUEST_CALL_PHONE );
                    }
                }
                else
                    Util.callPhone( this , phone );
            }
            else
                Util.callPhone( this , phone );
        }
        else
            Toast.makeText( this , R.string.no_phone_number , Toast.LENGTH_SHORT ).show();
    }
}
