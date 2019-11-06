package am.newway.aca.adapter.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.template.AdminItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public
class AdminMenuAdapter extends RecyclerView.Adapter<AdminMenuAdapter.ViewHolder> {
    private Context context;
    private String lang;
    private List<AdminItem> items;

    public
    AdminMenuAdapter ( Context context ) {
        this.context = context;
        items = new ArrayList<>();
    }

    public
    void setLanguage ( String lang ) {
        this.lang = lang;
    }

    public
    void setItems ( List<AdminItem> items ) {
        if ( items != null ) {
            this.items.clear();
            this.items.addAll( items );
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public
    ViewHolder onCreateViewHolder ( @NonNull final ViewGroup parent , int viewType ) {
        View view;
        view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.admin_item , parent , false );

        return new ViewHolder( view );
    }

    @Override
    public
    void onBindViewHolder ( @NonNull ViewHolder holder , int position ) {
        final Animation myAnim = AnimationUtils.loadAnimation( context , R.anim.bounce );
        holder.itemView.setAnimation( myAnim );
        holder.bind( items.get( position ) );
    }

    @Override
    public
    int getItemViewType ( int position ) {
        return super.getItemViewType( position );
    }

    @Override
    public
    int getItemCount () {
        return items.size();
    }

    public
    AdminItem getCourse ( final int i ) {
        return items.get( i );
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        SimpleDraweeView imageView;
        AdminItem item;
        PorterDuffColorFilter colorFilter =
                new PorterDuffColorFilter( context.getResources().getColor(R.color.colorPrimaryLight),
                PorterDuff.Mode.SRC_IN);

        ViewHolder ( @NonNull final View itemView ) {
            super( itemView );

            textDescription = itemView.findViewById( R.id.text_description );
            textName = itemView.findViewById( R.id.text_name );
            imageView = itemView.findViewById( R.id.imageView );

            itemView.setOnClickListener( new View.OnClickListener() {
                @Override
                public
                void onClick ( View view ) {
                    context.startActivity( new Intent( context , item.getCl() ) );
                }
            } );
        }

        void bind ( AdminItem item ) {

            this.item = item;
            textName.setText( item.getName( lang ) );
            textDescription.setText( item.getDescription( lang ) );
            if ( imageView != null ) {
                imageView.setColorFilter(colorFilter);
                imageView.setImageResource( item.getRes() );
            }
        }
    }
}
