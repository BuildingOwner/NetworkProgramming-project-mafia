package mafia;

import java.io.IOException;
import java.util.*;

public class Game extends Thread {
  private ArrayList<ServerThread> serverThreads;
  private String[] jobs = {"시민", "시민2","마피아","의사","경찰"}; //역할 추가 H
  private int[] jobCount = {1, 1, 1, 1, 1}; //5개 H
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
      while (jobCount[num] <= 0) {
        num = rand.nextInt(5); //5로 변경 H
      }
      jobCount[num]--;
      User u = new User(serverThreads.get(i).name, jobs[num]);
      users.add(u);
      noticePersonal(serverThreads.get(i), "당신의 직업은 " + u.jab + "입니다.");
    }
  }

  public void run() {
    timer();
    notice("", "member");
    while (gameFlag) {
      checkFinish();
      if (dayNight == DayNight.DAY) {

      }
    }
  }

  private void timer() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        switch (dayNight) {
          case DAY:
            dayNight = DayNight.NIGHT;
            notice("now Day", "chat");
            break;
          case NIGHT:
            dayNight = DayNight.DAY;
            notice("now Night", "chat");
            break;
        }
      }
    }, 0, 30000);
  }

  private void checkFinish() {
    int citizen = 0;
    int enemy = 0;
    for (User u : users) {
      if (u.jab.equals("시민") || u.jab.equals("의사")) {
        citizen++;
      } else {
        enemy++;
      }
    }
    if (enemy == 0) {
      gameFlag = false;
      notice("citizen win", "chat");
    } else if (citizen <= enemy) {
      gameFlag = false;
      notice("enemy win", "chat");
    }
  }

  private void notice(String str, String method) {
    StringBuilder names = new StringBuilder();
    if (method.equals("member")) {
      for (ServerThread s : serverThreads) {
        names.append("[").append(s.name).append("] ");
      }
    }
    for (ServerThread s : serverThreads) {
      try {
        if (method.equals("chat")) {
          s.os.writeUTF("chat/사회자 : " + str);
        } else if (method.equals("member")) {
          s.os.writeUTF("member/" + names.toString());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void noticePersonal(ServerThread s, String str) {
    try {
      s.os.writeUTF("chat/" + str);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}