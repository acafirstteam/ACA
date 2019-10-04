package am.newway.aca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.Course;
import am.newway.aca.anim.RecyclerViewAnimator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> courses;
    private int animPosition = -1;
    private boolean isLarge;
    private OnOrientationChangingListener listener;
    private Context context;
    private RecyclerViewAnimator mAnimator;

    public CourseAdapter (
            List<Course> products , RecyclerView recyclerView ,
            OnOrientationChangingListener listener
    ) {
        mAnimator = new RecyclerViewAnimator( recyclerView );

        this.courses = products;
        this.listener = listener;
        this.context = recyclerView.getContext();
    }

    public int getPosition ( String name ) {
        for ( int i = 0; i != courses.size(); i++ ) {
            if ( courses.get( i ).getUrl().equals( name.trim() ) ) {
                animPosition = i;
                return i;
            }
        }
        return -1;
    }

    public void setProducts ( List<Course> courses ) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.course_item , parent , false );
        mAnimator.onCreateViewHolder( view );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
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
    public int getItemViewType ( int position ) {
        return super.getItemViewType( position );
    }

    @Override
    public int getItemCount () {
        return courses.size();
    }

    public interface OnOrientationChangingListener {
        void OnChanged ( boolean isLarge , int position );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView imageView;

        ViewHolder ( @NonNull View itemView ) {
            super( itemView );

            imageView = itemView.findViewById( R.id.imageView );

            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick ( View view ) {

                }
            } );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick ( View view ) {
                    if ( isLarge ) {
                        isLarge = false;
                        listener.OnChanged( false , getAdapterPosition() );
                    }
                }
            } );
        }

        @SuppressLint ( "DefaultLocale" )
        void bind ( Course course ) {
            imageView.setImageURI( Uri.parse( course.getUrl() ) );
        }
    }
}
