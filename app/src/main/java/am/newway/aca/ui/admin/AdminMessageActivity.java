package am.newway.aca.ui.admin;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.spinner.AutocompleteCourseAdapter;
import am.newway.aca.adapter.spinner.AutocompleteStudentAdapter;
import am.newway.aca.adapter.spinner.SingleLineIconSpinnerAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Course;
import am.newway.aca.template.Notification;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public
class AdminMessageActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private String ident;
    private Spinner spinnerType;
    private Spinner spinnerSegment;
    private EditText editText;
    private TextView titlePerson;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification );

        initNavigationBar( 2 );

        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {
                sendNotification();
            }
        } );

        final AutoCompleteTextView autoCompleteTextView = findViewById( R.id.spinner_person );
        spinnerType = findViewById( R.id.spinner_type );
        spinnerSegment = findViewById( R.id.spinner_segment );
        editText = findViewById( R.id.edit_text );
        titlePerson = findViewById( R.id.titlePerson );

        editText.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public
            boolean onEditorAction ( final TextView textView , final int i ,
                    final KeyEvent keyEvent ) {
                if ( i == EditorInfo.IME_ACTION_SEND ) {
                    sendNotification();
                }
                return false;
            }
        } );

        List<String> strings =
                Arrays.asList( getResources().getStringArray( R.array.notification_type ) );
        List<Integer> images =
                Arrays.asList( R.drawable.message , R.drawable.alert , R.drawable.news );
        SingleLineIconSpinnerAdapter singleLineIconSpinnerAdapter =
                new SingleLineIconSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , strings );
        singleLineIconSpinnerAdapter.setImages( images );

        //        ArrayAdapter<?> adapterType =
        //                ArrayAdapter.createFromResource( this , R.array.notification_type ,
        //                        android.R.layout.simple_spinner_item );
        //        adapterType.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerType.setAdapter( singleLineIconSpinnerAdapter );

        List<String> stringsSegment =
                Arrays.asList( getResources().getStringArray( R.array.notification_segment ) );
        List<Integer> imagesSegment =
                Arrays.asList( R.drawable.all , R.drawable.administrator , R.drawable.lecturer ,
                        R.drawable.student , R.drawable.course );
        singleLineIconSpinnerAdapter =
                new SingleLineIconSpinnerAdapter( this , R.layout.custom_spinner_layout ,
                        android.R.layout.simple_spinner_item , stringsSegment );
        singleLineIconSpinnerAdapter.setImages( imagesSegment );

        //        ArrayAdapter<?> adapterSegment =
        //                ArrayAdapter.createFromResource( this , R.array.notification_segment ,
        //                        android.R.layout.simple_spinner_item );
        //        adapterSegment.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerSegment.setAdapter( singleLineIconSpinnerAdapter );

        spinnerType.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {

            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );

        spinnerSegment.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public
            void onItemSelected ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                autoCompleteTextView.setText( "" );
                autoCompleteTextView.setEnabled( i != 0 );

                if ( i != 0 && i != 4 ) {
                    FIRESTORE.getActiveStudents( new Firestore.OnStudentsLoadListener() {
                        @Override
                        public
                        void OnStudentLoaded ( @Nullable final List<Student> students ) {
                            AutocompleteStudentAdapter adapter =
                                    new AutocompleteStudentAdapter( AdminMessageActivity.this ,
                                            R.layout.student_autocomplete_item ,
                                            ( ArrayList<Student> ) students );
                            autoCompleteTextView.setAdapter( adapter );
                            titlePerson.setText( R.string.select_person );
                        }
                    } );
                }
                else if ( i == 4 ) {
                    FIRESTORE.getCourses( new Firestore.OnCourseReadListener() {
                        @Override
                        public
                        void OnCourseRead ( final List<Course> courses ) {
                            AutocompleteCourseAdapter adapter =
                                    new AutocompleteCourseAdapter( AdminMessageActivity.this ,
                                            R.layout.student_autocomplete_item ,
                                            ( ArrayList<Course> ) courses );
                            autoCompleteTextView.setAdapter( adapter );
                            titlePerson.setText( R.string.select_course );
                        }
                    } );
                }
            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );

        autoCompleteTextView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public
            void onItemClick ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                String name = "";
                if ( adapterView.getItemAtPosition( i ) instanceof Student ) {
                    name = ( ( Student ) adapterView.getItemAtPosition( i ) ).getName();
                    ident = ( ( Student ) adapterView.getItemAtPosition( i ) ).getId();
                }
                else if ( adapterView.getItemAtPosition( i ) instanceof Course ) {
                    ident = name = ( ( Course ) adapterView.getItemAtPosition( i ) ).getName();
                }
                autoCompleteTextView.setText( name );
            }
        } );
    }

    private
    void sendNotification () {

        FIRESTORE.getLastNotificationID( new Firestore.OnNotificationIdListener() {
            @Override
            public
            void OnLastId ( @Nullable final int id ) {
                Toast.makeText(AdminMessageActivity.this, "$$$ "+id, Toast.LENGTH_SHORT).show();
                int nType = spinnerType.getSelectedItemPosition();
                int nSegment = spinnerSegment.getSelectedItemPosition();

                String messageText = editText.getText().toString();

                //SimpleDateFormat formatter =
                //        new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
                Date date = new Date();

                Notification notification =
                        new Notification( messageText , ident , nType , nSegment ,
                                date  );
                notification.setId( id + 1 );

                FIRESTORE.sendMessage( notification );
            }
        } );

        finish();
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
