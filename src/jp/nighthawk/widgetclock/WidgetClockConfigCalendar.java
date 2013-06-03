package jp.nighthawk.widgetclock;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
//import android.widget.Toast;

public class WidgetClockConfigCalendar extends Activity {
	
	CalendarView cal;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        
        cal = (CalendarView) findViewById(R.id.calendarView1);
        
        cal.setOnDateChangeListener(new OnDateChangeListener() {
			
		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			
//			Toast.makeText(getBaseContext(),"Selected Date is\n\n"
//				+dayOfMonth+" : "+month+" : "+year , 
//				Toast.LENGTH_LONG).show();
		}
	});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
