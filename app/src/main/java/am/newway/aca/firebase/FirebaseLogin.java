package am.newway.aca.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import am.newway.aca.BaseActivity;
import am.newway.aca.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;

public class FirebaseLogin extends BaseActivity {

    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private LottieAnimationView animation;
    private Button buttonLogout;
    private SignInButton buttonSignIn;
    private TextView registerText;

    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_firebase_login );

        final ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null )
            actionBar.setDisplayHomeAsUpEnabled( true );

        final EditText fullName = findViewById( R.id.edit_fullname );
        final EditText email = findViewById( R.id.edit_email );
        final EditText phone = findViewById( R.id.edit_phone );
        final SimpleDraweeView image = findViewById( R.id.imageView );
        final ConstraintLayout layout_user = findViewById( R.id.student_info );
        buttonLogout = findViewById( R.id.logout );
        animation = findViewById( R.id.animation_view );
        registerText = findViewById( R.id.register_text );

        findViewById( R.id.logout ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( final View view ) {
                Log.i( TAG , "onClick: Firebase user signOut" );
                //firebaseUser.delete();
                //firebaseUser = null;
                mGoogleSignInClient.signOut();
                mAuth.signOut();
                firebaseUser = mAuth.getCurrentUser();
                finish();
            }
        } );

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged ( @NonNull FirebaseAuth firebaseAuth ) {
                firebaseUser = mAuth.getCurrentUser();
                if ( firebaseUser != null ) {
                    animation.setVisibility( View.GONE );
                    buttonSignIn.setVisibility( View.GONE );
                    registerText.setVisibility( View.GONE );
                    layout_user.setVisibility( View.VISIBLE );
                    buttonLogout.setVisibility( View.VISIBLE );

                    if ( actionBar != null )
                        actionBar.setTitle( R.string.personal );

                    fullName.setText( firebaseUser.getDisplayName() );
                    email.setText( firebaseUser.getEmail() );
                    phone.setText( firebaseUser.getPhoneNumber() );
                    image.setImageURI( firebaseUser.getPhotoUrl() );

                    Log.e( TAG , "onAuthStateChanged: " + firebaseUser.getUid() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getUid() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getEmail() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getDisplayName() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getPhoneNumber() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getPhotoUrl() );
                    //                    Log.e( TAG , "onAuthStateChanged: " + user.getProviderId() );
                }
                else
                    Log.e( TAG , "onAuthStateChanged: user is null" );
                hideProgressDialog();

            }
        };

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN ).requestIdToken( getString( R.string.default_web_client_id ) ).requestEmail().build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient( this , gso );

        //        mGoogleApiClient =
        //                new GoogleApiClient.Builder( getApplicationContext() ).enableAutoManage( this , new GoogleApiClient.OnConnectionFailedListener() {
        //
        //            @Override
        //            public void onConnectionFailed ( @NonNull ConnectionResult connectionResult ) {
        //
        //                Toast.makeText( FirebaseLogin.this , "You have got an Error" , Toast.LENGTH_LONG ).show();
        //            }
        //        } ).addApi( Auth.GOOGLE_SIGN_IN_API , gso ).build();

        buttonSignIn = findViewById( R.id.btnForGoogleSignIn );
        buttonSignIn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                //                Intent signInIntent =
                //                        Auth.GoogleSignInApi.getSignInIntent( mGoogleSignInClient.get );
                startActivityForResult( mGoogleSignInClient.getSignInIntent() , RC_SIGN_IN );
                showProgressDialog();
            }
        } );
    }

    @Override
    protected void onStart () {
        super.onStart();
        mAuth.addAuthStateListener( mAuthListener );
    }

    @Override
    protected void onStop () {
        super.onStop();
        mAuth.removeAuthStateListener( mAuthListener );
    }

    @Override
    public void onActivityResult ( int requestCode , int resultCode , Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if ( requestCode == RC_SIGN_IN ) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent( data );
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult( ApiException.class );
                if ( account != null ) {
                    AuthCredential credential = GoogleAuthProvider.getCredential( account.getIdToken() , null );
                    mAuth.signInWithCredential( credential );
                }
            } catch ( ApiException e ) {
                // Google Sign In failed, update UI appropriately
                Log.w( TAG , "Google sign in failed" , e );
                Toast.makeText( this , e.getMessage() != null ? e.getMessage() : "null" , Toast.LENGTH_LONG ).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu ( final Menu menu ) {
        getMenuInflater().inflate( R.menu.menu_personal , menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected ( @NonNull final MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home: {
                this.finish();
                return true;
            }
            case R.id.action_save: {
                Toast.makeText( this , "Save" , Toast.LENGTH_SHORT ).show();
                this.finish();
            }
        }
        return super.onOptionsItemSelected( item );
    }
}
