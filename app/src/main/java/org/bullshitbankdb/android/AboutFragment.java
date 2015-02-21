package org.bullshitbankdb.android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bullshitbankdb.android.compat.PreferenceFragment;


public class AboutFragment extends PreferenceFragment
implements Preference.OnPreferenceClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey()=="src"){

        }
        return false;
    }
}