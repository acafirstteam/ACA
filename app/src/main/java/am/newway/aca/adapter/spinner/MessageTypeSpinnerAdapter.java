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
class MessageTypeSpinnerAdapter extends ArrayAdapter<String> {

    private List<Integer> images;

    public
    MessageTypeSpinnerAdapter ( Activity context , int resourceId , int textViewId ,
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
        View rowview = convertView;
        if ( rowview == null ) {
            holder = new ViewHolder();
            final LayoutInflater flatter = ( LayoutInflater ) getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE );
            rowview = flatter.inflate( R.layout.custom_spinner_layout , null , false );

            holder.txtTitle = rowview.findViewById( R.id.icon_spinner_levl_text );
            holder.imageView = rowview.findViewById( R.id.icon_spinner_levl );
            rowview.setTag( holder );
        }
        else {
            holder = ( ViewHolder ) rowview.getTag();
        }
        holder.imageView.setImageResource( images.get( position ) );
        holder.txtTitle.setText( name );

        return rowview;
    }

    private
    class ViewHolder {
        TextView txtTitle;
        SimpleDraweeView imageView;
    }
}
