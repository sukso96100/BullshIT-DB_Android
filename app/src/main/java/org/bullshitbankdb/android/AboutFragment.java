package org.bullshitbankdb.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bullshitbankdb.android.compat.PreferenceFragment;


public class AboutFragment extends PreferenceFragment{
    GuidTool GT;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_general);

        //Get app version name from Manifest
        String app_ver = null;
        try {
            app_ver = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        findPreference("ver").setSummary(app_ver);
        findPreference("src").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://github.com/sukso96100/bullshitbankdb-android"));
                startActivity(intent);
                return false;
            }
        });
        findPreference("web").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sukso96100.github.io/bullsh-itbank-db"));
                startActivity(intent);
                return false;
            }
        });
        GT = new GuidTool(getActivity());
        Preference GUIDpref = findPreference("guid");
        GUIDpref.setSummary(GT.getGUID());
        GUIDpref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                GT.genGUID();
                preference.setSummary(GT.getGUID());
                return false;
            }
        });
    }
}