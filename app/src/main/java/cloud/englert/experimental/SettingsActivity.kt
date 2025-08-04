package cloud.englert.experimental

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import cloud.englert.experimental.custom.ThemeSetup.applyTheme

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val pref =
                preferenceManager.findPreference<Preference?>(getString(R.string.theme_key))
            pref?.onPreferenceChangeListener = (Preference.OnPreferenceChangeListener {
                    _: Preference?, newValue: Any? ->
                applyTheme(newValue as String?, requireContext())
                true
            })
        }
    }
}