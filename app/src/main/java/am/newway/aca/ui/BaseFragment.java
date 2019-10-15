package am.newway.aca.ui;

import android.app.Activity;
import android.os.Bundle;

import am.newway.aca.MainActivity;
import am.newway.aca.database.DatabaseHelper;
import am.newway.aca.firebase.Firestore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract
class BaseFragment extends Fragment {

    protected DatabaseHelper DATABASE;
    protected Firestore FIRESTORE;
    protected final String ENGLISH = "en";
    protected final String ARMENIAN = "hy";

    @Override
    public
    void onCreate ( @Nullable final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        Activity activity = getActivity();
        if ( activity != null ) {

            DATABASE = ( ( MainActivity ) activity ).getDATABASE();
            FIRESTORE = ( ( MainActivity ) activity ).getFIRESTORE();
        }
    }
}
