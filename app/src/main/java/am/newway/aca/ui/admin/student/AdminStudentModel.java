package am.newway.aca.ui.admin.student;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.template.Student;
import androidx.annotation.NonNull;

class AdminStudentModel {
    private List<Student> students;
    private FirebaseFirestore db;

    AdminStudentModel () {
        db = FirebaseFirestore.getInstance();
        students = new ArrayList<>();
    }

    void getStudents ( boolean onlyNew , final StudentLoadCallback callback ) {
        Query ref;
        Log.e( "AdminStudentModel" , "getStudents: " + onlyNew );
        if ( onlyNew )
            ref = db.collection( "Students" ).whereEqualTo( "type" , -1 );
        else
            ref = db.collection( "Students" ).orderBy( "age" );
        ref.get().addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
            @Override
            public
            void onComplete ( @NonNull Task<QuerySnapshot> task ) {
                Log.e( "AdminStudentModel" , "Database reading ...: " );
                if ( task.isSuccessful() ) {
                    students.clear();
                    QuerySnapshot ref = task.getResult();
                    if ( ref != null ) {
                        for ( DocumentSnapshot document : ref ) {
                            Student student = document.toObject( Student.class );
                            if ( student != null ) {
                                student.setId( document.getId() );
                                students.add( student );
                            }
                        }
                    }
                    callback.dataLoaded( students );
                }
                else {
                    Log.e( "AdminStudentModel" , "Error getting documents." , task.getException() );
                }
            }
        } );
    }

    interface StudentLoadCallback {
        void dataLoaded ( List<Student> students );
    }
}
