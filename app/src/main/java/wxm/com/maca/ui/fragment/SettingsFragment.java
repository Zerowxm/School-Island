package wxm.com.maca.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import wxm.com.maca.R;

/**
 * Created by Zero on 10/23/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
