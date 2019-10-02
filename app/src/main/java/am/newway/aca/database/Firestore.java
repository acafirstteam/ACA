package am.newway.aca.database;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import am.newway.aca.template.Course;
import am.newway.aca.template.Student;
import am.newway.aca.template.Visit;
import am.newway.aca.util.Crypto;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Firestore {
    private static final String TAG = "Firestore";
    private static volatile Firestore firestore;
    private FirebaseFirestore db;
    private static String VISIT_COLLECTION = "Visits";
    private static String STUDENT_COLLECTION = "Students";
    private static String COURSE_COLLECTION = "Courses";
    private static String QR_COLLECTION = "QR";
    private OnVisitChangeListener listener;
    private OnStudentCheckListener listener_student;
    private OnCourseReadListener listener_course;
    private OnQRGenerator listener_qr;
    private String qrText;

    private Firestore () {
    }

    public interface OnQRGenerator {
        void OnQrGenerated ( String strQr );

        void OnQrGenerationFailed ();
    }

    public interface OnCourseReadListener {
        void OnCourseRead ( List<Course> courses );
    }

    public interface OnVisitChangeListener {
        void OnChangeConfirmed ( Visit visit );
    }

    public interface OnStudentCheckListener {
        void OnStudentChecked ( Student student );

        void OnStudentCheckFailed ( String exception );
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

    /**
     * Այս ֆունկցիայի շնորիվ հնարավոր է ստուգել առկա է արդյոք նշված ուսանողը բազայում եթե այո ապա
     * ստանալ նրա մասին տեղություն 0․  սովորական ուսանող 1  QR ցուցադրող սարք 2  ադմին
     *
     * @param student
     *         ուսանողի մասին ինֆորմացիա կրող կլասի փոփոխական, այս դեպքում փոփոխականից վերցվում է
     *         միայն ուսանողին իդենտիֆիկացնող ID ին {@link Student#getId()}
     * @param bltCreateNew
     *         այս փոփոխականի դրական լինելու և ուսանողի բազայում բացակայության դեպքում կավելացվի նոր
     *         ուսանող
     * @param listener {@link OnStudentCheckListener} ինտերֆեյսին պատկանող փոփոխական որի միջոցով
     *                                               վերադարձվում է Firestor ից վերադարձված
     *                                               արժեքները
     */
    public void checkStudent (
            final Student student , final boolean bltCreateNew , OnStudentCheckListener listener
    ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_student = listener;

        DocumentReference docRef = db.collection( STUDENT_COLLECTION ).document( String.valueOf( student.getId() ) );
        docRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
                if ( task.isSuccessful() ) {
                    DocumentSnapshot document = task.getResult();
                    if ( document != null && document.exists() ) {
                        //Log.d( TAG , "DocumentSnapshot data: " + document.getData() );
                        //addNewStudent( student , null);
                        if ( listener_student != null )
                            listener_student.OnStudentChecked( document.toObject( Student.class ) );
                    }
                    else {
                        Log.w( TAG , "No such document" );
                        if ( bltCreateNew ) {
                            addNewStudent( student , null );
                            if ( listener_student != null )
                                listener_student.OnStudentChecked( null );
                        }
                    }
                }
                else {
                    Log.d( TAG , "get failed with " , task.getException() );
                    if ( listener_student != null )
                        listener_student.OnStudentCheckFailed( task.getException().toString() );

                }
            }
        } );
    }

    public void qrGenerator ( OnQRGenerator listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_qr = listener;

        SimpleDateFormat formatter = new SimpleDateFormat( "dd_MM_yyyy" , Locale.US );
        Date date = new Date();
        String strDate = formatter.format( date );

        DocumentReference docRef = db.collection( QR_COLLECTION ).document( strDate );
        docRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
                if ( task.isSuccessful() ) {
                    DocumentSnapshot document = task.getResult();
                    if ( document != null && document.exists() ) {
                        Log.d( TAG , "DocumentSnapshot data: " + document.getData() );
                        String strCrypt = document.getString( "qr" );
                        if ( listener_qr != null ) {
                            listener_qr.OnQrGenerated( strCrypt );
                            qrText = strCrypt;
                        }
                    }
                    else {
                        Log.d( TAG , "No such document" );
                        try {
                            addNewQR();
                        } catch ( Exception e ) {
                            listener_qr.OnQrGenerationFailed();
                        }
                    }
                }
                else {
                    Log.d( TAG , "get failed with " , task.getException() );
                }
            }
        } );
    }

    public void addListenerNewVisit () {
        if ( db == null )
            db = FirebaseFirestore.getInstance();

        db.collection( VISIT_COLLECTION ).addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent (
                    @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e
            ) {
                if ( queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty() ) {
                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                    for ( DocumentSnapshot doc : docs ) {
                        Visit visit = doc.toObject( Visit.class );
                        if ( qrText.equals( visit != null ? visit.getQrCode() : null ) ) {
                            confirmVisit( doc.getId() );
                        }
                    }
                }
            }
        } );
    }

    private void confirmVisit ( String strDoc ) {
        db.collection( VISIT_COLLECTION ).document( strDoc ).update( "confirm" , true ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete ( @NonNull final Task<Void> task ) {

            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure ( @NonNull final Exception e ) {

            }
        } );
    }

    private void addNewQR () throws Exception {
        if ( db == null )
            db = FirebaseFirestore.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat( "dd_MM_yyyy" , Locale.US );
        Date date = new Date();
        String strDate = formatter.format( date );
        final String strCrypt = Crypto.encrypt( "acacode" , strDate );
        Map<String, Object> map = new HashMap<>();
        map.put( "qr" , strCrypt );

        db.collection( QR_COLLECTION ).document( strDate ).set( map ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete ( @NonNull final Task<Void> task ) {
                Log.e( TAG , "onComplete: " + task.getResult() );
                if ( listener_qr != null ) {
                    listener_qr.OnQrGenerated( strCrypt );
                    qrText = strCrypt;
                }
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure ( @NonNull final Exception e ) {
                if ( listener_qr != null )
                    listener_qr.OnQrGenerationFailed();
            }
        } );
    }

    public void getCuorces (
            OnCourseReadListener listener
    ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_course = listener;

        db.collection( COURSE_COLLECTION ).get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete (
                    @NonNull final Task<QuerySnapshot> task
            ) {
                if ( task.isSuccessful() ) {
                    List<Course> courses = new ArrayList<>();
                    if ( task.getResult() != null ) {
                        List<DocumentSnapshot> docs = task.getResult().getDocuments();
                        for ( DocumentSnapshot doc : docs ) {
                            Course course = doc.toObject( Course.class );
                            courses.add( course );
                        }
                        if ( listener_course != null )
                            listener_course.OnCourseRead( courses );
                    }
                    else
                        Log.e( TAG , "getResult is null" );
                }
                else
                    Log.e( TAG , "task is not Successful" );
            }
        } );
    }

    private void addNewStudent (
            Student student , OnVisitChangeListener listener
    ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener = listener;

        db.collection( STUDENT_COLLECTION ).document( String.valueOf( student.getId() ) ).set( student ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public void onComplete ( @NonNull final Task<Void> task ) {
                Log.e( TAG , "onComplete: " + task.getResult() );
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
