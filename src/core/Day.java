package core;

import java.io.*;
import java.util.*;

public class Day {
	public Vector periods;
	public int day; //Day of the week

	public Day() {
		periods = new Vector();
		day = 0;
	}
	
	public byte[] toByteStream() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( bout );
		
		int size = periods.size();
		dout.writeInt(size);
		for(int i=0; i<size; i++)
		{
			Object o = periods.elementAt(i);
			Period p = (Period)o;
			byte[] data = p.toByteStream();
			
			dout.writeInt( data.length );
			dout.write( data, 0, data.length );
		}
		dout.writeInt(day);
		
		dout.close();
		return bout.toByteArray();
	}
	
	public void fromByteStream(byte[] data) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream( bin );
		
		int size = din.readInt();
		periods.ensureCapacity(size);
		for( int i=0; i<size; i++ )
		{
			int len = din.readInt();
			byte[] periodData = new byte[len];
			din.read( periodData, 0, len );
			
			Period p = new Period();
			p.fromByteStream(periodData);
			
			periods.addElement( p );
		}
		day = din.readInt();
		din.close();
	}

	public String getDay() {
		return getDay(day);
	}
	
	public static String getDay(int d) {
		switch( d ) {
			case Calendar.MONDAY :
				return "Monday";
			case Calendar.TUESDAY :
				return "Tuesday";
			case Calendar.WEDNESDAY :
				return "Wednesday";
			case Calendar.THURSDAY :
				return "Thursday";
			case Calendar.FRIDAY :
				return "Friday";
			case Calendar.SATURDAY :
				return "Saturday";
			case Calendar.SUNDAY :
				return "Sunday";
		}
		return "Unknown";
	}

	public static int getDay(String s) {
		if( s.compareTo("Monday") == 0 ) {
			return Calendar.MONDAY;
		}
		if( s.compareTo("Tuesday") == 0 ) {
			return Calendar.TUESDAY;
		}
		if( s.compareTo("Wednesday") == 0 ) {
			return Calendar.WEDNESDAY;
		}
		if( s.compareTo("Thursday") == 0 ) {
			return Calendar.THURSDAY;
		}
		if( s.compareTo("Friday") == 0 ) {
			return Calendar.FRIDAY;
		}
		if( s.compareTo("Saturday") == 0 ) {
			return Calendar.SATURDAY;
		}
		if( s.compareTo("Sunday") == 0 ) {
			return Calendar.SUNDAY;
		}
		return -1;
	}
}
