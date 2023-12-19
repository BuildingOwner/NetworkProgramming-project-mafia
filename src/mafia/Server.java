/*
 * Server 클래스
 * 이 클래스는 마피아 게임 서버를 구현합니다.
 */
package mafia;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

  static ArrayList<ServerThread> list = new ArrayList<>(); //서버에 연결된 클라이언트 스레드
  static Socket s;
  static DataInputStream is; //데이터 입력 스트림
  static DataOutputStream os; //데이터 출력 스트림
  static Boolean isStart = false; //게임 시작 여부 false로 설정
  static int counter = 0; //클라이언트 수 측정
  static Game game;

  //게임 시작 메서드
  static void startGame() {
    isStart = true;
    game = new Game(list);
    game.start();
  }

  public static void main(String[] args) throws IOException {
    ServerSocket ssocket = new ServerSocket(5000);

    while (!isStart) {
      s = ssocket.accept(); //클라이언트 연결 기다림

      is = new DataInputStream(s.getInputStream());
      os = new DataOutputStream(s.getOutputStream());

      //클라이언트들의 이름에 일련번호 붙여서 저장 -> 이름#1,이름#2 이런 식
      String name = is.readUTF() + "#" + String.valueOf(++counter);
      ServerThread thread = new ServerThread(s, name, is, os);
      list.add(thread);
      thread.start();
    }
  }

}