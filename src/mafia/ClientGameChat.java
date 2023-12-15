package mafia;

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
        }

        if (msg[0].equals("dayNight")) {
          switch (msg[1]) {
            case "day":
              clientGame.now = DayNight.DAY;
              break;
            case "vote":
              clientGame.now = DayNight.VOTE;
              break;
            case "night":
              clientGame.now = DayNight.NIGHT;
              break;
            case "heal":
              clientGame.now = DayNight.HEAL;
              break;
          }
          clientGame.gp.chat.setEnabled(clientGame.now == DayNight.DAY || clientGame.isDead);
          clientGame.gp.vote.setEnabled(
              (!clientGame.isDead && clientGame.now == DayNight.VOTE) ||
              (clientGame.job.equals("마피아") && clientGame.now == DayNight.NIGHT) ||
              (clientGame.job.equals("의사") && clientGame.now == DayNight.HEAL)
          );
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
}
