package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the the
 * {@link AlertFragment#newInstance} factory method to create an instance of this fragment.
 */
public
class AlertFragment extends BaseFragment {
    private final String TAG = "Alert";
    private RecyclerView recyclerView;

    public
    AlertFragment () {
        // Required empty public constructor
    }

    public static
    AlertFragment newInstance ( String param1 , String param2 ) {
        AlertFragment fragment = new AlertFragment();
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
                //recyclerView.setAdapter(adapter);

            }
        } );
    }
}
