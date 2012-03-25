import javax.microedition.lcdui.*;

public class MainMenu extends AppState implements CommandListener {
	public List _list;
	
	private static final Command CMD_EXIT = new Command("Exit", Command.EXIT, 1);
	private static final Command CMD_ABOUT = new Command("About", Command.BACK, 1);
	
	public MainMenu(AttendanceApp p) {
		super(p);
		_list = new List("Main Menu", Choice.IMPLICIT);
		_list.append("Mark Attendance", null);
		_list.append("Subjects", null);
		_list.append("Time Table", null);
		_list.append("Attendance", null);
		
		_list.setCommandListener(this);
		_list.addCommand( CMD_EXIT );
		_list.addCommand( CMD_ABOUT );
		
		_display.setCurrent(_list);
	}
	
	public void commandAction(Command c, Displayable d) {
		if( c == List.SELECT_COMMAND ) {
			int index = _list.getSelectedIndex();
			switch( index ) {
				case 0 :
					_parent.getSM().changeState( new AttendClassMenu(_parent));
					break;
				case 1 :
					_parent.getSM().changeState( new SubjectMenu(_parent));
					break;
				case 2 :
					_parent.getSM().changeState( new TimeTableMenu(_parent));
					break;
				case 3 :
					_parent.getSM().changeState( new AttendanceMenu(_parent));
					break;
			}
		}
		else if( c == CMD_EXIT ) {
			_parent.notifyDestroyed();
		}
		else if( c == CMD_ABOUT ) {
			createAlertBox();
		}
	}

	private void createAlertBox() {
		StringBuffer sb = new StringBuffer();
		sb.append("Created by Raminder Jeet\n (+91-9535615919)\n\n");
		sb.append("rjssodhi@gmail.com :-)");

		Alert a = new Alert("About", sb.toString(), null, AlertType.INFO );
		a.setTimeout(Alert.FOREVER);
		_display.setCurrent(a, _list);
	}
}