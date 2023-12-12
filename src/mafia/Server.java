package mafia;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

  static ArrayList<ServerThread> list = new ArrayList<>();
  static Socket s;
  static DataInputStream is;
  static DataOutputStream os;
  static Boolean isStart = false;

  static void startGame(){
    isStart = true;
    Game game = new Game(list);
    game.start();
  }

  public static void main(String[] args) throws IOException {
    ServerSocket ssocket = new ServerSocket(5000);

    while (!isStart) {
      s = ssocket.accept();

      is = new DataInputStream(s.getInputStream());
      os = new DataOutputStream(s.getOutputStream());

      String name = is.readUTF();
      ServerThread thread = new ServerThread(s, name, is, os);
      list.add(thread);
      thread.start();
    }
  }

}