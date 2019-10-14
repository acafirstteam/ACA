package am.newway.aca.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.Course;
import am.newway.aca.template.Student;
import am.newway.aca.ui.fragments.DialogFragments;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;

    public StudentAdapter(
            List<Student> students , RecyclerView recyclerView
    ) {

        this.students = students;
        this.context = recyclerView.getContext();
    }

    public int getPosition(String name) {
        for (int i = 0; i != students.size(); i++) {
//            if (students.get(i).getUrl().equals(name.trim())) {
//                return i;
//            }
        }
        return -1;
    }

    public void setStudents ( List<Student> students ) {
        this.students.clear();
        this.students.addAll( students );
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(students.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCourseName;
        TextView textViewCourseDescription;
        SimpleDraweeView imageView;
        Student student;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewCourseDescription = itemView.findViewById(R.id.textDialogCoursesDescription);
            textViewCourseName = itemView.findViewById(R.id.textDialogCoursesName);

            imageView = itemView.findViewById(R.id.imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                DialogFragments dialogFragments = new DialogFragments();

                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((AppCompatActivity) context)
                            .getSupportFragmentManager()
                            .getFragments().get(0).getChildFragmentManager();

//                    Uri uri = Uri.parse(student.getUrl());
//                    dialogFragments.setImageUrl(uri);
//                    dialogFragments.show(fragmentManager, " ara");
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //dialogFragments.getActivity().getFragmentManager();
                }
            });
        }

        @SuppressLint("DefaultLocale")
        void bind(Student student) {
            this.student = student;
            //imageView.setImageURI(Uri.parse(course.getUrl()));
        }
    }
}
