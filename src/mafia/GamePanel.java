package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
  private JTextField textField;
  public JTextArea textArea;
  public JButton startBtn;

  public GamePanel(JFrame frame, GameChat gameChat) {
    this.setLayout(new BorderLayout());
    textField = new JTextField(30);
    textField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = textField.getText();
        gameChat.sendMessage(gameChat.name + "/chat/" + s);
        textArea.append(gameChat.name + " : " + s + "\n");
        textField.setText("");
        textArea.setCaretPosition(textArea.getDocument().getLength());
      }
    });

    textArea = new JTextArea(10, 30);
    textArea.setEditable(false);

    startBtn = new JButton("시작");
    startBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gameChat.sendMessage(gameChat.name + "/gameStart");
      }
    });

    this.add(startBtn,BorderLayout.EAST);
    this.add(textField, BorderLayout.SOUTH);
    this.add(textArea, BorderLayout.CENTER);
    setVisible(true);
  }
}
