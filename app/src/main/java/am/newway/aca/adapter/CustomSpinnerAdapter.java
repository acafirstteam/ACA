package am.newway.aca.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import am.newway.aca.R;
import am.newway.aca.template.CustomItemSpinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class CustomSpinnerAdapter extends ArrayAdapter<CustomItemSpinner> {
    public CustomSpinnerAdapter(@NonNull Context context, ArrayList<CustomItemSpinner> customList) {
        super(context, 0, customList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
         if (convertView==null){
             convertView= LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_layout,parent,false);
         }
         CustomItemSpinner itemSpinner= getItem(position);
        ImageView spinnerIV=convertView.findViewById(R.id.icon_spinner_levl);
        TextView spinnerTV=convertView.findViewById(R.id.icon_spinner_levl_text);


        if (itemSpinner!=null) {
            spinnerIV.setImageResource(itemSpinner.getSpinnerItemImage());
            spinnerTV.setText(itemSpinner.getSpinnerItemName());
        }

return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.courese_item_level_recy,parent,false);
        }
        CustomItemSpinner itemSpinner= getItem(position);
        ImageView courseSpinnerIV=convertView.findViewById(R.id.icon_status_levl);
        TextView coursespinnerTV=convertView.findViewById(R.id.icon_status_levl_text);


        if (itemSpinner!=null) {
            courseSpinnerIV.setImageResource(itemSpinner.getSpinnerItemImage());
            coursespinnerTV.setText(itemSpinner.getSpinnerItemName());
        }

        return convertView;
    }


}
