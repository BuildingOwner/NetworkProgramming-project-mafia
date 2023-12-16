package mafia;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientGame {
  private final static int ServerPort = 5000;
  public DataInputStream is;
  public DataOutputStream os;
  public String name;
  public DayNight now;
  public GamePanel gp;
  public Boolean gameFlag = false;
  public Boolean isDead = false;
  public String job;
  public String[] userNames;

  public ClientGame(JFrame frame, String name) throws IOException {
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

    gp = new GamePanel(frame, this);

    ClientGameChat cgc = new ClientGameChat(this);
    cgc.start();

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