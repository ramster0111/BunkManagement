import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Command;

public class AppState {
	protected AttendanceApp _parent;
	protected Display _display;

	protected static final Command CMD_BACK = new Command("Back", Command.BACK, 1);
	protected static final Command CMD_ADD  = new Command("Add", Command.SCREEN, 1);
	protected static final Command CMD_EDIT = new Command("Edit", Command.SCREEN, 1);
	protected static final Command CMD_DEL  = new Command("Delete", Command.SCREEN, 1);
	protected static final Command CMD_CANCEL  = new Command("Cancel", Command.CANCEL, 1);
	protected static final Command CMD_SAVE = new Command("Save", Command.BACK, 1);
	
	public AppState(AttendanceApp p) {
		_parent = p;
		_display = Display.getDisplay(p);
	}
} 
