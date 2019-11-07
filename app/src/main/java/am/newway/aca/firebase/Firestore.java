package am.newway.aca.firebase;

import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import am.newway.aca.template.Course;
import am.newway.aca.template.Notification;
import am.newway.aca.template.Student;
import am.newway.aca.template.Visit;
import am.newway.aca.util.Crypto;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public
class Firestore {
    private static final String TAG = "Firestore";
    private static volatile Firestore firestore;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private static String NOTIFICATION_COLLECTION = "Notification";
    private static String VISIT_COLLECTION = "Visits";
    private static String VISIT_GROUPS = "Groups";
    private static String STUDENT_COLLECTION = "Students";
    private static String COURSE_COLLECTION = "Courses";
    private static String QR_COLLECTION = "QR";
    private FirebaseFirestore db;
    private OnVisitCompleteListener listener_complete_visit;
    private OnStudentCheckListener listener_student;
    private OnQRGenerator listener_qr;
    private OnVisitAddListener listener_add_visit;
    private String qrText;

    private
    Firestore () {
    }

    public static
    Firestore getInstance () {
        if ( firestore == null )
            synchronized ( Firestore.class ) {
                if ( firestore == null ) {
                    firestore = new Firestore();
                }
            }

        return firestore;
    }

    public
    void addOnStudentCheckListener ( OnStudentCheckListener listener ) {
        this.listener_student = listener;
    }

    private
    void initFirebaseStorage () {
        if ( storage == null ) {
            storage = FirebaseStorage.getInstance();
            if ( storageReference == null )
                storageReference = storage.getReference();
        }
    }

