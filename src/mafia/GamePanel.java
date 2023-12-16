package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel {
  public JTextField chat;
  public JScrollPane scrollPane;
  public JTextArea chatArea;
  public JButton startBtn;
  public JTextField vote;
  public JLabel member;
  public JLabel time;
  public JLabel jlabel;

  public GamePanel(JFrame frame, ClientGame clientGame) {
    this.setLayout(null);

    chat = new JTextField(30);
    chat.setBounds(20, 542, 762, 30);
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
    scrollPane = new JScrollPane(chatArea);
    scrollPane.setBounds(20, 39, 762, 493);
    this.add(scrollPane);
    chatArea.setEditable(false);

    JLabel[] roleLabels = new JLabel[10];

    // 생존자 사진
    for (int i = 0; i < 1; i++) {
      roleLabels[i] = createRoleLabel("src/mafia/image/citizen.png", 800 + i * 80, 39);
      this.add(roleLabels[i]);
    }

    // 경찰, 마피아, 의사 사진
    String[] roles = {"police", "mafia","doctor"};
    for (int i = 0; i < roles.length; i++) {
      roleLabels[i + 2] = createRoleLabel("src/mafia/image/" + roles[i] + ".png", 800 + (i + 2) * 80, 39);
      this.add(roleLabels[i + 2]);
    }


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


    jlabel = new JLabel("직업");
    jlabel.setFont(new Font("굴림", Font.PLAIN, 30));
    jlabel.setBounds(57, 700, 763, 85);

    vote = new JTextField();
    vote.setBounds(142, 776, 679, 59);
    vote.setColumns(10);
    vote.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String s = vote.getText();

        if (clientGame.now == DayNight.NIGHT) {
          clientGame.sendMessage(clientGame.name + "/kill/" + s);
        } else if (clientGame.now == DayNight.HEAL) {
          clientGame.sendMessage(clientGame.name + "/heal/" + s);
        } else {
          clientGame.sendMessage(clientGame.name + "/vote/" + s);
        }
        vote.setText("");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
      }
    });

    time = new JLabel("30");
    time.setBounds(887, 150, 103, 75);
    time.setFont(new Font("굴림", Font.PLAIN, 30));
    time.setForeground(Color.WHITE);
    time.setHorizontalAlignment(SwingConstants.CENTER);
    time.setVerticalAlignment(SwingConstants.CENTER);

// JLabel 추가 - 사진을 표시할 라벨
    JLabel imageLabel = new JLabel();
    ImageIcon bombIcon = new ImageIcon("src/mafia/image/bomb.png");
    Image image = bombIcon.getImage();
    Image newImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
    ImageIcon scaledBombIcon = new ImageIcon(newImage);
    imageLabel.setIcon(scaledBombIcon);
    imageLabel.setBounds(887, 100, 200, 200);

    this.add(startBtn);
    this.add(chat);
    this.add(scrollPane);
    this.add(votelable);
    this.add(member);
    this.add(vote);
    this.add(time);
    this.add(imageLabel);
    this.add(jlabel);

    setVisible(true);
  }

  private String extractName(String fullName) {
    String[] parts = fullName.split("#");
    return parts[0];
  }

  //역할 사진 보여주는 함수
  private JLabel createRoleLabel(String imagePath, int x, int y) {
    ImageIcon icon = new ImageIcon(imagePath);
    Image image = icon.getImage();
    Image newImage = image.getScaledInstance(70, 100, Image.SCALE_SMOOTH);
    icon = new ImageIcon(newImage);

    JLabel label = new JLabel(icon);
    label.setBounds(x, y, 70, 100);

    return label;
  }

}