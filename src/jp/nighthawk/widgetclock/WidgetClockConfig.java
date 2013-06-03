package jp.nighthawk.widgetclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.SharedPreferences;

// Activity �̑���ɁAPreferenceActivity ���p��
public class WidgetClockConfig extends PreferenceActivity {
	private static final String ACTION_WIDGETCLOCK_MINUTE_INTERVAL = "jp.nighthawk.widgetclock.MINUTE_INTERVAL";
	private static final String ACTION_WIDGETCLOCK_SECOND_INTERVAL = "jp.nighthawk.widgetclock.SECOND_INTERVAL";

	//Preference Key
	private static final String PREFERENCE_NAME = "widgetclock_preference";
	private static final String PREFERENCE_CONF_SHOW_SEC = "conf_show_second";
	private static final String PREFERENCE_CONF_SHOW_SCREENOFF = "conf_show_screenoff";
	private static final String PREFERENCE_CONF_SHOW_SEC_BATTERY = "conf_show_sec_battery";
	private static final String PREFERENCE_CONF_CALENDAR_TURNOVER_DAY = "conf_calendar_turnover_day";
	private static final String PREFERENCE_STAT_BATTERY_LEVEL = "stat_battery_level";
	private static final String PREFERENCE_STAT_SCREEN_ON = "stat_screen_on";
	private static final String PREFERENCE_STAT_LASTDATE = "stat_last_date";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// �ǂݏ�������v���t�@�����X�̃t�@�C�������w��
		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(PREFERENCE_NAME);
		
		// ��`�����ݒ荀��XML��ǂݍ���
		addPreferencesFromResource(R.xml.widget_clock_config);

		// Summary�̓��I�ȕ�����ݒ�
		String msg = "�ݒ�l: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_SHOW_SEC_BATTERY, "0") + "��";
		findPreference(PREFERENCE_CONF_SHOW_SEC_BATTERY).setSummary(msg);
		msg = "�ݒ�l: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY, "0") + "��";
		findPreference(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY).setSummary(msg);
	}
	
	@Override  
	protected void onResume() {  
	    super.onResume();  
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);  
	}  
	   
	@Override  
	protected void onPause() {  
	    super.onPause();  
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);  
	}
	
	// ������ summary �𓮓I�ɕύX  
	private SharedPreferences.OnSharedPreferenceChangeListener listener =   
	    new SharedPreferences.OnSharedPreferenceChangeListener() {  
	       
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {  
			Log.i("onSharedPreferenceChanged", key);
			String msg;
			if(key.equals(PREFERENCE_CONF_SHOW_SEC_BATTERY) || key.equals(PREFERENCE_STAT_BATTERY_LEVEL)){
				msg = "�ݒ�l: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_SHOW_SEC_BATTERY, "0") + "��";
				findPreference(PREFERENCE_CONF_SHOW_SEC_BATTERY).setSummary(msg);
			}else if(key.equals(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY)){
				msg = "�ݒ�l: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY, "1") + "��";
				findPreference(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY).setSummary(msg);
			}

	    }  
	};  

}