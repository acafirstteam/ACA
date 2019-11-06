package am.newway.aca.ui.admin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.spinner.AutocompleteStudentAdapter;
import am.newway.aca.adapter.spinner.SingleLineIconSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public
class AdminCourseEditActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private final String ADD = "add";
    private final String UPDATE = "update";

    private EditText editCourseName;
    private EditText editDescriptionEng;
    private EditText editDescriptionArm;
    private EditText editLink;
    private ImageView imageView;
    private String action = null;
    private String imageURI;
    private Uri imagePath;
    private boolean imagePicked = false;
    private Spinner spinnerGroup;
    private AutoCompleteTextView autoCompleteTextView;
    private Course course;

    @Override
    protected
    void onCreate ( @Nullable Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.admin_edit_course_layout );

        initNavigationBar( 2 );

        editCourseName = findViewById( R.id.admin_edit_courseName_id );
        editDescriptionEng = findViewById( R.id.admin_edit_DescriptionEng_id );
        editDescriptionArm = findViewById( R.id.admin_edit_DescriptionArm_id );
        editLink = findViewById( R.id.admin_edit_Link_id );
        spinnerGroup = findViewById( R.id.spinner_group );
        autoCompleteTextView = findViewById( R.id.spinner_person );
        imageView = findViewById( R.id.imageView );
        findViewById( R.id.fab ).setOnClickListener( this );

        editDescriptionEng.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public
            void onFocusChange ( View view , boolean hasFocus ) {
                editDescriptionEng.setSingleLine( !hasFocus );
            }
        } );

        editDescriptionArm.setOnFocusChangeListener( new View.OnFocusChangeListener() {
            @Override
            public
            void onFocusChange ( View view , boolean hasFocus ) {
                editDescriptionArm.setSingleLine( !hasFocus );
            }
        } );

        FIRESTORE.getActiveLecturers( new Firestore.OnStudentsLoadListener() {
            @Override
            public
            void OnStudentLoaded ( @Nullable final List<Student> students ) {
                AutocompleteStudentAdapter adapter =
                        new AutocompleteStudentAdapter( AdminCourseEditActivity.this ,
                                R.layout.student_autocomplete_item ,
                                ( ArrayList<Student> ) students );
                autoCompleteTextView.setAdapter( adapter );
            }
        } );

        Intent intent = getIntent();
        ArrayList<String> groups = ( ArrayList<String> ) intent.getSerializableExtra( "groups" );

        SingleLineIconSpinnerAdapter singleLineIconSpinnerAdapter =
                new SingleLineIconSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , groups );
        List<Integer> imagesIcon = new ArrayList<>();
        imagesIcon.add( R.drawable.ic_x );
        for ( String s : groups ) {
            imagesIcon.add( R.drawable.ic_group );
        }

        singleLineIconSpinnerAdapter.setImages( imagesIcon );
        spinnerGroup.setAdapter( singleLineIconSpinnerAdapter );
        spinnerGroup.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                FIRESTORE.getGroups( spinnerGroup.getSelectedItemPosition() ,
                        new Firestore.OnGroupReadListener() {

                            @Override
                            public
                            void OnGroupRead ( final Map<String, Object> groups ) {

                                course.setGroup_name( groups );
                                course.setGroup( spinnerGroup.getSelectedItemPosition() - 1 );
                                //                                Log.e( TAG , "OnGroupRead: " + groups.get( "en" ) );
                                //                                Log.e( TAG , "OnGroupRead: " + groups.get( "hy" ) );
                            }
                        } );
            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );

        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                if ( ContextCompat.checkSelfPermission( getApplicationContext() ,
                        Manifest.permission.READ_EXTERNAL_STORAGE ) !=
                        PackageManager.PERMISSION_GRANTED ) {

                    final int MY_PERMISSION_REQUEST = 1;
                    ActivityCompat.requestPermissions( AdminCourseEditActivity.this ,
                            new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE } ,
                            MY_PERMISSION_REQUEST );
                }
                else {
                    Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
                    photoPickerIntent.setType( "image/*" );
                    final int RESULT_LOAD_IMG = 5;
                    startActivityForResult( photoPickerIntent , RESULT_LOAD_IMG );
                }
            }
        } );

        //GET ACTION AND POSITION FROM BUNDLE

        final Bundle bundle = intent.getExtras();
        course = ( Course ) bundle.getSerializable( "course" );
        if ( course == null )
            course = new Course();
        action = intent.getStringExtra( "action" );
        Log.d( TAG , "action: " + action );

        switch ( action ) {
            case ADD:
                setTitle( R.string.add_course );
                gotActionAddCourse();
                break;
            case UPDATE:
                setTitle( R.string.edit_course );
                gotActionUpdateCourse( course );
                break;
        }
    }

    private
    int getGroupIndex ( Spinner spinner , String course ) {
        Log.e( TAG , "getGroupIndex: " + course );
        for ( int i = 0; i < spinner.getCount(); i++ ) {
            if ( spinner.getItemAtPosition( i ).toString().equalsIgnoreCase( course ) ) {
                return i;
            }
        }
        return 0;
    }

    //Action Update Course
    public
    void gotActionUpdateCourse ( Course course ) {

        if ( course.getUrl().trim().isEmpty() )
            imageView.setImageResource( R.drawable.ic_add_image_view );
        else
            imageView.setImageURI( Uri.parse( course.getUrl() ) );

        editCourseName.setText( course.getName() );
        editCourseName.setEnabled( false );

        editDescriptionEng.setText( course.getDescription( "en" ) );
        editDescriptionArm.setText( course.getDescription( "hy" ) );
        editLink.setText( course.getLink() );

        String lang = DATABASE.getSettings().getLanguage();
        spinnerGroup.setSelection( getGroupIndex( spinnerGroup , course.getGroup_name( lang ) ) );

        autoCompleteTextView.setText( course.getLecturer() );
    }

    //Action Add Course
    public
    void gotActionAddCourse () {
        FIRESTORE.getCourses( new Firestore.OnCourseReadListener() {
            @Override
            public
            void OnCourseRead ( List<Course> courses ) {
                //imageView.setImageResource( R.drawable.ic_add_image_view );
                //courseItems = new ArrayList<>( courses );
            }
        } );
    }

    //On Click
    @Override
    public
    void onClick ( View v ) {

        //Save Button clicked
        if ( v.getId() == R.id.fab ) {
            if ( editCourseName.getText().toString().trim().isEmpty() ||
                    editDescriptionEng.getText().toString().trim().isEmpty() ||
                    editDescriptionArm.getText().toString().trim().isEmpty() ||
                    editLink.getText().toString().trim().isEmpty() ) {
                Toast.makeText( getApplicationContext() , "Please fill all fields" ,
                        Toast.LENGTH_SHORT ).show();
                return;
            }

            switch ( action ) {
                //CASE ADD
                case ADD:
                    if ( !imagePicked ) {   //IMAGE NOT PICKED
                        Toast.makeText( getApplicationContext() , "Please Pick Course Image" ,
                                Toast.LENGTH_SHORT ).show();
                        return;
                    }
                    Log.d( TAG , "---------------------- Case ADD" );
                    FIRESTORE.uploadImage( imagePath , editCourseName.getText().toString() ,
                            new Firestore.OnImageUploadListener() {
                                @Override
                                public
                                void OnImageUploaded ( String uri ) {
                                    imageURI = uri;
                                    ArrayList<Course> courses = new ArrayList<>();
                                    courses.add( createCourse() );
                                    FIRESTORE.addCourses( courses );
                                }

                                @Override
                                public
                                void OnImageUploadFailed ( String error ) {
                                    Toast.makeText( getApplicationContext() ,
                                            "Image upload failed" , Toast.LENGTH_SHORT ).show();
                                }
                            } );
                    finish();

                    //CASE UPDATE
                    break;
                case UPDATE:
                    Log.d( TAG , "---------------------- Case UPDATE" );
                    //IMAGE PICKED
                    if ( imagePicked ) {
                        FIRESTORE.uploadImage( imagePath , editCourseName.getText().toString() ,
                                new Firestore.OnImageUploadListener() {
                                    @Override
                                    public
                                    void OnImageUploaded ( String uri ) {
                                        imageURI = uri;
                                        FIRESTORE.updateCourse( createCourse() ,
                                                new Firestore.OnCourseUpdateListener() {
                                                    @Override
                                                    public
                                                    void OnCourseUpdated () {
                                                    }

                                                    @Override
                                                    public
                                                    void OnCourseUpdateFailed () {
                                                        Toast.makeText( getApplicationContext() ,
                                                                "Course update failed" ,
                                                                Toast.LENGTH_SHORT ).show();
                                                    }
                                                } );
                                    }

                                    @Override
                                    public
                                    void OnImageUploadFailed ( String error ) {

                                    }
                                } );
                    }
                    else {
                        //IMAGE NOT PICKED
                        FIRESTORE.updateCourse( createCourse() ,
                                new Firestore.OnCourseUpdateListener() {
                                    @Override
                                    public
                                    void OnCourseUpdated () {
                                    }

                                    @Override
                                    public
                                    void OnCourseUpdateFailed () {
                                        Toast.makeText( getApplicationContext() ,
                                                "Course update failed" , Toast.LENGTH_SHORT )
                                                .show();
                                    }
                                } );

                    }
                    finish();
                    break;
            }
        }
    }

    //Activity Result
    @Override
    protected
    void onActivityResult ( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );

        if ( resultCode == RESULT_OK && data != null && data.getData() != null ) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream( imageUri );
                final Bitmap selectedImage = BitmapFactory.decodeStream( imageStream );
                imageView.setImageBitmap( selectedImage );
                imagePath = imageUri;
                imagePicked = true;
                Log.d( TAG , "------------------------------Image picked" );
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
                Toast.makeText( getApplicationContext() , "Something went wrong" ,
                        Toast.LENGTH_SHORT ).show();
            }
        }
        else {
            Toast.makeText( getApplicationContext() , "You haven't picked Image" ,
                    Toast.LENGTH_SHORT ).show();
        }
    }

    //Create Course object
    public
    Course createCourse () {

        final Map<String, Object> description = new HashMap<>();
        description.put( "en" , editDescriptionEng.getText().toString() );
        description.put( "hy" , editDescriptionArm.getText().toString() );

        Course newCourse =
                new Course( editCourseName.getText().toString() , editLink.getText().toString() ,
                        false , course.getGroup() , imagePicked ? imageURI : course.getUrl() );
        newCourse.setLecturer( autoCompleteTextView.getText().toString() );

        newCourse.setGroup_name( course.getGroup_name() );
        newCourse.setDescription( description );

        return newCourse;
    }

    //Nav Button
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
