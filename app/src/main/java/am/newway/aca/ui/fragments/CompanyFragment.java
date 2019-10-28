package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import am.newway.aca.R;
import am.newway.aca.firebase.Firestore;
import am.newway.aca.template.Student;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public
class CompanyFragment extends BaseFragment {

    private TextView textCode;

    public
    View onCreateView ( @NonNull LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        View root = inflater.inflate( R.layout.fragment_company , container , false );
        setHasOptionsMenu( true );
        return root;
    }

    @Override
    public
    void onViewCreated ( @NonNull final View view , @Nullable final Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        textCode = view.findViewById( R.id.textCode );

        Student student = DATABASE.getStudent();
        if ( student.getId() != null && !student.getId().equals( "-1" ) ) {

            FIRESTORE.getDoorCode( new Firestore.OnDoorCodeListener() {
                @Override
                public
                void OnLoaded ( @Nullable final List<String> developers ) {
                    if ( developers != null )
                        textCode.setText( developers.get( 0 ) );
                }
            } );
        }else{
            textCode.setVisibility( View.GONE );
            view.findViewById( R.id.textDoorCode ).setVisibility( View.GONE );
        }
    }

    @Override
    public
    void onPrepareOptionsMenu ( @NonNull Menu menu ) {
        super.onPrepareOptionsMenu( menu );
    }
}