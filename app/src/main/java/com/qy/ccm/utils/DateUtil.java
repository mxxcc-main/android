package com.qy.ccm.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期相关的操作
 */
public class DateUtil {

    //每一天的毫秒数
    private static final long MS_EVERY_DAY = 1000 * 60 * 60 *24;

    //默认的pattern
    public final static String PATTERN = "yyyyMMdd";

    private final static String PATTERN_ = "yyMMdd";

    public final static String PATTERN_1 = "yyyy/MM/dd";
    
    public final static String PATTERN_2 = "yyyy/MM/dd HH:mm:ss";

    private final static String PATTERN_TIME = "yyyy-MM-dd HH:mm:ss.S";

    public final static String PATTERN_TIME2 = "yyyy-MM-dd";

    private final static String PATTERN_TIME3 = "yyyy-MM-dd";

    private final static String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    public final static String TIMESTAMP_1 = "yyyyMMddHHmmss";
    
    public final static String TIMESTAMP_2 = "HHmmss";
    
    public final static String TIMESTAMP_3 = "HH:mm:ss";
    
    public final static String TIMESTAMP_4 = "HH:mm";

    
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 英文时分 如：2010-12-01 23:15
     */
    public static String FORMAT_MIDDLE = "yyyy-MM-dd HH:mm";

