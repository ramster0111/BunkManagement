import core.Day;
import javax.microedition.lcdui.*;






public class DayForm extends Form{
	private ChoiceGroup _days;
	private int _type;

	public final static int TYPE_ADD = 0;
	public final static int TYPE_EDIT = 1;

	// d - default day
	public DayForm(int d, int type) {
		super("Day");
		_type = type;
		_days =  new ChoiceGroup("Day Name : ", Choice.POPUP);

		for(int i=1;i<=7; i++) {
			_days.append( Day.getDay(i), null );
		}
		//1 - Sunday ... 7 - Saturday
		d = (d <= 0) ? 0 : d-1;
		_days.setSelectedIndex(d, true);
		
		append( _days );
	}

	int getDay() {
		//The +1 is because Sunday is stored at pos 0 and Calendar.SUNDAY's is 1.
		//So the +1.
		return _days.getSelectedIndex()+1;
	}

	int getType() {
		return _type;
	}
}
