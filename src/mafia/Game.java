package mafia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
  private ArrayList<ServerThread> serverThreads;
  private String[] jobs = {"시민", "마피아", "의사"};
  private int[] jobCount = {4, 2, 1};
  private List<User> users = new ArrayList<>();
  private DayNight dayNight = DayNight.DAY;

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
      users.add(new User(serverThreads.get(i).name, jobs[num]));
    }
  }
}
