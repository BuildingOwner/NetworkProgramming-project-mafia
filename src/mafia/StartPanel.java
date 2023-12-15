package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartPanel extends JPanel {
  private JTextField userName;

  public StartPanel(JFrame frame) {
    setBounds(100, 100, 1109, 969);
    this.setLayout(null);

    JLabel title = new JLabel("마피아 게임");
    title.setFont(new Font("굴림", Font.BOLD, 60));
    title.setBounds(379, 199, 329, 85);
    this.add(title);

    JLabel label = new JLabel("이름을 입력해 주세요.");
    label.setFont(new Font("굴림", Font.PLAIN, 30));
    label.setBounds(388, 405, 306, 59);
    this.add(label);

    userName = new JTextField();
    userName.setBounds(368, 474, 353, 39);
    this.add(userName);
    userName.setColumns(10);

    JButton startBtn = new JButton("게임시작");
    startBtn.setFont(new Font("굴림", Font.PLAIN, 30));
    startBtn.setBounds(458, 558, 188, 39);
    this.add(startBtn);

    startBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = userName.getText();
        if (name == null || name.trim().isEmpty()) {
          JOptionPane.showMessageDialog(frame, "이름을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        } else {
          try {
            new ClientGame(frame, name);
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    });

    JButton exitBtn = new JButton("나가기");
    exitBtn.setFont(new Font("굴림", Font.PLAIN, 30));
    exitBtn.setBounds(458, 652, 188, 39);
    this.add(exitBtn);

    exitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
      }
    });
  }

}
