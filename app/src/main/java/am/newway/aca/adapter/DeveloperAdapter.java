package am.newway.aca.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.api.model.Item;
import androidx.recyclerview.widget.RecyclerView;

public
class DeveloperAdapter extends RecyclerView.Adapter<DeveloperAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;

    public
    DeveloperAdapter ( Context applicationContext ) {
        this.context = applicationContext;
        this.items = new ArrayList<>();
    }

    public
    void setItems ( List<Item> items ) {
        if ( items != null ) {
            this.items.clear();
            this.items.addAll( items );
            notifyDataSetChanged();
        }
    }

    @Override
    public
    DeveloperAdapter.ViewHolder onCreateViewHolder ( ViewGroup viewGroup , int i ) {
        View view = LayoutInflater.from( viewGroup.getContext() )
                .inflate( R.layout.row_user , viewGroup , false );
        return new ViewHolder( view );
    }

    @Override
    public
    void onBindViewHolder ( DeveloperAdapter.ViewHolder viewHolder , int i ) {
        viewHolder.bind( items.get( i ) );
    }

    @Override
    public
    int getItemCount () {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private SimpleDraweeView imageView;
        private Item item;

        ViewHolder ( View view ) {
            super( view );
            title = view.findViewById( R.id.title );
            imageView = view.findViewById( R.id.cover );

            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View v ) {
                    String url = item.getHtml_url();
                    Intent i = new Intent( Intent.ACTION_VIEW );
                    i.setData( Uri.parse( url ) );
                    context.startActivity( i );
                }
            } );
        }

        void bind ( Item item ) {
            this.item = item;
            title.setText( item.getName() );
            imageView.setImageURI( item.getAvatar_url() );
        }
    }
}