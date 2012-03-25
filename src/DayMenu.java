import core.AttendanceManager;
import core.Period;
import core.Day;
import core.SubjectAttnRecord;
import core.TimeTableManager;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class DayMenu extends AppState implements CommandListener {
	private TimeTableManager _ttm;
	private Vector _periods;
	private List _list;
	private int _state;
	private PeriodForm _pf;

	private static final int STATE_NORMAL = 0;
	private static final int STATE_FORM = 1;

	public DayMenu(AttendanceApp p, int dayIndex) {
		super(p);
		_ttm = new TimeTableManager();

		_ttm.loadTimeTable();
		_ttm.loadSubjects();
		Vector days = _ttm.getDays();
		Day d = (Day) days.elementAt(dayIndex);
		_periods = d.periods;

		_list = new List("Periods", Choice.IMPLICIT);
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
		for( int i=0; i<_periods.size(); i++ )
		{
			Period p = (Period)_periods.elementAt(i);
			String s = _ttm.getSubject(p.subject);
			String periodTime = p.getSt();
			_list.append(s + " - " + periodTime, null);
		}
		setDefaultIndex();
	}

	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND )
		{
 			int index = _list.getSelectedIndex();
			createAlertBox(index);
		}

		else if ( c == CMD_ADD )
		{
			_pf = new PeriodForm(_ttm.getSubjects(), 0, 0, 0, SubjectForm.TYPE_ADD);
			_pf.addCommand(CMD_SAVE);
			_pf.addCommand(CMD_CANCEL);
			_pf.setCommandListener(this);

			_display.setCurrent(_pf);
			_state = STATE_FORM;
		}
		else if ( c == CMD_EDIT )
		{
			int index = _list.getSelectedIndex();
			Period p = (Period)_periods.elementAt(index);
			_pf = new PeriodForm(_ttm.getSubjects(), p.subject, p.startTime, p.duration, SubjectForm.TYPE_EDIT);
			_pf.addCommand(CMD_SAVE);
			_pf.addCommand(CMD_CANCEL);
			_pf.setCommandListener(this);

			_display.setCurrent(_pf);
			_state = STATE_FORM;
		}
		else if ( c == CMD_DEL )
		{
			int index = _list.getSelectedIndex();
			_periods.removeElementAt(index);
			initList();
		}
		else if( c == CMD_BACK && _state == STATE_NORMAL) {
			_ttm.saveTimeTable();
			_parent.getSM().changeState(new TimeTableMenu(_parent));
		}
		else if( c == CMD_SAVE &&  _state == STATE_FORM ) {
			if( _pf.getType() == DayForm.TYPE_ADD ) {
				Period p = new Period();
				p.startTime = _pf.getSt();
				p.duration = _pf.getDur();
				p.subject = _pf.getSubject();

				_periods.addElement( p );
			}
			else if ( _pf.getType() == DayForm.TYPE_EDIT ) {
				Period p = new Period();
				p.startTime = _pf.getSt();
				p.duration = _pf.getDur();
				p.subject = _pf.getSubject();
				
				int index = _list.getSelectedIndex();
				_periods.setElementAt(p, index);
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

	private void createAlertBox(int periodIndex) {
		AttendanceManager atm = new AttendanceManager();
		atm.loadSubAttn();

		Period p = (Period)_periods.elementAt(periodIndex);
		String subject = _ttm.getSubject(p.subject);
		SubjectAttnRecord sar = atm.getSubAttn(p.subject);

		StringBuffer sb = new StringBuffer();
		sb.append(subject + "\n");
		sb.append(p.getSt() + " - " + p.getEt() + "\n");
		sb.append("Duration : " + Integer.toString(p.duration) + " minutes \n");


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

	private void setDefaultIndex() {
		Date date = new Date();

		for(int i=0; i<_periods.size(); i++) {
			Period p = (Period)_periods.elementAt(i);

			Date startDate = new Date();
			startDate.setTime(p.startTime);
			if( dateLessThan(startDate, date) == true ) {
				Date endDate = new Date();
				endDate.setTime(p.getEndTime());
				if( dateLessThan(date, endDate) == true ) {
					_list.setSelectedIndex(i, true);
					return;
				}
			}
		}
	}

	private static boolean dateLessThan(Date a, Date b) {
		Calendar ca = Calendar.getInstance();
		Calendar cb = Calendar.getInstance();
		ca.setTime(a);
		cb.setTime(b);

		int ta = (ca.get(Calendar.HOUR_OF_DAY)*60*60) + (ca.get(Calendar.MINUTE)*60) + ca.get(Calendar.SECOND);
		int tb = (cb.get(Calendar.HOUR_OF_DAY)*60*60) + (cb.get(Calendar.MINUTE)*60) + cb.get(Calendar.SECOND);
		return ta < tb;

		//System.out
	}
}
