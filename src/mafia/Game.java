package mafia;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class Game extends Thread {
  private ArrayList<ServerThread> serverThreads;
  private String[] jobs = {"시민", "마피아", "의사", "경찰"};
  private int[] jobCount = {1, 1, 1, 1};
  private List<User> users = new ArrayList<>();
  DayNight dayNight = DayNight.HEAL;
  private Boolean gameFlag = true;
  private String[] voteName;
  private int[] voteCount;
  private String mafiaPick = "";
  private String doctorPick = " ";
  private int policeSkillPoint = 1;

  public Game(ArrayList<ServerThread> list) {
    this.serverThreads = list;
    init();
  }

  public void skillPick(String pick, String skill, ServerThread s) {
    if (pick == null || pick.isEmpty()) {
      return;
    }
    if (skill.equals("kill")) {
      mafiaPick = pick;
      noticePersonal(s, "개인 : 당신은 " + mafiaPick + "님을 죽일겁니다.", "chat");
    }
    if (skill.equals("heal")) {
      doctorPick = pick;
      noticePersonal(s, "개인 : 당신은 " + doctorPick + "님을 살릴겁니다.", "chat");
    }
    if (skill.equals("police")) {
      if(policeSkillPoint == 1){
        policeSkillPoint--;
        for (int i = 0; i < users.size(); i++) {
          if (pick.split("#")[0].equals(users.get(i).name.split("#")[0])) {
            noticePersonal(s, "개인 : " + users.get(i).name + "님의 직업은 " + users.get(i).jab + "입니다.", "chat");
          }
        }
      }else {
        noticePersonal(s, "개인 : 오늘은 이미 조사를 시도해서 더 이상 조사를 할 수 없습니다.", "chat");
      }
    }
  }

  public void killProcessing() {
    if (mafiaPick.split("#")[0].equals(doctorPick.split("#")[0])) {
      notice("의사가 치료에 성공했습니다.", "chat");
    } else {
      for (int i = 0; i < users.size(); i++) {
        if (mafiaPick.split("#")[0].equals(users.get(i).name.split("#")[0])) {
          notice("간밤에 " + users.get(i).name + "님이 마피아에게 습격당해 죽었습니다.", "chat");
          notice(users.get(i).name + "님이 사망했습니다", "job");
          noticePersonal(serverThreads.get(i), "", "death");
          users.get(i).isDead = true;
          serverThreads.get(i).isDead = true;
        }
      }
    }
    mafiaPick = "";
    doctorPick = "";
  }


  public void voting(String voter, String pick) {
    int index = 9999;
    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).name.equals(voter)) {
        index = i;
      }
    }
    if (index == 9999) {
      return;
    }
    voteName[index] = pick;
    noticePersonal(serverThreads.get(index), "개인 : 당신은 " + voteName[index] + "에게 투표했습니다.", "chat");
    System.out.println(Arrays.toString(voteName));
  }
  public void displayMafiaImage() {
    SwingUtilities.invokeLater(() -> {
      JFrame mafiaFrame = new JFrame();
      mafiaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

      ImageIcon mafiaIcon = new ImageIcon();
      JLabel mafiaLabel = new JLabel(mafiaIcon);

      mafiaFrame.getContentPane().add(mafiaLabel);
      mafiaFrame.pack();
      mafiaFrame.setLocationRelativeTo(null);
      mafiaFrame.setVisible(true);
    });
  }

  public Boolean voteKill() {
    boolean isEmpty = true;

    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).isDead) continue;

      if (voteName[i] != null && !voteName[i].isEmpty()) {
        isEmpty = false;
        break;
      }
    }

    if (isEmpty) {
      notice("투표로 죽은 사람이 없습니다.", "chat");
      return true;
    }

    for (int i = 0; i < voteName.length; i++) {
      for (int j = 0; j < users.size(); j++) {
        if (voteName[i].split("#")[0].equals(users.get(j).name.split("#")[0])) {
          voteCount[j]++;
        }
      }
    }

    System.out.println(Arrays.toString(voteName));
    System.out.println(Arrays.toString(voteCount));

    int max = Arrays.stream(voteCount).max().orElse(9999);
    if (max == 9999 || max == 0) {
      return true;
    }

    int index = 9999;
    int duplicationCount = 0;
    for (int i = 0; i < voteCount.length; i++) {
      if (voteCount[i] == max) {
        duplicationCount++;
        index = i;
      }
    }

    if(duplicationCount>1){
      notice("최다 득표 인물이 2명 이상입니다. 다시 투표해 주세요", "chat");
      dayNight = DayNight.DAY;
      return false;
    }

    notice(users.get(index).name + "님이 투표로 죽었습니다.", "chat");
    notice(users.get(index).name + "님은 " + users.get(index).jab + "입니다.", "chat");
    users.get(index).isDead = true;
    serverThreads.get(index).isDead = true;
    noticePersonal(serverThreads.get(index), "", "death");

    for (int i = 0; i < users.size(); i++) {
      voteCount[i] = 0;
      voteName[i] = "";
    }

    return true;
  }

  public void init() {
    Random rand = new Random();
    for (int i = 0; i < serverThreads.size(); i++) {
      int num = rand.nextInt(jobs.length);
      ;
      while (jobCount[num] <= 0) {
        num = rand.nextInt(jobs.length); // 이제 변경 안해도 됨
      }
      jobCount[num]--;
      User u = new User(serverThreads.get(i).name, jobs[num]);
      users.add(u);
      noticePersonal(serverThreads.get(i), "개인 : 당신의 직업은 " + u.jab + "입니다.", "chat");
      noticePersonal(serverThreads.get(i), u.jab, "job");
    }

    voteName = new String[serverThreads.size()];
    voteCount = new int[serverThreads.size()];
    for (int i = 0; i < voteName.length; i++) {
      voteName[i] = "";
      voteCount[i] = 0;
    }
  }

  public void run() {
    timer();
    notice("", "member");
    notice("true", "isGameRun");
    checkFinish();
  }

  private void timer() {
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (gameFlag) {
          switch (dayNight) {
            case DAY:
              dayNight = DayNight.VOTE;
              notice("now Vote", "chat");
              notice("vote", "dayNight");
              break;
            case VOTE:
              if (voteKill() && !checkFinish()) {
                dayNight = DayNight.NIGHT;
                notice("now Night", "chat");
                notice("night", "dayNight");
              }
              break;
            case NIGHT:
              dayNight = DayNight.POLICE;
              policeSkillPoint = 1;
              notice("now Police", "chat");
              notice("police", "dayNight");
              break;
            case POLICE:
              dayNight = DayNight.HEAL;
              notice("now Heal", "chat");
              notice("heal", "dayNight");
              break;
            case HEAL:
              killProcessing();
              if (!checkFinish()) {
                dayNight = DayNight.DAY;
                notice("now Day", "chat");
                notice("day", "dayNight");
              }
              break;
          }
        }
      }
    }, 0, 20000);


    final int[] leftTime = {20};
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (gameFlag) {
          notice(String.valueOf(leftTime[0]--), "leftTime");
          if (leftTime[0] <= 0) {
            leftTime[0] = 20;
          }
        }
      }
    }, 0, 1000);

  }

  private Boolean checkFinish() {
    int citizen = 0;
    int enemy = 0;
    for (User u : users) {
      if (!u.isDead) {
        if (u.jab.equals("마피아")) {
          enemy++;
        } else {
          citizen++;
        }
      }
    }

    if (enemy == 0) {
      gameFlag = false;
      notice("citizen win", "chat");
      notice("false", "isGameRun");
      return true;
    } else if (citizen <= enemy) {
      gameFlag = false;
      notice("enemy win", "chat");
      notice("false", "isGameRun");
      return true;
    }

    return false;
  }

  public void notice(String str, String method) {
    StringBuilder names = new StringBuilder();
    StringBuilder deadNames = new StringBuilder();
    if (method.equals("member")) {
      for (ServerThread s : serverThreads) {
        names.append("[").append(s.name).append("] ");
      }
    }

    for (ServerThread s : serverThreads) {
      try {
        if (method.equals("chat")) {
          s.os.writeUTF("chat/전체 : " + str);
        }

        if (method.equals("member")) {
          s.os.writeUTF("member/" + names.toString());
        }

        if (method.equals("dayNight")) {
          s.os.writeUTF("dayNight/" + str);
        }

        if (method.equals("isGameRun")) {
          s.os.writeUTF("isGameRun/" + str);
        }

        if (method.equals("leftTime")) {
          s.os.writeUTF("leftTime/" + str);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private void noticePersonal(ServerThread s, String str, String method) {
    try {
      if (method.equals("chat")) {
        s.os.writeUTF("chat/" + str);
      }

      if (method.equals("death")) {
        s.os.writeUTF("death/" + str);
      }

      if (method.equals("job")) {
        s.os.writeUTF("job/" + str);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}