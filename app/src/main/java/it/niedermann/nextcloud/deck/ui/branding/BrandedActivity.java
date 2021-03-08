package it.niedermann.nextcloud.deck.ui.branding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import it.niedermann.android.sharedpreferences.SharedPreferenceIntLiveData;
import it.niedermann.nextcloud.deck.R;

import static androidx.lifecycle.Transformations.map;
import static it.niedermann.nextcloud.deck.ui.branding.BrandingUtil.tintMenuIcon;

public abstract class BrandedActivity extends AppCompatActivity {

    @ColorInt
    protected int colorAccent;

    protected LiveData<Integer> accountColor$;
    protected LiveData<Integer> boardColor$;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
        colorAccent = typedValue.data;

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        accountColor$ = map(new SharedPreferenceIntLiveData(sharedPreferences, getString(R.string.shared_preference_color_account), colorAccent), (color) -> color == null ? colorAccent : color);
        boardColor$ = map(new SharedPreferenceIntLiveData(sharedPreferences, getString(R.string.shared_preference_theme_main), colorAccent), (color) -> color == null ? colorAccent : color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            tintMenuIcon(menu.getItem(i), colorAccent);
        }
        return super.onCreateOptionsMenu(menu);
    }
}
