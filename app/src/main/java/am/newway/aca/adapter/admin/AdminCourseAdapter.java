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

import am.newway.aca.R;
import am.newway.aca.template.Course;
import am.newway.aca.ui.admin.AdminCourseEditActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public
class AdminCourseAdapter extends RecyclerView.Adapter<AdminCourseAdapter.MyViewHolder> {

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ArrayList<Course> courses;
    private ArrayList<String> groups;
    private String lang;

    public
    AdminCourseAdapter ( Context context , ArrayList<Course> courses , String lang ) {
        this.context = context;
        this.courses = courses;
        this.lang = lang;

        groups = new ArrayList<>();

        String strGroup = "";
        groups.add( context.getString( R.string.select_group ) );
        for ( Course course : courses ) {
            if ( !strGroup.equals( course.getGroup_name( lang ) ) ) {
                strGroup = course.getGroup_name( lang );
                Log.e( TAG , "AdminCourseAdapter: #"+strGroup+"#"   );
                groups.add( strGroup );
            }
        }
    }

    @NonNull
    @Override
    public
    MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.admin_course_item , parent , false );
        return new MyViewHolder( itemView );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {
        holder.bind( courses.get( position ) );
    }

    public
    ArrayList<String> getGroups ( ) {
        return  groups;
    }

    @Override
    public
    int getItemCount () {
        return courses.size();
    }

    public
    void setLanguage ( final String language ) {
        this.lang = language;
    }

    class MyViewHolder extends ViewHolder {

        private SimpleDraweeView logo;
        private TextView name;
        private TextView group;
        private Course course;

        MyViewHolder ( @NonNull View itemView ) {
            super( itemView );

            logo = itemView.findViewById( R.id.logo );
            name = itemView.findViewById( R.id.name );
            group = itemView.findViewById( R.id.group );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View v ) {
                    Log.d( TAG , "--------------------------------Clicked" );

                    int pos = getAdapterPosition();
                    Intent intent = new Intent( context , AdminCourseEditActivity.class );
                    Bundle bundle = new Bundle();
                    bundle.putSerializable( "course", course );
                    intent.putExtras( bundle );
                    intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.putExtra( "groups" , groups );
                    intent.putExtra( "pos" , pos );
                    intent.putExtra( "action" , "update" );
                    context.startActivity( intent );
                }
            } );
        }

        void bind ( Course course ) {
            this.course = course;
            logo.setImageURI( Uri.parse( course.getUrl() ) );
            name.setText( course.getName() );
            group.setText( course.getGroup_name( lang ) );
        }
    }
}
