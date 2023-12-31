/*
 * Client 클래스
 * 이 클래스는 마피아 게임의 클라이언트 창을 나타내는 클래스입니다.
 */
package mafia;

import javax.swing.*;
import java.awt.*;

public class Client extends JFrame {

  public Client() { //생성자 함수
    super("Mafia");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1280, 900);
    setVisible(true);
    switchPanel(this, new StartPanel(this));
  }

  public static void switchPanel(JFrame frame, JPanel panel) {
    frame.getContentPane().removeAll(); // 현재 패널 제거
    frame.getContentPane().add(panel); // 새로운 패널 추가

    frame.revalidate(); // 프레임 다시 그리기
    frame.repaint(); // 프레임 다시 그리기
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Client client = new Client();
          client.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
}