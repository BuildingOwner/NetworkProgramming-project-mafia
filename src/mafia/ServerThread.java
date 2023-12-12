package mafia;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class ServerThread extends Thread {
  Scanner scn = new Scanner(System.in);
  public String name;
  final DataInputStream is;
  final DataOutputStream os;
  Socket s;
  boolean active;

  public ServerThread(Socket s, String name, DataInputStream is, DataOutputStream os) {
    this.is = is;
    this.os = os;
    this.name = name;
    this.s = s;
    this.active = true;
  }

  @Override
  public void run() {
    String message;
    while (true) {
      try {
        message = is.readUTF();
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
              t.os.writeUTF("chat/" + this.name + " : " + msg[2]);
            }
          }
        }

        if(msg[1].equals("gameStart")){
          Server.startGame();
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
