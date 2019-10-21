package am.newway.aca.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.interfaces.BottomReachedListener;
import am.newway.aca.template.Course;

public class AdminPageAdapter extends RecyclerView.Adapter<AdminPageAdapter.MyViewHolder> {

//    BottomReachedListener bottomReachedListener;

    private final String TAG = "AdminAdapter";
    private ArrayList<Course> courses = new ArrayList<>();

    public AdminPageAdapter(ArrayList<Course> courses){
        this.courses = courses;
    }

//    public void setBottomReachedListener(BottomReachedListener bottomReachedListener){
//        this.bottomReachedListener = bottomReachedListener;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_page_item_view, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        if (position == courses.size() - 1){
//            bottomReachedListener.onBottomReached(position);
//        }
        holder.bind(courses.get(position));

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class MyViewHolder extends ViewHolder {

        private SimpleDraweeView logo;
        private TextView name;
        private View editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = (SimpleDraweeView) itemView.findViewById(R.id.admin_course_item_logo_id);
            name = (TextView) itemView.findViewById(R.id.admin_course_item_name_id);
            editBtn = (View) itemView.findViewById(R.id.admin_course_item_edit_btn_id);

        }

        public void bind(Course course){

            logo.setImageURI(Uri.parse(course.getUrl()));
            name.setText(course.getName());

        }

    }
}
