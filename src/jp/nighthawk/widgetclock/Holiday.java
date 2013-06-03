package jp.nighthawk.widgetclock;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
/**
 * �j���v�Z�N���X.
 * 2007�N�ȍ~�̂ݗL��
 * �t���E�H���̓��́A�w�C��ۈ������H�� ��v�Z������� �V����ݕ֗����x�ɂ��v�Z����
 * ��錋�ʂ����߂Ă�ɂ����Ȃ��B���N�̊�������̌���ƈقȂ����犯������ɏ]�����ƁB
 * �N�Ԃ̏j�����X�g(�����̋x�����܂ށj�i�z��j�̎Z�o�A
 * �N�ƌ����w�肵�đΏی��̏j��(�����̋x�����܂ށj�̎Z�o�A
 * �w����t�̏j������A
 * �w�肷��j���̓��t�̎擾��ړI�Ƃ���B
 *
 * �C�ӂ̏j�����w�肵�ď����擾���邽�߂ɁA
 *     public abstract class HolidayBundle ��񋟂��Ă���B
 * ���̒��ۃN���X�̋�ۃN���X�Ƃ��ďj�����Ƃ̃N���X���p�ӂ���Ă���A�j�����A�j���v�Z��
 * �X�̏j��HolidayBundle �Ŏ�������B
 * �U�֋x���v�Z�́AHolidayBundle ���ۃN���X�Ŏ������邪�A����̏j���͋�ۃN���X��
 * �I�[�o�[���C�h�Ŏ�������B
 * �y�g�p��z
 *     // 2009�N�̏j���z��
 *         Holiday holday = new Holiday(2009);  // �R���X�g���N�^���g�p���邱��
 *         Date[] ary = holday.listHoliDays();
 *         for(int i=0;i < ary.length;i++){
 *            System.out.println(Holiday.YMD_FORMAT.format(ary[i])
 *                          +"\t"+Holiday.dateOfWeekJA(ary[i])
 *                          +"\t"+Holiday.queryHoliday(ary[i]));
 *         }
 *     // 2009�N�̂X���̏j���A
 *         int[] days = Holiday.listHoliDays(2009,Calendar.SEPTEMBER);
 *         Date[] dts = Holiday.listHoliDayDates(2009,Calendar.SEPTEMBER);
 *     // �w����t�̏j������
 *        String target = "2009/05/06";
 *        String res = Holiday.queryHoliday(Holiday.YMD_FORMAT.parse(target));
 *        System.out.println(res);
 *     // �w�肷��j���̓��t�̎擾
 *     //   Holiday.HolidayType enum ����AgetBundle���\�b�h�ŁAHoliday.HolidayBundle
 *     //   ���擾����Holiday.HolidayBundle���񋟂��郁�\�b�h�𗘗p����
 *          //    Holiday.HolidayBundle#getMonth()       �� ��
 *          //    Holiday.HolidayBundle#getDay()         �� ��
 *          //    Holiday.HolidayBundle#getDescription() �� �j����
 *          //    Holiday.HolidayBundle#getDate()        �� �j����Date
 *          //    Holiday.HolidayBundle#getChangeDay()   �� �U�֋x������ꍇ��Day
 *          //    Holiday.HolidayBundle#getChangeDate()  �� �U�֋x������ꍇ��Date
 *       // 2009�N�̏t���̓�
 *          Holiday.HolidayBundle h = Holiday.HolidayType.SPRING_EQUINOX_DAY.getBundle(2009);
 *          System.out.println(h.getMonth()+"�� "+h.getDay()+"��"
 *                              +"�i"+Holiday.WEEKDAYS_JA[h.getWeekDay()-1]+"�j"
 *                              +" "+h.getDescription());
 *     // �w��N�������̋x���݂̂�Date[]�̎擾
 *          // 2009�N�̍����̋x���z��
 *          Date[] ds = Holiday.getNatinalHoliday(2009);
 *          for(int i=0;i < ds.length;i++){
 *             System.out.println(Holiday.YMD_FORMAT.format(ds[i])+"-->"+Holiday.queryHoliday(ds[i]));
 *          }
 */
