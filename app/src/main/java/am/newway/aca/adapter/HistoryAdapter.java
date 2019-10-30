package am.newway.aca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.template.Visit;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public
class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private final static String TAG = "HistoryAdapter";

    private ArrayList<Visit> items = new ArrayList<>();

    public
    HistoryAdapter ( ArrayList<Visit> items ) {
        HistoryAdapter.this.items = items;
    }

    @NonNull
    @Override
    public
    HistoryAdapter.MyViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.history_item_layout , parent , false );
        return new MyViewHolder( itemView );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull MyViewHolder holder , int position ) {
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

        MyViewHolder ( @NonNull View itemView ) {
            super( itemView );

            dateTime = itemView.findViewById( R.id.dateTime_history_item );
            comingTime = itemView.findViewById( R.id.comming_time_history_id );
            completeTime = itemView.findViewById( R.id.complete_time_history_id );
        }

        void bind ( Visit item ) {
            String dateString = item.getDateTime();

            String[] split = dateString.split( " " );
            dateTime.setText( split[0] );
            String str1 = split[1].substring( 0 , 5 );
            comingTime.setText( str1 + "  - " );
            Log.e( TAG , "bind: " + item.getCompleteTime() );
            String dateString2 = item.getCompleteTime().substring( 10 , 16 );
            completeTime.setText( dateString2 );
        }
    }
}
