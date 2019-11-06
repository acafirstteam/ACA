package am.newway.aca.ui.home;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.newway.aca.template.Course;
import androidx.annotation.NonNull;

class HomeModel {
    private List<Course> courses;
    private FirebaseFirestore db;

    HomeModel () {
        db = FirebaseFirestore.getInstance();
        courses = new ArrayList<>();
    }

    void getProducts ( final CourseLoadCallback callback ) {
        db.collection( "Courses" )
                .whereEqualTo( "isdel" , false )
                .orderBy( "group" )
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public
                    void onComplete ( @NonNull Task<QuerySnapshot> task ) {
                        Log.e( "HomeModel" , "Database reading ...: " );
                        if ( task.isSuccessful() ) {
                            courses.clear();
                            for ( QueryDocumentSnapshot document : Objects.requireNonNull(
                                    task.getResult() ) ) {
                                Course course = document.toObject( Course.class );
                                courses.add( course );
                            }
                            callback.dataLoaded( courses );
                        }
                        else {
                            Log.e( "HomeModel" , "Error getting documents." , task.getException() );
                        }
                    }
                } );
    }

    interface CourseLoadCallback {
        void dataLoaded ( List<Course> courses );
    }
}
