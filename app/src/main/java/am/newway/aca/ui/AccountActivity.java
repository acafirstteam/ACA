package am.newway.aca.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import am.newway.aca.MainActivity;
import am.newway.aca.R;
import am.newway.aca.googleauth.CreateAcountGoogle;

public class AccountActivity extends AppCompatActivity {
   private ImageView studentPhotoFromGoogle;
   private TextView studentNameAndFnameFromGooGLE, studentEmailFromGoogle;
   private LottieAnimationView lottieAnimationViewForSaveAndToChangeActivity;




   private Button btnForLogOut;
   private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_account);

        studentPhotoFromGoogle =findViewById(R.id.studentImageFromGoogle);
        studentNameAndFnameFromGooGLE= findViewById(R.id.textViewStudentNameAndFname);
        studentEmailFromGoogle=findViewById(R.id.textViewStudentEmailFromGoogleAccount);
        lottieAnimationViewForSaveAndToChangeActivity = findViewById(R.id.animation_view);
        btnForLogOut= findViewById(R.id.btnLogOut);
        lottieAnimationViewForSaveAndToChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, MainActivity.class));

            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();


            studentNameAndFnameFromGooGLE.setText("Hi! "+personName);
            studentEmailFromGoogle.setText(personEmail);
            Glide.with(this).load(String.valueOf(personPhoto)).into(studentPhotoFromGoogle);
        }



        mAuth =FirebaseAuth.getInstance();


        mAuthListner= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(AccountActivity.this, CreateAcountGoogle.class));
                }
            }
        };
        btnForLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

}
