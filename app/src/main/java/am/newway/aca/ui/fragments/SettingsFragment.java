package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import am.newway.aca.R;
import am.newway.aca.ui.BaseFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;


public
class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private SwitchCompat notificationSwitch;
    private SimpleDraweeView armenia, english;
    private final String ENGLISH = "en";
    private final String ARMENIAN = "hy";

    public
    SettingsFragment () {

    }

    public static
    SettingsFragment newInstance () {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        if ( getArguments() != null ) {
        }
    }

    @Override
    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
            Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.fragment_settings , container , false );
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view , savedInstanceState );

        armenia = view.findViewById( R.id.armenian_flag_settings_id );
        english = view.findViewById( R.id.english_flag_settings_id );
        notificationSwitch = view.findViewById( R.id.notification_switch );

        notificationSwitch.setChecked( DATABASE.getSettings().isNotification() );

        notificationSwitch.setOnCheckedChangeListener( this );
        english.setOnClickListener( this );
        armenia.setOnClickListener( this );

        setSelectLanguage(
                DATABASE.getSettings().getLanguage().equals( ENGLISH ) ? english : armenia );
    }

    private
    void setSelectLanguage ( SimpleDraweeView drawer ) {
        int color = getResources().getColor( R.color.colorAccentDark );
        RoundingParams roundingParams = RoundingParams.fromCornersRadius( 5f );
        roundingParams.setRoundAsCircle( true );
        roundingParams.setOverlayColor( getResources().getColor( R.color.white ) );
        roundingParams.setBorder( color , 1.0f );

        english.getHierarchy().setRoundingParams( roundingParams );
        armenia.getHierarchy().setRoundingParams( roundingParams );

        roundingParams.setBorder( color , 10.0f );
        drawer.getHierarchy().setRoundingParams( roundingParams );
    }

    @Override
    public
    void onCheckedChanged ( CompoundButton buttonView , boolean isChecked ) {

        if ( buttonView.getId() == R.id.notification_switch )
            DATABASE.getSettings().setNotification( isChecked );
    }


    @Override
    public
    void onClick ( View v ) {

        switch ( v.getId() ) {

            case R.id.english_flag_settings_id:
                setSelectLanguage( english );
                DATABASE.getSettings().setLanguage( ENGLISH , getActivity() );
                Log.d( TAG , "------------------Set English" );

                break;
            case R.id.armenian_flag_settings_id:
                setSelectLanguage( armenia );
                DATABASE.getSettings().setLanguage( ARMENIAN , getActivity() );
                Log.d( TAG , "------------------Set Armenian" );

                break;
        }
    }
}


