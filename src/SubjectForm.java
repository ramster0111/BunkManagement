import javax.microedition.lcdui.*;

public class SubjectForm extends Form {
	private TextField _name;
	private int _type;

	public final static int TYPE_ADD = 0;
	public final static int TYPE_EDIT = 1;

	public SubjectForm(String n, int type) {
		super("Subject");
		_type = type;
		_name =  new TextField("Subject Name : ", n, 20, TextField.ANY);

		append( _name );
	}
	
	String getName() {
		return _name.getString();
	}
	
	int getType() {
		return _type;
	}
}
