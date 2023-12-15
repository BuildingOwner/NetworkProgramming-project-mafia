package mafia;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

class ServerThread extends Thread {

  public String name;
  final DataInputStream is;
  final DataOutputStream os;
  Socket s;
  boolean active;
  public Boolean isDead = false;

  public ServerThread(Socket s, String name, DataInputStream is, DataOutputStream os) {
    this.is = is;
    this.os = os;
    this.name = name;
    this.s = s;
    this.active = true;
    try {
      os.writeUTF("name/" + name);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run() {
    String message;
    while (true) {
      try {
        message = is.readUTF(); // 이름/메소드/내용 형식
        System.out.println(message);
        String[] msg = message.split("/");
        if (msg[1].equals("logout")) {
          this.active = false;
          this.s.close();
          break;
        }

        if (msg[1].equals("chat")) {
          for (ServerThread t : Server.list) {
            if (!t.name.equals(msg[0])) {
              if (msg[3].equals("death")) {
                if (t.isDead) {
                  t.os.writeUTF("chat/" + this.name + " (사망자) : " + msg[2]);
                }
              } else {
                t.os.writeUTF("chat/" + this.name + " (생존자) : " + msg[2]);
              }
            }
          }
        }

        if (msg[1].equals("gameStart")) {
          Server.startGame();
        }

        if (msg[1].equals("vote")) {
          Server.game.voting(msg[0], msg[2]);
        }

        if (msg[1].equals("kill")) {
          Server.game.skillPick(msg[2], "kill", this);
        }

        if (msg[1].equals("heal")) {
          Server.game.skillPick(msg[2], "heal", this);
        }

      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
    }
    try {
      this.is.close();
      this.os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}
