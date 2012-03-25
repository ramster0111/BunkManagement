import java.util.TimerTask;


class CountDown extends TimerTask {
  private final Logo logo;

  CountDown(Logo logo) {
    this.logo = logo;
  }

  public void run() {
    Logo.access(this.logo);
  }
  
}