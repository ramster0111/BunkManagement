import java.util.Date;
import javax.microedition.lcdui.*;

public class AttendClassMenu extends AppState implements CommandListener {
	private List _list;
	
	public AttendClassMenu(AttendanceApp p) {
		super(p);

		_list = new List("Attend Class", Choice.IMPLICIT );
		_list.addCommand(CMD_BACK);
		_list.setCommandListener(this);

		_list.append("Today", null);
		_list.append("Custom Day", null );

		_display.setCurrent(_list);
	}

	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND ) {
			int index = _list.getSelectedIndex();
			switch ( index ) {
				case 0 :
					_parent.getSM().changeState(new PeriodAttendMenu(_parent, new Date()) );
					break;
				case 1 :
					_parent.getSM().changeState(new CustomDateMenu(_parent) );
					break;
			}
		}
		else if( c == CMD_BACK ) {
			_parent.getSM().changeState(new MainMenu(_parent));
		}
	}
}
		