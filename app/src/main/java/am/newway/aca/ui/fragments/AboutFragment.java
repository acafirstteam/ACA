package am.newway.aca.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import am.newway.aca.R;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;

public class AboutFragment extends BaseFragment {
private Button btnExam;

    public View onCreateView (
            @NonNull LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState



    ) {
        View root = inflater.inflate( R.layout.fragment_about , container , false );
        final TextView textView = root.findViewById( R.id.text_slideshow );
        btnExam= root.findViewById(R.id.btnAdminExample);
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        setHasOptionsMenu( true );
        return root;
    }

    @Override
    public void onPrepareOptionsMenu ( Menu menu ) {
        //menu.findItem( R.id.app_bar_search ).setVisible( false );
        //menu.findItem( R.id.action_filter ).setVisible( false );
        super.onPrepareOptionsMenu( menu );
    }

}