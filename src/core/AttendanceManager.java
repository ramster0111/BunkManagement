package core;

import java.util.*;
import java.io.*;
import javax.microedition.rms.*;

public class AttendanceManager {
	private Vector _attn;
	private Vector _subAttn;
	
	public static final String RSN_SUB_ATTN = "Subject_Attn";
	public static final String RSN_ATTN = "Attendance";

	public AttendanceManager() {
        try {
			RecordStore rsAttn = RecordStore.openRecordStore(RSN_ATTN, true);
            int attnNum = rsAttn.getNumRecords();
            rsAttn.closeRecordStore();

			if( attnNum != 0 ) {
				return;
			}

			_attn = new Vector();
			AttendanceRecord ar = new AttendanceRecord();
			ar.setDefault();
			_attn.addElement(ar);
			saveAttendance();

			computeSubAttn();
			saveSubAttn();
		}
		catch( RecordStoreNotFoundException e ) {
            System.out.println("RecordStore not found - " + e);
        }
		catch( RecordStoreNotOpenException e ) {
			System.out.println("RecordStore not open - " + e);
		}
		catch( InvalidRecordIDException e ) {
			System.out.println("RecordStore invalid id - " + e);
		}
		catch( RecordStoreException e ) {
			System.out.println("RecordStore Exception Caught");
		}
	}

	public void computeSubAttn() {
		TimeTableManager ttm = new TimeTableManager();
		ttm.load();
		
		Vector subjects = ttm.getSubjects();
		Vector days = ttm.getDays();

		//Initialize _subAttn
		_subAttn = new Vector();
		_subAttn.ensureCapacity(subjects.size());
		for( int i = 0; i < subjects.size(); i++) {
			_subAttn.addElement( new SubjectAttnRecord(i) );
		}

		for( int i=0; i<_attn.size(); i++) {
			AttendanceRecord ar = (AttendanceRecord)_attn.elementAt(i);
			int dayType = ar.getDay();

			Day day = ttm.getDay(dayType);
			Vector periods = day.periods;

			for( int j=0; j< periods.size(); j++ ) {
				int status = ar.getAttn(j);
				Period p = (Period)periods.elementAt(j);
				int subIndex = p.subject;
				SubjectAttnRecord sar = (SubjectAttnRecord)_subAttn.elementAt(subIndex);

				switch( status ) {
					case 0 : //Absent
						sar.total += 1;
						break;
					case 1 : //Present
						sar.attended += 1;
						sar.total += 1;
					case -1 : //Did not occur
						break;
				}
			}
		}
	}

	public void load() {
		loadAttendance();
		loadSubAttn();
	}

	public void save() {
		//System.out.println("Saving");
		cleanUp();
		saveAttendance();
		computeSubAttn();
		saveSubAttn();
	}
	
	public void loadAttendance() {
		try {
            RecordStore rsAttn = RecordStore.openRecordStore(RSN_ATTN, false);
            int size = rsAttn.getNumRecords();

            byte[] data = rsAttn.getRecord(1);
            attendanceFromByteStream(data);

            rsAttn.closeRecordStore();
        }
        catch( RecordStoreNotFoundException e ) {
            System.out.println("RecordStore not found - " + e);
        }
		catch( RecordStoreNotOpenException e ) {
			System.out.println("RecordStore not open - " + e);
		}
		catch( InvalidRecordIDException e ) {
			System.out.println("RecordStore invalid id - " + e);
		}
		catch( RecordStoreException e ) {
			System.out.println("RecordStore Exception Caught");
		}
	}

	private void saveAttendance() {
        try {
            //Delete the record store
			RecordStore.deleteRecordStore(RSN_ATTN);

			//Create new
			RecordStore rsAttn = RecordStore.openRecordStore(RSN_ATTN, true);
            int size = rsAttn.getNumRecords();

            byte[] data = attendanceToByteStream();
            rsAttn.addRecord(data, 0, data.length );

            rsAttn.closeRecordStore();
        }
        catch( RecordStoreNotFoundException e ) {
            System.out.println("RecordStore not found - " + e);
        }
		catch( RecordStoreNotOpenException e ) {
			System.out.println("RecordStore not open - " + e);
		}
		catch( InvalidRecordIDException e ) {
			System.out.println("RecordStore invalid id - " + e);
		}
		catch( RecordStoreException e ) {
			System.out.println("RecordStore Exception Caught");
		}
	}