    /**
     * 英文带小时 如：2010-12-01 23
     */
    public static String FORMAT_HOUR = "yyyy-MM-dd HH";
    
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     * 
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     * 
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }
    
    /**
     * 根据预设格式返回当前日期
     * 
     * @return
     */
    public static Date getNowDate() {
        return parse(format(new Date()));
    }

    /**
     * 根据用户格式返回当前日期
     * 
     * @param format
     * @return
     */
    public static Date getNowDate(String format) {
        return parse(format(new Date(), format),format);
    }

    /**
     * 使用预设格式格式化日期
     * 
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }

    /**
     * 使用用户格式格式化日期
     * 
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 使用预设格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @return
     */
    public static Date parseLenient(String strDate) {
        return parseLenient(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     * 
     * @param strDate
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return
     */
    public static Date parseLenient(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        df.setLenient(false);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    /**
     * 在日期上增加年
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的年
     * @return
     */
    public static Date addYear(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加数个整月
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }
    
    /**
     * 在日期上增加小时
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的小时
     * @return
     */
    public static Date addHour(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, n);
        return cal.getTime();
    }
    
    /**
     * 在日期上增加分钟
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的分钟
     * @return
     */
    public static Date addMinute(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加秒
     * 
     * @param date
     *            日期
     * @param n
     *            要增加的秒数
     * @return
     */
    public static Date addSecond(Date date, int n) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.SECOND, n);
    	return cal.getTime();
    }
    
    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     * 
     * @param date
     *            日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     * 
     * @param date
     *            日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     * 
     * @param date
     *            日期字符串
     * @param format
     *            日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    // 日期格式化
    private static SimpleDateFormat dateFormat;


    static {
        dateFormat = new SimpleDateFormat();
    }
    /**
     *
     */
    public DateUtil() {
        super();
    }

    /*
     *  按自定义格式取得当前日期时间
     *  Sample: DateTimeUtil.getCurrDateTime("yyyyMMddHHmmss")
     */
    public static synchronized String getCurrDateTime(String format){
		Calendar calendar = GregorianCalendar.getInstance();
        Date date = calendar.getTime();
        dateFormat.applyPattern(format);
        return dateFormat.format(date);
	}

    /**
     * 返回昨天的日期
     * @return
     */
    public static String getYesterdayDate(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date beginDate = calendar.getTime();
        SimpleDateFormat dateFmt = new SimpleDateFormat(PATTERN);
        String yesterdayDate = dateFmt.format(beginDate);

        return yesterdayDate;
    }

    /**
     * 由日期型转化为"yyyyMMdd"形式的String类型
     * @param date
     * @return
     */
    public static String dateToString(Date date){

        SimpleDateFormat dateFmt = new SimpleDateFormat(PATTERN);
        return dateFmt.format(date);
    }

    /**
     * 由日期型转化为"yyyyMMdd"形式的String类型
     * @param date
     * @return
     */
    public static String dateToString(Date date , String pattern){

        SimpleDateFormat dateFmt = new SimpleDateFormat( pattern );
        return dateFmt.format(date);
    }
    /**
     * 由String类型，转化为日期型
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date stringToDate( String strDate ) throws ParseException{
        DateFormat df = new SimpleDateFormat(PATTERN);
        Date date = df.parse( strDate );
        return date;
    }

    /**
     * 由String类型，转化为日期型
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date stringToDate_Time( String strDate ) throws ParseException{
        DateFormat df = new SimpleDateFormat(PATTERN_TIME);
        Date date = df.parse( strDate );
        return date;
    }

    /**
     * 由String类型，转化为日期型
     * @param strDate zhs加
     * @return
     * @throws ParseException
     */
    public static Date stringToDate_Time2( String strDate ) throws ParseException{
        DateFormat df = new SimpleDateFormat(PATTERN_TIME2);
        Date date = df.parse( strDate );
        return date;
    }

    /**
     * 由String类型，转化为日期型
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date stringTo_Date_Time2( String strDate ) throws ParseException{
        DateFormat df = new SimpleDateFormat(PATTERN_TIME3);
        Date date = df.parse( strDate );
        return date;
    }

    /**
     * 由String类型，转化为日期型
     * @param strDate
     * @return
     * @throws ParseException
     */
    public static Date stringToDate( String strDate, String pattern ) throws ParseException{
        DateFormat df = new SimpleDateFormat(pattern);
        Date date = df.parse( strDate );
        return date;
    }

    /**
     * 得到两个日期型数据之间所差的天数,此处为闭区间
     * @param beginDate 起始的日期
     * @param endDate 结束的日期
     * @return 相差的天数
     */
    public static long compare( Date beginDate, Date endDate ){
        long beginTime = beginDate.getTime();
        long endTime = endDate.getTime();
        long betweenDays = (long)((endTime - beginTime) / MS_EVERY_DAY );
        if( betweenDays >= 0)
            return betweenDays + 1;
        else
            return betweenDays -1;
    }

    /**
     * 得到一个日期与当前时间之间所差的天数,此处为闭区间
     * @param beginDate 起始的日期
     * @return 相差的天数
     */
    public static long compare( Date beginDate ){
        long beginTime = beginDate.getTime();
        Calendar calendar = GregorianCalendar.getInstance();
        Date endDate = calendar.getTime();
        long endTime = endDate.getTime();
        long betweenDays = (long)((endTime - beginTime) / MS_EVERY_DAY );
        return betweenDays;
    }

    /**
     * 取date后第n天的Date
     * @param date
     * @param n
     * @return
     */
    public static Date nextDate( Date date , int n ){
        long day = n * MS_EVERY_DAY ;
        Date d = new Date( date.getTime() + day );
        return d ;
    }

    /**
     * 检查日期合法性,默认formatType为yyyyMMdd
     * @param date
     * @param n
     * @return
     */
    public static boolean checkDate(String date,String formatType){
    	if(null==date)
    		return false;
    	if("".equalsIgnoreCase(formatType)||null==formatType)
    		formatType = "yyyyMMdd";
    	try{
    		  //以SimpleDateFormat來檢核日期
    		  //關於 SimpleDateFormat 請自己看Java api spec
    		  SimpleDateFormat dFormat = new SimpleDateFormat(formatType);

    		  //就是這下面這一行，花了我大半天找問題…
    		  dFormat.setLenient(false);

    		  //如果成功就是正確的日期，失敗就是有錯誤的日期。
//    		  java.util.Date d = dFormat.parse(date);
    		  dFormat.parse(date);

    		  return true;
    		}catch(ParseException e){
    		  //告訴user，這個日期不是一個正確的日期"
    			return false;
    		}
    }
    
    /**
     * 根据日期获取工作日，如日期为周末着返回下周一的日期，否则返回传入日期
     * @param date
     * @return
     */
	public static Date getNextWorkDay(Date date) {
		Date resultDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			resultDate = addDay(date, 2);
		}else if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
			resultDate = addDay(date, 1);
		}else{
			resultDate = date;
		}
		return resultDate;
	}
	
	/**
     * 判断传入日期是否为周末
     * @param date
     * @return
     */
	public static Boolean isWeekend(Date date) {
		Boolean flg = false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			flg = true;
		}
		return flg;
	}

     /**
     * 取得n天时间
     *
     * @param n n=0 今天 n=1 明天；n=0 今天 n=-1 昨天
     * @return String 返回n天时间 yyyy-mm-dd
     */
    public static String getDateList(int n) {

        GregorianCalendar gcDate = new GregorianCalendar();
        String sbDateTodayTime;
        int year = gcDate.get(1);
        int month = gcDate.get(2);
        int date = gcDate.get(5);
        GregorianCalendar oneWeek = new GregorianCalendar(year, month, date);
        oneWeek.add(5, n);
        Date date2 = oneWeek.getTime();
        sbDateTodayTime = dateFormat.format(date2);
        return sbDateTodayTime;
    }

    /**
     * 获取当前时间，返回时间格式(如果调用参数为true时返回yyyy-MM-dd HH:mm:ss
     * ，否则为false时返回yyyy-MM-DD不带日期格式)
     * @param time boolean
     * @return String
     *
     */
    public static String getNowTime(boolean time){
        Date now = new Date();
        String format = "";
        //yyyy-MM-dd HH:mm:ss:S 年月日时分秒毫杪
        if (time) {
            format = "yyyy-MM-dd ";
        } else {
            format = "yyyy-MM-dd HH:mm:ss.s";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowtime = sdf.format(now);
        return nowtime;
    }


    /**
     * 获取当前时间，返回时间格式yyyyMMdd
     * @param time boolean
     * @return String
     *
     */
    public static String getNowTime(){
        Date now = new Date();
        String format = "yyyyMMdd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowtime = sdf.format(now);
        return nowtime;
    }

    /**
     * 返回6位当前时间 生成报文参考号时使用
     *
     * @return
     */
    public static String getCurrentTime() {
        return parseDate(new Date(), PATTERN_);
    }

    //
    // /**
    // * 返回当前时间 生成报文参考号时使用
    // *
    // * @return
    // */
    // public static String getCurrentTimeForHvps() {
    // return parseDate(new Date());
    // }

    /**
     * @param dateStr
     *            日期字符串
     * @return
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, PATTERN);
    }

    /**
     * @param date
     * @return
     */
    public static String parseDateTimeStamp(Date date) {
        return parseDate(date, TIMESTAMP_PATTERN);
    }

    /**
     * @param dateStr
     *            日期字符串
     * @return
     */
    public static Timestamp parseDateTimeStamp(String dateStr) {
        return new Timestamp(parseDate(dateStr, TIMESTAMP_PATTERN).getTime());
    }

    /**
     * @param dateStr
     *            日期字符串
     * @param pattern
     *            格式化字符串
     * @return
     */
    public static Date parseDate(String dateStr, String pattern) {
        // dateFmt.applyPattern(pattern);
        SimpleDateFormat dateFmt = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = dateFmt.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     *
     * @param date
     *            日期
     * @return
     */
    public static String parseDate(Date date) {
        return parseDate(date, PATTERN);
    }

    /**
     * @param date
     *            日期
     * @param pattern
     *            格式化字符串
     * @return
     */
    public static String parseDate(Date date, String pattern) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(pattern);
        return dateFmt.format(date);
    }

    /**
     * 计算某一月份的最大天数
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDay(int year, int month) {
    	Calendar time=Calendar.getInstance();
    	time.clear();//注：在使用set方法之前，必须先clear一下，否则很多信息会继承自系统当前时间
    	time.set(Calendar.YEAR,year);
    	time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0
    	int day=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数
    	return day;
    }

    /**计算两个时间之间相隔天数
     * @param startday  开始时间
     * @param endday 结束时间
     * @return
     */
    public int getIntervalDays(Calendar startday,Calendar endday){
        //确保startday在endday之前
        if(startday.after(endday)){
            Calendar cal=startday;
            startday=endday;
            endday=cal;
        }
        //分别得到两个时间的毫秒数
        long sl=startday.getTimeInMillis();
        long el=endday.getTimeInMillis();

        long ei=el-sl;
        //根据毫秒数计算间隔天数
        return (int)(ei/(1000*60*60*24));
    }

    /**计算两个时间之间相隔天数
     * @param startday  开始时间
     * @param endday 结束时间
     * @return
     */
    public int getIntervalDays(Date startday,Date endday){
        //确保startday在endday之前
        if(startday.after(endday)){
            Date cal=startday;
            startday=endday;
            endday=cal;
        }
        //分别得到两个时间的毫秒数
        long sl=startday.getTime();
        long el=endday.getTime();

        long ei=el-sl;
        //根据毫秒数计算间隔天数
        return (int)(ei/(1000*60*60*24));
    }

    /**
     * 改进精确计算相隔天数的方法
     */
    public int getDaysBetween (Calendar d1, Calendar d2){
        if (d1.after(d2)){  // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2){
            d1 = (Calendar) d1.clone();
            do{
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
    /**
	 * 返回时间间隔的秒数
	 * @param endDate
	 * @param nowDate
	 * @return
	 */
    public static int calLastedTime(Date endDate, Date nowDate) {
		long a = endDate.getTime();
		long b = nowDate.getTime();
		int c = (int) ((a - b) / 1000);
		return c;
	}

    
    public static String timeDifference(String time) throws ParseException{
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date now = df.parse(DateUtil.getNow(DateUtil.FORMAT_LONG));
    	Date date=df.parse(time);
    	long l=now.getTime()-date.getTime();
	   	return l/1000+"";
    }
    
    /**计算2个日期之间的月差，包含日期计算，即止日期超出起日期也算作一个月
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int calcMonthDiffContainDay(Date startDate,Date endDate){
		Calendar startCal = DateUtils.toCalendar(startDate);
		Calendar endCal = DateUtils.toCalendar(endDate);
		
		return (endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 + 
				endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH) + 
				(endCal.get(Calendar.DAY_OF_MONTH) >= startCal.get(Calendar.DAY_OF_MONTH) ? 1 : 0);
	}
	//返回当前年份 
	int getYear(){ 
		Date date=new Date(); 
		String year=new SimpleDateFormat("yyyy").format(date); 
		return Integer.parseInt(year); 
	} 
	//返回当前月份 
	int getMonth(){ 
		Date date=new Date(); 
		String month=new SimpleDateFormat("MM").format(date); 
		return Integer.parseInt(month); 
	} 
	//判断闰年 
	public static boolean isLeap(int year){ 
		if(((year%100==0)&&year%400==0)||((year%100!=0)&&year%4==0)) 
		return true; 
		else 
		return false; 
	} 
	
	//返回当月天数 
	public static  int getDays(int year,int month){ 
		int days; 
		int FebDay=28; 
		if(isLeap(year)) 
		FebDay=29; 
		switch(month){ 
		case 1: 
		case 3: 
		case 5: 
		case 7: 
		case 8: 
		case 10: 
		case 12: 
		days=31;break; 
		case 4: 
		case 6: 
		case 9: 
		case 11: 
		days=30;break; 
		case 2: 
		days=FebDay;break; 
		default: 
		days=0;break; 
		} 
		return days; 
		} 
	//返回当月星期数 
	public static int getSundays(int year,int month,String week,int startDay,int endDay){ 
		
		SimpleDateFormat sdfi = new SimpleDateFormat("yyyy/MM/dd"); 
		int sundays=0; 
		SimpleDateFormat sdf=new SimpleDateFormat("EEEE"); 
		Calendar setDate= Calendar.getInstance(); 
		//从第一天开始 
		int day = 1; 
		int end = getDays(year,month);
		if(startDay != 0){
			day = startDay;
		}
		if(endDay != 0){
			end = endDay;
		}
		for(;day<= end;day++){ 
			String m = (month+"").length() ==1?"0"+month:month+"";
			String d = (day+"").length() ==1?"0"+day:day+"";
			String time = year+"/"+m +"/"+d;
			try {
				setDate.setTime(sdfi.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String str=sdf.format(setDate.getTime()); 
			if(str.equals(week)){ 
				sundays++; 
			} 
		} 
		return sundays; 
	} 
	public static boolean checkOverlap(List<String> list){  
		SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd");
        Collections.sort(list);//排序ASC  
          
        boolean flag = false;//是否重叠标识  
        for(int i=0; i<list.size(); i++){  
            if(i>0){  
                //跳过第一个时间段不做判断  
                String[] itime = list.get(i).split("-");  
                for(int j=0; j<list.size(); j++){  
                    //如果当前遍历的i开始时间小于j中某个时间段的结束时间那么则有重叠，反之没有重叠  
                    //这里比较时需要排除i本身以及i之后的时间段，因为已经排序了所以只比较自己之前(不包括自己)的时间段  
                    if(j==i || j>i){  
                        continue;  
                    }  
                      
                    String[] jtime = list.get(j).split("-");  
                    //此处DateUtils.compare为日期比较(返回负数date1小、返回0两数相等、返回正整数date1大)  
                    int compare = compare(  
                            (dateFormat.format(new Date())+" "+itime[0]+":00"),   
                            (dateFormat.format(new Date())+" "+jtime[1]+":00"));  
                    if(compare<0){  
                        flag = true;  
                        break;//只要存在一个重叠则可退出内循环  
                    }  
                }  
            }  
              
            //当标识已经认为重叠了则可退出外循环  
            if(flag){  
                break;  
            }  
        }  
          
        return flag;  
    }  
	public static int compare(String  date1, String date2) {
		SimpleDateFormat dateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int count =0;
		try {
			 count = dateFormat.parse(date1).compareTo(dateFormat.parse(date2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}	
	public static String  DecimalHour(String start,String end) {
		SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");  
        Date time = null;
		Date time2 = null;
		try {
			time = sdf.parse(start);  
			time2 = sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        sdf = null;  
          
        Calendar instance = Calendar.getInstance();  
        instance.setTime(time);  
        long timeInMillis1 = instance.getTimeInMillis();  
        instance = null;  
        time = null;  
          
        Calendar instance2 = Calendar.getInstance();  
        instance2.setTime(time2);  
        long timeInMillis2 = instance2.getTimeInMillis();  
        instance2 = null;  
        time2 = null;  
          
        double hours = (timeInMillis2 - timeInMillis1)/1000/60/60.0;  
        DecimalFormat df = new DecimalFormat("##.#");  
        String dff=df.format(hours);  
        return dff;
	}
}
