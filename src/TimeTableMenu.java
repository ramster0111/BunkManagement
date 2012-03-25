import core.Day;
import core.TimeTableManager;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.lcdui.*;
import java.util.Vector;

public class TimeTableMenu extends AppState implements CommandListener {
	private TimeTableManager _ttm;
	private List _list;
	private Vector _days;
	private int _state;
	private DayForm _df;

	private static final int STATE_NORMAL = 0;
	private static final int STATE_FORM = 1;
	
	public TimeTableMenu(AttendanceApp p) {
		super(p);

		_ttm = new TimeTableManager();
		_ttm.loadTimeTable();
		_days = _ttm.getDays();

		//Init Commands
		_list = new List("Days", Choice.IMPLICIT);
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
		_list.deleteAll();
		//System.out.print(_days.size());
		for( int i=0; i<_days.size(); i++ )
		{
			Day d = (Day)_days.elementAt(i);
			String s = Day.getDay(d.day);
			_list.append(s, null);
		}
		setDefaultIndex();
	}
	
	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND )
		{
			_ttm.saveTimeTable();
			int index = _list.getSelectedIndex();
			_parent.getSM().changeState( new DayMenu(_parent, index));
		}

		else if ( c == CMD_ADD )
		{
			_df = new DayForm(-1, DayForm.TYPE_ADD);
			_df.addCommand(CMD_SAVE);
			_df.addCommand(CMD_CANCEL);
			_df.setCommandListener(this);

			_display.setCurrent(_df);
			_state = STATE_FORM;
		}
		else if ( c == CMD_EDIT )
		{
			int index = _list.getSelectedIndex();
			Day day = (Day)_days.elementAt(index);
			int dayNum = day.day;

			_df = new DayForm(dayNum, DayForm.TYPE_EDIT);
			_df.addCommand(CMD_SAVE);
			_df.addCommand(CMD_CANCEL);
			_df.setCommandListener(this);

			_display.setCurrent(_df);
			_state = STATE_FORM;
		}
		else if ( c == CMD_DEL )
		{
			int index = _list.getSelectedIndex();
			_days.removeElementAt(index);
			initList();
		}
		else if( c == CMD_BACK && _state == STATE_NORMAL) {
			_ttm.saveTimeTable();
			_parent.getSM().changeState(new MainMenu(_parent));
		}
		else if( c == CMD_SAVE &&  _state == STATE_FORM ) {
			if( _df.getType() == DayForm.TYPE_ADD ) {
				Day day = new Day();
				day.day = _df.getDay();
				
				_days.addElement( day );
			}
			else if ( _df.getType() == DayForm.TYPE_EDIT ) {
				int index = _list.getSelectedIndex();
				Day oldDay = (Day)_days.elementAt(index);
				
				Day day = new Day();
				day.day = _df.getDay();
				day.periods = oldDay.periods;

				_days.setElementAt(day, index);
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

	private void setDefaultIndex() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		int day = c.get(Calendar.DAY_OF_WEEK);
		for( int i=0; i<_days.size(); i++ ) {
			Day d = (Day)_days.elementAt(i);
			if( d.day == day ) {
				_list.setSelectedIndex(i, true);
				return;
			}
		}
	}
}
