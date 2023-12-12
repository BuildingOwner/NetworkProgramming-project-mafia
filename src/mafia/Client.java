package mafia;

import javax.swing.*;
import java.awt.*;

public class Client extends JFrame {

  public Client(){
    super("Mafia");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 1109, 969);
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
