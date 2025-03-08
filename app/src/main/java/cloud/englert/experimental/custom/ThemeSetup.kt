package cloud.englert.experimental.custom

import android.content.Context

import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

import cloud.englert.experimental.R

object ThemeSetup {
    fun applyTheme(mode: String?, context: Context) {
        when (mode) {
            context.getString(R.string.dark_theme_value) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            context.getString(R.string.light_theme_value) -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    fun applyTheme(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val theme: String = preferences.getString(
            context.getString(R.string.theme_key),
            context.getString(R.string.system_theme_value)
        )!!
        applyTheme(theme, context)
    }
}