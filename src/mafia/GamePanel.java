package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
  private JTextField chat;
  public JTextArea chatArea;
  public JButton startBtn;
  private JTextField vote;
  public JLabel member;

  public GamePanel(JFrame frame, GameChat gameChat) {
    this.setLayout(null);
    chat = new JTextField(30);
    chat.setBounds(91, 608, 624, 53);
    chat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = chat.getText();
        gameChat.sendMessage(gameChat.name + "/chat/" + s);
        chatArea.append(gameChat.name + " : " + s + "\n");
        chat.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
      }
    });

    chatArea = new JTextArea(10, 30);
    chatArea.setBounds(91, 39, 691, 493);
    chatArea.setEditable(false);

    startBtn = new JButton("시작");
    startBtn.setBounds(887, 636, 103, 75);
    startBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        gameChat.sendMessage(gameChat.name + "/gameStart");
      }
    });

    String[] processingTag = gameChat.name.split("#");
    String tag = processingTag[processingTag.length - 1];
    if(!tag.equals("1")){
      startBtn.setEnabled(false);
    }

    JLabel votelable = new JLabel("투표");
    votelable.setFont(new Font("굴림", Font.PLAIN, 30));
    votelable.setBounds(57, 774, 73, 53);


    member = new JLabel("참여인원");
    member.setFont(new Font("굴림", Font.PLAIN, 30));
    member.setBounds(57, 636, 763, 85);


    vote = new JTextField();
    vote.setBounds(142, 776, 679, 59);
    vote.setColumns(10);
    vote.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = vote.getText();
        gameChat.sendMessage(gameChat.name + "/vote/" + s);
        chatArea.append("("+gameChat.name + "님의 투표 : " + s + ")\n");
        vote.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
      }
    });

    this.add(startBtn);
    this.add(chat);
    this.add(chatArea);
    this.add(votelable);
    this.add(member);
    this.add(vote);

    setVisible(true);
  }
}