package mafia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
  public JTextField chat;
  public JScrollPane scrollPane;
  public JTextArea chatArea;
  public JButton startBtn;
  public JTextField vote;
  public JLabel member;
  public JLabel time;
  //public JLabel jlabel; //직업 표시
  public JLabel ment; //진행 상황 멘트 표시
  public JLabel roleImageLabel; //역할 사진 표시

  public GamePanel(JFrame frame, ClientGame clientGame) {
    this.setLayout(null);
    this.setBackground(new Color(24, 24, 40));

    ment = new JLabel("<html>** 현재 진행 상황 **</html>");
    ment.setForeground(Color.WHITE);
    ment.setFont(new Font("굴림", Font.PLAIN, 15));
    ment.setBounds(800, 500, 500, 200);
    this.add(ment);

    roleImageLabel = new JLabel();
    roleImageLabel.setBounds(800, 650, 600, 200);
    this.add(roleImageLabel);

    JLabel chatable = new JLabel("채팅");
    chatable.setForeground(Color.WHITE);
    chatable.setFont(new Font("굴림", Font.PLAIN, 25));
    chatable.setBounds(20, 530, 73, 53);

    chat = new JTextField(30);
    chat.setBounds(90, 542, 694, 30);
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
    chatArea.setBackground(new Color(224, 224, 224));
    scrollPane = new JScrollPane(chatArea);
    scrollPane.setBounds(20, 39, 762, 493);
    this.add(scrollPane);
    chatArea.setEditable(false);

    JLabel votelable = new JLabel("투표");
    votelable.setFont(new Font("굴림", Font.PLAIN, 25));
    votelable.setForeground(Color.WHITE);
    votelable.setBounds(20, 593, 73, 53);

    vote = new JTextField();
    vote.setBounds(90, 608, 694, 30);
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
          //clientGame.gp.jlabel2.setText(clientGame.name + "은 마피아!!");
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

    JLabel[] roleLabels = new JLabel[10];

    // 생존자 사진
    /*
    for (int i = 0; i < 2; i++) {
      roleLabels[i] = createRoleLabel("src/mafia/image/citizen.png", 790 + i * 80, 39);
      this.add(roleLabels[i]);
    }
    */
    //시민,경찰 사진
    String[] roles = {"question","question","question"};
    for (int i = 0; i < roles.length; i++) {
      roleLabels[i + 2] = createRoleLabel("src/mafia/image/" + roles[i] + ".png", 790 + i * 150, 39);
      this.add(roleLabels[i + 2]);
    }

    // 마피아, 의사 사진
    String[] roles2 = {"question","question","question"};
    for (int i = 0; i < roles2.length; i++) {
      roleLabels[i + 2] = createRoleLabel("src/mafia/image/" + roles2[i] + ".png", 790 + i* 150, 300);
      this.add(roleLabels[i + 2]);
    }
    member = new JLabel("참여인원");
    member.setForeground(Color.WHITE);
    member.setFont(new Font("굴림", Font.PLAIN, 30));
    member.setBounds(790, 230, 763, 85);

    startBtn = new JButton("시작");
    startBtn.setForeground(Color.WHITE);
    startBtn.setFont(new Font("굴림", Font.PLAIN, 15));
    Color burgundyColor = new Color(119, 27, 35);
    startBtn.setBackground(burgundyColor);
    startBtn.setFocusPainted(false);
    startBtn.setBounds(20, 680, 80, 45);

    ImageIcon startIcon = new ImageIcon("src/mafia/image/start.png");
    Image startImage = startIcon.getImage().getScaledInstance(80, 75, Image.SCALE_SMOOTH);
    startIcon = new ImageIcon(startImage);
    startBtn.setIcon(startIcon);

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
/*
    final ImageIcon startGameIcon = new ImageIcon("src/mafia/image/gameStart.png");
    Image startGameIconImageImage = startGameIcon.getImage();
    Image newStartImage = startGameIconImageImage.getScaledInstance(188, 39, Image.SCALE_SMOOTH);
    startGameIcon.setImage(newStartImage);

    JLabel startGameBtn = new JLabel(startGameIcon);
    startGameBtn.setBounds(10, 670, 188, 39);
    this.add(startGameBtn);

    startBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        ImageIcon ingIcon = new ImageIcon("src/mafia/image/진행중.png");
        Image ingIconImage = ingIcon.getImage();
        Image newIngImage = ingIconImage.getScaledInstance(startGameIcon.getIconWidth(), startGameIcon.getIconHeight(), Image.SCALE_SMOOTH);
        ingIcon.setImage(newIngImage);
        startGameBtn.setIcon(ingIcon);
        clientGame.sendMessage(clientGame.name + "/gameStart");
      }
    });

    String[] processingTag = clientGame.name.split("#");
    String tag = processingTag[processingTag.length - 1];
    if (!tag.equals("1")) {
      startGameBtn.setEnabled(false);
    }


 */





    //jlabel = new JLabel("직업");
    //jlabel.setFont(new Font("굴림", Font.PLAIN, 25));
    //jlabel.setBounds(20, 700, 763, 85);

    time = new JLabel("30");
    //time.setForeground(Color.YELLOW); //흰색으로 적용됨
    time.setBounds(150, 680, 80, 45);
    time.setFont(new Font("굴림", Font.PLAIN, 30));
    time.setForeground(Color.WHITE);
    time.setHorizontalAlignment(SwingConstants.CENTER);
    time.setVerticalAlignment(SwingConstants.CENTER);

    this.add(startBtn);
    this.add(chat);
    this.add(scrollPane);
    this.add(votelable);
    this.add(chatable);
    this.add(member);
    this.add(vote);
    this.add(time);
    //this.add(jlabel);

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
    Image newImage = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
    icon = new ImageIcon(newImage);

    JLabel label = new JLabel(icon);
    label.setBounds(x, y, 150, 200);

    return label;
  }
  private ImageIcon loadImageIcon(File file, int width, int height) {
    try {
      Image image = new ImageIcon(file.toURI().toURL()).getImage();
      Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      return new ImageIcon(scaledImage);
    } catch (IOException e) {
      e.printStackTrace();
      return new ImageIcon();
    }
  }


}