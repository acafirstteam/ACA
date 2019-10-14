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
import am.newway.aca.util.LocaleHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;


public
class SettingsFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    private SimpleDraweeView armenia, english;

    public
    SettingsFragment () {

    }

    @Override
    public
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
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
        final SwitchCompat notificationSwitch = view.findViewById( R.id.notification_switch );

        notificationSwitch.setChecked( DATABASE.getSettings().isNotification() );

        notificationSwitch.setOnCheckedChangeListener( this );
        english.setOnClickListener( this );
        armenia.setOnClickListener( this );

                Log.e( TAG ,
                        "onViewCreated: $$$$$$$$$$$$$$$ " +DATABASE.getSettings().getLanguage().equals( ENGLISH )  );
                Log.e( TAG ,
                        "onViewCreated: $$$$$$$$$$$$$$$ " +DATABASE.getSettings().getLanguage() );

        setSelectLanguage(
                DATABASE.getSettings().getLanguage().equals( ARMENIAN ) ? armenia : english );
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

        roundingParams.setBorder( color , 8.0f );
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

        if ( getActivity() != null ) {
            switch ( v.getId() ) {

                case R.id.english_flag_settings_id:
                    DATABASE.getSettings().setLanguage( ENGLISH , getActivity() );
                    setSelectLanguage( english );
                    changeLanguage( ENGLISH );
                    Log.e( TAG , "------------------Set English" );

                    break;
                case R.id.armenian_flag_settings_id:
                    DATABASE.getSettings().setLanguage( ARMENIAN , getActivity() );
                    setSelectLanguage( armenia );
                    changeLanguage( ARMENIAN );
                    Log.e( TAG , "------------------Set Armenian" );

                    break;
            }
        }
    }

    private
    void changeLanguage ( String lang ) {
        LocaleHelper.setLocale( getActivity() , lang );
        if ( getActivity() != null )
            getActivity().recreate();
    }
}


