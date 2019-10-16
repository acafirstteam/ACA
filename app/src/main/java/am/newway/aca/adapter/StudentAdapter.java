package am.newway.aca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> students;
    private Context context;

    public StudentAdapter(
            List<Student> students, RecyclerView recyclerView
    ) {
        this.students = students;
        this.context = recyclerView.getContext();
    }

    public void setStudents(List<Student> students) {
        this.students.clear();
        this.students.addAll(students);
        Log.e("@@@@@@@@@@@@@@@@@@", "onComplete: " + students.size());

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
    public int getItemCount() {
        return students.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCourseName;
        TextView textEmailStudentItm;
        TextView textPhoneStudentItm;
        TextView textIdStudentItm;
        TextView textNameStudentItm;

        SimpleDraweeView imageView;
        Student student;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            textViewCourseName = itemView.findViewById(R.id.textDialogCoursesName);
            textNameStudentItm = itemView.findViewById(R.id.textNameStudentItm);
            textEmailStudentItm = itemView.findViewById(R.id.textEmailStudentItm);
            textPhoneStudentItm = itemView.findViewById(R.id.textPhoneStudentItm);
            textIdStudentItm = itemView.findViewById(R.id.textIdStudentItm);

            imageView = itemView.findViewById(R.id.imageViewStudentItem);

            imageView.setOnClickListener(new View.OnClickListener() {
                // DialogFragments dialogFragments = new DialogFragments();

                @Override
                public void onClick(View view) {
       /*             FragmentManager fragmentManager = ((AppCompatActivity) context)
                            .getSupportFragmentManager()
                            .getFragments().get(0).getChildFragmentManager();*/

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
            textNameStudentItm.setText(student.getName());
            textEmailStudentItm.setText(student.getEmail());
            textIdStudentItm.setText(student.getId());
            textPhoneStudentItm.setText(student.getPhone());
            imageView.setImageURI(student.getPicture());
            //imageView.setImageURI(Uri.parse(course.getUrl()));
        }
    }
}
