package am.newway.aca.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import am.newway.aca.AdminEditCourseActivity;
import am.newway.aca.R;
import am.newway.aca.template.Course;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public
class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.MyViewHolder> {

    private final String TAG = "AdminMenuAdapter";
    private Context context;
    private ArrayList<Course> courses;

    public
    AdminCourseAdapter ( Context context, ArrayList<Course> courses ) {
        this.context = context;
        this.courses = courses;
    }



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


        MyViewHolder ( @NonNull View itemView ) {
            super( itemView );

            logo = itemView.findViewById( R.id.admin_course_item_logo_id );
            name = itemView.findViewById( R.id.admin_course_item_name_id );
            itemView.setOnClickListener(new View.OnClickListener() {
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

        void bind ( Course course ) {

            logo.setImageURI( Uri.parse( course.getUrl() ) );
            name.setText( course.getName() );
        }
    }
}
