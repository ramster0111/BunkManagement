package core;

import core.Day;
import core.TimeTableManager;
import java.util.*;
import java.io.*;

public class AttendanceRecord {
	public Date date;
	public Vector attn;

	public AttendanceRecord() {
		date = new Date();
		attn = new Vector();
	}

	public String getAttnSatus(int i) {
		int st = getAttn(i);
		switch( st ) {
			case 0 :
				return new String("Absent");
			case 1 :
				return new String("Present");
			case -1 :
				return new String("Canceled");
		}
		return new String("Unknown");
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return c.get(Calendar.DAY_OF_WEEK);
	}

	public byte[] toByteStream() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( bout );

		dout.writeLong(date.getTime());
		dout.writeInt( attn.size() );
		for( int i=0; i<attn.size(); i++ ) {
			Integer a = (Integer)attn.elementAt(i);
			dout.writeInt( a.intValue() );
		}
		
		dout.close();
		return bout.toByteArray();
	}

	public void fromByteStream(byte[] data) throws IOException {
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream( bin );

		long time = din.readLong();
		date = new Date();
		date.setTime(time);
		
		int size = din.readInt();
		attn.ensureCapacity(size);
		for( int i=0; i<size; i++ )
		{
			int a = din.readInt();
			Integer in = new Integer(a);
			attn.addElement( in );
		}

		din.close();
	}

	public void setDefault() {
		int d = getDay();
		TimeTableManager ttm = new TimeTableManager();
		ttm.loadTimeTable();

		Vector days = ttm.getDays();
		Day day;
		try {
			day = (Day) days.elementAt(d);
		}
		catch( ArrayIndexOutOfBoundsException e ) {
			day = (Day) days.elementAt(0);
		}

		Vector periods = day.periods;
		for( int i=0; i<periods.size(); i++ ) {
			attn.addElement( new Integer(-1) );
		}
	}

	public int getAttn(int index) {
		Integer i = (Integer)attn.elementAt(index);
		return i.intValue();
	}

	public boolean empty() {
		for( int i=0; i<attn.size(); i++) {
			if( getAttn(i) != -1 ) {
				return false;
			}
		}
		return true;
	}
}
