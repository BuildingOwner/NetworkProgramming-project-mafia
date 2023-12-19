/*
 * ClientGame 클래스
 * 이 클래스는 클라이언트의 게임 관련 기능을 담당하는 클래스입니다.
 */
package mafia;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientGame {
  private final static int ServerPort = 5000; //서버 포트 번호
  public DataInputStream is; //데이터 입력 스트림
  public DataOutputStream os; //데이터 출력 스트림

  // 플레이어 이름, 현재 게임 상태, 게임 화면, 게임 진행 여부, 사망 여부, 플레이어 역할, 모든 플레이어 이름
  public String name;
  public DayNight now;
  public GamePanel gp;
  public Boolean gameFlag = false;
  public Boolean isDead = false;
  public String job;
  public String[] userNames;

  public ClientGame(JFrame frame, String name) throws IOException { //생성자 함수
    this.name = name;

    //서버 접속
    InetAddress ip = InetAddress.getByName("localhost");
    Socket s = new Socket(ip, ServerPort);
    is = new DataInputStream(s.getInputStream());
    os = new DataOutputStream(s.getOutputStream());

    os.writeUTF(name); //서버에 플레이어 이름 전송

    //서버로부터 응답 수신
    String message = is.readUTF();
    String[] msg = message.split("/");
    if (msg[0].equals("name")) {
      //실제 플레이어 이름으로 설정
      this.name = msg[1];
    }

    //게임 화면 및 클라이언트 채팅 쓰레드 생성 및 시작
    gp = new GamePanel(frame, this);

    ClientGameChat cgc = new ClientGameChat(this);
    cgc.start();

    Client.switchPanel(frame, gp);
  }

  //서버에 메시지 전송하는 메서드
  public void sendMessage(String s) {
    try {
      os.writeUTF(s);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}