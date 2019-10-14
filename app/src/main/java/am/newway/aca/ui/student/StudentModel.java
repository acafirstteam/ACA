package am.newway.aca.ui.student;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

class StudentModel
{
    private List<Student> students;
    private FirebaseFirestore db;

    StudentModel()
    {
        db = FirebaseFirestore.getInstance();
        students = new ArrayList<>(  );
    }

    void getProducts( final StudentLoadCallback callback)
    {
        db.collection("Students")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override public void onComplete( @NonNull Task<QuerySnapshot> task )
                    {
                        Log.e( "StudentModel" , "Database reading ...: "  );
                        if (task.isSuccessful()) {
                            students.clear();
                            QuerySnapshot ref = task.getResult();
                            if(ref != null) {
                                for (DocumentSnapshot document : ref) {
                                    Student student = document.toObject(Student.class);
                                    if (student != null) {
                                        student.setId(document.getId());
                                        students.add(student);
                                    }
                                }
                            }
                            callback.dataLoaded( students );
                        } else {
                            Log.e("StudentModel", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    interface StudentLoadCallback {
        void dataLoaded(List<Student> students);
    }
}
