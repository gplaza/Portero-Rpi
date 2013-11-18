package cl.gplaza.portero_rpi;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ConnectSettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
}