    public
    void addNewVisit ( Student student , String qrCode , OnVisitAddListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_add_visit = listener;

        SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
        Date date = new Date();

        Visit visit = new Visit();
        visit.setConfirm( false );
        visit.setDateTime( formatter.format( date ) );
        visit.setQrCode( qrCode );
        visit.setUserIdent( String.valueOf( student.getId() ) );

        db.collection( VISIT_COLLECTION )
                .add( visit )
                .addOnCompleteListener( new OnCompleteListener<DocumentReference>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentReference> task ) {
                        addVisitConfirmListener(
                                Objects.requireNonNull( task.getResult() ).getId() );
                    }
                } );
    }

    public
    void checkVisit ( Student student , final OnVisitCheckListener listener ) {
        initFirestore();

        CollectionReference doc = db.collection( VISIT_COLLECTION );

        doc.whereEqualTo( "completeTime" , null )
                .whereEqualTo( "userIdent" , student.getId() )
                .whereEqualTo( "open", true )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        if ( task.isSuccessful() ) {
                            QuerySnapshot query = task.getResult();
                            if ( query != null ) {
                                List<DocumentSnapshot> list = query.getDocuments();
                                if ( list.size() > 0 ) {
                                    DocumentSnapshot document = list.get( 0 );
                                    if ( document != null && document.exists() ) {
                                        Visit visit = document.toObject( Visit.class );
                                        if ( visit != null ) {
                                            visit.setId( document.getId() );
                                            listener.OnVisitExisted( visit );
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                        listener.OnVisitNotExist();
                    }

                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: check visit" );
                    }
                } );
    }

    public
    void completeVisit ( String Id , final OnVisitCompleteListener listener ) {
        initFirestore();
        this.listener_complete_visit = listener;

        SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
        Date date = new Date();

        Log.e( TAG , "completeVisit: " + Id );
        DocumentReference doc = db.collection( VISIT_COLLECTION ).document( Id );

        doc.update( "completeTime" , formatter.format( date ) );
        doc.update( "open" , false ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                listener_complete_visit.OnVisitCompleted();
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public
            void onFailure ( @NonNull final Exception e ) {
                Log.e( TAG , "onFailure: complete visit" );
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
     * @param listener
     *         {@link OnStudentCheckListener} ինտերֆեյսին պատկանող փոփոխական որի միջոցով
     *         վերադարձվում է Firestor ից վերադարձված արժեքները
     */
    public
    void checkStudent ( final Student student , final boolean bltCreateNew ,
            final OnStudentCheckListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_student = listener;

        final DocumentReference docRef =
                db.collection( STUDENT_COLLECTION ).document( String.valueOf( student.getId() ) );
        docRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public
            void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
                if ( task.isSuccessful() ) {
                    DocumentSnapshot document = task.getResult();
                    if ( document != null && document.exists() ) {
                        if ( bltCreateNew ) {
                            updateStudent( docRef , student , listener );
                        }
                        else if ( listener_student != null ) {
                            Student student = document.toObject( Student.class );
                            if ( student != null ) {
                                student.setId( document.getId() );
                                listener_student.OnStudentChecked( student );
                            }
                        }
                    }
                    else {
                        Log.w( TAG , "No such document" );
                        if ( bltCreateNew ) {
                            addNewStudent( student , listener_student );
                        }
                        else if ( listener_student != null )
                            listener_student.OnStudentChecked( null );
                    }
                }
                else {
                    Log.d( TAG , "get failed with " , task.getException() );
                    if ( listener_student != null )
                        listener_student.OnStudentCheckFailed(
                                task.getException() != null ? task.getException().toString()
                                        : "unknown error" );
                }
            }
        } );
    }

    public
    void qrGenerator ( OnQRGenerator listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_qr = listener;

        SimpleDateFormat formatter = new SimpleDateFormat( "dd_MM_yyyy" , Locale.US );
        Date date = new Date();
        String strDate = formatter.format( date );

        DocumentReference docRef = db.collection( QR_COLLECTION ).document( strDate );
        docRef.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public
            void onComplete ( @NonNull Task<DocumentSnapshot> task ) {
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

    public
    void addListenerNewVisit () {
        initFirestore();

        db.collection( VISIT_COLLECTION )
                .whereEqualTo( "confirm" , false )
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public
                    void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                            @Nullable final FirebaseFirestoreException e ) {
                        if ( queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty() ) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for ( final DocumentSnapshot doc : docs ) {
                                Visit visit = doc.toObject( Visit.class );
                                if ( qrText != null && visit != null &&
                                        visit.getQrCode() != null ) {
                                    if ( qrText.equals( visit.getQrCode() ) ) {
                                        getStudent( visit.getUserIdent() ,
                                                new OnStudentCheckListener() {
                                                    @Override
                                                    public
                                                    void OnStudentChecked (
                                                            @Nullable final Student student ) {
                                                    }

                                                    @Override
                                                    public
                                                    void OnStudentCheckFailed (
                                                            final String exception ) {
                                                    }

                                                    @Override
                                                    public
                                                    void OnStudentIdentifier (
                                                            final Student student ) {
                                                        if ( student.getType() != -1 )
                                                            confirmVisit( doc.getId() );
                                                        else {
                                                            Student s = student;
                                                            s.setVerified( true );
                                                            updateStudent( s , null );
                                                        }
                                                    }
                                                } );
                                    }
                                }
                                else
                                    Log.e( TAG , "onEvent: " + "Unknown student" );
                            }
                        }
                    }
                } );
    }

    public
    void addListenerNewStudent ( final OnNewStudentListener listener ) {
        initFirestore();

        db.collection( STUDENT_COLLECTION )
                .whereEqualTo( "type" , -1 )
                .whereEqualTo( "verified" , true )
                .addSnapshotListener( new EventListener<QuerySnapshot>() {
                    @Override
                    public
                    void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                            @Nullable final FirebaseFirestoreException e ) {
                        if ( queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty() ) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                Student student = doc.toObject( Student.class );
                                if ( student != null ) {
                                    if ( listener != null )
                                        listener.OnNewStudentAdded( student );
                                }
                                else
                                    Log.e( TAG , "onEvent: " + "Unknown student" );
                            }
                        }
                    }
                } );
    }

    private
    void confirmVisit ( String docID ) {
        final DocumentReference doc = db.collection( VISIT_COLLECTION ).document( docID );
        doc.update( "confirm" , true ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                doc.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                        if ( task.getResult() == null )
                            return;
                        Visit visit = task.getResult().toObject( Visit.class );

                        if ( visit == null )
                            return;

                        final DocumentReference docStudent = db.collection( STUDENT_COLLECTION )
                                .document( visit.getUserIdent() );
                        docStudent.get()
                                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public
                                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                                        if ( task.getResult() != null ) {
                                            Student student =
                                                    task.getResult().toObject( Student.class );
                                            if ( student != null ) {
                                                showWelcome( student );
                                            }
                                            else
                                                Log.e( TAG , "confirmVisit: student is null" );
                                        }
                                        else
                                            Log.e( TAG , "confirmVisit: task.getResult() is null" );
                                    }
                                } )
                                .addOnFailureListener( new OnFailureListener() {
                                    @Override
                                    public
                                    void onFailure ( @NonNull final Exception e ) {
                                        Log.e( TAG , "confirmVisit: onFailure" );
                                    }
                                } );
                    }
                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {

                    }
                } );
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public
            void onFailure ( @NonNull final Exception e ) {

            }
        } );
    }

    private
    void showWelcome ( Student student ) {
        Log.e( TAG , "confirmVisit: student is null" + student.getName() );
        if ( listener_student != null )
            listener_student.OnStudentIdentifier( student );
    }

    private
    void addNewQR () throws Exception {
        if ( db == null )
            db = FirebaseFirestore.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat( "dd_MM_yyyy" , Locale.US );
        Date date = new Date();
        String strDate = formatter.format( date );
        final String strCrypt = Crypto.encrypt( "acacode" , strDate );
        Map<String, Object> map = new HashMap<>();
        map.put( "qr" , strCrypt );

        db.collection( QR_COLLECTION )
                .document( strDate )
                .set( map )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<Void> task ) {
                        Log.e( TAG , "onComplete: " + task.getResult() );
                        if ( listener_qr != null ) {
                            listener_qr.OnQrGenerated( strCrypt );
                            qrText = strCrypt;
                        }
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        if ( listener_qr != null )
                            listener_qr.OnQrGenerationFailed();
                    }
                } );
    }

    public
    void getNotifications ( boolean onlyNews , final OnNotificationListener listener ) {
        initFirestore();

        CollectionReference ref = db.collection( NOTIFICATION_COLLECTION );
        Query query;

        if ( onlyNews ) {
            query = ref.whereEqualTo( "messageType" , 2 )
                    .orderBy( "date" , Query.Direction.DESCENDING )
                    .limit( 20 );
        }
        else {
            query = ref.orderBy( "date" , Query.Direction.DESCENDING ).limit( 20 );
        }

        query.get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public
            void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                if ( task.isSuccessful() ) {
                    List<Notification> notifications = new ArrayList<>();
                    if ( task.getResult() != null ) {
                        List<DocumentSnapshot> docs = task.getResult().getDocuments();
                        for ( DocumentSnapshot doc : docs ) {
                            Notification notification = doc.toObject( Notification.class );
                            notifications.add( notification );
                        }
                        if ( listener != null )
                            listener.OnNotificationRead( notifications );
                    }
                    else {
                        Log.e( TAG , "getResult is null" );
                        if ( listener != null )
                            listener.OnNotificationFailed();
                    }
                }
                else {
                    Log.e( TAG , "task is not Successful" );
                    if ( listener != null )
                        listener.OnNotificationFailed();
                }
            }
        } );
    }

    public
    void getCourses ( final OnCourseReadListener listener ) {
        initFirestore();

        final String COURSE_COLLECTION = "Courses";
        db.collection( COURSE_COLLECTION )
                .whereEqualTo( "isdel" , false )
                .orderBy( "group" )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        if ( task.isSuccessful() ) {
                            List<Course> courses = new ArrayList<>();
                            if ( task.getResult() != null ) {
                                List<DocumentSnapshot> docs = task.getResult().getDocuments();
                                for ( DocumentSnapshot doc : docs ) {
                                    Course course = doc.toObject( Course.class );
                                    courses.add( course );
                                }
                                if ( listener != null )
                                    listener.OnCourseRead( courses );
                            }
                            else
                                Log.e( TAG , "getResult is null" );
                        }
                        else
                            Log.e( TAG , "task is not Successful" );
                    }
                } );
    }

    public
    void addCourses ( List<Course> courses ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();

        final String COURSE_COLLECTION = "Courses";

        for ( Course co : courses ) {
            db.collection( COURSE_COLLECTION ).document( co.getNameFormated() ).set( co );
        }
    }

    /**
     * Վերադարձնում է նոր ուսանողների ցանկը
     *
     * @return List<Students>
     */
    public
    void getNewStudents () {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        db.collection( STUDENT_COLLECTION )
                .whereEqualTo( "type" , "-1" )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<Student> students = new ArrayList<>();
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                students.add( doc.toObject( Student.class ) );
                            }
                        }
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading students" );
                    }
                } );
    }

    /**
     * Վերադարձնում է նոր ուսանողների ցանկը
     */
    public
    void getActiveStudents ( final OnStudentsLoadListener listener_student_loading ) {
        initFirestore();
        db.collection( STUDENT_COLLECTION )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        List<Student> students = new ArrayList<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                Student student = doc.toObject( Student.class );
                                if ( student != null ) {
                                    student.setId( doc.getId() );
                                    students.add( student );
                                }
                            }
                        }
                        if ( listener_student_loading != null )
                            listener_student_loading.OnStudentLoaded( students );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading students" );
                    }
                } );
    }

    public
    void getActiveLecturers ( final OnStudentsLoadListener listener_student_loading ) {
        initFirestore();
        db.collection( STUDENT_COLLECTION )
                .whereEqualTo( "type" , 3 )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        List<Student> students = new ArrayList<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                Student student = doc.toObject( Student.class );
                                if ( student != null ) {
                                    student.setId( doc.getId() );
                                    students.add( student );
                                }
                            }
                        }
                        if ( listener_student_loading != null )
                            listener_student_loading.OnStudentLoaded( students );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading students" );
                    }
                } );
    }


    public
    void sendMessage ( Notification notification ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        db.collection( NOTIFICATION_COLLECTION )
                .document( String.valueOf( notification.getId() ) )
                .set( notification )
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<Void> task ) {

                    }
                } );
    }

    public
    void getVisits ( String id , final OnVisitListener listener ) {
        initFirestore();
        Log.e( TAG , "getVisits: " + id );
        CollectionReference ref = db.collection( VISIT_COLLECTION );
        ref.whereEqualTo( "userIdent" , id )
                .whereEqualTo( "open" , false )
                .orderBy( "completeTime" , Query.Direction.DESCENDING )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        final List<Visit> visits = new ArrayList<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                visits.add( doc.toObject( Visit.class ) );
                            }
                        }
                        if ( listener != null )
                            listener.OnLoaded( visits );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading visits" );
                    }
                } );
    }

    public
    void getGroups ( int id , final OnGroupReadListener listener ) {
        if ( id == 0 )
            return;

        id--;
        initFirestore();
        Log.e( TAG , "getGroups: " + id );
        db.collection( VISIT_GROUPS )
                .document( String.valueOf( id ) )
                .get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                        final Map<String, Object> groups = new HashMap<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            DocumentSnapshot docs = task.getResult();
                            if ( docs != null && docs.get( "en" ) != null &&
                                    docs.get( "hy" ) != null ) {
                                groups.put( "en" , docs.get( "en" ).toString() );
                                groups.put( "hy" , docs.get( "hy" ).toString() );
                            }
                        }
                        if ( listener != null )
                            listener.OnGroupRead( groups );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading groups" );
                    }
                } );
    }

    public
    void getDevelopers ( final OnDeveloperListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        final String DEVELOPER_COLLECTION = "Developers";
        db.collection( DEVELOPER_COLLECTION )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        final List<String> developers = new ArrayList<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                developers.add( doc.get( "github" ).toString() );
                            }
                        }
                        if ( listener != null )
                            listener.OnLoaded( developers );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading developers" );
                    }
                } );
    }

    public
    void getDoorCode ( final OnDoorCodeListener listener ) {
        initFirestore();
        final String DOORCODE_COLLECTION = "DoorCode";
        db.collection( DOORCODE_COLLECTION )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        final List<String> codes = new ArrayList<>();
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                codes.add( doc.get( "code" ).toString() );
                            }
                        }
                        if ( listener != null )
                            listener.OnLoaded( codes );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading developers" );
                    }
                } );
    }

    /**
     * Վերադարձնում է ուսանողների ամբողջական ցանկը
     *
     * @return List<Students>
     */
    public
    void getAllStudents () {
        initFirestore();
        db.collection( STUDENT_COLLECTION )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        if ( task.isSuccessful() && task.getResult() != null ) {
                            final List<Student> students = new ArrayList<>();
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for ( DocumentSnapshot doc : docs ) {
                                students.add( doc.toObject( Student.class ) );
                            }
                        }
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading students" );
                    }
                } );
    }

    private
    void addNewStudent ( Student student , OnStudentCheckListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_student = listener;
        final DocumentReference doc =
                db.collection( STUDENT_COLLECTION ).document( String.valueOf( student.getId() ) );
        doc.set( student ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                doc.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                        Student student = Objects.requireNonNull( task.getResult() )
                                .toObject( Student.class );
                        if ( listener_student != null )
                            listener_student.OnStudentChecked( student );
                    }
                } );
            }
        } );
    }

    private
    void updateStudent ( final DocumentReference doc , final Student student ,
            final OnStudentCheckListener listener ) {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
        this.listener_student = listener;

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue( student , Map.class );

        doc.update( map ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                doc.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                        DocumentSnapshot document = task.getResult();
                        if ( document != null ) {

                            if ( student != null ) {
                                student.setId( document.getId() );
                                Map<String, Object> map = document.getData();
                                if ( map != null ) {
                                    Object object = map.get( "verified" );
                                    if ( object != null )
                                        student.setVerified( object.equals( "1" ) );
                                    object = map.get( "type" );
                                    if ( object != null )
                                        student.setType( ( int ) ( long ) object );

                                    if ( listener != null )
                                        listener.OnStudentChecked( student );
                                }
                            }
                        }
                    }
                } );
            }
        } );
    }

    private
    void initFirestore () {
        if ( db == null )
            db = FirebaseFirestore.getInstance();
    }

    public
    void updateCourse ( final Course course , final OnCourseUpdateListener listener ) {

        initFirestore();
        final DocumentReference docRef =
                db.collection( COURSE_COLLECTION ).document( course.getNameFormated() );

        ObjectMapper oMapper = new ObjectMapper();
        @SuppressWarnings ( "unchecked" ) Map<String, Object> map =
                oMapper.convertValue( course , Map.class );

        docRef.set( map ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                listener.OnCourseUpdated();
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public
            void onFailure ( @NonNull final Exception e ) {
                listener.OnCourseUpdateFailed();
            }
        } );
    }

    public
    void updateStudent ( final Student student , final OnStudentUpdateListener listener ) {

        initFirestore();
        final DocumentReference docRef =
                db.collection( STUDENT_COLLECTION ).document( student.getId() );

        ObjectMapper oMapper = new ObjectMapper();
        @SuppressWarnings ( "unchecked" ) Map<String, Object> map =
                oMapper.convertValue( student , Map.class );

        docRef.set( map ).addOnCompleteListener( new OnCompleteListener<Void>() {
            @Override
            public
            void onComplete ( @NonNull final Task<Void> task ) {
                if ( listener != null )
                    listener.OnStudentUpdated();
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public
            void onFailure ( @NonNull final Exception e ) {
                if ( listener != null )
                    listener.OnStudentUpdateFailed();
            }
        } );
    }

    private
    void addVisitConfirmListener ( String docID ) {
        Log.e( TAG , "document id = " + docID );
        final DocumentReference docRef = db.collection( VISIT_COLLECTION ).document( docID );
        docRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public
            void onEvent ( @Nullable DocumentSnapshot snapshot ,
                    @Nullable FirebaseFirestoreException e ) {
                if ( e != null ) {
                    Log.e( TAG , "Listen failed." , e );
                    return;
                }

                if ( snapshot != null && snapshot.exists() ) {
                    Log.e( TAG , "Current data: " + snapshot.getData() );
                    Visit visit = snapshot.toObject( Visit.class );
                    if ( visit != null ) {
                        visit.setId( snapshot.getId() );
                        if ( listener_add_visit != null )
                            listener_add_visit.OnChangeConfirmed( visit );
                    }
                }
                else {
                    Log.e( TAG , "Current data: null" );
                }
            }
        } );
    }

    public
    void addListenerNotifications ( String userId , String course , int nUserType ,
            final OnNotificationListener listener ) {
        Log.e( TAG , "user type = " + nUserType );

        switch ( nUserType ) {
            case 0:
                nUserType = 3;
                break;
            case 2:
                nUserType = 1;
                break;
            case 3:
                nUserType = 2;
                break;
        }
        Log.e( TAG , "user type = " + nUserType );

        initFirestore();
        final CollectionReference collRef = db.collection( NOTIFICATION_COLLECTION );
        Query query1 = collRef.whereEqualTo( "messageSegment" , 0 );
        Query query2 =
                collRef.whereEqualTo( "messageSegment" , nUserType ).whereEqualTo( "user" , null );
        Query query3 = collRef.whereEqualTo( "messageSegment" , nUserType )
                .whereEqualTo( "user" , userId );
        Query query4 = collRef.whereEqualTo( "messageSegment" , 4 ).whereEqualTo( "user" , null );
        Query query5 = collRef.whereEqualTo( "messageSegment" , 4 ).whereEqualTo( "user" , course );

        query1.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public
            void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e ) {
                if ( e != null || queryDocumentSnapshots == null ) {
                    Log.w( TAG , "listen:error" , e );
                    return;
                }

                for ( DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ) {
                    if ( dc.getType() == DocumentChange.Type.ADDED ) {
                        Notification notification = dc.getDocument().toObject( Notification.class );

                        notification.setId( Integer.valueOf( dc.getDocument().getId() ) );
                        if ( listener != null )
                            listener.OnNewNotification( notification );
                    }
                }
            }
        } );
        query2.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public
            void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e ) {
                if ( e != null || queryDocumentSnapshots == null ) {
                    Log.w( TAG , "listen:error" , e );
                    return;
                }

                for ( DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ) {
                    if ( dc.getType() == DocumentChange.Type.ADDED ) {
                        Notification notification = dc.getDocument().toObject( Notification.class );

                        notification.setId( Integer.valueOf( dc.getDocument().getId() ) );
                        if ( listener != null )
                            listener.OnNewNotification( notification );

                    }
                }
            }
        } );
        query3.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public
            void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e ) {
                if ( e != null || queryDocumentSnapshots == null ) {
                    Log.w( TAG , "listen:error" , e );
                    return;
                }

                for ( DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ) {
                    if ( dc.getType() == DocumentChange.Type.ADDED ) {
                        Notification notification = dc.getDocument().toObject( Notification.class );

                        notification.setId( Integer.valueOf( dc.getDocument().getId() ) );
                        if ( listener != null )
                            listener.OnNewNotification( notification );

                    }
                }
            }
        } );
        query4.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public
            void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e ) {
                if ( e != null || queryDocumentSnapshots == null ) {
                    Log.w( TAG , "listen:error" , e );
                    return;
                }

                for ( DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ) {
                    if ( dc.getType() == DocumentChange.Type.ADDED ) {
                        Notification notification = dc.getDocument().toObject( Notification.class );

                        notification.setId( Integer.valueOf( dc.getDocument().getId() ) );
                        if ( listener != null )
                            listener.OnNewNotification( notification );

                    }
                }
            }
        } );
        query5.addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public
            void onEvent ( @Nullable final QuerySnapshot queryDocumentSnapshots ,
                    @Nullable final FirebaseFirestoreException e ) {
                if ( e != null || queryDocumentSnapshots == null ) {
                    Log.w( TAG , "listen:error" , e );
                    return;
                }

                for ( DocumentChange dc : queryDocumentSnapshots.getDocumentChanges() ) {
                    if ( dc.getType() == DocumentChange.Type.ADDED ) {
                        Notification notification = dc.getDocument().toObject( Notification.class );

                        notification.setId( Integer.valueOf( dc.getDocument().getId() ) );
                        if ( listener != null )
                            listener.OnNewNotification( notification );

                    }
                }
            }
        } );
    }

    public
    void getLastNotificationID ( final OnNotificationIdListener listener ) {
        db.collection( NOTIFICATION_COLLECTION )
                .orderBy( "date" , Query.Direction.DESCENDING )
                .limit( 1 )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        int nID;
                        if ( task.isSuccessful() && task.getResult() != null &&
                                task.getResult().getDocuments().size() > 0 ) {
                            nID = Integer.valueOf(
                                    task.getResult().getDocuments().get( 0 ).getId() );
                        }
                        else
                            nID = 0;
                        if ( listener != null )
                            listener.OnLastId( nID );
                    }
                } );
    }

    public
    void uploadImage ( Uri path , String imageName , final OnImageUploadListener listener ) {
        initFirebaseStorage();

        final StorageReference riversRef = storageReference.child( "courses/" + imageName );
        UploadTask uploadTask = riversRef.putFile( path );

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener( new OnFailureListener() {
            @Override
            public
            void onFailure ( @NonNull Exception exception ) {
                listener.OnImageUploadFailed( exception.getMessage() );
            }
        } ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public
            void onSuccess ( UploadTask.TaskSnapshot taskSnapshot ) {
                riversRef.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
                    @Override
                    public
                    void onSuccess ( Uri uri ) {
                        //Log.d( TAG , "onSuccess: uri= " + uri.toString() );
                        listener.OnImageUploaded( uri.toString() );
                    }
                } );
            }
        } );
    }

    public
    interface OnStudentUpdateListener {
        void OnStudentUpdated ();

        void OnStudentUpdateFailed ();
    }

    public
    void getStudent ( final Student student , final OnStudentCheckListener listener ) {
        initFirestore();

        String textCourse = student.getCourse();
        db.collection( COURSE_COLLECTION )
                .document( Course.formatCourseName( textCourse ) )
                .get()
                .addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<DocumentSnapshot> task ) {
                        if ( task.isSuccessful() && task.getResult() != null &&
                                task.getResult().exists() ) {
                            Course course = task.getResult().toObject( Course.class );
                            if ( course != null ) {
                                String strLecturer = course.getLecturer();
                                Log.e( TAG , "onComplete: " + strLecturer );

                                db.collection( STUDENT_COLLECTION )
                                        .document( strLecturer )
                                        .get()
                                        .addOnCompleteListener(
                                                new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public
                                                    void onComplete ( @NonNull
                                                    final Task<DocumentSnapshot> task ) {
                                                        if ( task.isSuccessful() &&
                                                                task.getResult() != null &&
                                                                task.getResult().exists() ) {
                                                            Student student = task.getResult()
                                                                    .toObject( Student.class );
                                                            if ( student != null ) {
                                                                student.setId(
                                                                        task.getResult().getId() );
                                                                if ( listener != null )
                                                                    listener.OnStudentIdentifier(
                                                                            student );
                                                            }
                                                        }
                                                    }
                                                }

                                        )
                                        .addOnFailureListener( new OnFailureListener() {
                                            @Override
                                            public
                                            void onFailure ( @NonNull final Exception e ) {
                                                Log.e( TAG , "onFailure: loading student" );
                                            }
                                        } );
                            }
                        }
                    }
                } );
    }

    public
    void getStudent ( final String id , final OnStudentCheckListener listener ) {

        if ( id == null ) {
            Log.e( TAG , "getStudent: id is null" );
            if ( listener != null )
                listener.OnStudentCheckFailed( "id is null" );
            return;
        }
        initFirestore();

        Log.e( TAG , "getStudent: id= " + id );
        db.collection( STUDENT_COLLECTION )
                .whereEqualTo( "id" , id )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull final Task<QuerySnapshot> task ) {
                        if ( task.getResult() != null &&
                                task.getResult().getDocuments().size() > 0 ) {
                            DocumentSnapshot doc = task.getResult().getDocuments().get( 0 );
                            if ( task.isSuccessful() && task.getResult() != null && doc.exists() ) {
                                Student student = doc.toObject( Student.class );
                                if ( student != null ) {
                                    student.setId( doc.getId() );
                                    if ( listener != null )
                                        listener.OnStudentIdentifier( student );
                                }
                            }
                        }
                        else if ( listener != null )
                            listener.OnStudentCheckFailed( "Document not exist" );
                    }
                } )
                .addOnFailureListener( new OnFailureListener() {
                    @Override
                    public
                    void onFailure ( @NonNull final Exception e ) {
                        Log.e( TAG , "onFailure: loading student" );
                    }
                } );
    }

    public
    interface OnNotificationListener {
        void OnNotificationRead ( List<Notification> notifications );

        void OnNotificationFailed ();

        void OnNewNotification ( Notification notification );
    }

    public
    interface OnCourseUpdateListener {
        void OnCourseUpdated ();

        void OnCourseUpdateFailed ();
    }

    public
    interface OnImageUploadListener {
        void OnImageUploaded ( String uri );

        void OnImageUploadFailed ( String error );
    }

    public
    interface OnQRGenerator {
        void OnQrGenerated ( String strQr );

        void OnQrGenerationFailed ();
    }

    public
    interface OnCourseReadListener {
        void OnCourseRead ( List<Course> courses );
    }

    public
    interface OnGroupReadListener {
        void OnGroupRead ( Map<String, Object> groups );
    }

    public
    interface OnVisitAddListener {
        void OnChangeConfirmed ( Visit visit );
    }

    public
    interface OnVisitCheckListener {
        void OnVisitExisted ( Visit visit );

        void OnVisitNotExist ();
    }

    public
    interface OnVisitCompleteListener {
        void OnVisitCompleted ();
    }

    public
    interface OnStudentCheckListener {
        void OnStudentChecked ( @Nullable Student student );

        void OnStudentCheckFailed ( String exception );

        void OnStudentIdentifier ( Student student );
    }

    public
    interface OnStudentsLoadListener {
        void OnStudentLoaded ( @Nullable List<Student> students );
    }

    public
    interface OnNewStudentListener {
        void OnNewStudentAdded ( @Nullable Student student );
    }

    public
    interface OnVisitListener {
        void OnLoaded ( @Nullable List<Visit> visits );
    }

    public
    interface OnDeveloperListener {
        void OnLoaded ( @Nullable List<String> developers );
    }

    public
    interface OnDoorCodeListener {
        void OnLoaded ( @Nullable List<String> developers );
    }

    public
    interface OnNotificationIdListener {
        void OnLastId ( int id );
    }
}
