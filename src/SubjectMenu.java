import core.AttendanceManager;
import core.SubjectAttnRecord;
import core.TimeTableManager;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class SubjectMenu extends AppState implements CommandListener {
	private TimeTableManager _ttm;
	private Vector _subjects;
	private List _list;
	private int _state;
	private SubjectForm _sf;

	private static final int STATE_NORMAL = 0;
	private static final int STATE_FORM = 1;
	
	public SubjectMenu(AttendanceApp p) {
		super(p);
		_ttm = new TimeTableManager();
		_ttm.loadSubjects();
		_subjects = _ttm.getSubjects();

		_list = new List("Subjects", Choice.IMPLICIT);
		//Init Commands
		_list.addCommand(CMD_BACK);
		_list.addCommand(CMD_ADD);
		_list.addCommand(CMD_EDIT);
		_list.addCommand(CMD_DEL);
		_list.setCommandListener(this);
		
		initList();
		_display.setCurrent(_list);
		_state = STATE_NORMAL;
	}
	
	private void initList() {
		//Init the list
		_list.deleteAll();
		for( int i=0; i<_subjects.size(); i++ )
		{
			String s = (String)_subjects.elementAt(i);
			_list.append(s, null);
		}
	}
	
	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND ) 
		{
			//Show the attendance
			int index = _list.getSelectedIndex();
			createAlertBox(index);
		}
		
		else if ( c == CMD_ADD )
		{
			_sf = new SubjectForm("Add name", SubjectForm.TYPE_ADD);
			_sf.addCommand(CMD_SAVE);
			_sf.addCommand(CMD_CANCEL);
			_sf.setCommandListener(this);
			
			_display.setCurrent(_sf);
			_state = STATE_FORM;
		}
		else if ( c == CMD_EDIT )
		{
			int index = _list.getSelectedIndex();
			String s = (String)_subjects.elementAt(index);
			_sf = new SubjectForm(s, SubjectForm.TYPE_EDIT);
			_sf.addCommand(CMD_SAVE);
			_sf.addCommand(CMD_CANCEL);
			_sf.setCommandListener(this);

			_display.setCurrent(_sf);
			_state = STATE_FORM;
		}
		else if ( c == CMD_DEL )
		{
			int index = _list.getSelectedIndex();
			_subjects.removeElementAt(index);
			initList();
		}
		else if( c == CMD_BACK && _state == STATE_NORMAL) {
			_ttm.saveSubjects();
			_parent.getSM().changeState(new MainMenu(_parent));
		}
		else if( c == CMD_SAVE &&  _state == STATE_FORM ) {
			if( _sf.getType() == SubjectForm.TYPE_ADD ) {
				_subjects.addElement( _sf.getName() );
			}
			else if ( _sf.getType() == SubjectForm.TYPE_EDIT ) {
				String s = _sf.getName();
				int index = _list.getSelectedIndex();
				_subjects.setElementAt(s, index);
			}
			_state = STATE_NORMAL;
			initList();
			_display.setCurrent(_list);
		}
		else if( c == CMD_CANCEL && _state == STATE_FORM ) {
			_state = STATE_NORMAL;
			_display.setCurrent(_list);
		}
	}

	private void createAlertBox(int subId) {
		AttendanceManager atm = new AttendanceManager();
		atm.loadSubAttn();

		String subject = (String)_subjects.elementAt(subId);
		SubjectAttnRecord sar = atm.getSubAttn(subId);

		StringBuffer sb = new StringBuffer();
		sb.append( "Attendance : " );
		sb.append( sar.attended );
		sb.append( "/" );
		sb.append( sar.total );
		sb.append( " - " );
		sb.append( sar.getPer() );
		sb.append( "%" );

		Alert alert = new Alert(subject, sb.toString(), null, AlertType.INFO );
		alert.setTimeout(Alert.FOREVER);
		_display.setCurrent(alert, _list);
	}
}
		
		
		
