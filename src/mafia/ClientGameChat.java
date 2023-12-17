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
              //clientGame.gp.setBackground(Color.DARK_GRAY);
              break;
            case "vote":
              clientGame.now = DayNight.VOTE;
              //clientGame.gp.setBackground(Color.DARK_GRAY);
              break;
            case "night":
              clientGame.now = DayNight.NIGHT;
              //clientGame.gp.setBackground(Color.BLACK);
              displayRoleImage("src/mafia/image/mafia.png");
              break;
            case "police":
              clientGame.now = DayNight.POLICE;
              //clientGame.gp.setBackground(Color.BLUE);
              displayRoleImage("src/mafia/image/police.png");
              break;
            case "heal":
              clientGame.now = DayNight.HEAL;
              //clientGame.gp.setBackground(Color.RED);
              displayRoleImage("src/mafia/image/doctor.png");
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
    Image newRoleImage = roleImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    roleIcon = new ImageIcon(newRoleImage);

    clientGame.gp.roleImageLabel.setIcon(roleIcon);
  }
}