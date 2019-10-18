package am.newway.aca.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.template.Course;

public class AdminPageAdapter extends RecyclerView.Adapter<AdminPageAdapter.MyViewHolder> {

    private ArrayList<Course> courses = new ArrayList<>();

    public AdminPageAdapter(ArrayList<Course> courses){
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

    public static class MyViewHolder extends ViewHolder {

        private ImageView lgog;
        private TextView name;
        private View editBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Course course){

        }

    }
}
