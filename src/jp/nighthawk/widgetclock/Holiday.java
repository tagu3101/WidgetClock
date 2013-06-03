package jp.nighthawk.widgetclock;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
/**
 * j“úŒvZƒNƒ‰ƒX.
 * 2007”NˆÈ~‚Ì‚İ—LŒø
 * t•ªEH•ª‚Ì“ú‚ÍAwŠCã•ÛˆÀ’¡…˜H•” —ïŒvZŒ¤‹†‰ï•Ò V‚±‚æ‚İ•Ö—˜’ x‚É‚æ‚éŒvZ®‚É
 * ‚æ‚éŒ‹‰Ê‚ğ‹‚ß‚Ä‚é‚É‚·‚¬‚È‚¢B–ˆ”N‚ÌŠ¯•ñŒö¦‚ÌŒˆ’è‚ÆˆÙ‚È‚Á‚½‚çŠ¯•ñŒö¦‚É]‚¤‚±‚ÆB
 * ”NŠÔ‚Ìj“úƒŠƒXƒg(‘–¯‚Ì‹x“ú‚ğŠÜ‚Şji”z—ñj‚ÌZoA
 * ”N‚ÆŒ‚ğw’è‚µ‚Ä‘ÎÛŒ‚Ìj“ú(‘–¯‚Ì‹x“ú‚ğŠÜ‚Şj‚ÌZoA
 * w’è“ú•t‚Ìj“ú”»’èA
 * w’è‚·‚éj“ú‚Ì“ú•t‚Ìæ“¾‚ğ–Ú“I‚Æ‚·‚éB
 *
 * ”CˆÓ‚Ìj“ú‚ğw’è‚µ‚Äî•ñ‚ğæ“¾‚·‚é‚½‚ß‚ÉA
 *     public abstract class HolidayBundle ‚ğ’ñ‹Ÿ‚µ‚Ä‚¢‚éB
 * ‚±‚Ì’ŠÛƒNƒ‰ƒX‚Ì‹ïÛƒNƒ‰ƒX‚Æ‚µ‚Äj“ú‚²‚Æ‚ÌƒNƒ‰ƒX‚ª—pˆÓ‚³‚ê‚Ä‚¨‚èAj“ú–¼Aj“úŒvZ‚Í
 * ŒÂX‚Ìj“úHolidayBundle ‚ÅÀ‘•‚·‚éB
 * U‘Ö‹x“úŒvZ‚ÍAHolidayBundle ’ŠÛƒNƒ‰ƒX‚ÅÀ‘•‚·‚é‚ªA“Á’è‚Ìj“ú‚Í‹ïÛƒNƒ‰ƒX‚Å
 * ƒI[ƒo[ƒ‰ƒCƒh‚ÅÀ‘•‚·‚éB
 * yg—p—áz
 *     // 2009”N‚Ìj“ú”z—ñ
 *         Holiday holday = new Holiday(2009);  // ƒRƒ“ƒXƒgƒ‰ƒNƒ^‚ğg—p‚·‚é‚±‚Æ
 *         Date[] ary = holday.listHoliDays();
 *         for(int i=0;i < ary.length;i++){
 *            System.out.println(Holiday.YMD_FORMAT.format(ary[i])
 *                          +"\t"+Holiday.dateOfWeekJA(ary[i])
 *                          +"\t"+Holiday.queryHoliday(ary[i]));
 *         }
 *     // 2009”N‚Ì‚XŒ‚Ìj“úA
 *         int[] days = Holiday.listHoliDays(2009,Calendar.SEPTEMBER);
 *         Date[] dts = Holiday.listHoliDayDates(2009,Calendar.SEPTEMBER);
 *     // w’è“ú•t‚Ìj“ú”»’è
 *        String target = "2009/05/06";
 *        String res = Holiday.queryHoliday(Holiday.YMD_FORMAT.parse(target));
 *        System.out.println(res);
 *     // w’è‚·‚éj“ú‚Ì“ú•t‚Ìæ“¾
 *     //   Holiday.HolidayType enum ‚©‚çAgetBundleƒƒ\ƒbƒh‚ÅAHoliday.HolidayBundle
 *     //   ‚ğæ“¾‚µ‚ÄHoliday.HolidayBundle‚ª’ñ‹Ÿ‚·‚éƒƒ\ƒbƒh‚ğ—˜—p‚·‚é
 *          //    Holiday.HolidayBundle#getMonth()       ¨ Œ
 *          //    Holiday.HolidayBundle#getDay()         ¨ “ú
 *          //    Holiday.HolidayBundle#getDescription() ¨ j“ú–¼
 *          //    Holiday.HolidayBundle#getDate()        ¨ j“ú‚ÌDate
 *          //    Holiday.HolidayBundle#getChangeDay()   ¨ U‘Ö‹x“ú‚ ‚éê‡‚ÌDay
 *          //    Holiday.HolidayBundle#getChangeDate()  ¨ U‘Ö‹x“ú‚ ‚éê‡‚ÌDate
 *       // 2009”N‚Ìt•ª‚Ì“ú
 *          Holiday.HolidayBundle h = Holiday.HolidayType.SPRING_EQUINOX_DAY.getBundle(2009);
 *          System.out.println(h.getMonth()+"Œ "+h.getDay()+"“ú"
 *                              +"i"+Holiday.WEEKDAYS_JA[h.getWeekDay()-1]+"j"
 *                              +" "+h.getDescription());
 *     // w’è”N¨‘–¯‚Ì‹x“ú‚Ì‚İ‚ÌDate[]‚Ìæ“¾
 *          // 2009”N‚Ì‘–¯‚Ì‹x“ú”z—ñ
 *          Date[] ds = Holiday.getNatinalHoliday(2009);
 *          for(int i=0;i < ds.length;i++){
 *             System.out.println(Holiday.YMD_FORMAT.format(ds[i])+"-->"+Holiday.queryHoliday(ds[i]));
 *          }
 */
