package mafia;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class GameChat {
  private final static int ServerPort = 5000;
  public DataInputStream is;
  public DataOutputStream os;
  public String name;

  public GameChat(JFrame frame, String name) throws IOException {
    this.name = name;
    InetAddress ip = InetAddress.getByName("localhost");
    Socket s = new Socket(ip, ServerPort);
    is = new DataInputStream(s.getInputStream());
    os = new DataOutputStream(s.getOutputStream());

    os.writeUTF(name);
    String message = is.readUTF();
    String[] msg = message.split("/");
    if (msg[0].equals("name")) {
      this.name = msg[1];
    }

    GamePanel gp = new GamePanel(frame, this);

    Thread thread2 = new Thread(new Runnable() {

      @Override
      public void run() {
        while (true) {
          try {
            String message = is.readUTF();
            String[] msg = message.split("/");

            if (msg[0].equals("chat")) {
              gp.chatArea.append(new String(msg[1]) + "\n");
            }
            if (msg[0].equals("member")) {
              gp.member.setText(new String(msg[1]));
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    });
    thread2.start();

    Client.switchPanel(frame, gp);
  }

  public void sendMessage(String s) {
    try {
      os.writeUTF(s);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}