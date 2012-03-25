
import java.util.Timer;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public final class Logo extends Canvas {
  private Display display;
 ;
  private Timer timer;
  private Image image;    
  private int dismissTime;
  private AttendanceApp M;
  public Logo(Display display,AttendanceApp m, Image image,int dismissTime) {
    timer = new Timer();
    this.display = display;
    this.M=m;
    this.image = image;        
    this.dismissTime = dismissTime;
    display.setCurrent(this);
  }

  static void access( Logo logo ) {
	    logo.dismiss();
	  }

  private void dismiss() {
    timer.cancel();
    M.gotoMainMenu();
    return;
    
  }

  protected void keyPressed(int keyCode) {
    dismiss();
  }

  protected void paint(Graphics g) {
    g.setColor(0x00FFFFFF);    
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setColor(0x00000000);        
    g.drawImage(image, getWidth() / 2, getHeight() / 2 - 5, 3);        
  }

  protected void pointerPressed(int x, int y) {
    dismiss();
  }

  protected void showNotify() {
    if(dismissTime > 0)
      timer.schedule(new CountDown(this), dismissTime);

  }
}
