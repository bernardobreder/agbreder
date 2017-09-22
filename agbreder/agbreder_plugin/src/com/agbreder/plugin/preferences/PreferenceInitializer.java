package com.agbreder.plugin.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.agbreder.plugin.BActivator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = BActivator.getDefault().getPreferenceStore();
		{
			// String BREDER_HOME = System.getenv("BREDER_HOME");
			// store.setDefault(PreferenceConstants.BREDER_HOME, BREDER_HOME);
		}
	}

}
