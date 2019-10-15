package am.newway.aca.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import am.newway.aca.R;
import am.newway.aca.ui.BaseFragment;
import am.newway.aca.ui.fragments.api.Client;
import am.newway.aca.ui.fragments.api.Service;
import am.newway.aca.ui.fragments.model.Item;
import am.newway.aca.ui.fragments.model.ItemResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AboutFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private Item item;



    public View onCreateView (
            @NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState
    ) {
        View root = inflater.inflate( R.layout.fragment_about , container , false );
        setHasOptionsMenu( true );
        loadJson();
        return root;
    }

    @Override
    public void onPrepareOptionsMenu ( Menu menu ) {
        //menu.findItem( R.id.app_bar_search ).setVisible( false );
        //menu.findItem( R.id.action_filter ).setVisible( false );
        super.onPrepareOptionsMenu( menu );
    }

    @Override
    public int getIndex () {
        return 2;
    }

private  void loadJson(){

    Client Client = new Client();
    Service apiService =
            Client.getClient().create(Service.class);
    Call<ItemResponse> call = apiService.getItems("arsen-simonyan");
    call.enqueue(new Callback<ItemResponse>() {
        @Override
        public void onResponse(Call<ItemResponse> call, Response<ItemResponse> response) {
            List<Item> items = response.body().getItems();
            recyclerView.setAdapter(new ItemAdapter(getContext(), items));
            recyclerView.smoothScrollToPosition(0);
        }

        @Override
        public void onFailure(Call<ItemResponse> call, Throwable t) {
            Log.d("Error", t.getMessage());



        }
    });
}

    }