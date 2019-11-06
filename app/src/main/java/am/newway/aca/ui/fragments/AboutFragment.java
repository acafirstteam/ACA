package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import am.newway.aca.R;
import am.newway.aca.adapter.DeveloperAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.api.Client;
import am.newway.aca.api.Service;
import am.newway.aca.api.model.Item;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public
class AboutFragment extends BaseFragment {
    private DeveloperAdapter adapter;
    private List<Item> items = new ArrayList<>();
    private ProgressBar progressBar;
    private int nCount = 0;

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_about , container , false );
        setHasOptionsMenu( true );
        return root;
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );
        final RecyclerView recyclerView = view.findViewById( R.id.recycler_view_members );
        progressBar = view.findViewById( R.id.loading );

        FIRESTORE.getDevelopers( new Firestore.OnDeveloperListener() {
            @Override
            public
            void OnLoaded ( @Nullable final List<String> developers ) {
                if ( developers != null )
                    loadJSON( developers );
                else
                    progressBar.setVisibility( View.GONE );
            }
        } );

        recyclerView.setLayoutManager(
                new LinearLayoutManager( view.getContext() , LinearLayoutManager.VERTICAL ,
                        false ) );

        adapter = new DeveloperAdapter( getActivity() );

        recyclerView.setAdapter( adapter );
    }

    private
    void loadJSON ( final List<String> strNames ) {
        Service apiService = Client.getClient().create( Service.class );

        for ( String name : strNames ) {
            Call<Item> call = apiService.getItems( name );
            call.enqueue( new Callback<Item>() {
                @Override
                public
                void onResponse ( @NonNull Call<Item> call , @NonNull Response<Item> response ) {

                    Item item = response.body();
                    items.add( item );

                    nCount++;
                    if ( nCount == strNames.size() ) {
                        adapter.setItems( items );
                        progressBar.setVisibility( View.GONE );
                    }
                }

                @Override
                public
                void onFailure ( @NonNull Call<Item> call , @NonNull Throwable t ) {
                    Log.d( "Error" , Objects.requireNonNull( t.getMessage() ) );
                }
            } );
        }
    }

    @Override
    public
    void onPrepareOptionsMenu ( @NonNull Menu menu ) {
        super.onPrepareOptionsMenu( menu );
    }
}



