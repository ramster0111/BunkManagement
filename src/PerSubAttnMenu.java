import core.AttendanceManager;
import core.SubjectAttnRecord;
import core.TimeTableManager;
import java.util.Vector;
import javax.microedition.lcdui.*;

public class PerSubAttnMenu extends AppState implements CommandListener {
	private List _list;
	private TimeTableManager _ttm;
	private AttendanceManager _atm;

	private static final Command CMD_COMPUTE = new Command("Compute", Command.CANCEL, 1);
	
	public PerSubAttnMenu(AttendanceApp p) {
		super(p);

		_ttm = new TimeTableManager();
		_atm = new AttendanceManager();

		_ttm.loadSubjects();
		_atm.loadSubAttn();

		_list = new List("Choose Subjects", Choice.MULTIPLE);
		Vector subj = _ttm.getSubjects();
		for(int i=0; i< subj.size(); i++ ) {
			String s = (String)subj.elementAt(i);
			_list.append(s, null);
		}

		_list.addCommand( CMD_BACK );
		_list.addCommand( CMD_COMPUTE );
		_list.setCommandListener(this);

		_display.setCurrent(_list);
	}
	
	public void commandAction(Command c, Displayable d) {
		if( c == CMD_BACK ) {
			_parent.getSM().changeState(new AttendanceMenu(_parent));
		}
		else if ( c == CMD_COMPUTE ) {
			createAlertBox();
		}
	}

	private void createAlertBox() {
		//Create alert string
		StringBuffer sb = new StringBuffer();

		int totAttended = 0;
		int totTotal = 0;
		Vector subAttnVec = _atm.getSubAttn();
		for( int i=0; i<subAttnVec.size(); i++) {
			if( _list.isSelected(i) ) {
				String s = _ttm.getSubject(i);
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
