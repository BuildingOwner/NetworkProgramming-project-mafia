/*
 * ClientGameChat 클래스
 * 이 클래스는 클라이언트의 채팅 및 게임 진행 관련 기능을 담당하는 스레드입니다.
 */
package mafia;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientGameChat extends Thread {
  ClientGame clientGame;

  public ClientGameChat(ClientGame gameChat) {
    this.clientGame = gameChat;
  } //생성자 함수

  @Override
  public void run() {
    while (true) {
      try {
        //서버로부터 메시지 수신
        String message = clientGame.is.readUTF();
        String[] msg = message.split("/");

        //채팅 메시지 처리
        if (msg[0].equals("chat")) {
          clientGame.gp.chatArea.append(new String(msg[1]) + "\n");
        }

        //플레이어 이름 처리
        if (msg[0].equals("member")) {
          clientGame.gp.member.setText(new String(msg[1]));
          String nameProcessed = msg[1].replace("[","").replace("]","");
          clientGame.userNames = nameProcessed.split(" ");
          System.out.println(clientGame.userNames.toString());
        }

        //현재 상태 처리
        if (msg[0].equals("dayNight")) {
          switch (msg[1]) {
            case "day": //낮인 경우
              clientGame.now = DayNight.DAY;
              //GamePanel화면에 ment JLabel에 출력
              clientGame.gp.ment.setText("<html>시민 여러분, 지금은 낮입니다.<br>의심과 추론을 통해 진실을 밝혀내는 시간입니다.<br>채팅을 통해 대화할 수 있습니다.</html>"); //낮임을 알리는 멘트 출력
              displayRoleImage("src/mafia/image/토론활동.png"); //토론하는 이미지 출력
              break;
            case "vote": //투표 상황인 경우
              clientGame.now = DayNight.VOTE;
              clientGame.gp.ment.setText("<html>모든 플레이어들, 지금은 투표 시간입니다.<br>누구에게 의심의 눈길을 보낼까요?<br>투표 창에 지목할 사람의 이름을 입력하세요. 최다 투표 득점자는 처형!</html>"); //투표 시간임을 알리는 멘트 출력
              displayRoleImage("src/mafia/image/투표활동.png"); //투표하는 이미지 출력
              break;
            case "night": //밤인 경우, 마피아 활동
              clientGame.now = DayNight.NIGHT;
              clientGame.gp.ment.setText("<html>어둠이 내려오고, 밤이 시작됩니다.<br>마피아와 다른 역할들이 행동할 시간입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>"); //밤이 되었음을 알리는 멘트 출력, 마피아 활동
              displayRoleImage("src/mafia/image/마피아활동.png"); //마피아 이미지 출력
              break;
            case "police": //경찰 활동
              clientGame.now = DayNight.POLICE;
              clientGame.gp.ment.setText("<html>마피아의 정체를 파헤치기 위해 조사를 진행하고,<br>도시의 안전을 위해 헌신하는 경찰관입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>"); //경찰 활동
              displayRoleImage("src/mafia/image/경찰활동.png"); //경찰 이미지 출력
              break;
            case "heal": //의사 활동
              clientGame.now = DayNight.HEAL;
              clientGame.gp.ment.setText("<html>마피아의 공격으로부터 플레이어들을 보호하고,<br>생존을 위해 노력하는 의지 강한 의사입니다.<br>투표 창에 지목할 사람의 이름을 입력하세요.</html>"); //의사 활동
              displayRoleImage("src/mafia/image/의사활동.png"); //의사 이미지 출력
              break;
          }
          //버튼 비활성화 상태 업데이트
          clientGame.gp.chat.setEnabled(clientGame.now == DayNight.DAY || clientGame.isDead);
          clientGame.gp.vote.setEnabled(
              (!clientGame.isDead && clientGame.now == DayNight.VOTE) ||
                  (!clientGame.isDead && clientGame.job.equals("마피아") && clientGame.now == DayNight.NIGHT) ||
                  (!clientGame.isDead && clientGame.job.equals("의사") && clientGame.now == DayNight.HEAL) ||
                  (!clientGame.isDead && clientGame.job.equals("경찰") && clientGame.now == DayNight.POLICE)
          );
          clientGame.gp.repaint();
        }

        //플레이어 사망 처리
        if (msg[0].equals("death")) {
          clientGame.isDead = true;
        }

        //게임 진행 여부 처리
        if (msg[0].equals("isGameRun")) {
          switch (msg[1]) {
            case "true":
              //게임이 시작되면 시작 버튼 비활성화
              clientGame.gameFlag = true;
              clientGame.gp.startBtn.setText("ing");
              clientGame.gp.startBtn.setEnabled(false);
              break;
            case "false":
              //게임이 종료되면 게임 플래그 false로 설정
              clientGame.gameFlag = false;
              clientGame.gp.startBtn.setText("finish");
              break;
          }
        }

        //남은 시간 처리
        if (msg[0].equals("leftTime")) {
          clientGame.gp.time.setText(msg[1]);
        }

        //플레이어 역할 처리
        if (msg[0].equals("job")) {
          clientGame.job = msg[1];
        }

      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  //오른쪽 하단에 현재 진행 상황을 알리는 이미지 출력하는 함수
  private void displayRoleImage(String s) {
    ImageIcon roleIcon = new ImageIcon(s);
    Image roleImage = roleIcon.getImage();
    Image newRoleImage = roleImage.getScaledInstance(450, 300, Image.SCALE_SMOOTH); //이미지 크기 설정
    roleIcon = new ImageIcon(newRoleImage);

    clientGame.gp.roleImageLabel.setIcon(roleIcon); //roleImageLabel에 이미지 출력
  }
}