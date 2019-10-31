package am.newway.aca.ui.student;

import java.util.List;

import am.newway.aca.template.Student;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public
class StudentViewModel extends ViewModel {
    private MutableLiveData<List<Student>> mArray;
    private StudentModel model;

    LiveData<List<Student>> getData () {
        return mArray;
    }

    public
    StudentViewModel () {
        mArray = new MutableLiveData<>();
        model = new StudentModel();
    }

    void getStudents ( boolean onlyNew ) {
        model.getStudents( onlyNew , new StudentModel.StudentLoadCallback() {
            @Override
            public
            void dataLoaded ( List<Student> courseArray ) {
                mArray.setValue( courseArray );
            }
        } );
    }
}