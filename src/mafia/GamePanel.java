package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
  public JTextField chat;
  public JTextArea chatArea;
  public JButton startBtn;
  public JTextField vote;
  public JLabel member;
  public JLabel time;

  public GamePanel(JFrame frame, ClientGame clientGame) {
    this.setLayout(null);
    chat = new JTextField(30);
    chat.setBounds(91, 608, 624, 53);
    chat.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = chat.getText();

        if (clientGame.isDead) {
          clientGame.sendMessage(clientGame.name + "/chat/" + s + "/death");
          chatArea.append(clientGame.name + " (사망자) : " + s + "\n");
        } else {
          clientGame.sendMessage(clientGame.name + "/chat/" + s + "/alive");
          chatArea.append(clientGame.name + " (생존자) : " + s + "\n");
        }
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
        clientGame.sendMessage(clientGame.name + "/gameStart");
      }
    });

    String[] processingTag = clientGame.name.split("#");
    String tag = processingTag[processingTag.length - 1];
    if (!tag.equals("1")) {
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
        String[] strs = s.split("#");
        boolean nameCheck = false;

        List<String> registeredNames = new ArrayList<>();
        for(String name : clientGame.userNames){
          registeredNames.add(name.split("#")[0]);
          if(strs.length>1){
            if(s.equals(name)){
              nameCheck = true;
            }
          }else {
            if(strs[0].equals(name.split("#")[0])){
              nameCheck = true;
            }
          }
        }

        int count = 0;
        for(String rn : registeredNames){
          if(strs.length<2){
            if(strs[0].equals(rn)){
              count++;
            }
            if(count>1){
              nameCheck = false;
              break;
            }
          }
        }

        if(!nameCheck){
          JOptionPane.showMessageDialog(frame, "정확한 이름을 입력하세요.\n ex)(이름) or (이름)#(숫자)", "오류", JOptionPane.ERROR_MESSAGE);
          return;
        }

        if (clientGame.now == DayNight.NIGHT) {
          clientGame.sendMessage(clientGame.name + "/kill/" + s);
        } else if (clientGame.now == DayNight.HEAL) {
          clientGame.sendMessage(clientGame.name + "/heal/" + s);
        } else if (clientGame.now == DayNight.POLICE) {
          clientGame.sendMessage(clientGame.name + "/police/" + s);
        } else {
          clientGame.sendMessage(clientGame.name + "/vote/" + s);
        }
        vote.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
      }
    });

    time = new JLabel("30");
    time.setBounds(887, 336, 103, 75);
    time.setFont(new Font("굴림", Font.PLAIN, 30));

    this.add(startBtn);
    this.add(chat);
    this.add(chatArea);
    this.add(votelable);
    this.add(member);
    this.add(vote);
    this.add(time);

    setVisible(true);
  }
}