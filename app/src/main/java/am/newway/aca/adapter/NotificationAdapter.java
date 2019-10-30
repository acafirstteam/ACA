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
import am.newway.aca.template.Notification;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public
class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<Notification> notifications;
    private int animPosition = -1;
    private Context context;
    private String lang;
    private RecyclerViewAnimator mAnimator;

    public
    NotificationAdapter ( Context context , RecyclerViewAnimator mAnimator ) {
        this.mAnimator = mAnimator;
        this.context = context;
        notifications = new ArrayList<>();
    }

    public
    void setNotifications ( List<Notification> notifications ) {
        if(notifications != null) {
            this.notifications.clear();
            this.notifications.addAll( notifications );
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
                .inflate( R.layout.notification_item , parent , false );

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
        holder.bind( notifications.get( position ) );
    }

    @Override
    public
    int getItemViewType ( int position ) {
        return super.getItemViewType( position );
    }

    @Override
    public
    int getItemCount () {
        return notifications.size();
    }

    public
    Notification getNotification ( final int i ) {
        return notifications.get( i );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_courses;
        TextView textViewCourseName;
        TextView textViewCourseDescription;
        SimpleDraweeView imageView;
        Notification notification;

        ViewHolder ( @NonNull final View itemView ) {
            super( itemView );

            textViewCourseDescription = itemView.findViewById( R.id.textDialogCoursesDescription );
            textViewCourseName = itemView.findViewById( R.id.textDialogCoursesName );
            item_courses = itemView.findViewById( R.id.course_activity_layout_itm );
            imageView = itemView.findViewById( R.id.imageView );


        }

        //@SuppressLint ( "DefaultLocale" )
        void bind ( Notification notification ) {

            synchronized ( this ) {

                //Uri uri = Uri.parse( "https://aca.am/assets/img/intro-level/cpp.png" );
                this.notification = notification;
                if ( imageView != null ) {
                    imageView.setImageURI( notification.getDate() );
                }
            }
        }
    }
}
