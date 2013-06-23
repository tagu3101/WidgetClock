package jp.nighthawk.widgetclock;

//import java.util.TimeZone;
import java.util.*;

import jp.nighthawk.widgetclock.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.app.AlarmManager;
import android.app.PendingIntent;
//import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
//import android.os.BatteryManager;
//import android.os.IBinder;
//import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
//import android.app.Activity;
//import android.content.res.Resources;
//import android.app.Application;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.graphics.Typeface;
import jp.nighthawk.widgetclock.*;

public class WidgetClockProvider extends AppWidgetProvider {
	private static final String ACTION_WIDGETCLOCK_MINUTE_INTERVAL = "jp.nighthawk.widgetclock.MINUTE_INTERVAL";
	private static final String ACTION_WIDGETCLOCK_SECOND_INTERVAL = "jp.nighthawk.widgetclock.SECOND_INTERVAL";
	private static final long INTERVAL_MINUTE = 60 * 1000;
	private static final long INTERVAL_SECOND = 1000;
//	private static final long CALENDAR_CHANGE_DAY = 4;

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

	
	//----------------------------------------------------------
	private PendingIntent getAlermPendingIntent(Context context, String intentAction){
		Intent alarmIntent = new Intent(context, WidgetClockProvider.class);

		alarmIntent.setAction(intentAction);
		
		return PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
	}

	//----------------------------------------------------------
	private void setInterval(Context context, long interval, String intentAction) {
		PendingIntent operation = getAlermPendingIntent(context, intentAction);

		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		long now          = System.currentTimeMillis();
		long oneIntervalAfter = ((long)(now / interval)) * interval + interval;

		am.set(AlarmManager.RTC, oneIntervalAfter, operation);

	}

	private String[] HolidayList = {
			"20130320",
			"20130429",
			"20130430",
			"20130501",
			"20130502",
			"20130503",
			"20130506",
			"20130715",
			"20130716"
	};
	private boolean isHoliday(Calendar cal){
		Date date = cal.getTime();
		if(jp.nighthawk.widgetclock.Holiday.queryHoliday(date) == null){
			return false;
		}else{
			return true;
		}
//		String cal_str = String.format("%04d%02d%02d", cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH)+1), cal.get(Calendar.DATE));
//		for(String h: HolidayList ){
//			if(cal_str.equals(h)){
//				return true;
//			}
//		}
//		return false;
	}
	
	private void updateSecond(Context context){
		ComponentName	cn = new ComponentName(context, WidgetClockProvider.class);
		RemoteViews		rv = new RemoteViews(context.getPackageName(), R.layout.main);

		// ScreenOffまたは設定で秒表示OFFの時は後続処理をしない。
		// Intentも再設定されないので、1秒毎の継続処理が止まる。
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

		// 秒非表示設定の場合スキップ（以降のIntent発行も止める）
		boolean show_second = pref.getBoolean(PREFERENCE_CONF_SHOW_SEC, true);
		int show_second_threshold = Integer.parseInt(pref.getString(PREFERENCE_CONF_SHOW_SEC_BATTERY, "-1"));
		int battery_level = pref.getInt(PREFERENCE_STAT_BATTERY_LEVEL, -1);
		if((!show_second) || (show_second && (show_second_threshold > battery_level))){
			rv.setTextViewText(R.id.SecondText, "");
			AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
			Log.i("updateSecond", "NO Show_second Skip.");
			return;
		}
		// screen OFFで飛んできた場合に、時分と秒をクリアして、次回再開時に前のが見えないようにする
		boolean screenOn = pref.getBoolean(PREFERENCE_STAT_SCREEN_ON, true);
		boolean show_screen_off = pref.getBoolean(PREFERENCE_CONF_SHOW_SCREENOFF, false);
		if(!screenOn){
			rv.setTextViewText(R.id.SecondText, "");
			// 画面OFFのとき表示再更新をしない設定の場合
			if( !show_screen_off ){
				rv.setTextViewText(R.id.TimeText, "");
			}
			AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
			Log.i("updateSecond", "Screen OFF Skip.");
			return;
		}

		//*****************************************
		//
		// ローカル時刻(秒)の表示
		//
		//*****************************************
		Calendar cal = Calendar.getInstance();

		String timeText = String.format("%02d", cal.get(Calendar.SECOND));
		rv.setTextViewText(R.id.SecondText, timeText);
		
        // 時：分と秒の変更タイミングずれがないように、時：分も変更
		timeText = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		rv.setTextViewText(R.id.TimeText, timeText);
		
		// インターバルの設定
		setInterval(context, INTERVAL_SECOND, ACTION_WIDGETCLOCK_SECOND_INTERVAL);

		AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
		
		Log.i("updateSecond", "DONE.");

		return;
	}
	
