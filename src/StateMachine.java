public class StateMachine {
	private Object _prevState;
	private Object _currState;
	
	public StateMachine() {
		_prevState = null;
		_currState = null;
	}
	
	public void changeState(Object st) {
		_prevState = _currState;
		_currState = st;
	}
	
	public Object getPrevState() {
		return _prevState; 
	}
	
	public Object getCurrState() {
		return _currState;
	}
}
