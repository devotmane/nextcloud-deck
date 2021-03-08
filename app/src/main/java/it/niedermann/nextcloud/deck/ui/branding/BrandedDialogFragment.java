package it.niedermann.nextcloud.deck.ui.branding;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import it.niedermann.android.sharedpreferences.SharedPreferenceIntLiveData;
import it.niedermann.nextcloud.deck.R;

import static androidx.lifecycle.Transformations.map;

public abstract class BrandedDialogFragment extends DialogFragment {

    protected LiveData<Integer> accountColor$;
    protected LiveData<Integer> boardColor$;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext());

        @ColorInt final int colorAccent = ContextCompat.getColor(requireContext(), R.color.accent);
        accountColor$ = map(new SharedPreferenceIntLiveData(sharedPreferences, getString(R.string.shared_preference_color_account), colorAccent), (color) -> color == null ? colorAccent : color);
        boardColor$ = map(new SharedPreferenceIntLiveData(sharedPreferences, getString(R.string.shared_preference_theme_main), colorAccent), (color) -> color == null ? colorAccent : color);
    }
}
