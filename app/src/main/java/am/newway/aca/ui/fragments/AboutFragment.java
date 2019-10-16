package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import am.newway.aca.R;
import am.newway.aca.adapter.ItemAdapter;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;
import am.newway.aca.ui.fragments.api.Client;
import am.newway.aca.ui.fragments.api.Service;
import am.newway.aca.ui.fragments.model.Item;
import am.newway.aca.ui.fragments.model.ItemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private final String TAG = getClass().getSimpleName();
    private ItemAdapter adapter;
    private  List<String> item;
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        recyclerView =  view.findViewById(R.id.recycler_view_members);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new ItemAdapter(getActivity());

        recyclerView.setAdapter(adapter);
        loadJSON();
    }



    private void loadJSON() {
        Service apiService =
                Client.getClient().create(Service.class);
        Call<ItemResponse> call = apiService.getItems("arsen-simonyan");
        call.enqueue(new Callback<ItemResponse>() {
            @Override
            public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
                List<Item> items = response.body().getItems();
                adapter.setItems(items);
            }
            @Override
            public void onFailure(Call<ItemResponse> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //menu.findItem( R.id.app_bar_search ).setVisible( false );
        //menu.findItem( R.id.action_filter ).setVisible( false );
        super.onPrepareOptionsMenu(menu);
    }

    //@Override
    public int getIndex() {
        return 2;
    }

    }



