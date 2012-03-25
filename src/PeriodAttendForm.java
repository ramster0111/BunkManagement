import javax.microedition.lcdui.*;

public class PeriodAttendForm extends Form{
	private ChoiceGroup _status;

	public PeriodAttendForm(int st) {
		super("Period Status");
		_status = new ChoiceGroup( "Staus : ", Choice.POPUP );
		
		_status.append("Absent", null);
		_status.append("Present", null);
		_status.append("Canceled", null);

		st = (st == -1) ? 2 : st;
		_status.setSelectedIndex(st, true);
		
		append(_status);
	}

	int getStatus() {
		int index = _status.getSelectedIndex();
		if( index >= 2 ) {
			return -1;
		}
		return index;
	}
}
