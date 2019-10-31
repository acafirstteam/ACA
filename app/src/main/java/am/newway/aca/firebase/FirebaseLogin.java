package am.newway.aca.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;

public
class FirebaseLogin extends BaseActivity {

    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LottieAnimationView animation;
    private Button buttonLogout;
    private SignInButton buttonSignIn;
    private TextView registerText;
    private EditText fullName ;
    private EditText email;
    private EditText phone;
    private SimpleDraweeView image;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_firebase_login );

        final ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null )
            actionBar.setDisplayHomeAsUpEnabled( true );

        fullName = findViewById( R.id.edit_fullname );
        email = findViewById( R.id.edit_email );
        phone = findViewById( R.id.edit_phone );
        image = findViewById( R.id.imageView );
        final ConstraintLayout layout_user = findViewById( R.id.student_info );
        buttonLogout = findViewById( R.id.logout );
        animation = findViewById( R.id.animation_view );
        registerText = findViewById( R.id.register_text );

        fullName.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public
            boolean onEditorAction ( final TextView textView , final int i ,
                    final KeyEvent keyEvent ) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    saveStudent();
                    return true;
                }
                return false;
            }
        } );

        phone.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public
            boolean onEditorAction ( final TextView textView , final int i ,
                    final KeyEvent keyEvent ) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    saveStudent();
                    return true;
                }
                return false;
            }
        } );

        findViewById( R.id.logout ).setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( final View view ) {
                mGoogleSignInClient.signOut();
                mAuth.signOut();
                //mAuth.removeAuthStateListener( mAuthListener );
                firebaseUser = mAuth.getCurrentUser();
                DATABASE.deleteStudent();
                setResult( 1 );
                finish();
            }
        } );

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public
            void onAuthStateChanged ( @NonNull FirebaseAuth firebaseAuth ) {

                //firebaseUser= firebaseAuth.getCurrentUser();
                firebaseUser = mAuth.getCurrentUser();
                if ( firebaseUser != null ) {
                    animation.setVisibility( View.GONE );
                    buttonSignIn.setVisibility( View.GONE );
                    registerText.setVisibility( View.GONE );
                    layout_user.setVisibility( View.VISIBLE );
                    buttonLogout.setVisibility( View.VISIBLE );

                    if ( actionBar != null )
                        actionBar.setTitle( R.string.personal );

                    final Student student = DATABASE.getStudent();

                    fullName.setText( student.getName() == null || student.getName().isEmpty() ?
                            firebaseUser.getDisplayName() : student.getName());
                    email.setText( firebaseUser.getEmail() );
                    image.setImageURI( firebaseUser.getPhotoUrl() );

                    student.setId( firebaseUser.getUid() );
                    student.setEmail( firebaseUser.getEmail() );
                    student.setName( firebaseUser.getDisplayName() );
                    student.setPicture( Objects.requireNonNull( firebaseUser.getPhotoUrl() ).toString() );

                    FIRESTORE.getStudent( DATABASE.getStudent().getId() , new Firestore.OnStudentCheckListener() {
                        @Override
                        public
                        void OnStudentChecked ( @Nullable final Student student ) {

                        }

                        @Override
                        public
                        void OnStudentCheckFailed ( final String exception ) {
                            Log.e( TAG , "OnStudentCheckFailed: " +exception  );
                        }

                        @Override
                        public
                        void OnStudentIdentifier ( final Student stud ) {
                            student.setPhone( stud.getPhone() );
                            student.setType( stud.getType() );
                            student.setCourse( stud.getCourse() );
                            student.setVerified( stud.isVerified() );
                            student.setPhone( student.getPhone().isEmpty() ?
                                    firebaseUser.getPhoneNumber() : student.getPhone());
                            phone.setText( student.getPhone() == null || student.getPhone().isEmpty() ?
                                    firebaseUser.getPhoneNumber() : student.getPhone() );

                            DATABASE.setStudent( student );
                        }
                    } );
                }
                else
                    Log.e( TAG , "onAuthStateChanged: user is null" );

                hideProgressDialog();
            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN ).requestIdToken(
                getString( R.string.default_web_client_id ) ).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient( this , gso );

        buttonSignIn = findViewById( R.id.btnForGoogleSignIn );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public
            void onClick ( View view ) {
                startActivityForResult( mGoogleSignInClient.getSignInIntent() , RC_SIGN_IN );
                showProgressDialog();
            }
        } );
    }

    @Override
    protected
    void onStart () {
        super.onStart();
        mAuth.addAuthStateListener( mAuthListener );
    }

    @Override
    protected
    void onStop () {
        super.onStop();
        mAuth.removeAuthStateListener( mAuthListener );
    }

    @Override
    public
    void onActivityResult ( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if ( requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class );
                if ( account != null ) {
                    AuthCredential credential =
                            GoogleAuthProvider.getCredential( account.getIdToken() , null );
                    mAuth.signInWithCredential( credential );

//                    firebaseUser = mAuth.getCurrentUser();
//                    Log.e( TAG , "OnStudentIdentifier: Updated data1"   );
//                    Log.e( TAG , "OnStudentIdentifier: Updated data1"+firebaseUser   );
//                    if(firebaseUser != null) {
//
//                    }
                }
            } catch ( ApiException e ) {
                // Google Sign In failed, update UI appropriately
                Log.w( TAG , "Google sign in failed" , e );
                Toast.makeText( this , e.getMessage() != null ? e.getMessage() : "null" ,
                        Toast.LENGTH_LONG ).show();
            }
        }
    }

    @Override
    public
    boolean onCreateOptionsMenu ( final Menu menu ) {
        getMenuInflater().inflate( R.menu.menu_personal , menu );
        return true;
    }

    @Override
    public
    boolean onOptionsItemSelected ( @NonNull final MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home: {
                setResult( 1 );
                this.finish();
                return true;
            }
            case R.id.action_save: {
                saveStudent();
            }
        }
        return super.onOptionsItemSelected( item );
    }

    private
    void saveStudent (  ) {

        Student student = DATABASE.getStudent();
        student.setName( fullName.getText(  ).toString() );
        student.setPhone( phone.getText(  ).toString() );

        setResult( 1 );
        this.finish();
    }

    @Override
    public
    void onBackPressed () {
        super.onBackPressed();
        setResult( 1 );
        this.finish();
    }
}
