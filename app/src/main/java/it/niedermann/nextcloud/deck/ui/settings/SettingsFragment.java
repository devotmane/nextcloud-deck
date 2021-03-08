package it.niedermann.nextcloud.deck.ui.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import it.niedermann.android.sharedpreferences.SharedPreferenceIntLiveData;
import it.niedermann.nextcloud.deck.DeckLog;
import it.niedermann.nextcloud.deck.R;
import it.niedermann.nextcloud.deck.persistence.sync.SyncWorker;
import it.niedermann.nextcloud.deck.ui.branding.BrandedSwitchPreference;

import static androidx.lifecycle.Transformations.map;
import static it.niedermann.nextcloud.deck.DeckApplication.setAppTheme;

public class SettingsFragment extends PreferenceFragmentCompat {

    private BrandedSwitchPreference wifiOnlyPref;
    private BrandedSwitchPreference compactPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        wifiOnlyPref = findPreference(getString(R.string.pref_key_wifi_only));

        if (wifiOnlyPref != null) {
            wifiOnlyPref.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                final Boolean syncOnWifiOnly = (Boolean) newValue;
                DeckLog.log("syncOnWifiOnly: " + syncOnWifiOnly);
                return true;
            });
        } else {
            DeckLog.error("Could not find preference with key: \"" + getString(R.string.pref_key_wifi_only) + "\"");
        }

        Preference themePref = findPreference(getString(R.string.pref_key_dark_theme));
        if (themePref != null) {
            themePref.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                setAppTheme(Integer.parseInt((String) newValue));
                requireActivity().setResult(Activity.RESULT_OK);
                ActivityCompat.recreate(requireActivity());
                return true;
            });
        } else {
            DeckLog.error("Could not find preference with key: \"" + getString(R.string.pref_key_dark_theme) + "\"");
        }

        compactPref = findPreference(getString(R.string.pref_key_compact));

        final ListPreference backgroundSyncPref = findPreference(getString(R.string.pref_key_background_sync));
        if (backgroundSyncPref != null) {
            backgroundSyncPref.setOnPreferenceChangeListener((Preference preference, Object newValue) -> {
                SyncWorker.update(requireContext().getApplicationContext(), (String) newValue);
                return true;
            });
        } else {
            DeckLog.error("Could not find preference with key: \"" + getString(R.string.pref_key_background_sync) + "\"");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        @ColorInt final int colorAccent = ContextCompat.getColor(requireContext(), R.color.accent);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());
        map(
                new SharedPreferenceIntLiveData(sharedPreferences, getString(R.string.shared_preference_color_account), colorAccent),
                (color) -> color == null ? colorAccent : color
        ).observe(getViewLifecycleOwner(), (mainColor) -> {
            wifiOnlyPref.applyBrand(mainColor);
            compactPref.applyBrand(mainColor);
        });
    }
}
