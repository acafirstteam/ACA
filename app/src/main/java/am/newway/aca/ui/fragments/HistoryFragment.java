package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import am.newway.aca.R;
import am.newway.aca.adapter.HistoryAdapter;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Visit;
import am.newway.aca.ui.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * the {@link HistoryFragment#newInstance} factory method to create an instance of this fragment.
 */
public class HistoryFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private final String TAG = "History";
    private String mParam1;
    private String mParam2;
    private ArrayList<Visit> items;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;

    public HistoryFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1
     *         Parameter 1.
     * @param param2
     *         Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance ( String param1 , String param2 ) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString( ARG_PARAM1 , param1 );
        args.putString( ARG_PARAM2 , param2 );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
            mParam1 = getArguments().getString( ARG_PARAM1 );
            mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView (
            LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_history , container , false );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recycler_view_history_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        FIRESTORE.getVisits(new Firestore.OnVisitListener() {
            @Override
            public void OnLoaded(@Nullable List<Visit> visits) {
                items = new ArrayList<Visit>(visits);
                adapter = new HistoryAdapter(items);
                recyclerView.setAdapter(adapter);
                Log.d(TAG,"---------------------ListItem Count = " + items.size());
            }
        });



    }

}