public class Holiday{
   private Date[] holidayDates;
   /**
    * ƒfƒtƒHƒ‹ƒgƒRƒ“ƒXƒgƒ‰ƒNƒ^.
    * Œ»İ“ú‚Ì”N‚ÅAHoliday(int year) ƒRƒ“ƒXƒgƒ‰ƒNƒ^‚ğŒÄ‚Ño‚·‚Ì‚Æ“¯‚¶Œø‰Ê‚Å‚·B
    */
   public Holiday(){
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      this.init(cal.get(Calendar.YEAR));
   }
   /**
    * ‘ÎÛ”N w’èƒRƒ“ƒXƒgƒ‰ƒNƒ^.
    * @param year ¼—ï‚SŒ…
    */
   public Holiday(int year){
      this.init(year);
   }
   private void init(int year){
      TreeSet<Date> set = new TreeSet<Date>();
      HolidayType[] holidayTypes = HolidayType.values();
      for(int i=0;i < holidayTypes.length;i++){
         HolidayBundle hb = holidayTypes[i].getBundle(year);
         if (hb != null){
            set.add(hb.getDate());
            Date chgdt = hb.getChangeDate();
            if (chgdt != null){
               set.add(chgdt);
            }
         }
      }
      Date[] ds = getNatinalHoliday(year);
      if (ds != null){
         for(int i=0;i < ds.length;i++){
            set.add(ds[i]);
         }
      }
      this.holidayDates = new Date[set.size()];
      int n=0;
      for(Iterator<Date> it=set.iterator();it.hasNext();n++){
         this.holidayDates[n] = it.next();
      }
   }
   /** HolidayType ‚ÍAj“úƒ^ƒCƒv¨HolidayBundle class ‚ğ•R•t‚¯‚é enum */
   public enum HolidayType{
      /** Œ³’U        F‚PŒ‚P“ú            */  NEWYEAR_DAY             (NewYearDayBundle.class)
      /** ¬l‚Ì“ú    F‚PŒ‚Ì‘æ‚QŒ—j“ú    */ ,COMING_OF_AGE_DAY       (ComingOfAgeDayBundle.class)
      /** Œš‘‹L”O“ú  F‚QŒ‚P‚P“ú          */ ,NATIONAL_FOUNDATION_DAY (NatinalFoundationBundle.class)
      /** t•ª‚Ì“ú    F‚RŒ Š¯•ñŒö¦‚ÅŒˆ’è */ ,SPRING_EQUINOX_DAY      (SpringEquinoxBundle.class)
      /** º˜a‚Ì“ú    F‚SŒ‚Q‚X“ú          */ ,SHOUWA_DAY              (ShowaDayBundle.class)
      /** Œ›–@‹L”O“ú  F‚TŒ‚R“ú            */ ,KENPOUKINEN_DAY         (KenpoukikenDayBundle.class)
      /** ‚İ‚Ç‚è‚Ì“ú  F‚TŒ‚S“ú            */ ,MIDORI_DAY              (MidoriDayBundle.class)
      /** ‚±‚Ç‚à‚Ì“ú  F‚TŒ‚T“ú            */ ,KODOMO_DAY              (KodomoDayBundle.class)
      /** ŠC‚Ì“ú      F‚VŒ‚Ì‘æ‚RŒ—j“ú    */ ,SEA_DAY                 (SeaDayBundle.class)
      /** Œh˜V‚Ì“ú    F‚XŒ‚Ì‘æ‚RŒ—j      */ ,RESPECT_FOR_AGE_DAY     (RespectForAgeDayBundle.class)
      /** H•ª‚Ì“ú    F‚XŒ Š¯•ñŒö¦‚ÅŒˆ’è */ ,AUTUMN_EQUINOX_DAY      (AutumnEquinoxBundle.class)
      /** ‘Ìˆç‚Ì“ú    F‚P‚OŒ‚Ì‘æ‚QŒ—j“ú  */ ,HEALTH_SPORTS_DAY       (HealthSportsDayBundle.class)
      /** •¶‰»‚Ì“ú    F‚P‚PŒ‚R“ú          */ ,CULTURE_DAY             (CultureDayBundle.class)
      /** ‹Î˜JŠ´Ó‚Ì“úF‚P‚PŒ‚Q‚R“ú        */ ,LABOR_THANKS_DAY        (LaborThanksDayBundle.class)
      /** “Vc’a¶“ú  F‚P‚QŒ‚Q‚R“ú        */ ,TENNO_BIRTHDAY          (TennoBirthDayBundle.class)
      ;
      private Class<? extends HolidayBundle> cls;
      private HolidayType(Class<? extends HolidayBundle> cls){
         this.cls = cls;
      }
      public HolidayBundle getBundle(int year){
         try{
         Constructor<?> ct = this.cls.getDeclaredConstructor(Holiday.class,int.class);
         return (HolidayBundle)ct.newInstance(null,year);
         }catch(Exception e){
            return null;
         }
      }
   }
   // Œ¨HolidayBundle class QÆ enum
   enum MonthBundle{
      JANUARY       (NewYearDayBundle.class,ComingOfAgeDayBundle.class)
      ,FEBRUARY     (NatinalFoundationBundle.class)
      ,MARCH        (SpringEquinoxBundle.class)
      ,APRIL        (ShowaDayBundle.class)
      ,MAY          (KenpoukikenDayBundle.class,MidoriDayBundle.class,KodomoDayBundle.class)
      ,JUNE         ()
      ,JULY         (SeaDayBundle.class)
      ,AUGUST       ()
      ,SEPTEMBER    (RespectForAgeDayBundle.class,AutumnEquinoxBundle.class)
      ,OCTOBER      (HealthSportsDayBundle.class)
      ,NOVEMBER     (CultureDayBundle.class,LaborThanksDayBundle.class)
      ,DECEMBER     (TennoBirthDayBundle.class)
      ;
      //
      private Constructor<?>[] constructors;
      MonthBundle(Class<?>...clss){
         if (clss.length > 0){
            this.constructors = new Constructor<?>[clss.length];
            for(int i=0;i < clss.length;i++){
               try{
               this.constructors[i] = clss[i].getDeclaredConstructor(Holiday.class,int.class);
               }catch(Exception e){}
            }
         }
      }
      Constructor<?>[] getConstructors(){
         return this.constructors;
      }
   }
   //========================================================================
   /** j“úBundle’ŠÛƒNƒ‰ƒX */
   public abstract class HolidayBundle{
      int year;
      private Calendar mycal;
      public abstract int getDay();
      public abstract int getMonth();
      public abstract String getDescription();
      /** ‘ÎÛ”N‚ğw’è‚·‚éƒRƒ“ƒXƒgƒ‰ƒNƒ^
       * @param year ¼—ï‚SŒ…
       */
      public HolidayBundle(int year){
         this.year = year;
         this.mycal = Calendar.getInstance();
         this.mycal.set(this.year,this.getMonth()-1,this.getDay());
      }
      /** U‘Ö‹x“ú‚Ì‘¶İ‚·‚éê‡AU‘Ö‹x“ú‚Ì“ú‚ğ•Ô‚·B‘¶İ‚µ‚È‚¢ê‡¨ -1 ‚ğ•Ô‚·B*/
      public int getChangeDay(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,this.getDay());
            cal.add(Calendar.DAY_OF_MONTH,1);
            return cal.get(Calendar.DAY_OF_MONTH);
         }
         return -1;
      }
      /** U‘Ö‹x“ú‚Ì‘¶İ‚·‚éê‡AU‘Ö‹x“ú‚ÌDate‚ğ•Ô‚·B‘¶İ‚µ‚È‚¢ê‡¨ null ‚ğ•Ô‚·B*/
      public Date getChangeDate(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,this.getDay());
            cal.add(Calendar.DAY_OF_MONTH,1);
            return cal.getTime();
         }
         return null;
      }
      /** j“ú‚Ì—j“ú‚ğ Calendar.DAY_OF_WEEK ‚É]‚Á‚Ä‹‚ß‚é */
      public int getWeekDay(){
         return this.mycal.get(Calendar.DAY_OF_WEEK);
      }
      /** j“ú‚Ì Date ‚ğæ“¾ */
      public Date getDate(){
         return this.mycal.getTime();
      }
   }

   /** j“úAU‘Ö‹x“ú‚ğŠÜ‚ñ‚ÅADate”z—ñ‚Å•Ô‚·B*/
   public Date[] listHoliDays(){
      return this.holidayDates;
   }
   /**
    * w’è”NAŒ‚Ìj“úAU‘Ö‹x“úA‘–¯‚Ì‹x“úA“ú•t(int)”z—ñ‚Å•Ô‚·
    * @param year ¼—ï‚SŒ…
    * @param calender_MONTH java.util.Calendar.MONTH‚É‚æ‚éƒtƒB[ƒ‹ƒh’l 0=‚PŒA11=‚P‚QŒ
    * @return java.util.Calendar.DAY_OF_MONTH ‚Å‚ ‚é”z—ñ
    */
   public static int[] listHoliDays(int year,int calender_MONTH){
      if (calender_MONTH < 0 || 11 < calender_MONTH){
         throw new IllegalArgumentException("calender_MONTH parameter Error");
      }
      MonthBundle mb = MonthBundle.valueOf(MONTH_NAMES[calender_MONTH]);
      Constructor<?>[] constructors = mb.getConstructors();
      if (constructors==null) return null;
      Set<Integer> set = new TreeSet<Integer>();
      for(int i=0;i < constructors.length;i++){
         try{
         HolidayBundle b = (HolidayBundle)constructors[i].newInstance(null,year);
         set.add(b.getDay());
         int chgday = b.getChangeDay();
         if (chgday > 0) set.add(chgday);
         }catch(Exception e){
         }
      }
      // Œ»İA‘–¯‚Ì‹x“ú‚Ì”­¶‚Í‚XŒ‚µ‚©‚È‚¢
      if (calender_MONTH==Calendar.SEPTEMBER){
         Date[] ds = getNatinalHoliday(year);
         if (ds != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(ds[0]);
            set.add(cal.get(Calendar.DAY_OF_MONTH));
         }
      }
      int[] rtns = new int[set.size()];
      int i=0;
      for(Iterator<Integer> it=set.iterator();it.hasNext();i++){
         rtns[i] = it.next();
      }
      return rtns;
   }
   /**
    * w’è”NAŒ‚Ìj“úAU‘Ö‹x“úA‘–¯‚Ì‹x“úA“ú•t(Date)”z—ñ‚Å•Ô‚·
    * @param year ¼—ï‚SŒ…
    * @param calender_MONTH java.util.Calendar.MONTH‚É‚æ‚éƒtƒB[ƒ‹ƒh’l 0=‚PŒA11=‚P‚QŒ
    * @return Date”z—ñ
    */
   public static Date[] listHoliDayDates(int year,int calender_MONTH){
      if (calender_MONTH < 0 || 11 < calender_MONTH){
         throw new IllegalArgumentException("calender_MONTH parameter Error");
      }
      MonthBundle mb = MonthBundle.valueOf(MONTH_NAMES[calender_MONTH]);
      Constructor<?>[] constructors = mb.getConstructors();
      if (constructors==null) return null;
      Set<Date> set = new TreeSet<Date>();
      for(int i=0;i < constructors.length;i++){
         try{
         HolidayBundle b = (HolidayBundle)constructors[i].newInstance(null,year);
         set.add(b.getDate());
         Date chgdt = b.getChangeDate();
         if (chgdt != null) set.add(chgdt);
         }catch(Exception e){
         }
      }
      // Œ»İA‘–¯‚Ì‹x“ú‚Ì”­¶‚Í‚XŒ‚µ‚©‚È‚¢
      if (calender_MONTH==Calendar.SEPTEMBER){
         Date[] ds = getNatinalHoliday(year);
         if (ds != null){
            set.add(ds[0]);
         }
      }
      Date[] rtns = new Date[set.size()];
      int i=0;
      for(Iterator<Date> it=set.iterator();it.hasNext();i++){
         rtns[i] = it.next();
      }
      return rtns;
   }

   /** “ú•tƒtƒH[ƒ}ƒbƒg yyyy/MM/dd */
   public static SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
   /** Calendar.MONTH ‚É‰ˆ‚Á‚½Œ–¼‚Ì”z—ñ */
   public static String[] MONTH_NAMES = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};

   /** w’è“ú‚ªj“ú‚È‚çAj“ú–¼‚ğ•Ô‚·Biw’è“ú‚É‚æ‚éj“úAU‘Ö‹x“úƒ`ƒFƒbƒN‚Ìˆ×j
    * @parama w’è“ú
    * @return j“ú–¼‚ğ•Ô‚·Bj“úAU‘Ö‹x“ú‚ÉŠY“–‚µ‚È‚¯‚ê‚ÎAnull ‚ğ•Ô‚·B
    */
   public static String queryHoliday(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      MonthBundle mb = MonthBundle.valueOf(MONTH_NAMES[cal.get(Calendar.MONTH)]);
      Constructor<?>[] constructors = mb.getConstructors();
      if (constructors==null){
         return null; // j“ú‚Å‚È‚¢I
      }
      int targetDay = cal.get(Calendar.DAY_OF_MONTH);
      int targetYear = cal.get(Calendar.YEAR);
      for(int i=0;i < constructors.length;i++){
         try{
         HolidayBundle h = (HolidayBundle)constructors[i].newInstance(null,targetYear);
         if (targetDay==h.getDay()){ return h.getDescription(); }
         if (targetDay==h.getChangeDay()){ return "U‘Ö‹x“ú"+"i"+h.getDescription()+"j"; }
         }catch(Exception e){
         }
      }
      Date[] natinalHolidayDates = getNatinalHoliday(targetYear);
      if (natinalHolidayDates != null){
         String targetDateStr = YMD_FORMAT.format(dt);
         for(int i=0;i < natinalHolidayDates.length;i++){
            if (targetDateStr.equals(YMD_FORMAT.format(natinalHolidayDates[i]))){
               return "‘–¯‚Ì‹x“ú";
            }
         }
      }
      return null;
   }

   public static String[] WEEKDAYS_JA = {"“ú","Œ","‰Î","…","–Ø","‹à","“y" };
   /** —j“úStringZo Japanese */
   public static String dateOfWeekJA(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      return WEEKDAYS_JA[cal.get(Calendar.DAY_OF_WEEK)-1];
   }
   public static String[] WEEKDAYS_SIMPLE = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
   /** —j“úStringZo */
   public static String dateOfWeekSimple(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      return WEEKDAYS_SIMPLE[cal.get(Calendar.DAY_OF_WEEK)-1];
   }

   /** w’è”N¨‘–¯‚Ì‹x“ú‚Ì‚İ‚ÌDate[]‚Ìæ“¾ */
   public static Date[] getNatinalHoliday(int year){
      // Œ»İAŒh˜V‚Ì“ú‚ÆH•ª‚Ì“ú‚ª‚P“ú‚Å‹²‚Ü‚ê‚½ê‡‚Ì‚İB
      HolidayBundle k = HolidayType.RESPECT_FOR_AGE_DAY.getBundle(year);
      HolidayBundle a = HolidayType.AUTUMN_EQUINOX_DAY.getBundle(year);
      int aday = a.getDay();
      int kday = k.getDay();
      int chgday = k.getChangeDay();
      if ((aday - kday)==2){
         Calendar c = Calendar.getInstance();
         c.set(year,Calendar.SEPTEMBER,kday+1);
         return new Date[]{c.getTime()};
      }else if (chgday > 0 && ((aday - chgday)==2)){
         Calendar c = Calendar.getInstance();
         c.set(year,Calendar.SEPTEMBER,chgday+1);
         return new Date[]{c.getTime()};
      }
      return null;
   }
   //========================================================================
   // Œ³’U
   class NewYearDayBundle extends HolidayBundle{
      public NewYearDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 1;
      }
      @Override
      public int getMonth(){
         return 1;
      }
      @Override
      public String getDescription(){
         return "Œ³’U";
      }
   }
   // ¬l‚Ì“ú
   class ComingOfAgeDayBundle extends HolidayBundle{
      public ComingOfAgeDayBundle(int year){
         super(year);
      }
      /* ‚PŒ‘æ‚QŒ—j“ú‚Ì“ú•t‚ğ‹‚ß‚é */
      @Override
      public int getDay(){
         Calendar cal = Calendar.getInstance();
         cal.set(super.year,Calendar.JANUARY,1);
         int wday = cal.get(Calendar.DAY_OF_WEEK);
         return wday > Calendar.MONDAY ? (7*2+1)-(wday - Calendar.MONDAY) : 7+1+(Calendar.MONDAY - wday);
      }
      @Override
      public int getMonth(){
         return 1;
      }
      @Override
      public String getDescription(){
         return "¬l‚Ì“ú";
      }
   }
   // Œš‘‹L”O“ú
   class NatinalFoundationBundle extends HolidayBundle{
      public NatinalFoundationBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 11;
      }
      @Override
      public int getMonth(){
         return 2;
      }
      @Override
      public String getDescription(){
         return "Œš‘‹L”O“ú";
      }
   }
   // t•ª‚Ì“ú
   // wŠCã•ÛˆÀ’¡…˜H•” —ïŒvZŒ¤‹†‰ï•Ò V‚±‚æ‚İ•Ö—˜’ x‚É‚æ‚éŒvZ®
   // ‚³‚ç‚ÉA1979”NˆÈ‘O‚ğ–³‹I`2150”N‚Ü‚Å—LŒø
   class SpringEquinoxBundle extends HolidayBundle{
      public SpringEquinoxBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         if (super.year <= 2099){
            return (int)(20.8431 + (0.242194 * (super.year - 1980)) - ((super.year - 1980 )/4));
         }
         return (int)(21.851 + (0.242194 * (super.year - 1980)) - ((super.year - 1980)/4));
      }
      @Override
      public int getMonth(){
         return 3;
      }
      @Override
      public String getDescription(){
         return "t•ª‚Ì“ú";
      }
   }
   // º˜a‚Ì“ú
   class ShowaDayBundle extends HolidayBundle{
      public ShowaDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 29;
      }
      @Override
      public int getMonth(){
         return 4;
      }
      @Override
      public String getDescription(){
         return "º˜a‚Ì“ú";
      }
   }
   // Œ›–@‹L”O“ú
   class KenpoukikenDayBundle extends HolidayBundle{
      public KenpoukikenDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 3;
      }
      @Override
      public int getMonth(){
         return 5;
      }
      // ‚TŒ‚R“úSunday ‚ÌU‘Ö‚ÍA‚U“ú
      @Override
      public int getChangeDay(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            return 6;
         }
         return -1;
      }
      @Override
      public Date getChangeDate(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,6);
            return cal.getTime();
         }
         return null;
      }
      @Override
      public String getDescription(){
         return "Œ›–@‹L”O“ú";
      }
   }
   // ‚İ‚Ç‚è‚Ì“ú
   class MidoriDayBundle extends HolidayBundle{
      public MidoriDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 4;
      }
      @Override
      public int getMonth(){
         return 5;
      }
      // ‚TŒ‚S“úSunday ‚ÌU‘Ö‚ÍA‚U“ú
      @Override
      public int getChangeDay(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            return 6;
         }
         return -1;
      }
      @Override
      public Date getChangeDate(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,6);
            return cal.getTime();
         }
         return null;
      }
      @Override
      public String getDescription(){
         return "‚İ‚Ç‚è‚Ì“ú";
      }
   }
   // ‚±‚Ç‚à‚Ì“ú
   class KodomoDayBundle extends HolidayBundle{
      public KodomoDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 5;
      }
      @Override
      public int getMonth(){
         return 5;
      }
      @Override
      public String getDescription(){
         return "‚±‚Ç‚à‚Ì“ú";
      }
   }
   // ŠC‚Ì“ú
   class SeaDayBundle extends HolidayBundle{
      public SeaDayBundle(int year){
         super(year);
      }
      /* ‚VŒ‘æ‚RŒ—j“ú‚Ì“ú•t‚ğ‹‚ß‚é */
      @Override
      public int getDay(){
         Calendar cal = Calendar.getInstance();
         cal.set(super.year,Calendar.JULY,1);
         int wday = cal.get(Calendar.DAY_OF_WEEK);
         return wday > Calendar.MONDAY ? (7*3+1)-(wday - Calendar.MONDAY) : 14+1+(Calendar.MONDAY - wday);
      }
      @Override
      public int getMonth(){
         return 7;
      }
      @Override
      public String getDescription(){
         return "ŠC‚Ì“ú";
      }
   }
   // Œh˜V‚Ì“ú
   class RespectForAgeDayBundle extends HolidayBundle{
      public RespectForAgeDayBundle(int year){
         super(year);
      }
      /* ‚XŒ‘æ‚RŒ—j“ú‚Ì“ú•t‚ğ‹‚ß‚é */
      @Override
      public int getDay(){
         Calendar cal = Calendar.getInstance();
         cal.set(super.year,Calendar.SEPTEMBER,1);
         int wday = cal.get(Calendar.DAY_OF_WEEK);
         return wday > Calendar.MONDAY ? (7*3+1)-(wday - Calendar.MONDAY) : 14+1+(Calendar.MONDAY - wday);
      }
      @Override
      public int getMonth(){
         return 9;
      }
      @Override
      public String getDescription(){
         return "Œh˜V‚Ì“ú";
      }
   }
   // H•ª‚Ì“ú
   // wŠCã•ÛˆÀ’¡…˜H•” —ïŒvZŒ¤‹†‰ï•Ò V‚±‚æ‚İ•Ö—˜’ x‚É‚æ‚éŒvZ®
   // ‚³‚ç‚ÉA1979”NˆÈ‘O‚ğ–³‹I`2150”N‚Ü‚Å—LŒø
   class AutumnEquinoxBundle extends HolidayBundle{
      public AutumnEquinoxBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         if (super.year <= 2099){
            return (int)(23.2488 + (0.242194 * (super.year - 1980)) - ((super.year - 1980)/4));
         }
         return (int)(24.2488 + (0.242194 * (super.year - 1980)) - ((super.year - 1980)/4));
      }
      @Override
      public int getMonth(){
         return 9;
      }
      @Override
      public String getDescription(){
         return "H•ª‚Ì“ú";
      }
   }
   // ‘Ìˆç‚Ì“ú
   class HealthSportsDayBundle extends HolidayBundle{
      public HealthSportsDayBundle(int year){
         super(year);
      }
      /* ‚P‚OŒ‘æ‚QŒ—j“ú‚Ì“ú•t‚ğ‹‚ß‚é */
      @Override
      public int getDay(){
         Calendar cal = Calendar.getInstance();
         cal.set(super.year,Calendar.OCTOBER,1);
         int wday = cal.get(Calendar.DAY_OF_WEEK);
         return wday > Calendar.MONDAY ? (7*2+1)-(wday - Calendar.MONDAY) : 7+1+(Calendar.MONDAY - wday);
      }
      @Override
      public int getMonth(){
         return 10;
      }
      @Override
      public String getDescription(){
         return "‘Ìˆç‚Ì“ú";
      }
   }
   // •¶‰»‚Ì“ú
   class CultureDayBundle extends HolidayBundle{
      public CultureDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 3;
      }
      @Override
      public int getMonth(){
         return 11;
      }
      @Override
      public String getDescription(){
         return "•¶‰»‚Ì“ú";
      }
   }
   // ‹Î˜JŠ´Ó‚Ì“ú
   class LaborThanksDayBundle extends HolidayBundle{
      public LaborThanksDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 23;
      }
      @Override
      public int getMonth(){
         return 11;
      }
      @Override
      public String getDescription(){
         return "‹Î˜JŠ´Ó‚Ì“ú";
      }
   }
   // “Vc’a¶“ú
   class TennoBirthDayBundle extends HolidayBundle{
      public TennoBirthDayBundle(int year){
         super(year);
      }
      @Override
      public int getDay(){
         return 23;
      }
      @Override
      public int getMonth(){
         return 12;
      }
      @Override
      public String getDescription(){
         return "“Vc’a¶“ú";
      }
   }
}
