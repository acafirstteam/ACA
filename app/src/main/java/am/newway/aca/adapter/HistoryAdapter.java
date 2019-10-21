package am.newway.aca.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
//            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
//            try {
//                Date date = format.parse(dateString);
//            }catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

            String[] split = dateString.split(" ");
            dateTime.setText(split[0]);
            String str1 = split[1].substring(0,5);
            commingTime.setText(str1 + " - ");
//            commingTime.setText(split[1] + " - ");

            String dateString2 = item.getCompleteTime();
            String[] split2 = dateString2.split(" ");
            String str2 = split2[1].substring(0,5);
            completeTime.setText(str2);
    }
}

}
