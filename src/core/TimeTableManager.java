package core;

import java.util.*;
import java.io.*;
import javax.microedition.rms.*;

public class TimeTableManager {
	private Vector _subjects;
	private Vector _days;
	
	public static final String RSN_TIMETABLE = "TimeTable";
	public static final String RSN_SUBJECTS = "Subjects";
	
	public TimeTableManager() {
        try {
            //Check if the App has been run for the first time.

            //Subjects
            RecordStore rsSubjects = RecordStore.openRecordStore(RSN_SUBJECTS, true);
            int subNum = rsSubjects.getNumRecords();
            rsSubjects.closeRecordStore();

			_subjects = new Vector();
            if( subNum == 0 )
            {
                String s = new String("Default Subject");
                _subjects.addElement(s);
                saveSubjects();
            }

            //Days
            RecordStore rsDays = RecordStore.openRecordStore(RSN_TIMETABLE, true);
            int dayNum = rsDays.getNumRecords();
            rsDays.closeRecordStore();

			_days = new Vector();
            if( dayNum == 0 )
            {
                Day d = new Day();
				d.day = Calendar.MONDAY;
				Period p = new Period();
                d.periods.addElement(p);

                saveTimeTable();
            }
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

	//Loading Functions
	public void load() {
		loadSubjects();
		loadTimeTable();
	}

	public void loadSubjects() {
        try {
            RecordStore rsSubjects = RecordStore.openRecordStore(RSN_SUBJECTS, false);
            int size = rsSubjects.getNumRecords();

            byte[] data = rsSubjects.getRecord(1);
            subjectsFromByteStream(data);

            rsSubjects.closeRecordStore();
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
	
	public void loadTimeTable() {
        try {
            RecordStore rsDays = RecordStore.openRecordStore(RSN_TIMETABLE, false);
            int size = rsDays.getNumRecords();

            byte[] data = rsDays.getRecord(1);
            daysFromByteStream(data);

            rsDays.closeRecordStore();
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
	
	//Saving Functions
	public void save() {
		saveSubjects();
		saveTimeTable();
	}
	
	public void saveSubjects() {
        try {
			//Delete the record store
			RecordStore.deleteRecordStore(RSN_SUBJECTS);

			//Create new
            RecordStore rsSubjects = RecordStore.openRecordStore(RSN_SUBJECTS, true);
            int size = rsSubjects.getNumRecords();

            byte[] data = subjectsToByteStream();
            rsSubjects.addRecord(data, 0, data.length );

            rsSubjects.closeRecordStore();
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
	
	public void saveTimeTable() {
        try {
            //Delete the record store
			RecordStore.deleteRecordStore(RSN_TIMETABLE);

			//Create new
			RecordStore rsDays = RecordStore.openRecordStore(RSN_TIMETABLE, true);
            int size = rsDays.getNumRecords();

            byte[] data = daysToByteStream();
            rsDays.addRecord(data, 0, data.length );

            rsDays.closeRecordStore();
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
	
	//To Byte Stream
	private byte[] subjectsToByteStream(){
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream( bout );
        try {
            //Write the subjects array
            dout.writeInt( _subjects.size() );
            for(int i=0; i<_subjects.size(); i++)
            {
                String sub = (String)_subjects.elementAt(i);
                dout.writeUTF(sub);
            }
            dout.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }
        return bout.toByteArray();
	}
	
	private byte[] daysToByteStream(){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream( bout );

        try {
            //Write the days array
            dout.writeInt( _days.size() );
            for(int i=0; i<_days.size(); i++)
            {
                Day d = (Day)_days.elementAt(i);
                byte[] data = d.toByteStream();

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
	
	// From byte Stream
	private void subjectsFromByteStream(byte[] data){
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream( bin );

            //Read the subjects
            int subSize = din.readInt();
            _subjects.ensureCapacity(subSize);
            for( int i=0; i<subSize; i++ )
            {
                String s = din.readUTF();
                _subjects.addElement(s);
            }
            din.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }
	}
		
	private void daysFromByteStream(byte[] data){
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(data);
            DataInputStream din = new DataInputStream( bin );

            //Read the Days
            int daySize = din.readInt();
            _days.ensureCapacity(daySize);
            for( int i=0; i<daySize; i++ )
            {
                int len = din.readInt();
                byte[] dayData = new byte[len];
                din.read( dayData, 0, len );

                Day d = new Day();
                d.fromByteStream(dayData);

                _days.addElement( d );
            }
            din.close();
        }
        catch( IOException e ) {
            System.out.println("IO Exception Caught");
        }
	}
	
	public Vector getSubjects() {
		return _subjects;
	}

	public String getSubject(int id) {
		String s = (String)_subjects.elementAt(id);
		return s;
	}
	
	public Vector getDays() {
		return _days;
	}

	public Day getDay(int day) {
		for( int i=0; i<_days.size(); i++ ) {
			Day d = (Day)_days.elementAt(i);
			if( d.day == day ) {
				return d;
			}
		}
		return null;
	}

	public Vector getPeriods(int day) {
		Day d = getDay(day);
		return d == null ? null : d.periods;
	}
}
