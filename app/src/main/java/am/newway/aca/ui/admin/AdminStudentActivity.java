package am.newway.aca.ui.admin;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.spinner.MessageTypeSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import am.newway.aca.adapter.spinner.CourseSpinnerAdapter;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_EMAIL = 1;

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
        HashMap<String, String> map =
                ( HashMap<String, String> ) intent.getSerializableExtra( "map" );

        //Views
        final TextView textNameStudentItm = findViewById( R.id.textNameStudentItmA );
        final TextView textCourseStudentItm = findViewById( R.id.textCourseStudentItmA );
        final TextView textPhoneStudentItm = findViewById( R.id.textPhoneStudentItmA );
        final TextView textEmailStudentItm = findViewById( R.id.textEmailStudentItmA );
        textEmailStudentItm.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                String sendEmail = textEmailStudentItm.getText().toString();
                String[] sendEmails = sendEmail.split( "," );
                String sendSubject = ( "ACA Administration" );
                if ( sendEmail.length() > 0 ) {

                    {
                        if ( ContextCompat.checkSelfPermission( AdminStudentActivity.this ,
                                Manifest.permission.INTERNET ) !=
                                PackageManager.PERMISSION_GRANTED ) {
                            ActivityCompat.requestPermissions( AdminStudentActivity.this ,
                                    new String[]{ Manifest.permission.INTERNET } , REQUEST_EMAIL );

                        }
                        else {
                            Intent intent = new Intent( Intent.ACTION_SEND );
                            intent.putExtra( Intent.EXTRA_EMAIL , sendEmails );
                            intent.putExtra( Intent.EXTRA_SUBJECT , sendSubject );

                            intent.setType( "message/rfc822" );
                            startActivity(
                                    Intent.createChooser( intent , "choose an email client" ) );
                        }

                    }

                }
                else {
                    Toast.makeText( AdminStudentActivity.this , "Student's email does not right" ,
                            Toast.LENGTH_SHORT ).show();
                }

            }
        } );
        textPhoneStudentItm.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                String number = textPhoneStudentItm.getText().toString();
                if ( number.trim().length() > 0 ) {
                    if ( ContextCompat.checkSelfPermission( AdminStudentActivity.this ,
                            Manifest.permission.CALL_PHONE ) !=
                            PackageManager.PERMISSION_GRANTED ) {

                        ActivityCompat.requestPermissions( AdminStudentActivity.this ,
                                new String[]{ Manifest.permission.CALL_PHONE } , REQUEST_CALL );

                    }
                    else {
                        String dial = "tel:" + number;

                        startActivity( new Intent( Intent.ACTION_CALL , Uri.parse( dial ) ) );
                    }
                }
                else {
                    Toast.makeText( AdminStudentActivity.this , "Student has not any number" ,
                            Toast.LENGTH_SHORT ).show();
                }


            }
        } );
        final ImageView imageViewStudentItem = findViewById( R.id.imageViewStudentItemA );

        //Spinners
        customSpinner = findViewById( R.id.spinner1 );
        customSpinner2 = findViewById( R.id.spinner2 );
        customSpinnerStatus = findViewById( R.id.spinner );
        final FloatingActionButton buttonSave = findViewById( R.id.btndoneForstatus );

//        final ArrayAdapter<CharSequence> adapter =
//                ArrayAdapter.createFromResource( this , R.array.statusVsisitours ,
//                        android.R.layout.simple_spinner_item );
//        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        ObjectMapper maper = new ObjectMapper();
        final Student student = maper.convertValue( map , Student.class );

        int nIndex = 0;
        switch ( student.getType() ) {
            case 0:
                nIndex = 0;
                break;
            case 1:
                nIndex = 3;
                break;
            case 2:
                nIndex = 1;
                break;
            case 3:
                nIndex = 2;
                break;
        }

        customSpinnerStatus.setSelection( nIndex );
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

        textNameStudentItm.setText( student.getName() );
        textEmailStudentItm.setText( student.getEmail() );
        textPhoneStudentItm.setText( student.getPhone() );
        textCourseStudentItm.setText( student.getCourse() );
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
                adapter2 = new CourseSpinnerAdapter( AdminStudentActivity.this ,
                        R.layout.custom_spinner_layout , android.R.layout.simple_spinner_item ,
                        course );
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
