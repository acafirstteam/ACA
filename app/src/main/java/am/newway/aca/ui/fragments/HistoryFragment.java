package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import am.newway.aca.R;
import am.newway.aca.adapter.HistoryAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Visit;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the the
 * {@link HistoryFragment#newInstance} factory method to create an instance of this fragment.
 */
public
class HistoryFragment extends BaseFragment {
    private final String TAG = "History";
    private ArrayList<Visit> items;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    public
    HistoryFragment () {
        // Required empty public constructor
    }

    public static
    HistoryFragment newInstance ( String param1 , String param2 ) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
    }

    @Override
    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_history , container , false );
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        recyclerView = view.findViewById( R.id.recycler_view_history_id );
        LinearLayoutManager layoutManager =
                new LinearLayoutManager( view.getContext() , RecyclerView.VERTICAL , false );
        recyclerView.setLayoutManager( layoutManager );

        FIRESTORE.getVisits( new Firestore.OnVisitListener() {
            @Override
            public
            void OnLoaded ( @Nullable List<Visit> visits ) {
                items = new ArrayList<>( visits );
                adapter = new HistoryAdapter( items );
                recyclerView.setAdapter( adapter );
                Log.d( TAG , "---------------------ListItem Count = " + items.size() );
                Log.d( TAG , "---------------------List 1st data = " +
                        items.get( 0 ).getCompleteTime() );
            }
        } );
    }
}
