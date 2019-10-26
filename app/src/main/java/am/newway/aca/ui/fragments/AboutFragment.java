package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import am.newway.aca.R;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;

public
class AboutFragment extends BaseFragment {

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState

    ) {
        View root = inflater.inflate( R.layout.fragment_about , container , false );
        setHasOptionsMenu( true );
        return root;
    }

    @Override
    public
    void onPrepareOptionsMenu ( Menu menu ) {
        menu.findItem( R.id.action_lincenses ).setVisible( true );
        super.onPrepareOptionsMenu( menu );
    }
}