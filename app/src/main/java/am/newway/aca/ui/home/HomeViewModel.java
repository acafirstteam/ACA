package am.newway.aca.ui.home;

import java.util.List;

import am.newway.aca.template.Course;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Course>> mArray;
    private HomeModel model;

    LiveData<List<Course>> getData () {
        return mArray;
    }

    public HomeViewModel () {
        mArray = new MutableLiveData<>();
        model = new HomeModel();
    }

    void getProducts () {
        //List<Course> courses = App.getInstance().getCourses();
        //if ( courses.size() == 0 ) {
        model.getProducts( new HomeModel.CourseLoadCallback() {
            @Override
            public void dataLoaded ( List<Course> courseArray ) {
                mArray.setValue( courseArray );
                //App.getInstance().setCourse(courseArray);
            }
        } );
        //}
        //else
        //mArray.setValue( courses );
    }
}