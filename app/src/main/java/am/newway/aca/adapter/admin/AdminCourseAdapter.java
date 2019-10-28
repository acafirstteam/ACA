package am.newway.aca.adapter.admin;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.template.Course;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public
class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.MyViewHolder> {

    private final String TAG = "AdminMenuAdapter";
    private ArrayList<Course> courses;

    public
    AdminCourseAdapter ( ArrayList<Course> courses ) {
        this.courses = courses;
    }

    //    public void setBottomReachedListener(BottomReachedListener bottomReachedListener){
    //        this.bottomReachedListener = bottomReachedListener;
    //    }

    @NonNull
    @Override
    public
    MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.admin_page_item_view , parent , false );
        return new MyViewHolder( itemView );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {

        //        if (position == courses.size() - 1){
        //            bottomReachedListener.onBottomReached(position);
        //        }
        holder.bind( courses.get( position ) );

    }

    @Override
    public
    int getItemCount () {
        return courses.size();
    }

    class MyViewHolder extends ViewHolder {

        private SimpleDraweeView logo;
        private TextView name;
        private View editBtn;

        MyViewHolder ( @NonNull View itemView ) {
            super( itemView );

            logo = itemView.findViewById( R.id.admin_course_item_logo_id );
            name = itemView.findViewById( R.id.admin_course_item_name_id );
            editBtn = itemView.findViewById( R.id.admin_course_item_edit_btn_id );
        }

        void bind ( Course course ) {

            logo.setImageURI( Uri.parse( course.getUrl() ) );
            name.setText( course.getName() );
        }
    }
}
