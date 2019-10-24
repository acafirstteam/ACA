package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import am.newway.aca.AdminActivity;
import am.newway.aca.R;
import am.newway.aca.adapter.AdminAdapter;
import am.newway.aca.anim.RecyclerViewAnimator;
import am.newway.aca.template.AdminItem;
import am.newway.aca.ui.BaseFragment;
import am.newway.aca.ui.student.StudenActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * the {@link AdminFragment#newInstance} factory method to create an instance of this fragment.
 */
public class AdminFragment extends BaseFragment {
    private final String TAG = "Admin";
    private ArrayList<AdminItem> items;
    private RecyclerView recyclerView;
    private AdminAdapter adapter;

    public
    AdminFragment () {
        // Required empty public constructor
    }

    public static
    AdminFragment newInstance(String param1, String param2) {
        AdminFragment fragment = new AdminFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        items = new ArrayList<>(  );
        recyclerView = view.findViewById( R.id.recycler_view );
        LinearLayoutManager layoutManager =
                new LinearLayoutManager( view.getContext() , RecyclerView.VERTICAL , false );
        recyclerView.setLayoutManager( layoutManager );

        AdminItem item = new AdminItem();
        item.setName( "1" );
        item.setDescription( "2" );
        item.setUrl( "https://firebasestorage.googleapis.com/v0/b/acafirst-a0a43.appspot.com/o/47536.png?alt=media&token=494df820-fcd1-42bc-b7ac-dcc640a6c3fd" );
        item.setCl( AdminActivity.class );

        items.add( item );

        item = new AdminItem();
        item.setName( "3" );
        item.setDescription( "4" );
        item.setUrl( "https://firebasestorage.googleapis.com/v0/b/acafirst-a0a43.appspot.com/o/33308.png?alt=media&token=8adbf375-9d17-4557-86ac-9c437ce8484e" );
        item.setCl( StudenActivity.class );

        items.add( item );

        adapter = new AdminAdapter( getActivity(), new RecyclerViewAnimator( recyclerView ) );
        adapter.setItems( items );
        recyclerView.setAdapter( adapter );
    }
}
