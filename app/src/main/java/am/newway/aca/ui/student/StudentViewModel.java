package am.newway.aca.ui.student;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import am.newway.aca.template.Course;
import am.newway.aca.template.Student;

public class StudentViewModel extends ViewModel {
    private MutableLiveData<List<Student>> mArray;
    private StudentModel model;

    LiveData<List<Student>> getData () {
        return mArray;
    }

    public StudentViewModel() {
        mArray = new MutableLiveData<>();
        model = new StudentModel();
    }

    void getStudents () {
        model.getProducts( new StudentModel.StudentLoadCallback() {
            @Override
            public void dataLoaded ( List<Student> courseArray ) {
                mArray.setValue( courseArray );
            }
        } );
    }
}