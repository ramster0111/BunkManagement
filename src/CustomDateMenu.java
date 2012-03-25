import java.util.Date;
import javax.microedition.lcdui.*;

public class CustomDateMenu extends AppState implements CommandListener {
	private Form _form;
	private DateField _dateField;

	private static final Command CMD_ATTEND = new Command("Attend", Command.CANCEL, 1);
	
	public CustomDateMenu(AttendanceApp p) {
		super(p);

		
		_form = new Form("Attend Class");
		_dateField = new DateField("Date : ", DateField.DATE );
		Date d = new Date();
		_dateField.setDate(d);

		_form.append(_dateField);
		_form.setCommandListener(this);
		_form.addCommand( CMD_BACK );
		_form.addCommand( CMD_ATTEND );
		_display.setCurrent(_form);
	}


	public void commandAction(Command c, Displayable d) {
		if( c == CMD_BACK ) {
			_parent.getSM().changeState(new AttendClassMenu(_parent));
		}
		else if( c == CMD_ATTEND ) {
			_parent.getSM().changeState(new PeriodAttendMenu(_parent, _dateField.getDate()) );
		}
	}
}
