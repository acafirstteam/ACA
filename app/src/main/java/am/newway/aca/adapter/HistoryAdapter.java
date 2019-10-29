package am.newway.aca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import am.newway.aca.R;
import am.newway.aca.template.Visit;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    private final String TAG = "AdapterHistory";

    private ArrayList<Visit> items = new ArrayList<Visit>();

    public HistoryAdapter(ArrayList<Visit> items){
        HistoryAdapter.this.items = items;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView commingTime;
        private TextView completeTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTime = (TextView) itemView.findViewById(R.id.dateTime_history_item);
            commingTime = (TextView) itemView.findViewById(R.id.comming_time_history_id);
            completeTime = (TextView) itemView.findViewById(R.id.complete_time_history_id);
        }

        public void bind(Visit item){
            String dateString = item.getDateTime();
//            Log.d("history", "DateTime: " + dateString);
//            String dateString2 = item.getCompleteTime();
//            Log.d("history", "CompleteTime: " + dateString2);


            String[] split = dateString.split(" ");
            dateTime.setText(split[0]);
            String str1 = split[1].substring(0,5);
            commingTime.setText(str1);

            String dateString2 = item.getCompleteTime().substring(11,16);
            completeTime.setText(dateString2);
    }
}

}
