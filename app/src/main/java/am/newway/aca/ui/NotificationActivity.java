package am.newway.aca.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.AutocompleteAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Notification;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public
class NotificationActivity extends BaseActivity {

    private final String TAG = getClass().getSimpleName();
    private Student student;
    private Spinner spinnerType;
    private Spinner spinnerSegment;
    private EditText editText;

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

        ArrayAdapter<?> adapterType =
                ArrayAdapter.createFromResource( this , R.array.notification_type ,
                        android.R.layout.simple_spinner_item );
        adapterType.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerType.setAdapter( adapterType );

        ArrayAdapter<?> adapterSegment =
                ArrayAdapter.createFromResource( this , R.array.notification_segment ,
                        android.R.layout.simple_spinner_item );
        adapterSegment.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        spinnerSegment.setAdapter( adapterSegment );

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
                autoCompleteTextView.setEnabled( i == 3 );
            }

            @Override
            public
            void onNothingSelected ( final AdapterView<?> adapterView ) {

            }
        } );

        FIRESTORE.getActiveStudents( new Firestore.OnStudentsLoadistener() {
            @Override
            public
            void OnStudentLoaded ( @Nullable final List<Student> students ) {
                AutocompleteAdapter adapter = new AutocompleteAdapter( NotificationActivity.this ,
                        R.layout.student_autocomplete_item , ( ArrayList<Student> ) students );
                autoCompleteTextView.setAdapter( adapter );
            }
        } );

        autoCompleteTextView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public
            void onItemClick ( final AdapterView<?> adapterView , final View view , final int i ,
                    final long l ) {
                student = ( Student ) adapterView.getItemAtPosition( i );
                autoCompleteTextView.setText( student.getName() );
            }
        } );
    }

    private
    void sendNotification () {

        FIRESTORE.getLastNotificationID( new Firestore.OnNotificationIdListener() {
            @Override
            public
            void OnLastId ( @Nullable final int id ) {
                int nType = spinnerType.getSelectedItemPosition();
                int nSegment = spinnerSegment.getSelectedItemPosition();
                String strIdent = "";
                if ( student != null && student.getId() != null )
                    strIdent = student.getId();
                String messageText = editText.getText().toString();

                SimpleDateFormat formatter =
                        new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
                Date date = new Date();

                Notification notification =
                        new Notification( messageText , strIdent , nType , nSegment ,
                                formatter.format( date ) );
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
