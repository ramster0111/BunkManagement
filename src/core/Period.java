package core;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Period {
	public long startTime;
	public int duration;
	public int subject;
	
	public Period()
	{
		startTime = 0;
		duration = 0;
		subject = 0;
	}
	
	public byte[] toByteStream() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( bout );
		
		dout.writeLong(startTime);
		dout.writeInt(duration);
		dout.writeInt(subject);
		
		dout.close();
		return bout.toByteArray();
	}
	
	public void fromByteStream(byte[] data) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream( bin );
		
		startTime = din.readLong();
		duration = din.readInt();
		subject = din.readInt();

		din.close();
	}

	public long getEndTime() {
		Date d = new Date();
		d.setTime(startTime);

		Calendar c = Calendar.getInstance();
		c.setTime(d);

		int min = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR);

		min += duration % 60;
		hour += duration / 60;

		c.set(Calendar.MINUTE, min);
		c.set(Calendar.HOUR, hour);
		return c.getTime().getTime();
	}
	
	public String getSt() {
		return getTime(startTime);
	}

	public String getEt() {
		return getTime( getEndTime() );
	}

	public static String getTime(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime( d );
		int hours = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int am_pm = c.get(Calendar.AM_PM);
		String amPm = new String( am_pm == 0? "AM" : "PM" );
		
		if( min < 10 ){
			return new String( Integer.toString(hours) + ":" +Integer.toString(0)+Integer.toString(min) + " " +
					amPm );
		}
		return new String( Integer.toString(hours) + ":" + Integer.toString(min) + " " +
							amPm );
	}

	public static String getTime(long time) {
		Date d = new Date();
		d.setTime(time);
		return getTime(d);
	}
}
