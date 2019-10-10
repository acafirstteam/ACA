package am.newway.aca.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import am.newway.aca.R;
import am.newway.aca.database.DatabaseSettings;
import am.newway.aca.template.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * Use the {@link SettingsFragment#newInstance} factory method to create an instance of this
 * fragment.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "SettingsFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Switch notificationSwitch;
    private boolean notificationSwitchState;
    private TextView textViewNotif, textViewLang, textVIEW;
    private ImageView englishFlag, armenianFlag;
    private View englishSelectView, armenianSelectView;
    private DatabaseSettings dbSettings;
    private Settings settings;
    private boolean firstSet = true;
    private final String ENGLISH = "english";
    private final String ARMENIAN = "armenian";

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbSettings = new DatabaseSettings(getActivity());

        if (dbSettings.checkDB() == false) {
            settings = new Settings(true, true, ENGLISH);
            dbSettings.setSettings(settings);
            Log.d(TAG, "------------------New SETTINGS ADDED");
        } else {
            settings = dbSettings.getSettings();
            Log.d(TAG, "-------------------Settings loaded from DB");
        }

        notificationSwitch = new Switch(getActivity());
        notificationSwitch = (Switch) view.findViewById(R.id.notification_switch_id);
        textViewNotif = (TextView) view.findViewById(R.id.textViewNotif_settings);
        textViewLang = (TextView) view.findViewById(R.id.textViewLang_settings);
        englishFlag = (ImageView) view.findViewById(R.id.english_flag_settings_id);
        armenianFlag = (ImageView) view.findViewById(R.id.armenian_flag_settings_id);
        englishSelectView = (View) view.findViewById(R.id.english_selector_id);
        armenianSelectView = (View) view.findViewById(R.id.armenian_selector_id);


        englishFlag.setOnClickListener(this);
        armenianFlag.setOnClickListener(this);
        notificationSwitch.setOnCheckedChangeListener(this);



        String a = String.valueOf(settings.isCheckNotification());
        textViewNotif.setText(a);


        if (settings.isCheckNotification()) {
            notificationSwitch.setChecked(true);
            Log.d(TAG, "------------------set Switch true (if)");
            textViewNotif.setText("ON");
            firstSet = false;
        } else {
            notificationSwitch.setChecked(false);
            Log.d(TAG, "------------------set Switch false (if)");
            textViewNotif.setText("OFF");
        }

        if (settings.getLanguageCheck().toString().equals(ENGLISH)) {
            Log.d(TAG, "------------------set BlackColor English (if)");
            englishSelectView.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            Log.d(TAG, "------------------set BlackColor Armenian (if)");
            armenianSelectView.setBackgroundColor(getResources().getColor(R.color.black));
            textViewLang.setText(ARMENIAN);
        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "------------------Enter SwitchCheck");


        if (firstSet == false) {
            Log.d(TAG, "------------------Switch Changed");


            switch (buttonView.getId()) {
                case R.id.notification_switch_id:

                    if (settings.isCheckNotification()) {
                        Log.d(TAG, "------------------if isChecked---------->uncheck");
                        textViewNotif.setText("OFF");
                        notificationSwitch.setChecked(false);
                        Log.d(TAG, "------------------Switch set false");
                        settings.setCheckNotification(false);
                        dbSettings.updateSettings(settings);
                    } else {
                        Log.d(TAG, "------------------if notChecked---------> check");

                        textViewNotif.setText("ON");
                        notificationSwitch.setChecked(true);
                        settings.setCheckNotification(true);
                        dbSettings.updateSettings(settings);
                    }

                    break;
            }
        }
        firstSet = false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.english_flag_settings_id:
                englishSelectView.setBackgroundColor(getResources().getColor(R.color.black));
                armenianSelectView.setBackgroundResource(R.color.zxing_transparent);
                textViewLang.setText(ENGLISH);
                settings.setLanguageCheck(ENGLISH);
                dbSettings.updateSettings(settings);
                Log.d(TAG, "------------------Set English");

                break;
            case R.id.armenian_flag_settings_id:
                armenianSelectView.setBackgroundColor(getResources().getColor(R.color.black));
                englishSelectView.setBackgroundResource(R.color.zxing_transparent);
                textViewLang.setText(ARMENIAN);
                settings.setLanguageCheck(ARMENIAN);
                dbSettings.updateSettings(settings);
                Log.d(TAG, "------------------Set Armenian");

                break;
        }

}

}


