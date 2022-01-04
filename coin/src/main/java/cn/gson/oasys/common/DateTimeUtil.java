package cn.gson.oasys.common;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DateTimeUtil {
	public static final String GSTIME="yyyy-MM-dd HH:mm:ss";

	//UNIX时间戳转换日期:方法一
	//方法一，可能会报Long未定义错误;[未知，若有知道的，请留下言]
	//当然可以转换成 str=unix_time.format(new Date(Long.parseLong(nowtime+"000")));
	public static String getTimestampDate(String timestamp){
		String str;
		SimpleDateFormat unix_time=new SimpleDateFormat(GSTIME);
//		str=unix_time.format(new Date(Long.valueOf(timestamp+"000")));
		str=unix_time.format(new Date(Long.parseLong(timestamp+"000")));
		//此处增加"000"是因为Date类中，传入的时间单位为毫秒。
		return str;
	}

	//时间戳转换成日期格式：方法二
	public static void getUnixTransferTime(){
		System.out.println("转换的日期是：");
		long nowtime=1541261100;//某个时间戳;
		Date date=new Date(nowtime*1000);
		SimpleDateFormat format=new SimpleDateFormat(GSTIME);
		String nowDateString=format.format(date);
		System.out.println(nowDateString);
	}

	//日期格式转换为UNIX时间戳
	public static String getDateTimestamp(String timestamp) throws ParseException{
		String str;
		SimpleDateFormat date_time=new SimpleDateFormat(GSTIME);
		Date date=date_time.parse(timestamp);
		long ts=date.getTime();
		str=String.valueOf(ts/1000);
		return str;
	}

	//获取当前时间时间戳
	public static String timecurrentTime(){
		long time=System.currentTimeMillis();
		String str=String.valueOf(time/1000);
		return str;
	}

	public static void main(String[] args) {
		System.out.println(timecurrentTime());
		System.out.println(getTimestampDate("1640050711"));
		
		String stringStamp = "2021-12-21 09:38:31";
		try {
			System.out.println(getDateTimestamp(stringStamp));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
