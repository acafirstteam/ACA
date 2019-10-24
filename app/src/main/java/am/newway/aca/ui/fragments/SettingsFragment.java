package am.newway.aca.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLogTags;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import am.newway.aca.AdminActivity;
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
    private Button adminActivityBtn;

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

        adminActivityBtn = view.findViewById(R.id.to_adminActivityBtn_id);
        armenia = view.findViewById( R.id.armenian_flag_settings_id );
        english = view.findViewById( R.id.english_flag_settings_id );
        final SwitchCompat notificationSwitch = view.findViewById( R.id.notification_switch );
        final SwitchCompat animationSwitch = view.findViewById( R.id.animation_switch );

        notificationSwitch.setChecked( DATABASE.getSettings().isNotification() );
        animationSwitch.setChecked( DATABASE.getSettings().isFirstAnimation() );

        notificationSwitch.setOnCheckedChangeListener( this );
        animationSwitch.setOnCheckedChangeListener( this );
        english.setOnClickListener( this );
        armenia.setOnClickListener( this );
        adminActivityBtn.setOnClickListener(this);

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
        else if ( buttonView.getId() == R.id.animation_switch )
            DATABASE.getSettings().setFirstAnimation( isChecked );
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

                case R.id.to_adminActivityBtn_id:
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "------------------------------StartActivity ADMIN");
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


