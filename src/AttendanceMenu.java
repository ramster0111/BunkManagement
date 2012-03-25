import core.AttendanceManager;
import core.SubjectAttnRecord;
import core.TimeTableManager;
import java.util.Vector;
import javax.microedition.lcdui.*;

public class AttendanceMenu extends AppState implements CommandListener {
	private List _list;
	
	public AttendanceMenu(AttendanceApp p) {
		super(p);

		_list = new List("Days", Choice.IMPLICIT);
		_list.addCommand(CMD_BACK);
		_list.setCommandListener(this);
		
		_list.append( "Overall Attendance", null );
		_list.append( "Bunkable classes", null );

		_display.setCurrent(_list);
	}

	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND )
		{
			int index = _list.getSelectedIndex();
			if ( index == 0 ) {
				createAlertBox();
			}
			else if( index == 1 ) {
				createAlertBox2();

			}
		}
		else if( c == CMD_BACK ) {
			_parent.getSM().changeState(new MainMenu(_parent));
		}
	}
	private void createAlertBox2() {
		//Load necessary Managers
		TimeTableManager ttm = new TimeTableManager();
		AttendanceManager atm = new AttendanceManager();

		ttm.loadSubjects();
		atm.load();
		
		//Create alert string
		StringBuffer sb = new StringBuffer();

		int totAttended = 0;
		int totTotal = 0;
		int attended;
		int bunkable=0;
		int total;
		Vector subAttnVec = atm.getSubAttn();
		for( int i=0; i<subAttnVec.size(); i++) {
			bunkable = 0;
			String s = ttm.getSubject(i);
			sb.append(s);
			sb.append(" : ");

			SubjectAttnRecord sar = (SubjectAttnRecord)subAttnVec.elementAt(i);
			//sb.append(sar.attended);
			attended = sar.attended;
			//sb.append("/");
			//sb.append(sar.total);
			total = sar.total;
			
			if(total == 0 ){
				continue;
			}
			
			while( ( ( attended - bunkable ) / total ) *100 >=  75 ) {
				bunkable ++ ;
				if(( ( attended - bunkable ) / total ) *100 <  75){
					bunkable--;
					break;
				}
			}
			
			sb.append(bunkable);
			sb.append("\n");

		}

		Alert alert = new Alert("Bunkable Classes", sb.toString(), null, AlertType.INFO );
		alert.setTimeout(Alert.FOREVER);
		_display.setCurrent(alert, _list);
	}


	private void createAlertBox() {
		//Load necessary Managers
		TimeTableManager ttm = new TimeTableManager();
		AttendanceManager atm = new AttendanceManager();

		ttm.loadSubjects();
		atm.load();
		
		//Create alert string
		StringBuffer sb = new StringBuffer();

		int totAttended = 0;
		int totTotal = 0;
		Vector subAttnVec = atm.getSubAttn();
		for( int i=0; i<subAttnVec.size(); i++) {
			String s = ttm.getSubject(i);
			sb.append(s);
			sb.append(" : ");

			SubjectAttnRecord sar = (SubjectAttnRecord)subAttnVec.elementAt(i);
			sb.append(sar.attended);
			sb.append("/");
			sb.append(sar.total);
			sb.append(" - ");
			sb.append( sar.getPer() );
			sb.append("%\n");

			totAttended += sar.attended;
			totTotal += sar.total;
		}
		sb.append("Total : ");
		sb.append(totAttended);
		sb.append("/");
		sb.append(totTotal);
		sb.append(" - ");
		sb.append( (totAttended*100.0)/totTotal );
		sb.append("% \n");

		Alert alert = new Alert("Attendance", sb.toString(), null, AlertType.INFO );
		alert.setTimeout(Alert.FOREVER);
		_display.setCurrent(alert, _list);
	}
}