	private void updateMinute(Context context){
		ComponentName	cn = new ComponentName(context, WidgetClockProvider.class);
		RemoteViews		rv = new RemoteViews(context.getPackageName(), R.layout.main);
		
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		// 設定でスクリーンOFF時に更新しない場合は後続処理をしない。
		// Intentも再設定されないので、1分毎の継続処理が止まる。
		boolean show_screen_off = pref.getBoolean(PREFERENCE_CONF_SHOW_SCREENOFF, true);
		boolean screenOn = pref.getBoolean(PREFERENCE_STAT_SCREEN_ON, true);
		// Screen OFF かつ OFFのとき表示更新しない 時分消す　イベント停止　WidgetUpdate要
		// Screen OFF かつ OFFのとき表示更新する　時分消さない　イベント継続 WidgetUpdate不要
		if( !screenOn ){
//			editor.putBoolean("screenOnContinue", false); //この設定で次回フル表示
//			editor.commit();

			// 再開時に古い秒が残って見えないように、更新停止処理に入る前に秒を消す
			rv.setTextViewText(R.id.SecondText, "");

			// 画面OFFのとき表示再更新をしない設定の場合、継続処理を止めるためreturn
			if( !show_screen_off ){
				rv.setTextViewText(R.id.TimeText, "");
				AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
				Log.i("updateMinute", "NO show_screen_off Skip.");
				return;
			}
		}
		
		//*****************************************
		//
		// ローカル日付の表示
		//
		//*****************************************
		Calendar cal = Calendar.getInstance();

		String timeText = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		rv.setTextViewText(R.id.TimeText, timeText);
		
		// バッテリー残量表示
//		String batteryLevel = Integer.toString(pref.getInt(PREFERENCE_STAT_BATTERY_LEVEL, -1)) + "%";
//		rv.setTextViewText(R.id.BatteryLevel, batteryLevel);

		//*****************************************
		// 日付・カレンダー更新処理
		// 高速化のため同日の場合は実行しない
		int lastDate = pref.getInt(PREFERENCE_STAT_LASTDATE, 0);
//		boolean screenOnContinue = pref.getBoolean("screenOnContinue", false);
//		if(lastDate == cal.get(Calendar.DATE) && (screenOnContinue)){
		if(lastDate == cal.get(Calendar.DATE)){
			Log.i("updateMinute", "Same LastDate Skip.");
		}else{
			Log.i("updateMinute", "NOT Same LastDate CONTINUE.");

			// 日付更新処理開始
			String dateText = String.format("%02d.%02d", (cal.get(Calendar.MONTH)+1), cal.get(Calendar.DATE) );

			String dateWeekText = "(";
			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:    dateWeekText+="日"; break;
			case Calendar.MONDAY:    dateWeekText+="月"; break;
			case Calendar.TUESDAY:   dateWeekText+="火"; break;
			case Calendar.WEDNESDAY: dateWeekText+="水"; break;
			case Calendar.THURSDAY:  dateWeekText+="木"; break;
			case Calendar.FRIDAY:    dateWeekText+="金"; break;
			case Calendar.SATURDAY:  dateWeekText+="土"; break;
			}
			dateWeekText+=")";

			rv.setTextViewText(R.id.DateText,     dateText);
			rv.setTextViewText(R.id.DateWeekText, dateWeekText);

			String yearText = cal.get(Calendar.YEAR) + ".";
			rv.setTextViewText(R.id.DateYearText, yearText);

			String yearWarekiText = "H." + (cal.get(Calendar.YEAR)-1988);
			rv.setTextViewText(R.id.DateWarekiText, yearWarekiText);

			// カレンダーの表示
			Calendar cal_w = Calendar.getInstance();
			cal_w.setTimeInMillis(cal.getTimeInMillis());
			// CALENDAR_CHANGE_DAYより前の日だったら前月から表示開始
			int calendar_change_day = Integer.parseInt(pref.getString(PREFERENCE_CONF_CALENDAR_TURNOVER_DAY, "1"));
			if(cal.get(Calendar.DATE) < calendar_change_day){
				cal_w.add(Calendar.MONTH, -1);
			}

			// L1~R38まで描画処理
			int id,id_bg;
			for(String prefix : new String[]{"L","R"}){
				id = context.getResources().getIdentifier(prefix + "name", "id", context.getPackageName());
				rv.setTextViewText(id, (cal_w.get(Calendar.MONTH)+1)+"月");

				cal_w.set(Calendar.DATE, 1);
				int CurrentMonth = cal_w.get(Calendar.MONTH); //月の変化判定用
				for(int i = 1; i<=38; i++){
					// 1日の曜日まで""を入れながらスキップ
					if(cal_w.get(Calendar.DAY_OF_WEEK) > i){ //SUNDAY=1,MONDAY=2...
						id = context.getResources().getIdentifier(prefix + i, "id", context.getPackageName());
						rv.setTextViewText(id, "");
						continue;
					}
					// 月末以降を""入れながら埋める
					if(cal_w.get(Calendar.MONTH) != CurrentMonth){
						id = context.getResources().getIdentifier(prefix + i, "id", context.getPackageName());
						rv.setTextViewText(id, "");
						continue;
					}

					//
					//表示
					//
					id = context.getResources().getIdentifier(prefix + i, "id", context.getPackageName());
					id_bg = context.getResources().getIdentifier(prefix + i + "bg", "id", context.getPackageName());

					rv.setTextViewText(id, Integer.toString(cal_w.get(Calendar.DATE)));

					// 祝日・今日の場合の着色処理
					if(isHoliday(cal_w) || cal_w.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
						rv.setTextColor(id, 0xffff0033); //RED
					}else if(cal_w.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
						rv.setTextColor(id, 0xff3366ff); //BLUE
					}else{
						rv.setTextColor(id, 0xffffffff); //NORMAL
						if(cal.get(Calendar.DATE)  == cal_w.get(Calendar.DATE) &&
								cal.get(Calendar.MONTH) == cal_w.get(Calendar.MONTH) &&
								cal.get(Calendar.YEAR)  == cal_w.get(Calendar.YEAR)) {
							rv.setTextColor(id, 0xff000000); //NORMAL            			
						}
					}
					if(cal.get(Calendar.DATE)  == cal_w.get(Calendar.DATE) &&
							cal.get(Calendar.MONTH) == cal_w.get(Calendar.MONTH) &&
							cal.get(Calendar.YEAR)  == cal_w.get(Calendar.YEAR)) {
						rv.setInt(id_bg, "setBackgroundColor", 0xffaaaaaa);
					}else{
						rv.setInt(id_bg, "setBackgroundColor", 0x00000000);
					}

					cal_w.add(Calendar.DATE, 1);
				}
			}

//			editor.putBoolean("screenOnContinue", true);
	        editor.putInt(PREFERENCE_STAT_LASTDATE, cal.get(Calendar.DATE));
		    editor.commit();

		}// End of if(lastDate == cal.get(Calendar.DATE)){
		
