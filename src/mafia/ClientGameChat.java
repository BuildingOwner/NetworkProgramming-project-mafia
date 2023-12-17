package mafia;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGameChat extends Thread {
  ClientGame clientGame;

  public ClientGameChat(ClientGame gameChat) {
    this.clientGame = gameChat;
  }

  @Override
  public void run() {
    while (true) {
      try {
        String message = clientGame.is.readUTF();
        String[] msg = message.split("/");

        if (msg[0].equals("chat")) {
          clientGame.gp.chatArea.append(new String(msg[1]) + "\n");
        }

        if (msg[0].equals("member")) {
          clientGame.gp.member.setText(new String(msg[1]));
          String nameProcessed = msg[1].replace("[","").replace("]","");
          clientGame.userNames = nameProcessed.split(" ");
          System.out.println(clientGame.userNames.toString());
        }

        if (msg[0].equals("job")) {
          clientGame.gp.jlabel.setText(new String(msg[1]));
        }


        if (msg[0].equals("dayNight")) {
          switch (msg[1]) {
            case "day":
              clientGame.now = DayNight.DAY;
              clientGame.gp.ment.setText("<html>시민 여러분, 지금은 낮입니다.<br>의심과 추론을 통해 진실을 밝혀내는 시간입니다.<br>채팅을 통해 대화할 수 있습니다.</html>");
              //clientGame.gp.setBackground(Color.DARK_GRAY);
              displayRoleImage("src/mafia/image/토론활동.png");
              break;
            case "vote":
              clientGame.now = DayNight.VOTE;
              clientGame.gp.ment.setText("<html>모든 플레이어들, 지금은 투표 시간입니다.<br>누구에게 의심의 눈길을 보낼까요?<br>투표 창에 지목할 사람의 이름을 입력하세요. 최다 투표 득점자는 처형!</html>");
              //clientGame.gp.setBackground(Color.DARK_GRAY);
              displayRoleImage("src/mafia/image/투표활동.png");
              break;
            case "night":
              clientGame.now = DayNight.NIGHT;
              clientGame.gp.ment.setText("<html>어둠이 내려오고, 밤이 시작됩니다.<br>마피아와 다른 역할들이 행동할 시간입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>");
              //clientGame.gp.setBackground(Color.BLACK);
              displayRoleImage("src/mafia/image/마피아활동.png");
              break;
            case "police":
              clientGame.now = DayNight.POLICE;
              clientGame.gp.ment.setText("<html>마피아의 정체를 파헤치기 위해 조사를 진행하고,<br>도시의 안전을 위해 헌신하는 경찰관입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>");
              //clientGame.gp.setBackground(Color.BLUE);
              displayRoleImage("src/mafia/image/경찰활동.png");
              break;
            case "heal":
              clientGame.now = DayNight.HEAL;
              clientGame.gp.ment.setText("<html>마피아의 공격으로부터 플레이어들을 보호하고,<br>생존을 위해 노력하는 의지 강한 의사입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>");
              //clientGame.gp.setBackground(Color.RED);
              displayRoleImage("src/mafia/image/의사활동.png");
              break;
          }
          clientGame.gp.chat.setEnabled(clientGame.now == DayNight.DAY || clientGame.isDead);
          clientGame.gp.vote.setEnabled(
              (!clientGame.isDead && clientGame.now == DayNight.VOTE) ||
                  (!clientGame.isDead && clientGame.job.equals("마피아") && clientGame.now == DayNight.NIGHT) ||
                  (!clientGame.isDead && clientGame.job.equals("의사") && clientGame.now == DayNight.HEAL) ||
                  (!clientGame.isDead && clientGame.job.equals("경찰") && clientGame.now == DayNight.POLICE)
          );
          clientGame.gp.repaint();
        }

        if (msg[0].equals("death")) {
          clientGame.isDead = true;
        }
        if (msg[0].equals("isGameRun")) {
          switch (msg[1]) {
            case "true":
              clientGame.gameFlag = true;
              clientGame.gp.startBtn.setEnabled(false);
              break;
            case "false":
              clientGame.gameFlag = false;
              break;
          }
        }

        if (msg[0].equals("leftTime")) {
          clientGame.gp.time.setText(msg[1]);
        }

        if (msg[0].equals("job")) {
          clientGame.job = msg[1];
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void displayRoleImage(String s) {
    ImageIcon roleIcon = new ImageIcon(s);
    Image roleImage = roleIcon.getImage();
    Image newRoleImage = roleImage.getScaledInstance(450, 300, Image.SCALE_SMOOTH);
    roleIcon = new ImageIcon(newRoleImage);

    clientGame.gp.roleImageLabel.setIcon(roleIcon);
  }
}