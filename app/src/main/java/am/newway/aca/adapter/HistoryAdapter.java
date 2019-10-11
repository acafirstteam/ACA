package am.newway.aca.adapter;

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

    ArrayList<Visit> items = new ArrayList<Visit>();

    public HistoryAdapter(ArrayList<Visit> items){
        this.items = items;
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTime = (TextView) itemView.findViewById(R.id.dateTime_history_item);

        }

        public void bind(Visit item){
            dateTime.setText(item.getDateTime());
    }
}

}
