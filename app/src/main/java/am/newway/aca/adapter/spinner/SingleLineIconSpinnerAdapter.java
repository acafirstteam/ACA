package am.newway.aca.adapter.spinner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import am.newway.aca.R;

public
class SingleLineIconSpinnerAdapter extends ArrayAdapter<String> {

    private List<Integer> images;

    public
    SingleLineIconSpinnerAdapter ( Activity context , int resourceId , int textViewId ,
            List<String> list ) {

        super( context , resourceId , textViewId , list );
    }

    public
    void setImages ( List<Integer> images ) {
        this.images = images;
    }

    @Override
    public
    View getView ( int position , View convertView , ViewGroup parent ) {
        return rowView( convertView , position );
    }

    @Override
    public
    View getDropDownView ( int position , View convertView , ViewGroup parent ) {
        return rowView( convertView , position );
    }

    private
    View rowView ( View convertView , int position ) {

        String name = getItem( position );

        ViewHolder holder;
        View rowView = convertView;
        if ( rowView == null ) {
            holder = new ViewHolder();
            final LayoutInflater flatter = ( LayoutInflater ) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE );
            rowView = flatter.inflate( R.layout.custom_spinner_layout , null , false );

            holder.txtTitle = rowView.findViewById( R.id.icon_spinner_levl_text );
            holder.imageView = rowView.findViewById( R.id.icon_spinner_levl );
            rowView.setTag( holder );
        }
        else {
            holder = ( ViewHolder ) rowView.getTag();
        }
        holder.imageView.setImageResource( images.get( position ) );
        holder.txtTitle.setText( name );

        return rowView;
    }

    private
    class ViewHolder {
        TextView txtTitle;
        SimpleDraweeView imageView;
    }
}
