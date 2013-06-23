package jp.nighthawk.widgetclock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.SharedPreferences;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

// Activity の代わりに、PreferenceActivity を継承
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
	private static final String PREFERENCE_HOLIDAY_PREFIX = "conf_holiday_";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 読み書きするプリファレンスのファイル名を指定
		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName(PREFERENCE_NAME);
		
		// 定義した設定項目XMLを読み込む
		setContentView(R.layout.config_linear);
		addPreferencesFromResource(R.xml.widget_clock_config);

		
		// Summaryの動的な部分を設定
		String msg = "設定値: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_SHOW_SEC_BATTERY, "0") + "％";
		findPreference(PREFERENCE_CONF_SHOW_SEC_BATTERY).setSummary(msg);
		msg = "設定値: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY, "0") + "日";
		findPreference(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY).setSummary(msg);
		
		// 祝日部分表示
		Calendar now = Calendar.getInstance();
		int this_year = now.get(now.YEAR);          //年を取得
		PreferenceCategory myCategory = (PreferenceCategory) findPreference("holiday_category");
		myCategory.setTitle("祝日設定 (" + this_year + "-" + (this_year + 1) + ")");
	    CheckBoxPreference cb1 = null;
	    
	    // ためし
	    PreferenceScreen ps1 = prefMgr.createPreferenceScreen(this);
		myCategory.addPreference(ps1);
		ps1.setTitle("yyyy/MM/dd");
		ps1.setSummary("説明説明説明");
	    
		//myCategory.removePreference(ps1);
		
		for(int y: new int[]{this_year, this_year+1}){
			Holiday holday = new Holiday(y);
			Date[] ary = holday.listHoliDays();
			for(int i=0;i < ary.length;i++){
			    cb1 = new CheckBoxPreference(this);
				cb1.setKey("conf_holiday_" + new SimpleDateFormat("yyyyMMdd").format(ary[i]));
				cb1.setTitle(new SimpleDateFormat("yyyy/MM/dd").format(ary[i]));
				cb1.setSummary(Holiday.queryHoliday(ary[i]));
				cb1.setDefaultValue(true);
				myCategory.addPreference(cb1);
			}
		}

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
	
	// ここで summary を動的に変更  
	private SharedPreferences.OnSharedPreferenceChangeListener listener =   
	    new SharedPreferences.OnSharedPreferenceChangeListener() {  
	       
	    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {  
			Log.i("onSharedPreferenceChanged", key);
			String msg;
			if(key.equals(PREFERENCE_CONF_SHOW_SEC_BATTERY) || key.equals(PREFERENCE_STAT_BATTERY_LEVEL)){
				msg = "設定値: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_SHOW_SEC_BATTERY, "0") + "％";
				findPreference(PREFERENCE_CONF_SHOW_SEC_BATTERY).setSummary(msg);
			}else if(key.equals(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY)){
				msg = "設定値: " + getPreferenceScreen().getSharedPreferences().getString(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY, "1") + "日";
				findPreference(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY).setSummary(msg);
			}else if(key.startsWith(PREFERENCE_HOLIDAY_PREFIX)){
				
			}
	    }  
	};  

}