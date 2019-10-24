package am.newway.aca.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.ArrayList;
import am.newway.aca.AdminEditCourseActivity;
import am.newway.aca.R;
import am.newway.aca.template.Course;

public class AdminPageAdapter extends RecyclerView.Adapter<AdminPageAdapter.MyViewHolder> {

    private final String TAG = "AdminAdapter";
    private Context context;
    private ArrayList<Course> courses = new ArrayList<>();

    public AdminPageAdapter(Context context, ArrayList<Course> courses){
        this.context = context;
        this.courses = courses;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_page_item_view, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "--------------------------------Cklicked");

                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, AdminEditCourseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pos", pos);
                    bundle.putString("action", "update");
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }

        public void bind(Course course){

            logo.setImageURI(Uri.parse(course.getUrl()));
            name.setText(course.getName());

        }

    }
}
