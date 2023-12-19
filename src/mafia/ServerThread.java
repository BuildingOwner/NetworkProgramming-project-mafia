/*
 * ServerThread 클래스
 * 이 클래스는 서버에서 클라이언트 각각의 연결을 관리하는 스레드입니다.
 */
package mafia;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

class ServerThread extends Thread {
  // 플레이어 이름, 데이터 입출력 스트림, 소켓, 활성 여부, 사망 여부
  public String name;
  final DataInputStream is;
  final DataOutputStream os;
  Socket s;
  boolean active;
  public Boolean isDead = false;

  //생성자 함수
  public ServerThread(Socket s, String name, DataInputStream is, DataOutputStream os) {
    this.is = is;
    this.os = os;
    this.name = name;
    this.s = s;
    this.active = true;
    //클라이언트에게 플레이어 이름을 전송
    try {
      os.writeUTF("name/" + name);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //스레드 실행 메서드
  @Override
  public void run() {
    String message;
    while (true) {
      try {
        message = is.readUTF(); // 이름/메소드/내용 형식
        System.out.println(message);
        String[] msg = message.split("/");
        //로그아웃 수신 시 스레드 종료
        if (msg[1].equals("logout")) {
          this.active = false;
          this.s.close();
          break;
        }

        //채팅 메시지 처리
        if (msg[1].equals("chat")) {
          //모든 클라이언트들에게 전송
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

        //게임 시작 메시지 처리
        if (msg[1].equals("gameStart")) {
          Server.startGame();
        }

        //투표 메시지 처리
        if (msg[1].equals("vote")) {
          Server.game.voting(msg[0], msg[2]);
        }

        //마피아 활동 메시지 처리
        if (msg[1].equals("kill")) {
          Server.game.skillPick(msg[2], "kill", this);
        }

        //의사 치료 메시지 처리
        if (msg[1].equals("heal")) {
          Server.game.skillPick(msg[2], "heal", this);
        }

        //경찰 조사 메시지 처리
        if (msg[1].equals("police")) {
          Server.game.skillPick(msg[2], "police", this);
        }

      } catch (IOException e) {
        e.printStackTrace();
        break;
      }
    }
    try {
      //입출력 스트림 닫기
      this.is.close();
      this.os.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


}