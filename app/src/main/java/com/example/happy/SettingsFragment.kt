package com.example.happy

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedIntanceState: Bundle?, rootKey: String?){
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}