	public void loadSubAttn() {
		try {
            RecordStore rsSubAttn = RecordStore.openRecordStore(RSN_SUB_ATTN, false);
            int size = rsSubAttn.getNumRecords();

            byte[] data = rsSubAttn.getRecord(1);
            subAttnFromByteStream(data);

            rsSubAttn.closeRecordStore();
        }
        catch( RecordStoreNotFoundException e ) {
            System.out.println("RecordStore not found - " + e);
        }
		catch( RecordStoreNotOpenException e ) {
			System.out.println("RecordStore not open - " + e);
		}
		catch( InvalidRecordIDException e ) {
			System.out.println("RecordStore invalid id - " + e);
		}
		catch( RecordStoreException e ) {
			System.out.println("RecordStore Exception Caught");
		}
	}

	private void saveSubAttn() {
        try {
            //Delete the record store
			try {
				RecordStore.deleteRecordStore(RSN_SUB_ATTN);
			}
			catch( RecordStoreException e ) {
				//Ignore it
			}
			
			//Create new
			RecordStore rsSubAttn = RecordStore.openRecordStore(RSN_SUB_ATTN, true);
            int size = rsSubAttn.getNumRecords();

            byte[] data = subAttnToByteStream();
            rsSubAttn.addRecord(data, 0, data.length );

            rsSubAttn.closeRecordStore();
        }
        catch( RecordStoreNotFoundException e ) {
            System.out.println("RecordStore not found - " + e);
        }
		catch( RecordStoreNotOpenException e ) {
			System.out.println("RecordStore not open - " + e);
		}
		catch( InvalidRecordIDException e ) {
			System.out.println("RecordStore invalid id - " + e);
		}
		catch( RecordStoreException e ) {
			System.out.println("RecordStore Exception Caught");
		}
	}

	private byte[] attendanceToByteStream() {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream( bout );

        try {
            //Write the days array
            dout.writeInt( _attn.size() );
            for(int i=0; i<_attn.size(); i++)
            {
                AttendanceRecord ar = (AttendanceRecord)_attn.elementAt(i);
                byte[] data = ar.toByteStream();

                dout.writeInt( data.length );
                dout.write( data, 0, data.length );
            }
            dout.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }

        return bout.toByteArray();
	}

	private void attendanceFromByteStream(byte[] data){
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream( bin );

            int attnSize = din.readInt();
			_attn = new Vector();
            _attn.ensureCapacity(attnSize);
            for( int i=0; i<attnSize; i++ )
            {
                int len = din.readInt();
                byte[] arData = new byte[len];
                din.read( arData, 0, len );

                AttendanceRecord ar = new AttendanceRecord();
                ar.fromByteStream(arData);

                _attn.addElement( ar );
            }
            din.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }
	}

	private byte[] subAttnToByteStream() {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream( bout );

        try {
            //Write the days array
            dout.writeInt( _subAttn.size() );
            for(int i=0; i<_subAttn.size(); i++)
            {
                SubjectAttnRecord sar = (SubjectAttnRecord)_subAttn.elementAt(i);
                byte[] data = sar.toByteStream();

                dout.writeInt( data.length );
                dout.write( data, 0, data.length );
            }
            dout.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }

        return bout.toByteArray();
	}

	private void subAttnFromByteStream(byte[] data){
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream( bin );

            int attnSize = din.readInt();
			_subAttn = new Vector();
            _subAttn.ensureCapacity(attnSize);
            for( int i=0; i<attnSize; i++ )
            {
                int len = din.readInt();
                byte[] arData = new byte[len];
                din.read( arData, 0, len );

                SubjectAttnRecord sar = new SubjectAttnRecord();
                sar.fromByteStream(arData);

                _subAttn.addElement( sar );
            }
            din.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }
	}

	public Vector getAttendance() {
		return _attn;
	}

	public Vector getSubAttn() {
		return _subAttn;
	}

	public SubjectAttnRecord getSubAttn(int index) {
		SubjectAttnRecord sar = (SubjectAttnRecord)_subAttn.elementAt(index);
		if ( sar.subject == index ) {
			return sar;
		}
		return null;
	}

	public AttendanceRecord getAttnRecord(Date d) {
		for( int i=0; i<_attn.size(); i++) {
			AttendanceRecord ar = (AttendanceRecord)_attn.elementAt(i);
			if( DayCounter.daysUntil(d, ar.date) == 0 ) {
				return ar;
			}
		}
		return null;
	}

	private void cleanUp() {
		//System.out.println("Cleaning up");

		Vector v = _attn;
		_attn = new Vector();
		//System.out.println("Size : " + Integer.toString(v.size()));
		
		for( int i=0; i<v.size(); i++) {
			AttendanceRecord ar = (AttendanceRecord)v.elementAt(i);
			if( ar.empty() == false) {
				_attn.addElement(ar);
			}
		}
		//System.out.println("Size : " + Integer.toString(_attn.size()));
	}
}
