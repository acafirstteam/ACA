package am.newway.aca.ui.admin.student;

import java.util.List;

import am.newway.aca.template.Student;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public
class AdminStudentViewModel extends ViewModel {
    private MutableLiveData<List<Student>> mArray;
    private AdminStudentModel model;

    LiveData<List<Student>> getData () {
        return mArray;
    }

    public
    AdminStudentViewModel () {
        mArray = new MutableLiveData<>();
        model = new AdminStudentModel();
    }

    void getStudents ( boolean onlyNew ) {
        model.getStudents( onlyNew , new AdminStudentModel.StudentLoadCallback() {
            @Override
            public
            void dataLoaded ( List<Student> courseArray ) {
                mArray.setValue( courseArray );
            }
        } );
    }
}