package am.newway.aca.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import am.newway.aca.R;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.Visit;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public
class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private final static String TAG = HistoryAdapter.class.getSimpleName();
    private ArrayList<Visit> items;
    private RecyclerViewAnimator mAnimator;
    private int animPosition = -1;
    private Context context;

    public
    HistoryAdapter ( Context context, ArrayList<Visit> items , RecyclerViewAnimator mAnimator  ) {
        this.mAnimator = mAnimator;
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public
    HistoryAdapter.MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.history_item_layout , parent , false );

        mAnimator.onCreateViewHolder( itemView );

        return new MyViewHolder( itemView );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {
        if ( animPosition != -1 ) {
            final Animation myAnim = AnimationUtils.loadAnimation( context , R.anim.bounce );
            holder.itemView.setAnimation( myAnim );
            animPosition = -1;
        }
        else
            mAnimator.onBindViewHolder( holder.itemView , position );
        holder.bind( items.get( position ) );
    }

    @Override
    public
    int getItemCount () {
        return items.size();
    }


    static
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView comingTime;
        private TextView completeTime;
        private TextView durationTime;

        MyViewHolder ( @NonNull View itemView ) {
            super( itemView );

            dateTime = itemView.findViewById( R.id.dateTime_history_item );
            comingTime = itemView.findViewById( R.id.comming_time_history_id );
            completeTime = itemView.findViewById( R.id.complete_time_history_id );
            durationTime = itemView.findViewById( R.id.duration_time_history_id );
        }

        void bind ( Visit item ) {
            Date dateStart, dateEnd;
            SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" , Locale.US );
            try {
                Log.e( TAG , "bind: " +item.getDateTime()  );
                dateStart = formatter.parse( item.getDateTime() );
                dateEnd = formatter.parse( item.getCompleteTime() );
            } catch ( ParseException e ) {
                e.printStackTrace();
                return;
            }

            if ( dateEnd == null || dateStart == null ) {
                Log.e( TAG , "bind: date is null" );
                return;
            }

            SimpleDateFormat fTime = new SimpleDateFormat( "HH:mm" , Locale.US );
            SimpleDateFormat fDate = new SimpleDateFormat( "dd/MM/yyyy" , Locale.US );

            long diff = dateEnd.getTime() - dateStart.getTime();

            long hours = TimeUnit.MILLISECONDS.toHours( diff );
            long minutes = TimeUnit.MILLISECONDS.toMinutes( diff );

            dateTime.setText( fDate.format( dateStart ) );
            comingTime.setText( fTime.format( dateStart ) );
            completeTime.setText( fTime.format( dateEnd ) );
            durationTime.setText( String.format( Locale.US , "%02d:%02d" , hours , minutes ) );
        }
    }
}
