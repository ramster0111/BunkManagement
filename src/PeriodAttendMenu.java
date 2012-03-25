import core.AttendanceManager;
import core.AttendanceRecord;
import core.Period;
import core.Day;
import core.TimeTableManager;
import java.util.*;
import javax.microedition.lcdui.*;

public class PeriodAttendMenu extends AppState implements CommandListener {
	private AttendanceManager _atm;
	private TimeTableManager _ttm;
	private Vector _periods;
	private List _list;
	private int _state;
	private int _formState;
	private PeriodAttendForm _paf;
	private AttendanceRecord _ar;

	private static final int STATE_NORMAL = 0;
	private static final int STATE_FORM = 1;
	private static final int FORM_ADD = 0;
	private static final int FORM_EDIT = 1;

	public PeriodAttendMenu(AttendanceApp p, Date date) {
		super(p);
		_atm = new AttendanceManager();
		_atm.loadAttendance();

		_ttm = new TimeTableManager();
		_ttm.load();

		//Calculate dayIndex
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int dayType = c.get(Calendar.DAY_OF_WEEK);
		
		Day d = _ttm.getDay(dayType);
		if( d == null ) {
			_parent.getSM().changeState( new AttendClassMenu(_parent));
			return;
		}
		_periods = d.periods;

		_list = new List("Attend Period", Choice.IMPLICIT);
		//Init Commands
		_list.addCommand(CMD_SAVE);
		_list.addCommand(CMD_CANCEL);
		_list.setCommandListener(this);

		_display.setCurrent(_list);
		_state = STATE_NORMAL;
		
		//Get the correct attendance record
		_ar = _atm.getAttnRecord(date);
		_formState = FORM_EDIT;
		
		//If not present create one.
		if( _ar == null ) {
			_ar = new AttendanceRecord();
			_ar.date = date;
			_ar.setDefault();
			_atm.getAttendance().addElement(_ar);
			_formState = FORM_ADD;
			//System.out.println("Form add");
		}
		
		initList();
	}

	private void initList() {
		//Init the list
		_list.deleteAll();
		for( int i=0; i<_periods.size(); i++ )
		{
			String status = _ar.getAttnSatus(i);
			Period p = (Period)_periods.elementAt(i);
			String s = (String)_ttm.getSubjects().elementAt(p.subject);
			_list.append(s + "  -  " + status, null);
		}
	}

	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND )
		{
			int index = _list.getSelectedIndex();
			int status = _ar.getAttn(index);
			_paf = new PeriodAttendForm(status);
			_paf.addCommand(CMD_SAVE);
			_paf.addCommand(CMD_CANCEL);
			_paf.setCommandListener(this);

			_display.setCurrent(_paf);
			_state = STATE_FORM;
		}

		
		else if( c == CMD_SAVE && _state == STATE_NORMAL) {
			_atm.save();
			_parent.getSM().changeState(new AttendClassMenu(_parent));
		}
		else if( c == CMD_CANCEL && _state == STATE_NORMAL) {
			_parent.getSM().changeState(new AttendClassMenu(_parent));
		}
		
		else if( c == CMD_SAVE &&  _state == STATE_FORM ) {
			int index = _list.getSelectedIndex();
			Integer st = new Integer( _paf.getStatus() );

			if( _formState == FORM_ADD ) {
				_ar.attn.addElement(st);
			}
			else if ( _formState == FORM_EDIT ) {
				_ar.attn.setElementAt(st, index);
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
}
