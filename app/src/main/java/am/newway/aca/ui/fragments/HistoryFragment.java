package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import am.newway.aca.R;
import am.newway.aca.adapter.HistoryAdapter;
import am.newway.aca.template.HistoryItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * the {@link HistoryFragment#newInstance} factory method to create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HistoryItem> items;
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

        items = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view_history_id);

        HistoryItem ob1, ob2,ob3,ob4,ob5,ob6,ob7,ob8;
        ob1 = new HistoryItem("00.00.01", "qrcode", "ident", true);
        ob2 = new HistoryItem("00.00.02", "qrcode", "ident", true);
        ob3 = new HistoryItem("00.00.03", "qrcode", "ident", true);
        ob4 = new HistoryItem("00.00.04", "qrcode", "ident", true);
        ob5 = new HistoryItem("00.00.05", "qrcode", "ident", true);
        ob6 = new HistoryItem("00.00.06", "qrcode", "ident", true);
        ob7 = new HistoryItem("00.00.07", "qrcode", "ident", true);
        ob8 = new HistoryItem("00.00.08", "qrcode", "ident", true);

        items.add(ob1);
        items.add(ob2);
        items.add(ob3);
        items.add(ob4);
        items.add(ob5);
        items.add(ob6);
        items.add(ob7);
        items.add(ob8);

        adapter = new HistoryAdapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
