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
    private List<String> item;
    List<String> strNames = new ArrayList<>();
    List<Item> items = new ArrayList<>();
    private int nCount = 0;


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
        recyclerView = view.findViewById(R.id.recycler_view_members);

        strNames.add("arsen-simonyan");
        strNames.add("Delpy");
        strNames.add("karenlllgabrielyan");
        strNames.add("arsenayan");

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new ItemAdapter(getActivity());

        recyclerView.setAdapter(adapter);
        loadJSON();
    }


    private void loadJSON() {
        Service apiService =
                Client.getClient().create(Service.class);

        for (String name : strNames) {
            Call<Item> call = apiService.getItems(name);
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {

                    if (response == null) Log.e(TAG, "onResponse: is not null");
                    else Log.e(TAG, "onResponse: is null");

                    Item item = response.body();
                    //Log.e(TAG, "onResponse: " + item.getAvatar_url());
                    items.add(item);

                    nCount++;
                    if (nCount == 4)
                        adapter.setItems(items);
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                }
            });
        }
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



