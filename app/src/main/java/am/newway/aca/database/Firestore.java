package am.newway.aca.database;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import am.newway.aca.template.Visit;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Firestore {
    private static final String TAG = "Firestore";
    private static volatile Firestore firestore;
    private FirebaseFirestore db;
    private static String VISIT_COLLECTION = "Visits";
    private OnVisitChangeListener listener;

    private Firestore () {
    }

    public interface OnVisitChangeListener {
        void OnChangeConfirmed ( Visit visit );
    }

    public static Firestore getInstance () {
        if ( firestore == null )
            synchronized ( Firestore.class ) {
                if ( firestore == null ) {
                    firestore = new Firestore();
                }
            }
        return firestore;
    }

    public void addNewVisit ( String userIdent , String qrCode , OnVisitChangeListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener = listener;

        SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
        Date date = new Date();
        System.out.println( formatter.format( date ) );

        Visit visit = new Visit();
        visit.setConfirm( false );
        visit.setDateTime( formatter.format( date ) );
        visit.setQrCode( "5566778899" );
        visit.setUserIdent( userIdent );

        db.collection( VISIT_COLLECTION ).add( visit ).addOnCompleteListener( new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete (
                    @NonNull final Task<DocumentReference> task
            ) {
                addListener( Objects.requireNonNull( task.getResult() ).getId() );
            }
        } );
    }

    private void addListener ( String docID ) {
        Log.e( TAG , "document id = " + docID );
        final DocumentReference docRef = db.collection( VISIT_COLLECTION ).document( docID );
        ListenerRegistration reg = docRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent (
                    @Nullable DocumentSnapshot snapshot , @Nullable FirebaseFirestoreException e
            ) {
                if ( e != null ) {
                    Log.e( TAG , "Listen failed." , e );
                    return;
                }

                if ( snapshot != null && snapshot.exists() ) {
                    Log.e( TAG , "Current data: " + snapshot.getData() );
                    Visit visit = snapshot.toObject( Visit.class );
                    if ( listener != null )
                        listener.OnChangeConfirmed( visit );
                }
                else {
                    Log.e( TAG , "Current data: null" );
                }
            }
        } );
    }
}