		// インターバルの設定
		setInterval(context, INTERVAL_MINUTE, ACTION_WIDGETCLOCK_MINUTE_INTERVAL);
		AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
		
		Log.i("updateMinute", "DONE.");

		return;
	}

	@Override
	public void onEnabled(Context context){
		super.onEnabled(context);

		Log.i("onEnable", "DONE.");
		return;
	}

	
	@Override
	public void onUpdate(Context context, AppWidgetManager manager, int ids []){
		super.onUpdate(context, manager, ids);

		// SharedPreferences の初期設定
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREFERENCE_STAT_LASTDATE, 0);
	    editor.commit();
		// 初回のScreen ON/OFF を記録
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE); 
		if(pm.isScreenOn()){
		    editor.putBoolean(PREFERENCE_STAT_SCREEN_ON, true);
		    editor.commit();			
			Log.i("onUpdate", "Screen ON.");
		}else{
		    editor.putBoolean(PREFERENCE_STAT_SCREEN_ON, false);
		    editor.commit();
			Log.i("onUpdate", "Screen OFF.");
		}

	    IntentFilter filter_on = new IntentFilter(Intent.ACTION_SCREEN_ON);
		context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	WidgetClockProvider.this.onReceive(context, intent);
		    }
        }, filter_on);

		IntentFilter filter_off = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	WidgetClockProvider.this.onReceive(context, intent);
		    }
		}, filter_off);

		IntentFilter battery_status = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		context.getApplicationContext().registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
		    	WidgetClockProvider.this.onReceive(context, intent);
			}
		}, battery_status);

		//時計の更新処理
		//updateClock(context);
		setInterval(context, 10, ACTION_WIDGETCLOCK_SECOND_INTERVAL);
		setInterval(context, 100, ACTION_WIDGETCLOCK_MINUTE_INTERVAL);
		Log.i("onUpdate", "DONE.");
				
		return;
	}

	@Override
	public void onReceive(Context context, Intent intent){
		SharedPreferences pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();

		if(ACTION_WIDGETCLOCK_SECOND_INTERVAL.equals(intent.getAction())){
			updateSecond(context);
			Log.i("onReceive", "ACTION_WIDGETCLOCK_SECOND_INTERVAL.");
		}else if(ACTION_WIDGETCLOCK_MINUTE_INTERVAL.equals(intent.getAction())){
			updateMinute(context);
			Log.i("onReceive", "ACTION_WIDGETCLOCK_MINUTE_INTERVAL.");
		}else if(Intent.ACTION_SCREEN_OFF.equals(intent.getAction())){
		    editor.putBoolean(PREFERENCE_STAT_SCREEN_ON, false);
	        editor.putInt(PREFERENCE_STAT_LASTDATE, 0);
		    editor.commit();
//			updateMinute(context);
//			updateSecond(context);
			setInterval(context, 10, ACTION_WIDGETCLOCK_SECOND_INTERVAL);
			setInterval(context, 100, ACTION_WIDGETCLOCK_MINUTE_INTERVAL);
			Log.i("onReceive", "ACTION_SCREEN_OFF.");
		}else if(Intent.ACTION_SCREEN_ON.equals(intent.getAction())){
		    editor.putBoolean(PREFERENCE_STAT_SCREEN_ON, true);
	        editor.putInt(PREFERENCE_STAT_LASTDATE, 0);
		    editor.commit();
//			updateMinute(context);
//			updateSecond(context);
			setInterval(context, 10, ACTION_WIDGETCLOCK_SECOND_INTERVAL);
			setInterval(context, 100, ACTION_WIDGETCLOCK_MINUTE_INTERVAL);
			Log.i("onReceive", "ACTION_SCREEN_ON.");
		}else if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
			// 電池残量
			int level = intent.getIntExtra("level", 0);
		    editor.putInt(PREFERENCE_STAT_BATTERY_LEVEL, level);
		    editor.commit();
			Log.i("onReceive", "ACTION_BATTERY_CHANGED. Level:" + Integer.toString(pref.getInt(PREFERENCE_STAT_BATTERY_LEVEL, -1)));			
		}
        
		super.onReceive(context, intent);
		return;
	}
	
	@Override
	public void onDeleted(Context context, int[] ids){

		super.onDeleted(context, ids);
		
		Log.i("onDeleted", "DONE.");
		
		return;
	}
	
	@Override
	public void onDisabled(Context context){

		// 時計更新のタイマーを停止する
		for(String act:
			new String[]{ACTION_WIDGETCLOCK_MINUTE_INTERVAL,
				ACTION_WIDGETCLOCK_MINUTE_INTERVAL}){
    		PendingIntent operation = getAlermPendingIntent(context, act);
    		AlarmManager	am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    		am.cancel(operation);
		}

		SharedPreferences prefScreen = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefScreen.edit();
		editor.clear();
		
		Log.i("onDisabled", "DONE.");

		super.onDisabled(context);
		return;
	}
}
