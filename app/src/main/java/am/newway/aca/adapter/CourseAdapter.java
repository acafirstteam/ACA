package am.newway.aca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.Course;
import am.newway.aca.ui.fragments.DialogFragments;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public
class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> courses;
    private int animPosition = -1;
    private Context context;
    private String lang;
    private RecyclerViewAnimator mAnimator;

    public
    CourseAdapter ( Context context , RecyclerViewAnimator mAnimator ) {
        this.mAnimator = mAnimator;
        this.context = context;
        courses = new ArrayList<>();
    }

    public
    int getPosition ( String name ) {
        for ( int i = 0; i != courses.size(); i++ ) {
            if ( courses.get( i ).getUrl().equals( name.trim() ) ) {
                animPosition = i;
                return i;
            }
        }
        return -1;
    }

    public
    void setCourses ( List<Course> courses ) {
        if(courses != null) {
            this.courses.clear();
            this.courses.addAll( courses );
            notifyDataSetChanged();
        }
    }

    public
    void setLanguage ( String lang ) {
        this.lang = lang;
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull final ViewGroup parent , int viewType ) {
        View view;
        view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.course_item , parent , false );

        mAnimator.onCreateViewHolder( view );

        return new ViewHolder( view );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
        if ( animPosition != -1 ) {
            final Animation myAnim = AnimationUtils.loadAnimation( context , R.anim.bounce );
            holder.itemView.setAnimation( myAnim );
            animPosition = -1;
        }
        else
            mAnimator.onBindViewHolder( holder.itemView , position );
        holder.bind( courses.get( position ) );
    }

    @Override
    public
    int getItemViewType ( int position ) {
        return super.getItemViewType( position );
    }

    @Override
    public
    int getItemCount () {
        return courses.size();
    }

    public
    Course getCourse ( final int i ) {
        return courses.get( i );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_courses;
        TextView textViewCourseName;
        TextView textViewCourseDescription;
        SimpleDraweeView imageView;
        Course course;

        ViewHolder ( @NonNull final View itemView ) {
            super( itemView );

            textViewCourseDescription = itemView.findViewById( R.id.textDialogCoursesDescription );
            textViewCourseName = itemView.findViewById( R.id.textDialogCoursesName );
            item_courses = itemView.findViewById( R.id.course_activity_layout_itm );
            imageView = itemView.findViewById( R.id.imageView );

            if ( imageView != null ) {
                imageView.setOnClickListener( new View.OnClickListener() {
                    DialogFragments dialogFragments = new DialogFragments();

                    @Override
                    public
                    void onClick ( View view ) {
                        final FragmentManager fragmentManager =
                                ( ( AppCompatActivity ) context ).getSupportFragmentManager()
                                        .getFragments()
                                        .get( 0 )
                                        .getChildFragmentManager()
                                        .getFragments()
                                        .get( 0 )
                                        .getChildFragmentManager();

                        dialogFragments.setContext( context );
                        dialogFragments.setCourse( course );
                        dialogFragments.setLanguage( lang );
                        dialogFragments.show( fragmentManager , course.getName() );
                    }
                } );
            }
        }

        //@SuppressLint ( "DefaultLocale" )
        void bind ( Course course ) {

            synchronized ( this ) {

                //Uri uri = Uri.parse( "https://aca.am/assets/img/intro-level/cpp.png" );
                this.course = course;
                if ( imageView != null ) {
                    imageView.setImageURI( course.getUrl() );
                }
                //Log.e( "########" , "bind: " + course.getRes() );
            }
        }
    }
}
