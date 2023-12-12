package mafia;

import java.io.IOException;
import java.util.*;

public class Game extends Thread {
  private ArrayList<ServerThread> serverThreads;
  private String[] jobs = {"시민", "마피아", "의사"};
  private int[] jobCount = {1, 1, 1};
  private List<User> users = new ArrayList<>();
  private DayNight dayNight = DayNight.NIGHT;
  private Boolean gameFlag = true;

  public Game(ArrayList<ServerThread> list) {
    this.serverThreads = list;
    init();
  }

  public void init() {
    Random rand = new Random();
    for (int i = 0; i < serverThreads.size(); i++) {
      int num = 0;
      while (jobCount[num]<=0){
        num = rand.nextInt(3);
      }
      jobCount[num]--;
      users.add(new User(serverThreads.get(i).name, jobs[num]));
    }
  }

   public void run(){
    timer();
    while (gameFlag){
      checkFinish();
      if(dayNight == DayNight.DAY){

      }
    }
  }

  private void timer(){
    Timer timer = new Timer();
    timer.schedule(new TimerTask(){
      @Override
      public void run() {
        switch(dayNight) {
          case DAY:
            dayNight = DayNight.NIGHT;
            notice("now Day");
            break;
          case NIGHT:
            dayNight = DayNight.DAY;
            notice("now Night");
            break;
        }
      }
    }, 0, 30000);
  }

  private void checkFinish(){
    int citizen = 0;
    int enemy = 0;
    for(User u : users){
      if(u.jab.equals("시민") || u.jab.equals("의사")){
        citizen++;
      }
      else {
        enemy++;
      }
    }
    if(enemy == 0){
      gameFlag = false;
      notice("citizen win");
    } else if (citizen <= enemy) {
      gameFlag = false;
      notice("enemy win");
    }
  }

  private void notice(String str){
    for(ServerThread s : serverThreads){
      try {
        s.os.writeUTF("chat/Server : " + str);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
