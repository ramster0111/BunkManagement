import javax.microedition.lcdui.*;
import java.util.*;

public class PeriodForm extends Form{
	private ChoiceGroup _subject;
	private DateField _st;
	private TextField _dur;
	private int _type;

	public final static int TYPE_ADD = 0;
	public final static int TYPE_EDIT = 1;

	public PeriodForm(Vector s, int defaultSub, long st, int dur, int type) {
		super("Period");
		_type = type;
		_subject =  new ChoiceGroup("Subject : ", Choice.POPUP);
		for( int i=0; i<s.size(); i++ ) {
			_subject.append( (String)s.elementAt(i), null );
		}
		_subject.setSelectedIndex(defaultSub, true);

		_st = new DateField("Start Time : ", DateField.TIME );
		_st.setDate( new Date(st));
		_dur = new TextField("Duration (minutes): ", Integer.toString(dur), 3, TextField.NUMERIC );

		append( _subject );
		append( _st );
		append( _dur );
	}

	int getSubject() {
		return _subject.getSelectedIndex();
	}

	long getSt() {
		return _st.getDate().getTime();
	}

	int getDur() {
		String s = _dur.getString();
		return Integer.parseInt(s);
	}

	int getType() {
		return _type;
	}
}