public class Holiday{
   private Date[] holidayDates;
   /**
    * �f�t�H���g�R���X�g���N�^.
    * ���ݓ��̔N�ŁAHoliday(int year) �R���X�g���N�^���Ăяo���̂Ɠ������ʂł��B
    */
   public Holiday(){
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      this.init(cal.get(Calendar.YEAR));
   }
   /**
    * �Ώ۔N �w��R���X�g���N�^.
    * @param year ����S��
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
   /** HolidayType �́A�j���^�C�v��HolidayBundle class ��R�t���� enum */
   public enum HolidayType{
      /** ���U        �F�P���P��            */  NEWYEAR_DAY             (NewYearDayBundle.class)
      /** ���l�̓�    �F�P���̑�Q���j��    */ ,COMING_OF_AGE_DAY       (ComingOfAgeDayBundle.class)
      /** �����L�O��  �F�Q���P�P��          */ ,NATIONAL_FOUNDATION_DAY (NatinalFoundationBundle.class)
      /** �t���̓�    �F�R�� ��������Ō��� */ ,SPRING_EQUINOX_DAY      (SpringEquinoxBundle.class)
      /** ���a�̓�    �F�S���Q�X��          */ ,SHOUWA_DAY              (ShowaDayBundle.class)
      /** ���@�L�O��  �F�T���R��            */ ,KENPOUKINEN_DAY         (KenpoukikenDayBundle.class)
      /** �݂ǂ�̓�  �F�T���S��            */ ,MIDORI_DAY              (MidoriDayBundle.class)
      /** ���ǂ��̓�  �F�T���T��            */ ,KODOMO_DAY              (KodomoDayBundle.class)
      /** �C�̓�      �F�V���̑�R���j��    */ ,SEA_DAY                 (SeaDayBundle.class)
      /** �h�V�̓�    �F�X���̑�R���j      */ ,RESPECT_FOR_AGE_DAY     (RespectForAgeDayBundle.class)
      /** �H���̓�    �F�X�� ��������Ō��� */ ,AUTUMN_EQUINOX_DAY      (AutumnEquinoxBundle.class)
      /** �̈�̓�    �F�P�O���̑�Q���j��  */ ,HEALTH_SPORTS_DAY       (HealthSportsDayBundle.class)
      /** �����̓�    �F�P�P���R��          */ ,CULTURE_DAY             (CultureDayBundle.class)
      /** �ΘJ���ӂ̓��F�P�P���Q�R��        */ ,LABOR_THANKS_DAY        (LaborThanksDayBundle.class)
      /** �V�c�a����  �F�P�Q���Q�R��        */ ,TENNO_BIRTHDAY          (TennoBirthDayBundle.class)
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
   // ����HolidayBundle class �Q�� enum
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
   /** �j��Bundle���ۃN���X */
   public abstract class HolidayBundle{
      int year;
      private Calendar mycal;
      public abstract int getDay();
      public abstract int getMonth();
      public abstract String getDescription();
      /** �Ώ۔N���w�肷��R���X�g���N�^
       * @param year ����S��
       */
      public HolidayBundle(int year){
         this.year = year;
         this.mycal = Calendar.getInstance();
         this.mycal.set(this.year,this.getMonth()-1,this.getDay());
      }
      /** �U�֋x���̑��݂���ꍇ�A�U�֋x���̓���Ԃ��B���݂��Ȃ��ꍇ�� -1 ��Ԃ��B*/
      public int getChangeDay(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,this.getDay());
            cal.add(Calendar.DAY_OF_MONTH,1);
            return cal.get(Calendar.DAY_OF_MONTH);
         }
         return -1;
      }
      /** �U�֋x���̑��݂���ꍇ�A�U�֋x����Date��Ԃ��B���݂��Ȃ��ꍇ�� null ��Ԃ��B*/
      public Date getChangeDate(){
         if (this.getWeekDay()==Calendar.SUNDAY){
            Calendar cal =  Calendar.getInstance();
            cal.set(this.year,this.getMonth()-1,this.getDay());
            cal.add(Calendar.DAY_OF_MONTH,1);
            return cal.getTime();
         }
         return null;
      }
      /** �j���̗j���� Calendar.DAY_OF_WEEK �ɏ]���ċ��߂� */
      public int getWeekDay(){
         return this.mycal.get(Calendar.DAY_OF_WEEK);
      }
      /** �j���� Date ���擾 */
      public Date getDate(){
         return this.mycal.getTime();
      }
   }

   /** �j���A�U�֋x�����܂�ŁADate�z��ŕԂ��B*/
   public Date[] listHoliDays(){
      return this.holidayDates;
   }
   /**
    * �w��N�A���̏j���A�U�֋x���A�����̋x���A���t(int)�z��ŕԂ�
    * @param year ����S��
    * @param calender_MONTH java.util.Calendar.MONTH�ɂ��t�B�[���h�l 0=�P���A11=�P�Q��
    * @return java.util.Calendar.DAY_OF_MONTH �ł���z��
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
      // ���݁A�����̋x���̔����͂X�������Ȃ�
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
    * �w��N�A���̏j���A�U�֋x���A�����̋x���A���t(Date)�z��ŕԂ�
    * @param year ����S��
    * @param calender_MONTH java.util.Calendar.MONTH�ɂ��t�B�[���h�l 0=�P���A11=�P�Q��
    * @return Date�z��
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
      // ���݁A�����̋x���̔����͂X�������Ȃ�
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

   /** ���t�t�H�[�}�b�g yyyy/MM/dd */
   public static SimpleDateFormat YMD_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
   /** Calendar.MONTH �ɉ����������̔z�� */
   public static String[] MONTH_NAMES = {"JANUARY","FEBRUARY","MARCH","APRIL","MAY","JUNE","JULY","AUGUST","SEPTEMBER","OCTOBER","NOVEMBER","DECEMBER"};

   /** �w������j���Ȃ�A�j������Ԃ��B�i�w����ɂ��j���A�U�֋x���`�F�b�N�ׁ̈j
    * @parama �w���
    * @return �j������Ԃ��B�j���A�U�֋x���ɊY�����Ȃ���΁Anull ��Ԃ��B
    */
   public static String queryHoliday(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      MonthBundle mb = MonthBundle.valueOf(MONTH_NAMES[cal.get(Calendar.MONTH)]);
      Constructor<?>[] constructors = mb.getConstructors();
      if (constructors==null){
         return null; // �j���łȂ��I
      }
      int targetDay = cal.get(Calendar.DAY_OF_MONTH);
      int targetYear = cal.get(Calendar.YEAR);
      for(int i=0;i < constructors.length;i++){
         try{
         HolidayBundle h = (HolidayBundle)constructors[i].newInstance(null,targetYear);
         if (targetDay==h.getDay()){ return h.getDescription(); }
         if (targetDay==h.getChangeDay()){ return "�U�֋x��"+"�i"+h.getDescription()+"�j"; }
         }catch(Exception e){
         }
      }
      Date[] natinalHolidayDates = getNatinalHoliday(targetYear);
      if (natinalHolidayDates != null){
         String targetDateStr = YMD_FORMAT.format(dt);
         for(int i=0;i < natinalHolidayDates.length;i++){
            if (targetDateStr.equals(YMD_FORMAT.format(natinalHolidayDates[i]))){
               return "�����̋x��";
            }
         }
      }
      return null;
   }

   public static String[] WEEKDAYS_JA = {"��","��","��","��","��","��","�y" };
   /** �j��String�Z�o Japanese */
   public static String dateOfWeekJA(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      return WEEKDAYS_JA[cal.get(Calendar.DAY_OF_WEEK)-1];
   }
   public static String[] WEEKDAYS_SIMPLE = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
   /** �j��String�Z�o */
   public static String dateOfWeekSimple(Date dt){
      Calendar cal = Calendar.getInstance();
      cal.setTime(dt);
      return WEEKDAYS_SIMPLE[cal.get(Calendar.DAY_OF_WEEK)-1];
   }

   /** �w��N�������̋x���݂̂�Date[]�̎擾 */
   public static Date[] getNatinalHoliday(int year){
      // ���݁A�h�V�̓��ƏH���̓����P���ŋ��܂ꂽ�ꍇ�̂݁B
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
   // ���U
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
         return "���U";
      }
   }
   // ���l�̓�
   class ComingOfAgeDayBundle extends HolidayBundle{
      public ComingOfAgeDayBundle(int year){
         super(year);
      }
      /* �P����Q���j���̓��t�����߂� */
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
         return "���l�̓�";
      }
   }
   // �����L�O��
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
         return "�����L�O��";
      }
   }
   // �t���̓�
   // �w�C��ۈ������H�� ��v�Z������� �V����ݕ֗����x�ɂ��v�Z��
   // ����ɁA1979�N�ȑO�𖳎��I�`2150�N�܂ŗL��
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
         return "�t���̓�";
      }
   }
   // ���a�̓�
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
         return "���a�̓�";
      }
   }
   // ���@�L�O��
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
      // �T���R����Sunday �̐U�ւ́A�U��
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
         return "���@�L�O��";
      }
   }
   // �݂ǂ�̓�
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
      // �T���S����Sunday �̐U�ւ́A�U��
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
         return "�݂ǂ�̓�";
      }
   }
   // ���ǂ��̓�
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
         return "���ǂ��̓�";
      }
   }
   // �C�̓�
   class SeaDayBundle extends HolidayBundle{
      public SeaDayBundle(int year){
         super(year);
      }
      /* �V����R���j���̓��t�����߂� */
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
         return "�C�̓�";
      }
   }
   // �h�V�̓�
   class RespectForAgeDayBundle extends HolidayBundle{
      public RespectForAgeDayBundle(int year){
         super(year);
      }
      /* �X����R���j���̓��t�����߂� */
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
         return "�h�V�̓�";
      }
   }
   // �H���̓�
   // �w�C��ۈ������H�� ��v�Z������� �V����ݕ֗����x�ɂ��v�Z��
   // ����ɁA1979�N�ȑO�𖳎��I�`2150�N�܂ŗL��
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
         return "�H���̓�";
      }
   }
   // �̈�̓�
   class HealthSportsDayBundle extends HolidayBundle{
      public HealthSportsDayBundle(int year){
         super(year);
      }
      /* �P�O����Q���j���̓��t�����߂� */
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
         return "�̈�̓�";
      }
   }
   // �����̓�
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
         return "�����̓�";
      }
   }
   // �ΘJ���ӂ̓�
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
         return "�ΘJ���ӂ̓�";
      }
   }
   // �V�c�a����
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
         return "�V�c�a����";
      }
   }
}
