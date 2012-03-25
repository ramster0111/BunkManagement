import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

public class AttendanceApp extends MIDlet {
	StateMachine sm;
	
	protected Display display;
	private Image splashLogo;
	private boolean isSplash = true;

	//Member functions
	public AttendanceApp() {
        sm = new StateMachine();
	}

	public void startApp() {
		 display = Display.getDisplay(this);
		
		 if(isSplash) {
		      isSplash = false;
		      try {
		        splashLogo = Image.createImage("/logo2.png");
		        new Logo(display, this, splashLogo,3000);
		      } catch(Exception ex) {
		        gotoMainMenu();
		      }
		 }
	}
	
	public void gotoMainMenu(){
   	  MainMenu mainMenu = new MainMenu(this);
		  sm.changeState( mainMenu );
		
	}
	public void pauseApp() {
	}

	public void destroyApp(boolean unconditional) {
	}

	StateMachine getSM() {
		return sm;
	}
}
