package am.newway.aca.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import am.newway.aca.R;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CompanyFragment extends BaseFragment {

    public View onCreateView (
            @NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState
    ) {
        View root = inflater.inflate( R.layout.fragment_company , container , false );
        setHasOptionsMenu( true );
        return root;
    }

    @Override
    public void onViewCreated (
            @NonNull final View view , @Nullable final Bundle savedInstanceState
    ) {
        super.onViewCreated( view , savedInstanceState );

        view.findViewById( R.id.partner1 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( final View view ) {
                openPartnePage( Uri.parse( view.getTag().toString() ) );
            }
        } );
        view.findViewById( R.id.partner2 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( final View view ) {
                openPartnePage( Uri.parse( view.getTag().toString() ) );
            }
        } );
        view.findViewById( R.id.partner3 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( final View view ) {
                openPartnePage( Uri.parse( view.getTag().toString() ) );
            }
        } );
        view.findViewById( R.id.partner4 ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( final View view ) {
                openPartnePage( Uri.parse( view.getTag().toString() ) );
            }
        } );
    }

    @Override
    public void onPrepareOptionsMenu ( Menu menu ) {
        //menu.findItem( R.id.app_bar_search ).setVisible( false );
        //menu.findItem( R.id.action_filter ).setVisible( false );
        super.onPrepareOptionsMenu( menu );
    }

    @Override
    public int getIndex () {
        return 1;
    }

    private void openPartnePage ( Uri uri ) {
        Intent intent = new Intent( Intent.ACTION_VIEW );
        intent.setData( uri );
        startActivity( intent );
    }
}