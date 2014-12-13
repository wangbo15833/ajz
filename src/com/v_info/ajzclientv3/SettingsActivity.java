package com.v_info.ajzclientv3;

import java.util.List;

import com.v_info.ajzclientv3.service.ScreenManager;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * 该Activity用于修改软件的一些基本设置，例如是否记住密码、服务器ip地址、消息提示音、是否震动等等
 * @author Administrator
 *
 */
public class SettingsActivity extends PreferenceActivity {
	private static final String TAG= "SettingsActivity ";
	private static final boolean ALWAYS_SIMPLE_PREFS = false;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		new ScreenManager().pushActivity(this);
		setupSimplePreferencesScreen();
	}

	@SuppressWarnings("deprecation")
	private void setupSimplePreferencesScreen() {
		addPreferencesFromResource(R.xml.pref_general);

		bindPreferenceSummaryToValue(findPreference("serveraddr"));
		bindPreferenceSummaryToValue(findPreference("vibrate"));
		bindPreferenceSummaryToValue(findPreference("rempsw"));
		bindPreferenceSummaryToValue(findPreference("ring"));
	}

	/** {@inheritDoc} */
	@Override
	public boolean onIsMultiPane() {
		return isXLargeTablet(this) && !isSimplePreferences(this);
	}

	private static boolean isXLargeTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}

	private static boolean isSimplePreferences(Context context) {
		return ALWAYS_SIMPLE_PREFS
				|| Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
				|| !isXLargeTablet(context);
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void onBuildHeaders(List<Header> target) {
		if (!isSimplePreferences(this)) {
		}
	}

	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				preference
						.setSummary(index >= 0 ? listPreference.getEntries()[index]
								: null);
			}else {
				preference.setSummary(stringValue);
				
			}
			preference.getEditor().commit();
			return true;
		}
	};

	private static void bindPreferenceSummaryToValue(Preference preference) {
		
		preference
				.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		if(preference instanceof CheckBoxPreference){
			sBindPreferenceSummaryToValueListener.onPreferenceChange(
					preference,
					PreferenceManager.getDefaultSharedPreferences(
							preference.getContext()).getBoolean(preference.getKey(),
							true));
		}else{
		sBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getString(preference.getKey(),
						""));
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);

			bindPreferenceSummaryToValue(findPreference("serveraddr"));
			bindPreferenceSummaryToValue(findPreference("ring"));
		}
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class NotificationPreferenceFragment extends
			PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class DataSyncPreferenceFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
	}
}
